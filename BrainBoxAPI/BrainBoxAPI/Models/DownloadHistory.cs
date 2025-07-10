using System.ComponentModel.DataAnnotations;

namespace BrainBoxAPI.Models
{
    public class DownloadHistory
    {
        [Key]
        public int Id { get; set; }
        public int UserId { get; set; }

        public int TargetId { get; set; }
        public string TargetType { get; set; } = string.Empty;

        public long DownloadedAt { get; set; }

        public User? User { get; set; }
    }

}
