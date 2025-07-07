namespace BrainBoxAPI.Models
{
    public class Bookmark
    {
        public int UserId { get; set; }
        public int QuizId { get; set; }
        public int? LastCardId { get; set; }
        public long BookmarkedAt { get; set; }

        public User? User { get; set; }
        public Quiz? Quiz { get; set; }
        public Flashcard? Flashcard { get; set; }
    }

}
