package com.example.prm392_v1.utils;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class VNPayHelper {

    private static final String VNP_VERSION = "2.1.0";
    private static final String VNP_COMMAND = "pay";
    private static final String VNP_TMNCODE = "6GWD4286";
    private static final String VNP_CURRCODE = "VND";
    private static final String VNP_LOCALE = "vn";
    private static final String RETURN_URL = "https://cd673cbbd4b2.ngrok-free.app/api/payment/vnpay-return";
    private static final String SECRET_KEY = "UPMIDBW8I5YYRMS91S7HISCMEVVBGS1E";

    public static String createVNPayUrl(int userId, long amount, String ipAddress) {
        try {
            String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
            String vnp_OrderInfo = "Nap goi Premium cho userId " + userId;
            String vnp_CreateDate = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());

            Map<String, String> vnpParams = new TreeMap<>();
            vnpParams.put("vnp_Version", VNP_VERSION);
            vnpParams.put("vnp_Command", VNP_COMMAND);
            vnpParams.put("vnp_TmnCode", VNP_TMNCODE);
            vnpParams.put("vnp_Amount", String.valueOf(amount * 100));
            vnpParams.put("vnp_CurrCode", VNP_CURRCODE);
            vnpParams.put("vnp_TxnRef", vnp_TxnRef);
            vnpParams.put("vnp_OrderInfo", vnp_OrderInfo);
            vnpParams.put("vnp_Locale", VNP_LOCALE);
            vnpParams.put("vnp_ReturnUrl", RETURN_URL);
            vnpParams.put("vnp_CreateDate", vnp_CreateDate);
            vnpParams.put("vnp_IpAddr", ipAddress);
            vnpParams.put("vnp_SecureHashType", "HmacSHA256");

            StringBuilder hashData = new StringBuilder();
            StringBuilder queryUrl = new StringBuilder();
            for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
                if (hashData.length() > 0) {
                    hashData.append('&');
                    queryUrl.append('&');
                }
                hashData.append(entry.getKey()).append('=').append(entry.getValue());
                queryUrl.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            // Bước 2: Ký SHA256 với key bí mật
            String vnp_SecureHash = hmacSHA256(SECRET_KEY, hashData.toString());
            queryUrl.append("&vnp_SecureHash=").append(vnp_SecureHash);

            return "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?" + queryUrl.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private static String hmacSHA256(String key, String data) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        byte[] hashBytes = mac.doFinal(data.getBytes("UTF-8"));
        StringBuilder hash = new StringBuilder();
        for (byte b : hashBytes) {
            hash.append(String.format("%02x", b & 0xff));
        }
        return hash.toString();
    }
}