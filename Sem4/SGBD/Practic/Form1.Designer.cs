namespace SGBD_Practic
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
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
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.parentDGW = new System.Windows.Forms.DataGridView();
            this.childDGW = new System.Windows.Forms.DataGridView();
            this.panel1 = new System.Windows.Forms.Panel();
            this.pretTB = new System.Windows.Forms.TextBox();
            this.anulAparitieiTB = new System.Windows.Forms.TextBox();
            this.numarPaginiTB = new System.Windows.Forms.TextBox();
            this.titluTB = new System.Windows.Forms.TextBox();
            this.button1 = new System.Windows.Forms.Button();
            this.button2 = new System.Windows.Forms.Button();
            this.button3 = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.parentDGW)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.childDGW)).BeginInit();
            this.panel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // parentDGW
            // 
            this.parentDGW.AllowUserToAddRows = false;
            this.parentDGW.AllowUserToDeleteRows = false;
            this.parentDGW.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.AllCells;
            this.parentDGW.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.parentDGW.Location = new System.Drawing.Point(12, 12);
            this.parentDGW.MultiSelect = false;
            this.parentDGW.Name = "parentDGW";
            this.parentDGW.ReadOnly = true;
            this.parentDGW.RowTemplate.Height = 24;
            this.parentDGW.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.parentDGW.Size = new System.Drawing.Size(400, 529);
            this.parentDGW.TabIndex = 0;
            this.parentDGW.CellClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.parentDGW_CellClick);
            // 
            // childDGW
            // 
            this.childDGW.AllowUserToAddRows = false;
            this.childDGW.AllowUserToDeleteRows = false;
            this.childDGW.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.AllCells;
            this.childDGW.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.childDGW.Location = new System.Drawing.Point(485, 12);
            this.childDGW.MultiSelect = false;
            this.childDGW.Name = "childDGW";
            this.childDGW.ReadOnly = true;
            this.childDGW.RowTemplate.Height = 24;
            this.childDGW.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.childDGW.Size = new System.Drawing.Size(585, 300);
            this.childDGW.TabIndex = 1;
            this.childDGW.CellClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.childDGW_CellClick);
            // 
            // panel1
            // 
            this.panel1.Controls.Add(this.pretTB);
            this.panel1.Controls.Add(this.anulAparitieiTB);
            this.panel1.Controls.Add(this.numarPaginiTB);
            this.panel1.Controls.Add(this.titluTB);
            this.panel1.Location = new System.Drawing.Point(485, 318);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(504, 223);
            this.panel1.TabIndex = 2;
            // 
            // pretTB
            // 
            this.pretTB.Location = new System.Drawing.Point(7, 179);
            this.pretTB.Name = "pretTB";
            this.pretTB.Size = new System.Drawing.Size(167, 22);
            this.pretTB.TabIndex = 3;
            // 
            // anulAparitieiTB
            // 
            this.anulAparitieiTB.Location = new System.Drawing.Point(7, 120);
            this.anulAparitieiTB.Name = "anulAparitieiTB";
            this.anulAparitieiTB.Size = new System.Drawing.Size(167, 22);
            this.anulAparitieiTB.TabIndex = 2;
            // 
            // numarPaginiTB
            // 
            this.numarPaginiTB.Location = new System.Drawing.Point(7, 60);
            this.numarPaginiTB.Name = "numarPaginiTB";
            this.numarPaginiTB.Size = new System.Drawing.Size(167, 22);
            this.numarPaginiTB.TabIndex = 1;
            // 
            // titluTB
            // 
            this.titluTB.Location = new System.Drawing.Point(7, 3);
            this.titluTB.Name = "titluTB";
            this.titluTB.Size = new System.Drawing.Size(171, 22);
            this.titluTB.TabIndex = 0;
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(995, 318);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(75, 23);
            this.button1.TabIndex = 3;
            this.button1.Text = "Add";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // button2
            // 
            this.button2.Location = new System.Drawing.Point(995, 419);
            this.button2.Name = "button2";
            this.button2.Size = new System.Drawing.Size(75, 23);
            this.button2.TabIndex = 4;
            this.button2.Text = "Update";
            this.button2.UseVisualStyleBackColor = true;
            this.button2.Click += new System.EventHandler(this.button2_Click);
            // 
            // button3
            // 
            this.button3.Location = new System.Drawing.Point(995, 518);
            this.button3.Name = "button3";
            this.button3.Size = new System.Drawing.Size(75, 23);
            this.button3.TabIndex = 5;
            this.button3.Text = "Delete";
            this.button3.UseVisualStyleBackColor = true;
            this.button3.Click += new System.EventHandler(this.button3_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1082, 553);
            this.Controls.Add(this.button3);
            this.Controls.Add(this.button2);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.panel1);
            this.Controls.Add(this.childDGW);
            this.Controls.Add(this.parentDGW);
            this.Name = "Form1";
            this.Text = "Form1";
            this.Load += new System.EventHandler(this.Form1_Load);
            ((System.ComponentModel.ISupportInitialize)(this.parentDGW)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.childDGW)).EndInit();
            this.panel1.ResumeLayout(false);
            this.panel1.PerformLayout();
            this.ResumeLayout(false);
        }

        private System.Windows.Forms.TextBox numarPaginiTB;
        private System.Windows.Forms.TextBox anulAparitieiTB;

        private System.Windows.Forms.TextBox titluTB;
        private System.Windows.Forms.TextBox pretTB;

        private System.Windows.Forms.DataGridView parentDGW;
        private System.Windows.Forms.DataGridView childDGW;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button button2;
        private System.Windows.Forms.Button button3;

        #endregion
    }
}