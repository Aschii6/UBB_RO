using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Windows.Forms;

namespace SGBD_L2
{
    public partial class MainWindow : Form
    {
        string connectionString = "";

        DataSet ds = new DataSet();

        SqlDataAdapter parentAdapter;
        SqlDataAdapter childAdapter;

        BindingSource parentBS;
        BindingSource childBS;

        string parentName, childName;
        string parentIdName, childIdName;

        List<string> childColumnNames = new List<string>();

        public MainWindow()
        {
            InitializeComponent();
        }

        private void MainWindow_Load(object sender, EventArgs e)
        {
            connectionString = ConfigurationManager.ConnectionStrings["cn"].ConnectionString;

            parentName = ConfigurationManager.AppSettings["parentName"];
            childName = ConfigurationManager.AppSettings["childName"];

            parentIdName = ConfigurationManager.AppSettings["parentIdName"];
            childIdName = ConfigurationManager.AppSettings["childIdName"];

            try
            {
                using (SqlConnection conn = new SqlConnection(connectionString))
                {
                    conn.Open();

                    parentAdapter = new SqlDataAdapter($"SELECT * FROM {parentName}", conn);
                    childAdapter = new SqlDataAdapter($"SELECT * FROM {childName}", conn);

                    parentAdapter.Fill(ds, parentName);
                    childAdapter.Fill(ds, childName);

                    DataColumn parentColumn = ds.Tables[parentName].Columns[parentIdName];
                    DataColumn childColumn = ds.Tables[childName].Columns[parentIdName];

                    DataRelation relation = new DataRelation("FK_" + parentName + "_" + childName, parentColumn, childColumn);

                    ds.Relations.Add(relation);

                    parentBS = new BindingSource();
                    parentBS.DataSource = ds.Tables[parentName];
                    parentDGW.DataSource = parentBS;

                    childBS = new BindingSource();
                    childBS.DataSource = parentBS;
                    childBS.DataMember = "FK_" + parentName + "_" + childName;
                    childDGW.DataSource = childBS;

                    childDGW.ClearSelection();
                }

                foreach (DataColumn column in ds.Tables[childName].Columns)
                {
                    if (column.ColumnName != parentIdName && column.ColumnName != childIdName)
                        childColumnNames.Add(column.ColumnName);
                }

                int pointX = 0;
                int pointY = 12;

                foreach (string columnName in childColumnNames)
                {
                    TextBox textBox = new TextBox();
                    textBox.Text = columnName;
                    textBox.Name = columnName;
                    textBox.Location = new Point(pointX, pointY);
                    textBox.Visible = true;
                    textBox.Parent = panel1;

                    pointY += 40;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            try
            {
                int parentId = int.Parse(parentDGW.CurrentRow.Cells[parentIdName].Value.ToString());

                using (SqlConnection conn = new SqlConnection(connectionString))
                {
                    conn.Open();

                    string addString = ConfigurationManager.AppSettings["childInsertString"];

                    SqlCommand addCmd = new SqlCommand(addString, conn);

                    addCmd.Parameters.AddWithValue($"@{parentIdName}", parentId);

                    foreach (string columnName in childColumnNames)
                    {
                        TextBox textBox = panel1.Controls[columnName] as TextBox;
                        addCmd.Parameters.AddWithValue($"@{columnName}", textBox.Text);
                    }

                    addCmd.ExecuteNonQuery();

                    refresh(conn);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            if (childDGW.SelectedRows.Count == 0)
            {
                MessageBox.Show("Niciun animal selectat");
                return;
            }

            try
            {
                using (SqlConnection conn = new SqlConnection(connectionString))
                {
                    conn.Open();

                    string updateString = ConfigurationManager.AppSettings["childUpdateString"];

                    SqlCommand updateCmd = new SqlCommand(updateString, conn);

                    int childId = int.Parse(childDGW.CurrentRow.Cells[childIdName].Value.ToString());

                    updateCmd.Parameters.AddWithValue($"@{childIdName}", childId);

                    foreach (string column in childColumnNames)
                    {
                        TextBox textBox = panel1.Controls[column] as TextBox;
                        updateCmd.Parameters.AddWithValue($"@{column}", textBox.Text);
                    }

                    updateCmd.ExecuteNonQuery();

                    refresh(conn);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void button3_Click(object sender, EventArgs e)
        {
            if (childDGW.SelectedRows.Count == 0)
            {
                MessageBox.Show("Niciun animal selectat");
                return;
            }

            try
            {
                using (SqlConnection conn = new SqlConnection(connectionString))
                {
                    conn.Open();

                    string deleteString = $"DELETE FROM {childName} WHERE {childIdName} = @{childIdName}";

                    SqlCommand deleteCmd = new SqlCommand(deleteString, conn);

                    int childId = int.Parse(childDGW.CurrentRow.Cells[childIdName].Value.ToString());

                    deleteCmd.Parameters.AddWithValue($"@{childIdName}", childId);

                    deleteCmd.ExecuteNonQuery();

                    refresh(conn);
                }
            } catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void refresh(SqlConnection conn)
        {
            parentAdapter.SelectCommand.Connection = conn;
            childAdapter.SelectCommand.Connection = conn;

            ds.Tables[childName].Clear();

            childAdapter.Fill(ds, childName);

            childDGW.ClearSelection();

            foreach (string columnName in childColumnNames)
            {
                TextBox textBox = panel1.Controls[columnName] as TextBox;
                textBox.Text = columnName;
            }
        }

        private void childDGW_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            foreach (string columnName in childColumnNames)
            {
                TextBox textBox = panel1.Controls[columnName] as TextBox;
                textBox.Text = childDGW.CurrentRow.Cells[columnName].Value.ToString();
            }
        }

        private void parentDGW_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            childDGW.ClearSelection();

            foreach (string columnName in childColumnNames)
            {
                TextBox textBox = panel1.Controls[columnName] as TextBox;
                textBox.Text = columnName;
            }
        }
    }
}
