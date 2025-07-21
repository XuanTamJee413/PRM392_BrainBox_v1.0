using BrainBoxAPI.Data;
using BrainBoxAPI.DTOs;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace BrainBoxAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PaymentController : ControllerBase
    {
        private readonly BrainBoxDbContext _context;

        public PaymentController(BrainBoxDbContext context)
        {
            _context = context;
        }

        [HttpGet("vnpay-return")]
        public IActionResult VNPayReturn([FromQuery] VNPayReturnModel model)
        {
            // ✅ Kiểm tra thanh toán thành công
            if (model.vnp_ResponseCode == "00")
            {
                int userId = int.Parse(model.vnp_OrderInfo); 
                long amount = long.Parse(model.vnp_Amount) / 100;

                long addedMillis = 0;
                if (amount == 10000) addedMillis = 32L * 24 * 60 * 60 * 1000;
                else if (amount == 49000) addedMillis = 182L * 24 * 60 * 60 * 1000;
                else if (amount == 99000) addedMillis = 367L * 24 * 60 * 60 * 1000;
                else if (amount == 89000) addedMillis = 100L * 365 * 24 * 60 * 60 * 1000;

                var user = _context.Users.Find(userId);
                if (user == null) return NotFound("Không tìm thấy người dùng");

                long now = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
                user.PremiumExpiredAt = now + addedMillis;
                _context.SaveChanges();

                return Content("Thanh toán thành công. Premium đã được kích hoạt.");
            }

            return Content("Giao dịch thất bại hoặc bị huỷ.");
        }
    }
}
