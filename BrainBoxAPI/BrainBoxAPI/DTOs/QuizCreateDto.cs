using System.ComponentModel.DataAnnotations;

namespace BrainBoxAPI.DTOs
{
    public class QuizCreateDto
    {
        [Required]
        public string QuizName { get; set; }
        public string? Description { get; set; }
        public bool IsPublic { get; set; } = true;

        [Required]
        public List<FlashcardCreateDto> Flashcards { get; set; }
    }

   
}

