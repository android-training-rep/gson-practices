package com.example.okhttppractices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button toNetworkBtn = findViewById(R.id.btn_network);
        toNetworkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toNetworkPage();
            }
        });
    }

    private void toNetworkPage() {
        Intent intent = new Intent(this, NetworkActivity.class);
        startActivity(intent);
    }
}