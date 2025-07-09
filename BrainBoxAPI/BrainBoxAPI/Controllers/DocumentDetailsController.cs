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
    public class DocumentDetailsController : ODataController
    {
        private readonly BrainBoxDbContext _context;

        public DocumentDetailsController(BrainBoxDbContext context)
        {
            _context = context;
        }
        [EnableQuery]
        public IActionResult Get()
        {
            return Ok(_context.DocumentDetails);
        }
        [EnableQuery]
        public IActionResult Get([FromODataUri] int key)
        {
            var detail = _context.DocumentDetails.Where(d => d.DocDetailId == key);
            return Ok(SingleResult.Create(detail));
        }

        public async Task<IActionResult> Post([FromBody] DocumentDetail detail)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            _context.DocumentDetails.Add(detail);
            await _context.SaveChangesAsync();
            return Created(detail);
        }

        public async Task<IActionResult> Put([FromODataUri] int key, [FromBody] DocumentDetail updated)
        {
            if (key != updated.DocDetailId)
                return BadRequest();

            _context.Entry(updated).State = EntityState.Modified;
            await _context.SaveChangesAsync();
            return Updated(updated);
        }

        public async Task<IActionResult> Patch([FromODataUri] int key, [FromBody] Delta<DocumentDetail> delta)
        {
            var entity = await _context.DocumentDetails.FindAsync(key);
            if (entity == null) return NotFound();

            delta.Patch(entity);
            await _context.SaveChangesAsync();
            return Updated(entity);
        }

        public async Task<IActionResult> Delete([FromODataUri] int key)
        {
            var entity = await _context.DocumentDetails.FindAsync(key);
            if (entity == null) return NotFound();

            _context.DocumentDetails.Remove(entity);
            await _context.SaveChangesAsync();
            return NoContent();
        }
    }
}
