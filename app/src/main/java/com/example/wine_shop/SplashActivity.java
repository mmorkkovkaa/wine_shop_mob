package com.example.wine_shop;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.wine_shop.Users.MainActivity;

public class SplashActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private ImageView backgroundImage;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mediaPlayer = MediaPlayer.create(this, R.raw.wine_sound);
        backgroundImage = findViewById(R.id.background_image);
        welcomeText = findViewById(R.id.welcome_text);

        // Запуск проигрывания музыки
        mediaPlayer.start();

        // Первый Handler: Изменяем фоновое изображение и текст через 3 секунды
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backgroundImage.setImageResource(R.drawable.w_6);
                welcomeText.setText("Лучшие вина для вечеров с семьёй");
                welcomeText.setTextColor(ContextCompat.getColor(SplashActivity.this, R.color.white));

                // Второй Handler: Изменяем фоновое изображение и текст через 3 секунды после первого изменения
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backgroundImage.setImageResource(R.drawable.w_5);
                        welcomeText.setText("И для уютной посиделки на едине");
                        welcomeText.setTextColor(ContextCompat.getColor(SplashActivity.this, R.color.white));


                        // Третий Handler: Переход к MainActivity через 2 секунды после второго изменения
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mediaPlayer.release();
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            }
                        }, 3000); // Устанавливаем задержку на 2 секунды
                    }
                }, 3000); // Устанавливаем задержку на 3 секунды
            }
        }, 3000); // Устанавливаем задержку на 3 секунды
    }
}
