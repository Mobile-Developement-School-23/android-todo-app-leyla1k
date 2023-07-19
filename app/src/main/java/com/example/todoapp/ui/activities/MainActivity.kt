package com.example.todoapp.ui.activities
import android.Manifest
import android.content.pm.PackageManager
import com.example.todoapp.R
import javax.inject.Singleton

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.todoapp.TodoApplication
import com.example.todoapp.notifications.Notification
import kotlinx.coroutines.launch
import javax.inject.Inject


@Singleton
class MainActivity : AppCompatActivity() {


    @Inject
    lateinit var notification: Notification


    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.SCHEDULE_EXACT_ALARM
        )
        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                notification.scheduleAlarm()
            } else {
                notification.cancelAlarm()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        (application as TodoApplication).applicationComponent.inject(this)
        super.onCreate(savedInstanceState)
        requestPermissions()
        setContentView(R.layout.activity_main)



    }
    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 123
    }
}


