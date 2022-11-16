package com.example.shofun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread = new Thread() {
            public void run() {
                try{
                    sleep(5000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(MainActivity.this, Log_in_activity.class));
                }
            }
        };
        thread.start();
    }
}
