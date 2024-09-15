using System;
using System.Collections.Generic;
using System.Data;
using System.Drawing;
using System.Windows.Forms;
using Microsoft.Data.SqlClient;

namespace SGBD_Practic
{
    public partial class Form1 : Form
    {
        string connectionString = "Server=DESKTOP-FHMI43Q\\SQLEXPRESS;Database=PracticS1;" +
                                  "Integrated Security=true;TrustServerCertificate=true";

        DataSet ds = new DataSet();

        SqlDataAdapter parentAdapter;
        SqlDataAdapter childAdapter;

        BindingSource parentBS;
        BindingSource childBS;

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            try
            {
                using (SqlConnection conn = new SqlConnection(connectionString))
                {
                    conn.Open();

                    parentAdapter = new SqlDataAdapter("SELECT * FROM Autori", conn);
                    childAdapter = new SqlDataAdapter("SELECT * FROM Carti", conn);

                    parentAdapter.Fill(ds, "Autori");
                    childAdapter.Fill(ds, "Carti");

                    DataColumn parentColumn = ds.Tables["Autori"].Columns["cod_autor"];
                    DataColumn childColumn = ds.Tables["Carti"].Columns["cod_autor"];

                    DataRelation relation = new DataRelation("FK", parentColumn, childColumn);

                    ds.Relations.Add(relation);

                    parentBS = new BindingSource();
                    parentBS.DataSource = ds.Tables["Autori"];
                    parentDGW.DataSource = parentBS;

                    childBS = new BindingSource();
                    childBS.DataSource = parentBS;
                    childBS.DataMember = "FK";
                    childDGW.DataSource = childBS;

                    childDGW.ClearSelection();
                    
                    titluTB.Text = "titlu";
                    numarPaginiTB.Text = "numar_pagini";
                    anulAparitieiTB.Text = "anul_aparitiei";
                    pretTB.Text = "pret";
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
                int parentId = int.Parse(parentDGW.CurrentRow.Cells["cod_autor"].Value.ToString());

                using (SqlConnection conn = new SqlConnection(connectionString))
                {
                    conn.Open();

                    string addString = "INSERT INTO Carti(cod_autor, titlu, numar_pagini, anul_aparitiei, pret)" +
                                       "VALUES (@cod_autor, @titlu, @numar_pagini, @anul_aparitiei, @pret)";

                    SqlCommand addCmd = new SqlCommand(addString, conn);

                    addCmd.Parameters.AddWithValue("@cod_autor", parentId);
                    addCmd.Parameters.AddWithValue("@titlu", titluTB.Text);
                    addCmd.Parameters.AddWithValue("@numar_pagini", numarPaginiTB.Text);
                    addCmd.Parameters.AddWithValue("@anul_aparitiei", anulAparitieiTB.Text);
                    addCmd.Parameters.AddWithValue("@pret", pretTB.Text);

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
                MessageBox.Show(@"Nicio carte selectata");
                return;
            }

            try
            {
                using (SqlConnection conn = new SqlConnection(connectionString))
                {
                    conn.Open();

                    string updateString = "UPDATE Carti SET titlu = @titlu, numar_pagini = @numar_pagini, " +
                                          "anul_aparitiei = @anul_aparitiei, pret = @pret WHERE cod_carte = @cod_carte";

                    SqlCommand updateCmd = new SqlCommand(updateString, conn);

                    int childId = int.Parse(childDGW.CurrentRow.Cells["cod_carte"].Value.ToString());

                    updateCmd.Parameters.AddWithValue("@cod_carte", childId);
                    updateCmd.Parameters.AddWithValue("@titlu", titluTB.Text);
                    updateCmd.Parameters.AddWithValue("@numar_pagini", numarPaginiTB.Text);
                    updateCmd.Parameters.AddWithValue("@anul_aparitiei", anulAparitieiTB.Text);
                    updateCmd.Parameters.AddWithValue("@pret", pretTB.Text);
                    
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
                MessageBox.Show("Nicio carte selectata");
                return;
            }

            try
            {
                using (SqlConnection conn = new SqlConnection(connectionString))
                {
                    conn.Open();

                    string deleteString = "DELETE FROM Carti WHERE cod_carte = @cod_carte";

                    SqlCommand deleteCmd = new SqlCommand(deleteString, conn);

                    int childId = int.Parse(childDGW.CurrentRow.Cells["cod_carte"].Value.ToString());

                    deleteCmd.Parameters.AddWithValue("@cod_carte", childId);

                    deleteCmd.ExecuteNonQuery();

                    refresh(conn);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void refresh(SqlConnection conn)
        {
            parentAdapter.SelectCommand.Connection = conn;
            childAdapter.SelectCommand.Connection = conn;

            ds.Tables["Carti"].Clear();

            childAdapter.Fill(ds, "Carti");

            childDGW.ClearSelection();

            titluTB.Text = "titlu";
            numarPaginiTB.Text = "numar_pagini";
            anulAparitieiTB.Text = "anul_aparitiei";
            pretTB.Text = "pret";
        }

        private void parentDGW_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            childDGW.ClearSelection();

            titluTB.Text = "titlu";
            numarPaginiTB.Text = "numar_pagini";
            anulAparitieiTB.Text = "anul_aparitiei";
            pretTB.Text = "pret";
        }

        private void childDGW_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            titluTB.Text = childDGW.CurrentRow.Cells["titlu"].Value.ToString();
            numarPaginiTB.Text = childDGW.CurrentRow.Cells["numar_pagini"].Value.ToString();
            anulAparitieiTB.Text = childDGW.CurrentRow.Cells["anul_aparitiei"].Value.ToString();
            pretTB.Text = childDGW.CurrentRow.Cells["pret"].Value.ToString();
        }
    }
}