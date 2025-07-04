using BrainBoxAPI.Data;
using BrainBoxAPI.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.OData.Query;
using Microsoft.AspNetCore.OData.Routing.Controllers;
using Microsoft.EntityFrameworkCore;

namespace BrainBoxAPI.Controllers
{
    [Route("odata/[controller]")]
    [Authorize]
    public class QuizzesController : ODataController
    {
        private readonly BrainBoxDbContext _context;

        public QuizzesController(BrainBoxDbContext context)
        {
            _context = context;
        }

        [EnableQuery]
        [HttpGet]
        public IActionResult Get()
        {
            return Ok(_context.Quizzes.Include(q => q.User));
        }

        [HttpPost]
        public async Task<IActionResult> Post([FromBody] Quiz quiz)
        {
            _context.Quizzes.Add(quiz);
            await _context.SaveChangesAsync();
            return Created(quiz);
        }
    }
}
