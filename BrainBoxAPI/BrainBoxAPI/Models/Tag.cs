namespace BrainBoxAPI.Models
{
    public class Tag
    {
        public int TagId { get; set; }
        public string TagName { get; set; }
        public List<Document> Documents { get; set; } = new List<Document>();
    }
}
