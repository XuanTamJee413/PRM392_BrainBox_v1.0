namespace BrainBoxAPI.Models
{
    public class Document
    {
        public int DocId { get; set; }
        public string Title { get; set; }
        public string Content { get; set; }
        public string AuthorId { get; set; }
        public DateTime CreatedAt { get; set; }
        public List<Tag> Tags { get; set; } = new List<Tag>();
    }
}
