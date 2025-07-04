using BrainBoxAPI.Data;
using BrainBoxAPI.DTOs;
using BrainBoxAPI.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace BrainBoxAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthController : ControllerBase
    {
        private readonly BrainBoxDbContext _context;
        private readonly TokenService _tokenService;

        public AuthController(BrainBoxDbContext context, TokenService tokenService)
        {
            _context = context;
            _tokenService = tokenService;
        }

        [HttpPost("login")]
        public IActionResult Login([FromBody] LoginDto dto)
        {
            var user = _context.Users.FirstOrDefault(u =>
                u.Username == dto.UsernameOrEmail || u.Email == dto.UsernameOrEmail);

            if (user == null)
                return NotFound("Tài khoản không tồn tại");

            if (!BCrypt.Net.BCrypt.Verify(dto.Password, user.Password))
                return Unauthorized("Sai mật khẩu");

            if (!user.Status)
                return Forbid("Tài khoản đã bị khóa liên hệ Admin để biết thêm");

            var token = _tokenService.GenerateToken(user);

            return Ok(new { token });
        }
    }
}
