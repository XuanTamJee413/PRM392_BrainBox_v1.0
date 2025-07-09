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
    public class DocumentsController : ODataController
    {
        private readonly BrainBoxDbContext _context;

        public DocumentsController(BrainBoxDbContext context)
        {
            _context = context;
        }
        [AllowAnonymous]
        [EnableQuery]
        public IActionResult Get()
        {
            return Ok(_context.Documents.Include(d => d.Author));
        }
        [AllowAnonymous]
        [EnableQuery]
        public IActionResult Get([FromODataUri] int key)
        {
            var doc = _context.Documents.Include(d => d.Author).FirstOrDefault(d => d.DocId == key);
            if (doc == null) return NotFound();
            return Ok(doc);
        }

        public async Task<IActionResult> Post([FromBody] Document doc)
        {
            _context.Documents.Add(doc);
            await _context.SaveChangesAsync();
            return Created(doc);
        }
        public async Task<IActionResult> Put([FromODataUri] int key, [FromBody] Document doc)
        {
            if (key != doc.DocId) return BadRequest();

            _context.Entry(doc).State = EntityState.Modified;
            await _context.SaveChangesAsync();
            return Updated(doc);
        }
        public async Task<IActionResult> Patch([FromODataUri] int key, [FromBody] Delta<Document> delta)
        {
            var entity = await _context.Documents.FindAsync(key);
            if (entity == null) return NotFound();

            delta.Patch(entity);
            await _context.SaveChangesAsync();
            return Updated(entity);
        }
        public async Task<IActionResult> Delete([FromODataUri] int key)
        {
            var doc = await _context.Documents.FindAsync(key);
            if (doc == null) return NotFound();

            _context.Documents.Remove(doc);
            await _context.SaveChangesAsync();
            return NoContent();
        }

    }
}
