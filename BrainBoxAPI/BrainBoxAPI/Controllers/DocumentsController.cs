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
    public class DocumentsController : ODataController
    {
        private readonly BrainBoxDbContext _context;

        public DocumentsController(BrainBoxDbContext context)
        {
            _context = context;
        }

        [EnableQuery]
        public IActionResult Get()
        {
            return Ok(_context.Documents.Include(d => d.Author));
        }

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
    }
}
