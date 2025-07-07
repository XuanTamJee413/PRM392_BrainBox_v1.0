using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace BrainBoxAPI.Models
{
    public class DocumentDetail
    {
        [Key]
        public int DocDetailId { get; set; }

        [ForeignKey("Document")]
        public int DocId { get; set; }

        public string ImageUrl { get; set; }

        public string? Caption { get; set; }

        public long CreatedAt { get; set; }

        public Document? Document { get; set; }

    }
}
