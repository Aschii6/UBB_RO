using Microsoft.Data.SqlClient;
using System.Data;

namespace SGBD_L1
{
    partial class MainWindow
    {
        /// <summary>
        ///  Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        ///  Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        ///  Required method for Designer support - do not modify
        ///  the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            speciesDGW = new DataGridView();
            label1 = new Label();
            animalsDGW = new DataGridView();
            label2 = new Label();
            textBox1 = new TextBox();
            label3 = new Label();
            label4 = new Label();
            numericUpDown1 = new NumericUpDown();
            button1 = new Button();
            button2 = new Button();
            button3 = new Button();
            ((System.ComponentModel.ISupportInitialize)speciesDGW).BeginInit();
            ((System.ComponentModel.ISupportInitialize)animalsDGW).BeginInit();
            ((System.ComponentModel.ISupportInitialize)numericUpDown1).BeginInit();
            SuspendLayout();
            // 
            // speciesDGW
            // 
            speciesDGW.AllowUserToAddRows = false;
            speciesDGW.AllowUserToDeleteRows = false;
            speciesDGW.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            speciesDGW.Location = new Point(12, 40);
            speciesDGW.Name = "speciesDGW";
            speciesDGW.ReadOnly = true;
            speciesDGW.RowHeadersWidth = 51;
            speciesDGW.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            speciesDGW.Size = new Size(400, 288);
            speciesDGW.TabIndex = 0;
            speciesDGW.CellClick += speciesDGW_CellClick;
            // 
            // label1
            // 
            label1.AutoSize = true;
            label1.Location = new Point(12, 9);
            label1.Name = "label1";
            label1.Size = new Size(49, 20);
            label1.TabIndex = 1;
            label1.Text = "Specii";
            // 
            // animalsDGW
            // 
            animalsDGW.AllowUserToAddRows = false;
            animalsDGW.AllowUserToDeleteRows = false;
            animalsDGW.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            animalsDGW.EditMode = DataGridViewEditMode.EditOnF2;
            animalsDGW.Location = new Point(470, 40);
            animalsDGW.Name = "animalsDGW";
            animalsDGW.ReadOnly = true;
            animalsDGW.RowHeadersWidth = 51;
            animalsDGW.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            animalsDGW.Size = new Size(600, 288);
            animalsDGW.TabIndex = 2;
            animalsDGW.CellClick += animalsDGW_CellClick;
            // 
            // label2
            // 
            label2.AutoSize = true;
            label2.Location = new Point(470, 9);
            label2.Name = "label2";
            label2.Size = new Size(64, 20);
            label2.TabIndex = 3;
            label2.Text = "Animale";
            // 
            // textBox1
            // 
            textBox1.Location = new Point(562, 376);
            textBox1.MaxLength = 50;
            textBox1.Name = "textBox1";
            textBox1.Size = new Size(125, 27);
            textBox1.TabIndex = 4;
            // 
            // label3
            // 
            label3.AutoSize = true;
            label3.Location = new Point(470, 376);
            label3.Name = "label3";
            label3.Size = new Size(49, 20);
            label3.TabIndex = 6;
            label3.Text = "Nume";
            // 
            // label4
            // 
            label4.AutoSize = true;
            label4.Location = new Point(758, 376);
            label4.Name = "label4";
            label4.Size = new Size(78, 20);
            label4.TabIndex = 7;
            label4.Text = "Cod Cusca";
            // 
            // numericUpDown1
            // 
            numericUpDown1.Location = new Point(878, 374);
            numericUpDown1.Name = "numericUpDown1";
            numericUpDown1.Size = new Size(150, 27);
            numericUpDown1.TabIndex = 8;
            // 
            // button1
            // 
            button1.Location = new Point(470, 433);
            button1.Name = "button1";
            button1.Size = new Size(94, 29);
            button1.TabIndex = 9;
            button1.Text = "Adauga";
            button1.UseVisualStyleBackColor = true;
            button1.Click += button1_Click;
            // 
            // button2
            // 
            button2.Location = new Point(659, 433);
            button2.Name = "button2";
            button2.Size = new Size(94, 29);
            button2.TabIndex = 10;
            button2.Text = "Modifica";
            button2.UseVisualStyleBackColor = true;
            button2.Click += button2_Click;
            // 
            // button3
            // 
            button3.Location = new Point(826, 433);
            button3.Name = "button3";
            button3.Size = new Size(94, 29);
            button3.TabIndex = 11;
            button3.Text = "Sterge";
            button3.UseVisualStyleBackColor = true;
            button3.Click += button3_Click;
            // 
            // MainWindow
            // 
            AutoScaleDimensions = new SizeF(8F, 20F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(1082, 503);
            Controls.Add(button3);
            Controls.Add(button2);
            Controls.Add(button1);
            Controls.Add(numericUpDown1);
            Controls.Add(label4);
            Controls.Add(label3);
            Controls.Add(textBox1);
            Controls.Add(label2);
            Controls.Add(animalsDGW);
            Controls.Add(label1);
            Controls.Add(speciesDGW);
            Name = "MainWindow";
            Text = "Specii-Animale";
            Load += MainWindow_Load;
            ((System.ComponentModel.ISupportInitialize)speciesDGW).EndInit();
            ((System.ComponentModel.ISupportInitialize)animalsDGW).EndInit();
            ((System.ComponentModel.ISupportInitialize)numericUpDown1).EndInit();
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private DataGridView speciesDGW;
        private Label label1;
        private DataGridView animalsDGW;
        private Label label2;
        private TextBox textBox1;
        private Label label3;
        private Label label4;
        private NumericUpDown numericUpDown1;
        private Button button1;
        private Button button2;
        private Button button3;
    }
}
