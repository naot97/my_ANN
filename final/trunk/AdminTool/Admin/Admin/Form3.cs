using Admin.Models;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Admin
{
    public partial class Form3 : Form
    {
        public Form3()
        {
            InitializeComponent();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            MessageBox.Show("You sure !!!");
            this.Close();
            Form2 form2 = new Form2();
            form2.Show();
        }

        public static String ListToString(ListBox.SelectedObjectCollection a)
        {
            try
            {
                String s = "";
                for (int i = 1; i < a.Count; i++)
                    s += "-" + a[i].ToString();
                s = a[0].ToString() + s;

                return s;
            }
            catch
            {
                return "";
            }
        }

        private async void button1_Click(object sender, EventArgs e)
        {
             try
            {
                String name = textBox1.Text;
                String desc = textBox2.Text;
                String image = textBox3.Text;
                DateTime date = dateTimePicker1.Value;
                String type = ListToString(checkedListBox1.SelectedItems) + ListToString(checkedListBox2.SelectedItems);
                String trailer = textBox4.Text;
                int movieId = 1 + ((List<Movie>)await Program.MobileService.GetTable<Movie>().ToListAsync()).Count;
                Movie m = new Movie(movieId, name, type, date, image, trailer, desc);
                await Program.MobileService.GetTable<Movie>().InsertAsync(m);
                MessageBox.Show("Create Movie Success!");
            }
            catch
            {
                MessageBox.Show("Fail ! Please check.");
            }
            
        }

        private void label6_Click(object sender, EventArgs e)
        {

        }
    }
}
