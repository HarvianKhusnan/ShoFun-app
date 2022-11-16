package com.example.shofun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shofun.Models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class sign_up_activity extends AppCompatActivity {

    ImageView signUp;
    EditText name,email,password;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        signUp = findViewById(R.id.loginbuttonID);
        name = findViewById(R.id.usernameID);
        email = findViewById(R.id.emailID);
        password = findViewById(R.id.passwordID);
        backbtn = findViewById(R.id.arrowleft);

        database = FirebaseDatabase.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sign_up_activity.this,Log_in_activity.class));
            }
        });


    }

    private void createUser() {
        String userName = name.getText().toString();
        String Email = email.getText().toString();
        String UserPassword = password.getText().toString();

        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(Email)){
            Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show();
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

        auth.createUserWithEmailAndPassword(Email,UserPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            UserModel userModel = new UserModel(userName,Email,UserPassword);
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(userModel);
                            Toast.makeText(sign_up_activity.this, "Registration Succes", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(sign_up_activity.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }
}