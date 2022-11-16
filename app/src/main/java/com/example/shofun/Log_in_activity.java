package com.example.shofun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Log_in_activity extends AppCompatActivity {

    EditText username,password;
    ImageView forgotPW,logginbtn;
    TextView signupbtn;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        auth = FirebaseAuth.getInstance();


        username = findViewById(R.id.UsernameTXT);
        password = findViewById(R.id.passwordTXT);
        forgotPW = findViewById(R.id.frgtpw);
        logginbtn = findViewById(R.id.lgnbtn);
        signupbtn = findViewById(R.id.sgnbttn);


        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Log_in_activity.this,sign_up_activity.class));
            }
        });

        logginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginuser();
            }
        });



    }

    private void loginuser() {
        String userName = username.getText().toString();
        String UserPassword = password.getText().toString();

        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(UserPassword)){
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(UserPassword.length() < 6){
            Toast.makeText(this, "Password Better 6 letter", Toast.LENGTH_SHORT).show();
            return;
        }

        //login user
        auth.signInWithEmailAndPassword(userName,UserPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Log_in_activity.this, "Login Succes", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Log_in_activity.this, Katalog.class));
                        }else {
                            Toast.makeText(Log_in_activity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}