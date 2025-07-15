package com.example.prm392_v1.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.prm392_v1.R;
import com.example.prm392_v1.ui.main.SplashActivity;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "study_reminder_channel";
    private static final int NOTIFICATION_ID = 101;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Tạo NotificationManager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo Notification Channel (bắt buộc từ Android 8.0 Oreo trở lên)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Study Reminder Channel";
            String description = "Channel for daily study reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        // Tạo Intent để mở ứng dụng khi người dùng nhấn vào thông báo
        Intent notificationIntent = new Intent(context, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Xây dựng thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher) // Thay bằng icon của bạn
                .setContentTitle("Đến giờ học rồi! ⏰")
                .setContentText("Mở BrainBox và củng cố kiến thức nào!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true); // Tự động xóa thông báo khi người dùng nhấn vào

        // Hiển thị thông báo
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}