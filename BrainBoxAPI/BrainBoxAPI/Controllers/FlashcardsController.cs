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
    public class FlashcardsController : ODataController
    {
        private readonly BrainBoxDbContext _context;
        public FlashcardsController(BrainBoxDbContext context)
        {
            _context = context;
        }
        [EnableQuery]
        public IActionResult Get()
        {
            return Ok(_context.Flashcards.Include(f => f.Creator).Include(f => f.Quiz));
        }
        [EnableQuery]
        public IActionResult Get([FromODataUri] int key)
        {
            var flashcard = _context.Flashcards
                .Include(f => f.Creator)
                .Include(f => f.Quiz)
                .FirstOrDefault(f => f.CardId == key);

            if (flashcard == null) return NotFound();
            return Ok(flashcard);
        }

        public async Task<IActionResult> Post([FromBody] Flashcard flashcard)
        {
            _context.Flashcards.Add(flashcard);
            await _context.SaveChangesAsync();
            return Created(flashcard);
        }

        public async Task<IActionResult> Put([FromODataUri] int key, [FromBody] Flashcard flashcard)
        {
            if (key != flashcard.CardId) return BadRequest();

            _context.Entry(flashcard).State = EntityState.Modified;
            await _context.SaveChangesAsync();
            return Updated(flashcard);
        }

        public async Task<IActionResult> Patch([FromODataUri] int key, [FromBody] Delta<Flashcard> delta)
        {
            var entity = await _context.Flashcards.FindAsync(key);
            if (entity == null) return NotFound();

            delta.Patch(entity);
            await _context.SaveChangesAsync();
            return Updated(entity);
        }

        public async Task<IActionResult> Delete([FromODataUri] int key)
        {
            var flashcard = await _context.Flashcards.FindAsync(key);
            if (flashcard == null) return NotFound();

            _context.Flashcards.Remove(flashcard);
            await _context.SaveChangesAsync();
            return NoContent();
        }
    }
}
