package com.priyanshparekh.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIMEOUT = 3750;

    TextView splashScreenText1, splashScreenText2;
    View hrLine1, hrLine2;
    Animation ssLineTopAnim, ssLineBottomAnim, ssTextAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashScreenText1 = findViewById(R.id.splash_screen_text_1);
        splashScreenText2 = findViewById(R.id.splash_screen_text_2);

        hrLine1 = findViewById(R.id.hr_line_1);
        hrLine2 = findViewById(R.id.hr_line_2);

        ssTextAnim = AnimationUtils.loadAnimation(this, R.anim.splash_screen_text_anim);
        ssLineTopAnim = AnimationUtils.loadAnimation(this, R.anim.ss_line_top_anim);
        ssLineBottomAnim = AnimationUtils.loadAnimation(this, R.anim.ss_line_bottom_anim);

        splashScreenText1.setAnimation(ssTextAnim);
        splashScreenText2.setAnimation(ssTextAnim);
        hrLine1.setAnimation(ssLineTopAnim);
        hrLine2.setAnimation(ssLineBottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }
}