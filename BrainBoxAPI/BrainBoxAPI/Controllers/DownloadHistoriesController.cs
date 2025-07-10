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
    public class DownloadHistoriesController : ODataController
    {
        private readonly BrainBoxDbContext _context;

        public DownloadHistoriesController(BrainBoxDbContext context)
        {
            _context = context;
        }

        [EnableQuery]
        public IActionResult Get()
        {
            return Ok(_context.DownloadHistories.Include(d => d.User));
        }

        [EnableQuery]
        public IActionResult Get([FromODataUri] int key)
        {
            var record = _context.DownloadHistories
                .Include(d => d.User)
                .FirstOrDefault(d => d.Id == key);

            if (record == null) return NotFound();
            return Ok(record);
        }

        public async Task<IActionResult> Post([FromBody] DownloadHistory history)
        {
            _context.DownloadHistories.Add(history);
            await _context.SaveChangesAsync();
            return Created(history);
        }

        public async Task<IActionResult> Put([FromODataUri] int key, [FromBody] DownloadHistory history)
        {
            if (key != history.Id) return BadRequest();

            _context.Entry(history).State = EntityState.Modified;
            await _context.SaveChangesAsync();
            return Updated(history);
        }

        public async Task<IActionResult> Patch([FromODataUri] int key, [FromBody] Delta<DownloadHistory> delta)
        {
            var entity = await _context.DownloadHistories.FindAsync(key);
            if (entity == null) return NotFound();

            delta.Patch(entity);
            await _context.SaveChangesAsync();
            return Updated(entity);
        }

        public async Task<IActionResult> Delete([FromODataUri] int key)
        {
            var record = await _context.DownloadHistories.FindAsync(key);
            if (record == null) return NotFound();

            _context.DownloadHistories.Remove(record);
            await _context.SaveChangesAsync();
            return NoContent();
        }

    }
}
