namespace SGBD_L2
{
    partial class MainWindow
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
            this.button1 = new System.Windows.Forms.Button();
            this.button2 = new System.Windows.Forms.Button();
            this.button3 = new System.Windows.Forms.Button();
            this.panel1 = new System.Windows.Forms.Panel();
            ((System.ComponentModel.ISupportInitialize)(this.parentDGW)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.childDGW)).BeginInit();
            this.SuspendLayout();
            // 
            // parentDGW
            // 
            this.parentDGW.AllowUserToAddRows = false;
            this.parentDGW.AllowUserToDeleteRows = false;
            this.parentDGW.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.parentDGW.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.parentDGW.Location = new System.Drawing.Point(14, 14);
            this.parentDGW.Margin = new System.Windows.Forms.Padding(4);
            this.parentDGW.MultiSelect = false;
            this.parentDGW.Name = "parentDGW";
            this.parentDGW.ReadOnly = true;
            this.parentDGW.RowHeadersWidth = 51;
            this.parentDGW.RowTemplate.Height = 24;
            this.parentDGW.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.parentDGW.Size = new System.Drawing.Size(400, 526);
            this.parentDGW.TabIndex = 0;
            this.parentDGW.CellClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.parentDGW_CellClick);
            // 
            // childDGW
            // 
            this.childDGW.AllowUserToAddRows = false;
            this.childDGW.AllowUserToDeleteRows = false;
            this.childDGW.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.childDGW.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.childDGW.Location = new System.Drawing.Point(484, 13);
            this.childDGW.Margin = new System.Windows.Forms.Padding(4);
            this.childDGW.MultiSelect = false;
            this.childDGW.Name = "childDGW";
            this.childDGW.ReadOnly = true;
            this.childDGW.RowHeadersWidth = 51;
            this.childDGW.RowTemplate.Height = 24;
            this.childDGW.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.childDGW.Size = new System.Drawing.Size(585, 300);
            this.childDGW.TabIndex = 1;
            this.childDGW.CellClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.childDGW_CellClick);
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(984, 354);
            this.button1.Margin = new System.Windows.Forms.Padding(4);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(85, 26);
            this.button1.TabIndex = 2;
            this.button1.Text = "Adauga";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // button2
            // 
            this.button2.Location = new System.Drawing.Point(984, 434);
            this.button2.Margin = new System.Windows.Forms.Padding(4);
            this.button2.Name = "button2";
            this.button2.Size = new System.Drawing.Size(85, 26);
            this.button2.TabIndex = 3;
            this.button2.Text = "Modifica";
            this.button2.UseVisualStyleBackColor = true;
            this.button2.Click += new System.EventHandler(this.button2_Click);
            // 
            // button3
            // 
            this.button3.Location = new System.Drawing.Point(984, 514);
            this.button3.Margin = new System.Windows.Forms.Padding(4);
            this.button3.Name = "button3";
            this.button3.Size = new System.Drawing.Size(85, 26);
            this.button3.TabIndex = 4;
            this.button3.Text = "Sterge";
            this.button3.UseVisualStyleBackColor = true;
            this.button3.Click += new System.EventHandler(this.button3_Click);
            // 
            // panel1
            // 
            this.panel1.Location = new System.Drawing.Point(484, 320);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(493, 220);
            this.panel1.TabIndex = 5;
            // 
            // MainWindow
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(9F, 18F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1082, 553);
            this.Controls.Add(this.panel1);
            this.Controls.Add(this.button3);
            this.Controls.Add(this.button2);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.childDGW);
            this.Controls.Add(this.parentDGW);
            this.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.Margin = new System.Windows.Forms.Padding(4);
            this.Name = "MainWindow";
            this.Load += new System.EventHandler(this.MainWindow_Load);
            ((System.ComponentModel.ISupportInitialize)(this.parentDGW)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.childDGW)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.DataGridView parentDGW;
        private System.Windows.Forms.DataGridView childDGW;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button button2;
        private System.Windows.Forms.Button button3;
        private System.Windows.Forms.Panel panel1;
    }
}

