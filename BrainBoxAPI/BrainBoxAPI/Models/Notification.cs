using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace BrainBoxAPI.Models
{
    public class Notification
    {
        [Key]
        public int NotificationId { get; set; }

        [ForeignKey("User")]
        public int UserId { get; set; }

        public string Content { get; set; }
        public string Type { get; set; }
        public int RelatedId { get; set; }
        public bool IsRead { get; set; } = false;
        public long ReadAt { get; set; } = 0;
        public long CreatedAt { get; set; }

        public User? User { get; set; }
    }

}
