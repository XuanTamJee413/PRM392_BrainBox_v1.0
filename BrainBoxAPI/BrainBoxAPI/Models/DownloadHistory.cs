namespace BrainBoxAPI.Models
{
    public class DownloadHistory
    {
        public int UserId { get; set; }

        public int TargetId { get; set; }

        public string TargetType { get; set; } 

        public long DownloadedAt { get; set; }

        public User? User { get; set; }
    }

}
