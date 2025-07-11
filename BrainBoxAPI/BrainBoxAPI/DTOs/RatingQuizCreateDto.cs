using System.ComponentModel.DataAnnotations;

namespace BrainBoxAPI.DTOs
{
    public class RatingQuizCreateDto
    {
        [Required]
        public int QuizId { get; set; }

        [Required]
        [Range(1, 5)]
        public int Rating { get; set; }

        public string? Comment { get; set; }
    }
}