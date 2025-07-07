using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace BrainBoxAPI.Models
{
    public class Comment
    {
        [Key]   
        public int CommentId { get; set; }

        [ForeignKey("DocumentDetail")]
        public int DocDetailId { get; set; }

        [ForeignKey("User")]
        public int UserId { get; set; }

        public string Content { get; set; }
        public long CreatedAt { get; set; }

        public DocumentDetail? DocumentDetail { get; set; }
        public User? User { get; set; }
    }

}
