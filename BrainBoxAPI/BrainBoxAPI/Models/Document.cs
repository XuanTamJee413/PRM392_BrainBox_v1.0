using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace BrainBoxAPI.Models
{
    public class Document
    {
        [Key]
        public int DocId { get; set; }

        public string Title { get; set; }

        public string Content { get; set; }

        [ForeignKey("User")]
        public int AuthorId { get; set; }

        public bool IsPublic { get; set; }

        public int Views { get; set; }

        public long CreatedAt { get; set; }

        public User? User { get; set; }
    }
}
