using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace BrainBoxAPI.Models
{
    public class RatingQuiz
    {
        [Key]
        public int RatingId { get; set; }

        [ForeignKey("Quiz")]
        public int QuizId { get; set; }

        [ForeignKey("User")]
        public int UserId { get; set; }

        public int Rating { get; set; }
        public string? Comment { get; set; }
        public long RatedAt { get; set; }

        public Quiz? Quiz { get; set; }
        public User? User { get; set; }
    }

}
