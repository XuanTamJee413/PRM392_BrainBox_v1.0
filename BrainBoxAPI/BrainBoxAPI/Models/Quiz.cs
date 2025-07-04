using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace BrainBoxAPI.Models
{
    public class Quiz
    {
        [Key]
        public int QuizId { get; set; }
        public string QuizName { get; set; }
        public string Description { get; set; }

        [ForeignKey("User")]
        public int CreatorId { get; set; }
        public bool IsPublic { get; set; }
        public long CreatedAt { get; set; }

        public User? User { get; set; }
    }
}
