package com.example.prm392_v1.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.example.prm392_v1.receiver.NotificationReceiver;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class NotificationScheduler {

    // Danh sách các khung giờ "dự đoán" người dùng rảnh (ví dụ)
    // 12:00 (giờ nghỉ trưa), 17:00 (tan làm/tan học), 20:00 (buổi tối)
    private static final List<Integer> REMINDER_HOURS = Arrays.asList(12, 17, 20);

    /**
     * Đặt các lời nhắc hàng ngày vào nhiều khung giờ.
     * @param context Context của ứng dụng.
     */
    public static void setDailyReminders(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        for (int hour : REMINDER_HOURS) {
            Intent intent = new Intent(context, NotificationReceiver.class);

            // QUAN TRỌNG: Sử dụng `hour` làm requestCode để mỗi PendingIntent là duy nhất.
            // Điều này đảm bảo các báo thức không ghi đè lên nhau.
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    hour, // Request code duy nhất
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            // Thiết lập thời gian cho báo thức
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            // Nếu thời gian đã trôi qua trong ngày, đặt lịch cho ngày hôm sau
            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            // Đặt báo thức lặp lại hàng ngày cho khung giờ này
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, // Lặp lại mỗi ngày
                    pendingIntent
            );
        }
    }

    /**
     * Hủy tất cả các lời nhắc đã được đặt.
     * @param context Context của ứng dụng.
     */
    public static void cancelAllReminders(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for (int hour : REMINDER_HOURS) {
            Intent intent = new Intent(context, NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    hour, // Phải dùng đúng requestCode đã đặt
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            alarmManager.cancel(pendingIntent);
        }
    }
}