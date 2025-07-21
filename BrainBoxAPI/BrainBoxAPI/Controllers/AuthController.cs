using BrainBoxAPI.Data;
using BrainBoxAPI.DTOs;
using BrainBoxAPI.Models;
using BrainBoxAPI.Services;
using BrainBoxAPI.Utilities;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Linq;

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
            Console.WriteLine(">>>>>>>>>> Có request login từ client đến API <<<<<<<<<<");
            Console.WriteLine(dto.UsernameOrEmail + " ++ " + dto.Password);
            var user = _context.Users.FirstOrDefault(u =>
                u.Username == dto.UsernameOrEmail || u.Email == dto.UsernameOrEmail);

            if (user == null)
                return NotFound("Tài khoản không tồn tại");

            if (HashHelper.Hash(dto.Password) != user.Password)
                return Unauthorized("Sai mật khẩu");

            if (!user.Status)
                return Forbid("Tài khoản đã bị khóa liên hệ Admin để biết thêm");

            var token = _tokenService.GenerateToken(user);

            return Ok(new { token });
        }

        [HttpPost("register")]
        public IActionResult Register([FromBody] LoginDto dto)
        {
            Console.WriteLine(">>>> Có request register từ client đến API <<<<");
            Console.WriteLine(dto.UsernameOrEmail + " ++ " + dto.Password);

            var existed = _context.Users.Any(u => u.Username == dto.UsernameOrEmail);
            if (existed)
                return Conflict("Tên người dùng đã tồn tại");

            var user = new User
            {
                Username = dto.UsernameOrEmail,
                Password = HashHelper.Hash(dto.Password),
                Email = "",
                Role = "user",
                Status = true,
                Avatar = "",
                CreatedAt = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds(),
                PremiumExpiredAt = 0
            };

            _context.Users.Add(user);
            _context.SaveChanges();

            var token = _tokenService.GenerateToken(user);
            return Ok(new { token });
        }
        [Authorize]
        [HttpPost("change-password")]
        public IActionResult ChangePassword([FromBody] ChangePasswordDto dto)
        {
            Console.WriteLine(">>>> Có request change-password từ client đến API <<<<");
            Console.WriteLine($"Current Password: {dto.CurrentPassword}, New Password: {dto.NewPassword}");

            // Debug: Log all claims in the JWT token
            Console.WriteLine("Claims in JWT:");
            foreach (var claim in User.Claims)
            {
                Console.WriteLine($"Type: {claim.Type}, Value: {claim.Value}");
            }

            // Get the user ID from the JWT token
            var userIdClaim = User.Claims.FirstOrDefault(c => c.Type == "id")?.Value;
            if (!int.TryParse(userIdClaim, out int userId))
                return Unauthorized("Không thể xác định người dùng");

            var user = _context.Users.FirstOrDefault(u => u.Id == userId);
            if (user == null)
                return NotFound("Tài khoản không tồn tại");

            // Verify current password
            if (HashHelper.Hash(dto.CurrentPassword) != user.Password)
                return Unauthorized("Mật khẩu hiện tại không đúng");

            // Validate new password and confirm password
            if (dto.NewPassword != dto.ConfirmPassword)
                return BadRequest("Mật khẩu mới và xác nhận mật khẩu không khớp");

            // Update password
            user.Password = HashHelper.Hash(dto.NewPassword);
            _context.SaveChanges();

            return Ok();
        }
    }
}