using System.ComponentModel.DataAnnotations;

namespace BrainBoxAPI.DTOs
{
    public class FlashcardUpdateDto
    {
        public int CardId { get; set; }

        [Required]
        public string Question { get; set; }

        [Required]
        public string Option1 { get; set; }

        [Required]
        public string Option2 { get; set; }

        [Required]
        public string Option3 { get; set; }

        [Required]
        public string Option4 { get; set; }

        [Required]
        [Range(1, 4)]
        public int Answer { get; set; }
    }
}