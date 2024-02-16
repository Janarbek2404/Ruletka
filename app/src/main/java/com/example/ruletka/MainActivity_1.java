package com.example.ruletka;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity_1 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        Button btnGo = (Button) findViewById(R.id.btnGo);
        View.OnClickListener oclBtnGo=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_1.this, MainActivity.class);
                startActivity(intent);
            }
        };
        btnGo.setOnClickListener(oclBtnGo);

        Button btnGoSet = (Button) findViewById(R.id.btnGoSet);
        View.OnClickListener oclBtnGoSet=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_1.this, MainActivity_Set.class);
                startActivity(intent);
            }
        };
        btnGoSet.setOnClickListener(oclBtnGoSet);
    }
}