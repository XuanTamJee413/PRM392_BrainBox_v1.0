using System.ComponentModel.DataAnnotations;

namespace BrainBoxAPI.Models
{
    public class Challenge
    {
        [Key]
        public int ChallengeId { get; set; }
        public int QuizId { get; set; }
        public int ChallengerId { get; set; }
        public int OpponentId { get; set; }
        public int Status { get; set; }
        public int ChallengerScore { get; set; }
        public int OpponentScore { get; set; }
        public long CreatedAt { get; set; }

        public Quiz? Quiz { get; set; }
        public User? Challenger { get; set; }
        public User? Opponent { get; set; }
    }

}
