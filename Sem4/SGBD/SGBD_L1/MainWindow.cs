using Microsoft.Data.SqlClient;
using System.Data;

namespace SGBD_L1
{
    public partial class MainWindow : Form
    {
        string connectionString = "Server=DESKTOP-FHMI43Q\\SQLEXPRESS;Database=GradinaZoo;" +
            "Integrated Security=true;TrustServerCertificate=true";

        DataSet ds = new DataSet();

        SqlDataAdapter speciesAdapter;
        SqlDataAdapter animalsAdapter;

        BindingSource speciesBs;
        BindingSource animalsBs;


        public MainWindow()
        {
            InitializeComponent();
        }

        private void MainWindow_Load(object sender, EventArgs e)
        {
            try
            {
                using (SqlConnection conn = new SqlConnection(connectionString))
                {
                    conn.Open();

                    speciesAdapter = new SqlDataAdapter("SELECT * FROM Specie", conn);
                    animalsAdapter = new SqlDataAdapter("SELECT * FROM Animal", conn);

                    speciesAdapter.Fill(ds, "Specie");
                    animalsAdapter.Fill(ds, "Animal");

                    DataColumn speciesColumn = ds.Tables["Specie"].Columns["cod_specie"];
                    DataColumn animalsColumn = ds.Tables["Animal"].Columns["cod_specie"];

                    DataRelation relation = new DataRelation("FK_Specie_Animal", speciesColumn, animalsColumn);

                    ds.Relations.Add(relation);

                    speciesBs = new BindingSource();
                    speciesBs.DataSource = ds.Tables["Specie"];
                    speciesDGW.DataSource = speciesBs;

                    animalsBs = new BindingSource();
                    animalsBs.DataSource = speciesBs;
                    animalsBs.DataMember = "FK_Specie_Animal";
                    animalsDGW.DataSource = animalsBs;

                    animalsDGW.ClearSelection();
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void speciesDGW_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            textBox1.Text = "";
            numericUpDown1.Value = 0;
            animalsDGW.ClearSelection();
        }

        private void animalsDGW_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0)
            {
                DataGridViewRow row = animalsDGW.Rows[e.RowIndex];

                textBox1.Text = row.Cells["nume_animal"].Value.ToString();
                numericUpDown1.Value = (int)row.Cells["cod_cusca"].Value;
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            try
            {
                using (SqlConnection conn = new SqlConnection(connectionString))
                {
                    conn.Open();

                    SqlCommand addCmd = new SqlCommand("INSERT INTO Animal(nume_animal, cod_specie, cod_cusca) " +
                                               "VALUES (@nume_animal, @cod_specie, @cod_cusca)", conn);

                    addCmd.Parameters.AddWithValue("@nume_animal", textBox1.Text);
                    addCmd.Parameters.AddWithValue("@cod_specie", speciesDGW.SelectedRows[0].Cells["cod_specie"].Value);
                    addCmd.Parameters.AddWithValue("@cod_cusca", numericUpDown1.Value);

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
            if (animalsDGW.SelectedRows.Count == 0)
            {
                MessageBox.Show("Niciun animal selectat");
                return;
            }
            try
            {
                using (SqlConnection conn = new SqlConnection(connectionString))
                {
                    conn.Open();

                    SqlCommand updateCmd = new SqlCommand("UPDATE Animal SET nume_animal = @nume_animal, cod_cusca = @cod_cusca " +
                                               "WHERE cod_animal = @cod_animal", conn);

                    updateCmd.Parameters.AddWithValue("@nume_animal", textBox1.Text);
                    updateCmd.Parameters.AddWithValue("@cod_cusca", numericUpDown1.Value);
                    updateCmd.Parameters.AddWithValue("@cod_animal", animalsDGW.SelectedRows[0].Cells["cod_animal"].Value);

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
            if (animalsDGW.SelectedRows.Count == 0)
            {
                MessageBox.Show("Niciun animal selectat");
                return;
            }
            try
            {
                using (SqlConnection conn = new SqlConnection(connectionString))
                {
                    conn.Open();

                    SqlCommand deleteCmd = new SqlCommand("DELETE FROM Animal WHERE cod_animal = @cod_animal", conn);
                    deleteCmd.Parameters.AddWithValue("@cod_animal", animalsDGW.SelectedRows[0].Cells["cod_animal"].Value);

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
            speciesAdapter.SelectCommand.Connection = conn;
            animalsAdapter.SelectCommand.Connection = conn;

            ds.Tables["Animal"].Clear();
            animalsAdapter.Fill(ds, "Animal");

            animalsDGW.ClearSelection();

            textBox1.Text = "";
            numericUpDown1.Value = 0;
        }
    }
}
