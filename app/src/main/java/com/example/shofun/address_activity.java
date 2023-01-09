package com.example.shofun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.shofun.Models.Alamat;
import com.example.shofun.Models.LoadingDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class address_activity extends AppCompatActivity {

    EditText alamat,namaawal,namaakhir,kodepos,kabkota;
    ImageView cnfrm;

    DatabaseReference dbref;
    Alamat alamatt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        alamat = findViewById(R.id.alamat);
        namaawal = findViewById(R.id.namadepan);
        namaakhir = findViewById(R.id.namabelakang);
        kodepos = findViewById(R.id.kodepos);
        kabkota = findViewById(R.id.kotakab);
        cnfrm = findViewById(R.id.confirmbtnn);
        final LoadingDialog loadingDialog = new LoadingDialog(address_activity.this);

        alamatt = new Alamat();
        dbref= FirebaseDatabase.getInstance().getReference().child("Address");

        cnfrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismissDialog();
                        }
                    },5000);
                }
                alamatt.setNamadepan(namaawal.getText().toString().trim());
                alamatt.setNamabelakang(namaakhir.getText().toString().trim());
                alamatt.setAlamat(alamat.getText().toString().trim());
                alamatt.setKota(kabkota.getText().toString().trim());
                alamatt.setKodepos(kodepos.getText().toString().trim());


                startActivity(new Intent(address_activity.this, paymentsucces.class));




                dbref.push().setValue(alamatt);

            }
        });


    }
}