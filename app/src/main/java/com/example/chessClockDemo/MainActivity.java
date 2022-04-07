package com.example.chessClockDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String TAG = "Main";
    Button upButton;
    Button downButton;
    ImageButton startButton;
    ImageButton resetButton;
    ImageButton settingsButton;
    ImageButton updateTimeButton;
    ImageButton doftdareButton;

    int time = 10;//real minute
    long upTimeSecond;
    long downTimeSecond;

    MediaPlayer voiceMedia;

    private boolean controlUp = false;
    private boolean controlDown = true;
    private boolean firstControl = true;
    private boolean control = true;

    //Geriye doğru sayım işlemleri için kullanılır
    CountDownTimer countDownTimer1 = null;
    CountDownTimer countDownTimer2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //kullanıcıdan güncel zamanı aldık
        Intent timeIntent = getIntent();
        time = timeIntent.getIntExtra("time", 10);

        upTimeSecond = (long) time * 60 * 1000;
        downTimeSecond = (long) time * 60 * 1000;

        upButton = findViewById(R.id.upButton);//id'leri kullanarak ara yüzdeki nesneleri oluşturduğumuz nesnelere atadık
        downButton = findViewById(R.id.downButton);
        startButton = findViewById(R.id.startButton);
        resetButton = findViewById(R.id.resetButton);
        settingsButton = findViewById(R.id.settingsButton);
        updateTimeButton = findViewById(R.id.updateTimeButton);
        doftdareButton = findViewById(R.id.doftdareButton);

        String newTimeText = String.format(Locale.getDefault(), "%02d:%02d", time, 0);
        downButton.setText(newTimeText);
        upButton.setText(newTimeText);

        voiceMedia = MediaPlayer.create(getApplicationContext(), R.raw.voice);

        resetButton.setEnabled(false);
        resetButton.setImageResource(R.drawable.white_reset_icon);
        settingsButton.setImageResource(R.drawable.settings_icon);
        updateTimeButton.setImageResource(R.drawable.timer_icon);


        startButton.setOnClickListener(new View.OnClickListener() {//start butonuna tıkladığımda

            @Override
            public void onClick(View view) {

                if (control) {

                    control = false;

                    if (firstControl) {
                        firstControl = false;
                        upButton.setEnabled(false);
                        downButton.setEnabled(false);

                        startCountDownTimer1();//ilk sayaç başladı
                    } else {
                        if (controlUp) {
                            startCountDownTimer2();
                        } else {
                            startCountDownTimer1();
                        }
                    }
                    resetButton.setEnabled(true);
                    updateTimeButton.setEnabled(false);
                    settingsButton.setEnabled(false);

                    startButton.setImageResource(R.drawable.pause_icon);
                    resetButton.setImageResource(R.drawable.reset_icon);
                    settingsButton.setImageResource(R.drawable.white_settings_icon);
                    updateTimeButton.setImageResource(R.drawable.white_timer_icon);
                    doftdareButton.setImageResource(R.drawable.company5);
                } else {

                    control = true;

                    if (controlUp) {
                        cancelCountDownTimer1();
                        controlDown = true;
                        controlUp = false;
                    } else {
                        cancelCountDownTimer2();
                        controlDown = false;
                        controlUp = true;
                    }
                    updateTimeButton.setEnabled(true);
                    resetButton.setEnabled(false);
                    settingsButton.setEnabled(true);

                    startButton.setImageResource(R.drawable.play_icon);
                    resetButton.setImageResource(R.drawable.white_reset_icon);
                    settingsButton.setImageResource(R.drawable.settings_icon);
                    updateTimeButton.setImageResource(R.drawable.timer_icon);
                    doftdareButton.setImageResource(R.drawable.company4);
                }

            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (firstControl) {
                    firstControl = false;
                    control = false;
                    upButton.setEnabled(false);
                    downButton.setEnabled(false);

                    startCountDownTimer1();

                    firstControl();
                } else {
                    if (!controlDown) {
                        cancelCountDownTimer1();
                        startCountDownTimer2();
                    }
                }
            }
        });

        upButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (firstControl) {
                    firstControl = false;
                    control = false;
                    upButton.setEnabled(false);
                    downButton.setEnabled(false);

                    controlDown = false;
                    controlUp = true;

                    startCountDownTimer2();

                    firstControl();
                } else {
                    if (!controlUp) {
                        cancelCountDownTimer2();
                        startCountDownTimer1();
                    }
                }
            }
        });

        updateTimeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UpdateMinuteActivity.class);//sayfa arasında geçiş yapabilmek için
                startActivity(intent);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                resetApp();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });

        doftdareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.doftdare.com/"));
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void firstControl() {
        resetButton.setEnabled(true);
        updateTimeButton.setEnabled(false);
        settingsButton.setEnabled(false);
        startButton.setImageResource(R.drawable.pause_icon);
        resetButton.setImageResource(R.drawable.reset_icon);
        settingsButton.setImageResource(R.drawable.white_settings_icon);
        updateTimeButton.setImageResource(R.drawable.white_timer_icon);
        doftdareButton.setImageResource(R.drawable.company5);
    }

    private void startCountDownTimer1() {

        downButton.setEnabled(true);
        if (controlDown) {

            Log.i(TAG, "startCountDownTimer1 çalıştı...");

            controlDown = false;
            controlUp = true;

            countDownTimer1 = new CountDownTimer(downTimeSecond, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {//her tetiklendiğinde yapılmasını istediğim işler
                    downTimeSecond = millisUntilFinished;
                    int minutes = (int) (downTimeSecond / 60000);
                    int second = (int) ((downTimeSecond / 1000) % 60);
                    String text = String.format(Locale.getDefault(), "%02d:%02d", minutes, second);
                    downButton.setText(text);

                    if ((((long) time * 60 * 1000) * 10) / 100 >= downTimeSecond) {
                        downButton.setBackgroundColor(Color.parseColor("#FF0000"));
                    }
                }

                @Override
                public void onFinish() {
                    downButton.setText(R.string.finishName);
                    downButton.setEnabled(false);

                    printMessage();
                }
            };
            countDownTimer1.start();
        }
    }

    private void cancelCountDownTimer1() {

        downButton.setEnabled(false);
        if (!controlDown) {
            countDownTimer1.cancel();
        }
    }

    private void startCountDownTimer2() {

        upButton.setEnabled(true);
        if (controlUp) {

            Log.i(TAG, "startCountDownTimer2 çalıştı...");

            controlDown = true;
            controlUp = false;

            countDownTimer2 = new CountDownTimer(upTimeSecond, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {//her tetiklendiğinde yapılmasını istediğim işler
                    upTimeSecond = millisUntilFinished;
                    int minutes = (int) (upTimeSecond / 60000);
                    int second = (int) ((upTimeSecond / 1000) % 60);
                    String text = String.format(Locale.getDefault(), "%02d:%02d", minutes, second);
                    upButton.setText(text);

                    if ((((long) time * 60 * 1000) * 10) / 100 >= upTimeSecond) {
                        upButton.setBackgroundColor(Color.parseColor("#FF0000"));
                    }
                }

                @Override
                public void onFinish() {
                    upButton.setText(R.string.finishName);
                    upButton.setEnabled(false);

                    printMessage();
                }
            };
            countDownTimer2.start();
        }
    }

    private void cancelCountDownTimer2() {

        upButton.setEnabled(false);
        if (!controlUp) {
            countDownTimer2.cancel();
        }
    }

    private void resetApp() {
        if (controlUp) {
            cancelCountDownTimer1();
        } else {
            cancelCountDownTimer2();
        }

        /*firstControl = true;
        controlDown = true;
        controlUp = false;
        control = true;

        upButton.setText("10:00");
        downButton.setText("10:00");
        //upButton.setBackgroundColor(Color.parseColor("#FF3700B3"));
        //downButton.setBackgroundColor(Color.parseColor("#FF3700B3"));

        downTimeSecond = 10 * 60 * 1000;
        upTimeSecond = 10 * 60 * 1000;

        updateTimeButton.setEnabled(true);
        resetButton.setEnabled(false);
        settingsButton.setEnabled(true);

        startButton.setImageResource(R.drawable.pause_icon);
        resetButton.setImageResource(R.drawable.white_reset_icon);
        settingsButton.setImageResource(R.drawable.settings_icon);
        updateTimeButton.setImageResource(R.drawable.timer_icon);
        doftdareButton.setImageResource(R.drawable.company4);*/

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void printMessage() {

        voiceMedia.start();

        Context context = getApplicationContext();
        CharSequence text = "Time is over.";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public boolean isControlUp() {
        return controlUp;
    }

    public void setControlUp(boolean controlUp) {
        this.controlUp = controlUp;
    }

    public boolean isControlDown() {
        return controlDown;
    }

    public void setControlDown(boolean controlDown) {
        this.controlDown = controlDown;
    }

    public boolean isFirstControl() {
        return firstControl;
    }

    public void setFirstControl(boolean firstControl) {
        this.firstControl = firstControl;
    }

    public boolean isControl() {
        return control;
    }

    public void setControl(boolean control) {
        this.control = control;
    }
}