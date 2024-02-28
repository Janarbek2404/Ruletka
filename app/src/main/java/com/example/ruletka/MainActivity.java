package com.example.ruletka;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

        // Добавляем слушатель текста к полю ввода числа
        EditText editTextBet = findViewById(R.id.editTextBet);
        editTextBet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Не требуется реализация
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Проверяем, пустое ли поле ввода
                String betStr = charSequence.toString();
                Button btnStart = findViewById(R.id.button);
                Button btnGuess = findViewById(R.id.btnGuess);
                btnStart.setEnabled(!betStr.isEmpty());
                btnGuess.setEnabled(!betStr.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Не требуется реализация
            }
        });
    }


    public void onClickStart(View v){
        soundPlay(puskSound);

        // Заблокировать кнопки "Сделать ставку" и "Угадать"
        Button btnBet = findViewById(R.id.btnBet);
        Button btnGuess = findViewById(R.id.btnGuess);
        Button btnStart = findViewById(R.id.button);
        btnStart.setEnabled(false);
        btnGuess.setEnabled(false);
        btnBet.setEnabled(false);

        // Проверяем, что текущая ставка больше нуля
        if (currentBet > 0) {
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

                    // Разблокировать кнопку "Крутить" после завершения вращения рулетки
                    btnStart.setEnabled(true);
                    btnGuess.setEnabled(true);
                    btnBet.setEnabled(true);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            rul.startAnimation(rotate);
        } else {
            showToast("Сделайте ставку, прежде чем крутить рулетку.");
            btnStart.setEnabled(true);
            btnGuess.setEnabled(true);
            btnBet.setEnabled(true);
        }
    }

    private void init(){
        tvResult = findViewById(R.id.tvResult);
        tvBank = findViewById(R.id.tvBank);
        tvCurrentBet = findViewById(R.id.tvCurrentBet);
        rul = findViewById(R.id.rul);
        random = new Random();
        updateBankAndBet();

        // Добавление слушателя текста к EditText
        EditText editTextGuess = findViewById(R.id.editTextGuess);
        editTextGuess.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Не требуется реализация
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Проверка введенного текста при изменении текста
                String guessStr = charSequence.toString();
                Button btnGuess = findViewById(R.id.btnGuess);
                btnGuess.setEnabled(!guessStr.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Не требуется реализация
            }
        });
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

        Button btnGuess = findViewById(R.id.btnGuess);
        btnGuess.setEnabled(false);
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
                btnGuess.setEnabled(true);
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
            if (betAmount > 0) {
                if (betAmount <= bank) {
                    currentBet += betAmount; // Увеличиваем текущую ставку
                    bank -= betAmount; // Уменьшаем банк на сумму ставки
                    updateBankAndBet(); // Обновляем отображаемую информацию
                } else {
                    // Предложение взять кредит
                    showToast("Недостаточно средств для совершения ставки. Хотите взять кредит?");
                    // Разблокировка кнопки для взятия кредита
                    Button btnCredit = findViewById(R.id.btnCredit);
                    btnCredit.setVisibility(View.VISIBLE);
                    btnCredit.setEnabled(true);
                }
            } else {
                showToast("Пожалуйста, введите сумму ставки.");
            }

            // Блокировка кнопок "Крутить" и "Угадать", если ставка не сделана
            Button btnStart = findViewById(R.id.button);
            Button btnGuess = findViewById(R.id.btnGuess);
            if (currentBet <= 0) {
                btnStart.setEnabled(false);
                btnGuess.setEnabled(false);
            }
        }
    }

    public void onClickCredit(View v) {
        // Предложение взять кредит
        showToast("Вы взяли кредит. У вас плюс 1000 на счету.");
        bank += 1000; // Добавляем 1000 к банку
        updateBankAndBet(); // Обновляем отображаемую информацию о банке и ставке

        // Блокировка кнопки "Взять кредит" после взятия
        Button btnCredit = findViewById(R.id.btnCredit);
        btnCredit.setVisibility(View.GONE);
        btnCredit.setEnabled(false);
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
