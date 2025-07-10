using BrainBoxAPI.Data;
using BrainBoxAPI.DTOs;
using BrainBoxAPI.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.OData.Deltas;
using Microsoft.AspNetCore.OData.Formatter;
using Microsoft.AspNetCore.OData.Query;
using Microsoft.AspNetCore.OData.Routing.Controllers;
using Microsoft.EntityFrameworkCore;
using System.Security.Claims;

namespace BrainBoxAPI.Controllers
{
    [Authorize]
    public class QuizzesController : ODataController
    {
        private readonly BrainBoxDbContext _context;

        public QuizzesController(BrainBoxDbContext context)
        {
            _context = context;
        }
        [AllowAnonymous]
        [EnableQuery]
        public IActionResult Get()
        {
            return Ok(_context.Quizzes.Include(q => q.Creator));
        }
        [AllowAnonymous]
        [EnableQuery]
        public IActionResult Get([FromODataUri] int key)
        {
            var quiz = _context.Quizzes.Include(q => q.Creator).FirstOrDefault(q => q.QuizId == key);
            if (quiz == null) return NotFound();
            return Ok(quiz);
        }

        

        public async Task<IActionResult> Post([FromBody] QuizCreateDto quizDto)
        {
            var userIdString = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (!int.TryParse(userIdString, out var creatorId))
            {
                return Unauthorized("Không thể xác thực người dùng.");
            }

            using var transaction = await _context.Database.BeginTransactionAsync();

            try
            {
                var newQuiz = new Quiz
                {
                    QuizName = quizDto.QuizName,
                    Description = quizDto.Description,
                    IsPublic = quizDto.IsPublic,
                    CreatorId = creatorId,
                    CreatedAt = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds()
                };

                _context.Quizzes.Add(newQuiz);
                await _context.SaveChangesAsync(); 

                if (quizDto.Flashcards != null && quizDto.Flashcards.Any())
                {
                    foreach (var flashcardDto in quizDto.Flashcards)
                    {
                        var newFlashcard = new Flashcard
                        {
                            Question = flashcardDto.Question,
                            Option1 = flashcardDto.Option1,
                            Option2 = flashcardDto.Option2,
                            Option3 = flashcardDto.Option3,
                            Option4 = flashcardDto.Option4,
                            Answer = flashcardDto.Answer,
                            CreatorId = creatorId,
                            CreatedAt = newQuiz.CreatedAt,
                            QuizId = newQuiz.QuizId 
                        };
                        _context.Flashcards.Add(newFlashcard);
                    }
                    await _context.SaveChangesAsync();
                }

                await transaction.CommitAsync();

                return Created(newQuiz);
            }
            catch (Exception ex)
            {
                await transaction.RollbackAsync();

                return StatusCode(500, $"Lỗi máy chủ nội bộ: {ex.Message}");
            }
        }

        public async Task<IActionResult> Put([FromODataUri] int key, [FromBody] QuizUpdateDto quizDto)
        {
            
            var userIdString = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (!int.TryParse(userIdString, out var currentUserId))
            {
                return Unauthorized();
            }

           
            var quizToUpdate = await _context.Quizzes.FindAsync(key);
            if (quizToUpdate == null)
            {
                return NotFound();
            }

            
            if (quizToUpdate.CreatorId != currentUserId)
            {
                return Forbid(); 
            }

            using var transaction = await _context.Database.BeginTransactionAsync();
            try
            {
               
                quizToUpdate.QuizName = quizDto.QuizName;
                quizToUpdate.Description = quizDto.Description;
                quizToUpdate.IsPublic = quizDto.IsPublic;

                
                if (quizDto.FlashcardIdsToDelete != null && quizDto.FlashcardIdsToDelete.Any())
                {
                    var flashcardsToDelete = await _context.Flashcards
                        .Where(f => f.QuizId == key && quizDto.FlashcardIdsToDelete.Contains(f.CardId))
                        .ToListAsync();
                    _context.Flashcards.RemoveRange(flashcardsToDelete);
                }

              
                foreach (var flashcardDto in quizDto.Flashcards)
                {
                    if (flashcardDto.CardId > 0) 
                    {
                        var flashcardToUpdate = await _context.Flashcards.FindAsync(flashcardDto.CardId);
                        if (flashcardToUpdate != null && flashcardToUpdate.QuizId == key)
                        {
                            flashcardToUpdate.Question = flashcardDto.Question;
                            flashcardToUpdate.Option1 = flashcardDto.Option1;
                            flashcardToUpdate.Option2 = flashcardDto.Option2;
                            flashcardToUpdate.Option3 = flashcardDto.Option3;
                            flashcardToUpdate.Option4 = flashcardDto.Option4;
                            flashcardToUpdate.Answer = flashcardDto.Answer;
                        }
                    }
                    else 
                    {
                        var newFlashcard = new Flashcard
                        {
                            Question = flashcardDto.Question,
                            Option1 = flashcardDto.Option1,
                            Option2 = flashcardDto.Option2,
                            Option3 = flashcardDto.Option3,
                            Option4 = flashcardDto.Option4,
                            Answer = flashcardDto.Answer,
                            CreatorId = currentUserId,
                            CreatedAt = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds(),
                            QuizId = key
                        };
                        _context.Flashcards.Add(newFlashcard);
                    }
                }

                await _context.SaveChangesAsync();
                await transaction.CommitAsync();

                return Updated(quizToUpdate);
            }
            catch (Exception ex)
            {
                await transaction.RollbackAsync();
                return StatusCode(500, $"Lỗi máy chủ nội bộ: {ex.Message}");
            }
        }

        public async Task<IActionResult> Patch([FromODataUri] int key, [FromBody] Delta<Quiz> delta)
        {
            var entity = await _context.Quizzes.FindAsync(key);
            if (entity == null) return NotFound();

            delta.Patch(entity);
            await _context.SaveChangesAsync();
            return Updated(entity);
        }

        public async Task<IActionResult> Delete([FromODataUri] int key)
        {
            var quiz = await _context.Quizzes.FindAsync(key);
            if (quiz == null) return NotFound();

            _context.Quizzes.Remove(quiz);
            await _context.SaveChangesAsync();
            return NoContent();
        }

    }
}
