using BrainBoxAPI.Data;
using BrainBoxAPI.DTOs;
using BrainBoxAPI.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Security.Claims;

namespace BrainBoxAPI.Controllers
{
    [Authorize]
    [ApiController] 
    [Route("api/[controller]")] 
    public class RatingQuizzesController : ControllerBase 
    {
        private readonly BrainBoxDbContext _context;

        public RatingQuizzesController(BrainBoxDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        [AllowAnonymous]

        public async Task<IActionResult> Get([FromQuery] int quizId)
        {
            var userIdString = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (!int.TryParse(userIdString, out var userId))
            {
                return Unauthorized();
            }

            var rating = await _context.RatingQuizzes
                .Where(r => r.UserId == userId && r.QuizId == quizId)
                .FirstOrDefaultAsync();

            if (rating == null)
            {
                return NotFound();
            }
            return Ok(rating);
        }

        [HttpGet("getRatingQuizById")]
        [AllowAnonymous]
        public async Task<IActionResult> GetRatingQuizById([FromQuery] int quizId)
        {
            var ratings = await _context.RatingQuizzes
                .Where(r => r.QuizId == quizId)
                .ToListAsync();

            if (ratings == null || !ratings.Any()) 
            {
                return NotFound("No ratings found for the specified quiz ID.");
            }

            return Ok(ratings);
        }

        [HttpPost]
        public async Task<IActionResult> Post([FromBody] RatingQuizCreateDto ratingDto)
        {
            var userIdString = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (!int.TryParse(userIdString, out var userId))
            {
                return Unauthorized();
            }

            var existingRating = await _context.RatingQuizzes
                .FirstOrDefaultAsync(r => r.QuizId == ratingDto.QuizId && r.UserId == userId);

            if (existingRating != null)
            {
                existingRating.Rating = ratingDto.Rating;
                existingRating.Comment = ratingDto.Comment;
                existingRating.RatedAt = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
            }
            else
            {
                var newRating = new RatingQuiz
                {
                    QuizId = ratingDto.QuizId,
                    UserId = userId,
                    Rating = ratingDto.Rating,
                    Comment = ratingDto.Comment,
                    RatedAt = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds()
                };
                _context.RatingQuizzes.Add(newRating);
            }

            await _context.SaveChangesAsync();
            return Ok();
        }
    }
}