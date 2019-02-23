using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Admin.Models
{
    public class Acting
    {
        public String id { get; set; }
        public int actorId { get; set; }
        public int movieId { get; set; }

        public Acting(int actorId, int movieId)
        {
            this.actorId = actorId;
            this.movieId = movieId;
        }
    }
}
