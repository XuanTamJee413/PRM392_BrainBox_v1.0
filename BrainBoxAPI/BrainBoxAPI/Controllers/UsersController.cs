using BrainBoxAPI.Data;
using BrainBoxAPI.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.OData.Query;
using Microsoft.AspNetCore.OData.Routing.Controllers;

namespace BrainBoxAPI.Controllers
{
    [Authorize(Roles = "admin")]
    public class UsersController : ODataController
    {
        private readonly BrainBoxDbContext _context;

        public UsersController(BrainBoxDbContext context)
        {
            _context = context;
        }

        [EnableQuery]
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
    }
}
