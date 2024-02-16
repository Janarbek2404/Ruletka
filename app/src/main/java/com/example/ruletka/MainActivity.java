package com.example.ruletka;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int bank = 1000; // Начальный банк
    private int currentBet = 0; // Текущая ставка

    private MediaPlayer musicSound, puskSound;
    private TextView tvResult, tvBank, tvCurrentBet;
    private ImageView rul;
    private Random random;
    private int old_deegere = 0;
    private int deegere = 0;
    private static final float FACTOR = 4.7368421f;
    private String[] numbers = {"32","15",
            "19","4","21","2","25","17",
            "34","37","6","27","13","36",
            "11","30","8","23","10","5",
            "24","16","33","1","20","14",
            "31","9","22","18","29","7",
            "28","12","35","3","26","0"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        musicSound = MediaPlayer.create(this, R.raw.music);
        puskSound = MediaPlayer.create(this, R.raw.pusk);
        soundPlay(musicSound);
    }

    public void onClickStart(View v){
        soundPlay(puskSound);

        // Заблокировать кнопки "Сделать ставку" и "Угадать"
        Button btnBet = findViewById(R.id.btnBet);
        Button btnGuess = findViewById(R.id.btnGuess);
        btnBet.setEnabled(false);
        btnGuess.setEnabled(false);

        old_deegere = deegere % 360;
        deegere = random.nextInt(3600) + 720;
        RotateAnimation rotate = new RotateAnimation(old_deegere, deegere,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF,0.5f);
        rotate.setDuration(3600);
        rotate.setFillAfter(true);
        rotate.setInterpolator(new DecelerateInterpolator());
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                tvResult.setText("");
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                tvResult.setText(getResult(360 - (deegere % 360)));

                // Разблокировать кнопки "Сделать ставку" и "Угадать" после завершения вращения рулетки
                btnBet.setEnabled(true);
                btnGuess.setEnabled(true);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        rul.startAnimation(rotate);
    }

    private void init(){
        tvResult = findViewById(R.id.tvResult);
        tvBank = findViewById(R.id.tvBank);
        tvCurrentBet = findViewById(R.id.tvCurrentBet);
        rul = findViewById(R.id.rul);
        random = new Random();
        updateBankAndBet();
    }

    private String getResult(int deegere){
        String text="";
        int factor_x = 1;
        int factor_y = 3;
        for(int i = 0; i<38;i++) {
            if (deegere >= (FACTOR * factor_x) && deegere <= (FACTOR * factor_y)) {
                text = numbers[i];
            }
            factor_x += 2;
            factor_y += 2;
            if(deegere >= (FACTOR * 73) && deegere < 360 || deegere >= 0 && deegere <(FACTOR * 1)){
                text = numbers[numbers.length - 1];
            }
        }
        return text;
    }

    public void onClickGuess(View view) {
        EditText editTextGuess = findViewById(R.id.editTextGuess);
        String guessStr = editTextGuess.getText().toString();
        if (!guessStr.isEmpty()) {
            // Получаем текст из TextView и удаляем знаки, оставляя только цифры
            String resultStr = tvResult.getText().toString().replaceAll("\\D+", "");

            // Проверяем, что в TextView есть число
            if (!resultStr.isEmpty()) {
                // Преобразуем текст из TextView в число
                int randomNumber = Integer.parseInt(resultStr);

                // Получаем предполагаемое пользователем число из EditText
                int userGuess = Integer.parseInt(editTextGuess.getText().toString());

                // Проверяем, совпадает ли предполагаемое число с числом из TextView
                if (userGuess == randomNumber) {
                    showToast("Поздравляем, вы угадали число: " + randomNumber);
                    // Умножение банка на 2 при угадывании числа
                    int t=currentBet*2;
                    bank =bank+t;
                    // Обновление отображаемой информации о банке
                    updateBankAndBet();
                } else {
                    showToast("Число не угадано. Попробуйте еще раз!");
                    currentBet = 0;
                    updateBankAndBet();
                }
            } else {
                showToast("Внимание! Не удалось получить число для угадывания.");
            }
        } else {
            showToast("Пожалуйста, введите число.");
        }
    }

    public void onClickBet(View v) {
        EditText editTextBet = findViewById(R.id.editTextBet);
        String betStr = editTextBet.getText().toString();
        if (!betStr.isEmpty()) {
            int betAmount = Integer.parseInt(betStr);
            if (betAmount > 0 && betAmount <= bank) {
                currentBet += betAmount; // Увеличиваем текущую ставку
                bank -= betAmount; // Уменьшаем банк на сумму ставки
                updateBankAndBet(); // Обновляем отображаемую информацию
            } else {
                showToast("Недостаточно средств для совершения ставки.");
            }
        } else {
            showToast("Пожалуйста, введите сумму ставки.");
        }
    }

    private void updateBankAndBet() {
        tvBank.setText("Банк: " + bank);
        tvCurrentBet.setText("Текущая ставка: " + currentBet);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void soundPlay(MediaPlayer sound){
        sound.start();
    }
}
