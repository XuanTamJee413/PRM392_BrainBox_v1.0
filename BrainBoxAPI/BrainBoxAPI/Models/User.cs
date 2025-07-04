using System.ComponentModel.DataAnnotations;

namespace BrainBoxAPI.Models
{
    public class User
    {
        [Key]
        public int Id { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }
        public string Role { get; set; }
        public string Email { get; set; }
        public bool Status { get; set; }
        public string Avatar { get; set; }
        public long CreatedAt { get; set; }
        public long PremiumExpiredAt { get; set; }

        public bool IsPremium => PremiumExpiredAt > DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
        public ICollection<Quiz> Quizzes { get; set; } = new List<Quiz>();
        public ICollection<Document> Documents { get; set; } = new List<Document>();

    }
}
