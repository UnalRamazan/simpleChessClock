package com.example.chessClockDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UpdateMinuteActivity extends AppCompatActivity {

    Button uptadeTimeButton;
    EditText text;
    int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_minute);

        uptadeTimeButton = findViewById(R.id.uptadeTimeButton);
        text = findViewById(R.id.editTime);

        uptadeTimeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String check = text.getText().toString();

                if(check.equals("")){
                    time = 10;
                }else{
                    time = Integer.parseInt(check);
                }

                Intent intent = new Intent(UpdateMinuteActivity.this, MainActivity.class);//sayfa arasında geçiş yapabilmek için
                intent.putExtra("time", time);
                startActivity(intent);
            }
        });
    }
}