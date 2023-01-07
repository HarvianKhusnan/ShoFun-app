package com.example.shofun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class paymentsucces extends AppCompatActivity {

    ImageView bckhomee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentsucces);
        bckhomee =  findViewById(R.id.bckhome);

        bckhomee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(paymentsucces.this,Katalog.class));
            }
        });




    }
}