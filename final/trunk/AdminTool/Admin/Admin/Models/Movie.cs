using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Admin.Models
{
    public class Movie
    {
        #region property
        public String id { get; set; }

        public int movieId { get; set; }
        public DateTime date { get; set; }
        public String type { get; set; }
        public String name { get; set; }
        public String linkImage { get; set; }

        public string linkTrailer { get; set; }

        public double rate { get; set; }

        public int numRate { get; set; }

        public String desc { get; set; }
        #endregion
        public Movie(int movieId,String name,String type, DateTime date, String linkImage, String linkTrailer, String desc)
        {
            this.movieId = movieId;
            this.name = name;
            this.type = type;
            this.date = date;
            this.linkImage = linkImage;
            this.linkTrailer = linkTrailer;
            this.rate = 5.0;
            this.numRate = 1;
            this.desc = desc;
            
        }
    }
}
