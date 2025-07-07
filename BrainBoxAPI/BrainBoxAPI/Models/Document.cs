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
        public int AuthorId { get; set; }
        public bool IsPublic { get; set; }
        public int Views { get; set; }
        public long CreatedAt { get; set; }
        [ForeignKey("AuthorId")]
        public User? Author { get; set; }

        public ICollection<DocumentDetail> DocumentDetails { get; set; }

    }
}
