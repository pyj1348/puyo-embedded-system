package com.example.puyo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        ((Button) findViewById(R.id.menu_button1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SinglePlay.class);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.menu_button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, Players2Activity.class);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.menu_button3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, Players3Activity.class);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.menu_button4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, Players4Activity.class);
                startActivity(intent);
            }
        });

    }
}
