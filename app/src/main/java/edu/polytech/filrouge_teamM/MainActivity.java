package edu.polytech.filrouge_teamM;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "teamM " + getClass().getSimpleName();

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.d(TAG, "Permission POST_NOTIFICATIONS accordée");
                } else {
                    Log.d(TAG, "Permission POST_NOTIFICATIONS refusée");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView image = findViewById(R.id.imageView);
        image.setBackgroundResource(R.drawable.animation);
        AnimationDrawable animation = (AnimationDrawable) image.getBackground();
        animation.start();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d(TAG, "Fetching FCM registration token failed", task.getException());
                return;
            }
            Log.d(TAG, "FCM Token: " + task.getResult());
        });

        askNotificationPermission();

        findViewById(R.id.goDefault).setOnClickListener(clic -> {
            Intent intent = new Intent(getApplicationContext(), ControlActivity.class);
            intent.putExtra(getString(R.string.index), ControlActivity.TAB_LIST);
            startActivity(intent);
        });

        findViewById(R.id.option).setOnClickListener(clic -> {
            Intent intent = new Intent(getApplicationContext(), ControlActivity.class);
            intent.putExtra(getString(R.string.index), ControlActivity.TAB_REPORT);
            startActivity(intent);
        });
    }

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
}
