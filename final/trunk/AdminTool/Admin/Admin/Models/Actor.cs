using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Admin.Models
{
    public class Actor
    {   
        public string id { get; set; }
        public int actorId { get; set; }
        public String actorName { get; set;}

        public String actorImage { get; set; }

        public String desc { get; set; }

        public Actor(int actorId, String actorName, String actorImage, String desc)
        {
            this.actorId = actorId;
            this.actorName = actorName;
            this.actorImage = actorImage;
            this.desc = desc;
        }
    }
}
