using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace BrainBoxAPI.Models
{
    public class Flashcard
    {
        [Key]
        public int CardId { get; set; }

        [ForeignKey("Quiz")]
        public int QuizId { get; set; }

        public string Question { get; set; }
        public string Option1 { get; set; }
        public string Option2 { get; set; }
        public string Option3 { get; set; }
        public string Option4 { get; set; }
        public int Answer { get; set; }

        public int CreatorId { get; set; }
        public long CreatedAt { get; set; }

        [ForeignKey("CreatorId")]
        public User? Creator { get; set; }

        public Quiz? Quiz { get; set; }
    }

}
