class MyForegroundService : Service() {
ok
    override fun onCreate() {
        super.onCreate()
        startForegroundNotification()  // ត្រូវ call ក្នុង 3 វិនាទី
    }

    private fun startForegroundNotification() {
        val channelId = "my_channel"
        // បង្កើត Notification Channel សម្រាប់ Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "My Service", NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Service Running")
            .setContentText("This service is running in background")
            .setSmallIcon(R.drawable.ic_notification)  // រូប icon របស់អ្នក
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)  // មិនអាច dismiss បាន
            .build()

        // ប្រើ ServiceCompat សម្រាប់ compatibility
        ServiceCompat.startForeground(
            this,
            100,  // notification ID
            notification,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) FOREGROUND_SERVICE_TYPE_DATA_SYNC else 0
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // ដាក់ code ដែលចង់ run ជានិច្ច នៅទីនេះ (thread ឬ loop)
        return START_STICKY  // ធ្វើឲ្យ restart បើ kill
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
