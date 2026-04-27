package edu.polytech.filrouge_teamM;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "teamM " + getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView image = findViewById(R.id.imageView);
        image.setBackgroundResource(R.drawable.animation);

        AnimationDrawable animation = (AnimationDrawable) image.getBackground();
        animation.start();

        findViewById(R.id.goDefault).setOnClickListener(clic -> {
            Intent intent = new Intent(getApplicationContext(), ControlActivity.class);
            intent.putExtra(getString(R.string.index), 0);
            startActivity(intent);
        });

        findViewById(R.id.option).setOnClickListener(clic -> {
            Intent intent = new Intent(getApplicationContext(), ControlActivity.class);

            // 2 = Screen3Fragment = Nouveau signalement / Description de l'obstacle
            intent.putExtra(getString(R.string.index), 2);

            startActivity(intent);
        });
    }
}