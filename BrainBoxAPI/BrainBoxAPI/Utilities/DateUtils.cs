using System.Globalization;

namespace BrainBoxAPI.Utilities
{
    public static class DateUtils
    {
        // Chuyển từ chuỗi ngày -> timestamp (milliseconds since 1970-01-01 UTC)
        // input: "dd/MM/yyyy" hoặc "dd/MM/yyyy HH:mm"
        public static long DateTimeToTimestamp(string inputDate)
        {
            if (!inputDate.Contains(":"))
            {
                inputDate += " 00:00";
            }

            if (DateTime.TryParseExact(inputDate, "dd/MM/yyyy HH:mm", CultureInfo.InvariantCulture, DateTimeStyles.AssumeLocal, out DateTime dt))
            {
                DateTimeOffset dto = new DateTimeOffset(dt);
                return dto.ToUnixTimeMilliseconds();
            }

            return -1;
        }


        // Chuyển từ timestamp -> chuỗi ngày "dd/MM/yyyy HH:mm"
        public static string TimestampToDate(long timestamp)
        {
            try
            {
                DateTimeOffset dto = DateTimeOffset.FromUnixTimeMilliseconds(timestamp);
                return dto.ToLocalTime().ToString("dd/MM/yyyy HH:mm");
            }
            catch
            {
                return "Invalid timestamp";
            }
        }
    }
}
