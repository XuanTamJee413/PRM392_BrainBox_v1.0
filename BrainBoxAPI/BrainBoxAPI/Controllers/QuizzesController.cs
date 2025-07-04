using BrainBoxAPI.Data;
using BrainBoxAPI.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
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
            return Ok(_context.Quizzes.Include(q => q.User));
        }

        [EnableQuery]
        public IActionResult Get([FromODataUri] int key)
        {
            var quiz = _context.Quizzes.Include(q => q.User).FirstOrDefault(q => q.QuizId == key);
            if (quiz == null) return NotFound();
            return Ok(quiz);
        }

        public async Task<IActionResult> Post([FromBody] Quiz quiz)
        {
            _context.Quizzes.Add(quiz);
            await _context.SaveChangesAsync();
            return Created(quiz);
        }
    }
}
