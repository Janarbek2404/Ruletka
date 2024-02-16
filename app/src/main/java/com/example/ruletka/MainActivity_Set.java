package com.example.ruletka;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity_Set extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_set);

        // Найти переключатели звука и вибрации
        Switch switchSound = findViewById(R.id.switchSound);
        Switch switchVibration = findViewById(R.id.switchVibration);

        // Загрузить сохраненные настройки звука и вибрации
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isSoundEnabled = preferences.getBoolean("music", true);
        boolean isVibrationEnabled = preferences.getBoolean("vibration_enabled", true);

        // Установить значения переключателей в соответствии с загруженными настройками
        switchSound.setChecked(isSoundEnabled);
        switchVibration.setChecked(isVibrationEnabled);

        // Обработчик изменений состояния переключателей
        switchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Сохранить настройки звука
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("music", isChecked);
                editor.apply();
            }
        });

        switchVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Сохранить настройки вибрации
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("vibration_enabled", isChecked);
                editor.apply();
            }
        });

        // Найти кнопку и установить обработчик кликов
        Button btnGoToAct = findViewById(R.id.btnGoToAct);
        btnGoToAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Перейти на другую активность
                Intent intent = new Intent(MainActivity_Set.this, MainActivity_1.class);
                startActivity(intent);
            }
        });
    }
}
