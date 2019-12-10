package com.example.puyo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_of_game);

        ((Button) findViewById(R.id.btnStartServer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SimpleServerActivity.class);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.btnStartClient)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SimpleClientActivity.class);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.btnSinglePlay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Room.class);
                intent.putExtra("player1", 0);
                startActivity(intent);
            }
        });
    }
}