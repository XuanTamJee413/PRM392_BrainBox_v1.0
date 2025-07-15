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
    [Authorize(Roles = "admin,teacher,user")]
    public class UsersController : ODataController
    {
        private readonly BrainBoxDbContext _context;

        public UsersController(BrainBoxDbContext context)
        {
            _context = context;
        }
        [EnableQuery]
        [AllowAnonymous]
        public IActionResult Get([FromODataUri] int key)
        {
            var user = _context.Users.FirstOrDefault(u => u.Id == key);
            if (user == null) return NotFound();
            return Ok(user);
        }

        [EnableQuery]
        [AllowAnonymous]
        public IActionResult Get()
        {
            return Ok(_context.Users.AsQueryable());
        }

        public async Task<IActionResult> Post([FromBody] User user)
        {
            _context.Users.Add(user);
            await _context.SaveChangesAsync();
            return Created(user);
        }
        public async Task<IActionResult> Put([FromODataUri] int key, [FromBody] User user)
        {
            if (key != user.Id) return BadRequest();

            _context.Entry(user).State = EntityState.Modified;
            await _context.SaveChangesAsync();
            return Updated(user);
        }
        public async Task<IActionResult> Patch([FromODataUri] int key, [FromBody] Delta<User> delta)
        {
            var entity = await _context.Users.FindAsync(key);
            if (entity == null) return NotFound();

            delta.Patch(entity);
            await _context.SaveChangesAsync();
            return Updated(entity);
        }
        public async Task<IActionResult> Delete([FromODataUri] int key)
        {
            var user = await _context.Users.FindAsync(key);
            if (user == null) return NotFound();

            _context.Users.Remove(user);
            await _context.SaveChangesAsync();
            return NoContent();
        }

    }
}
