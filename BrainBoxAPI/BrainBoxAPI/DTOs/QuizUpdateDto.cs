using System.ComponentModel.DataAnnotations;

namespace BrainBoxAPI.DTOs
{
    public class QuizUpdateDto
    {
        [Required]
        public string QuizName { get; set; }
        public string? Description { get; set; }
        public bool IsPublic { get; set; }

        public List<FlashcardUpdateDto> Flashcards { get; set; }

        public List<int>? FlashcardIdsToDelete { get; set; }
    }
}