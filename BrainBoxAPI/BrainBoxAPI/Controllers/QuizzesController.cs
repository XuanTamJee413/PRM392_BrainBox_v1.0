using BrainBoxAPI.Data;
using BrainBoxAPI.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.OData.Deltas;
using Microsoft.AspNetCore.OData.Formatter;
using Microsoft.AspNetCore.OData.Query;
using Microsoft.AspNetCore.OData.Routing.Controllers;
using Microsoft.EntityFrameworkCore;

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

        [EnableQuery]
        public IActionResult Get()
        {
            return Ok(_context.Quizzes.Include(q => q.Creator));
        }

        [EnableQuery]
        public IActionResult Get([FromODataUri] int key)
        {
            var quiz = _context.Quizzes.Include(q => q.Creator).FirstOrDefault(q => q.QuizId == key);
            if (quiz == null) return NotFound();
            return Ok(quiz);
        }

        public async Task<IActionResult> Post([FromBody] Quiz quiz)
        {
            _context.Quizzes.Add(quiz);
            await _context.SaveChangesAsync();
            return Created(quiz);
        }
        public async Task<IActionResult> Put([FromODataUri] int key, [FromBody] Quiz quiz)
        {
            if (key != quiz.QuizId) return BadRequest();

            _context.Entry(quiz).State = EntityState.Modified;
            await _context.SaveChangesAsync();
            return Updated(quiz);
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
