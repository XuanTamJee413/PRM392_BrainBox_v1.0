using BrainBoxAPI.Data;
using BrainBoxAPI.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.OData.Deltas;
using Microsoft.AspNetCore.OData.Formatter;
using Microsoft.AspNetCore.OData.Query;
using Microsoft.AspNetCore.OData.Results;
using Microsoft.AspNetCore.OData.Routing.Controllers;
using Microsoft.EntityFrameworkCore;

namespace BrainBoxAPI.Controllers
{
    [Authorize]
    public class CommentsController : ODataController
    {
        private readonly BrainBoxDbContext _context;

        public CommentsController(BrainBoxDbContext context)
        {
            _context = context;
        }

        [EnableQuery]
        public IActionResult Get()
        {
            return Ok(_context.Comments
                .Include(c => c.User)
                .Include(c => c.DocumentDetail));
        }

        [EnableQuery]
        public IActionResult Get([FromODataUri] int key)
        {
            var comment = _context.Comments
                .Include(c => c.User)
                .Include(c => c.DocumentDetail)
                .Where(c => c.CommentId == key);

            return Ok(SingleResult.Create(comment));
        }

        public async Task<IActionResult> Post([FromBody] Comment comment)
        {
            _context.Comments.Add(comment);
            await _context.SaveChangesAsync();
            return Created(comment);
        }

        public async Task<IActionResult> Put([FromODataUri] int key, [FromBody] Comment comment)
        {
            if (key != comment.CommentId)
                return BadRequest();

            _context.Entry(comment).State = EntityState.Modified;
            await _context.SaveChangesAsync();
            return Updated(comment);
        }

        public async Task<IActionResult> Patch([FromODataUri] int key, [FromBody] Delta<Comment> delta)
        {
            var entity = await _context.Comments.FindAsync(key);
            if (entity == null) return NotFound();

            delta.Patch(entity);
            await _context.SaveChangesAsync();
            return Updated(entity);
        }

        public async Task<IActionResult> Delete([FromODataUri] int key)
        {
            var comment = await _context.Comments.FindAsync(key);
            if (comment == null) return NotFound();

            _context.Comments.Remove(comment);
            await _context.SaveChangesAsync();
            return NoContent();
        }
    }
}
