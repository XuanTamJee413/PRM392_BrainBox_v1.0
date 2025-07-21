namespace BrainBoxAPI.DTOs
{
    public class VNPayReturnModel
    {
        public string vnp_Amount { get; set; }
        public string vnp_OrderInfo { get; set; }
        public string vnp_ResponseCode { get; set; }

        public string vnp_TxnRef { get; set; }
        public string vnp_SecureHash { get; set; }
    }
}
