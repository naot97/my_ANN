using Admin.Models;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Admin
{
    public partial class Form4 : Form
    {
        public Form4()
        {
            InitializeComponent();
        }

        private void Form4_Load(object sender, EventArgs e)
        {

        }

       

        private void button2_Click(object sender, EventArgs e)
        {
            MessageBox.Show("You sure !!!");
            this.Close();
            Form2 form2 = new Form2();
            form2.Show();
        }

        List<int> StringToList(String s)
        {try
            {
                List<String> lst = s.Split(',').ToList<String>();
                List<int> result = new List<int>();
                    foreach (string i in lst)
                        result.Add(int.Parse(i));
                return result;
            }
            catch
            {
                return new List<int>();
            }
        }

        private async void button4_Click(object sender, EventArgs e)
        {
            try
            {
                int actorId = 1 + ((List<Actor>)await Program.MobileService.GetTable<Actor>().ToListAsync()).Count;
                String name = textBox1.Text;
                String image = textBox2.Text;
                String desc = textBox5.Text;
                Actor actor = new Actor(actorId, name, image, desc);
                List<int> listMovieId = StringToList(textBox3.Text);
                foreach (int movieId in listMovieId)
                {
                    Acting acting = new Acting(actorId, movieId);
                    await Program.MobileService.GetTable<Acting>().InsertAsync(acting);
                }
                await Program.MobileService.GetTable<Actor>().InsertAsync(actor);
                MessageBox.Show("Create success!");
            }
            catch
            {
                MessageBox.Show("Faild!");
            }


        }

        private void label5_Click(object sender, EventArgs e)
        {

        }
    }
}
