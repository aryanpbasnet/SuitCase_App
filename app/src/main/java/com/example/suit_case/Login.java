package com.example.suit_case;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.suit_case.databinding.ActivityLoginBinding;

public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new DatabaseHelper(this);

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.logEmail.getText().toString().trim();
                String password = binding.logPassword.getText().toString().trim();
                if (email.equals("") || password.equals(""))
                    Toast.makeText(Login.this, "Please Enter Your Login Credentials", Toast.LENGTH_SHORT).show();
                else {
                    // Check if the entered email and password match database records
                    Boolean checkCredential = databaseHelper.checkEmailPassword(email, password);
                    if (checkCredential==true){
                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        // Navigate to the main activity upon successful login
                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Set a click listener for the "Sign Up" text to navigate to the Sign Up screen
        binding.signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Signup.class);
                startActivity(i);
            }
        });


        // Set a click listener for the "Forgot Password" link to navigate to the password reset screen
        binding.forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ResetPassword.class);
                startActivity(i);
            }
        });

    }
}