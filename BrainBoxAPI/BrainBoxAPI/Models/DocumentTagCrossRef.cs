namespace BrainBoxAPI.Models
{
    public class DocumentTagCrossRef
    {
        public int DocId { get; set; }
        public int TagId { get; set; }

        public Document? Document { get; set; }
        public Tag? Tag { get; set; }
    }

}
