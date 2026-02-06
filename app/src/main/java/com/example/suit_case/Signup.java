package com.example.suit_case;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.suit_case.databinding.ActivitySignupBinding;

public class Signup extends AppCompatActivity {
    ActivitySignupBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialise the database helper for user registration
        databaseHelper = new DatabaseHelper(this);

        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the user email, password, and password confirmation
                String email = binding.signupEmail.getText().toString().trim();
                String password = binding.signupPwd.getText().toString().trim();
                String confirmPassword = binding.ConfirmPwd.getText().toString().trim();

                // Check if any of the fields are empty (null point validation)
                if (email.equals("") || password.equals("") || confirmPassword.equals(""))
                    Toast.makeText(Signup.this, "Please fill out all the fields", Toast.LENGTH_SHORT).show();
                else {
                    // Check if the entered passwords match
                    if (password.equals(confirmPassword)){
                        // Check if the email is not already registered
                        Boolean checkEmail = databaseHelper.checkEmail(email);
                        if (checkEmail==false){
                            Boolean insert = databaseHelper.insertUsers(email, password);
                            if (insert==true){
                                // If the registration is successful, display a success message and navigate to the login screen
                                Toast.makeText(Signup.this, "Account Created", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                            }else {
                                // If the registration fails, display an error message
                                Toast.makeText(Signup.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            // If the email is already registered, display an error message
                            Toast.makeText(Signup.this, "User already exists", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        // If the entered passwords don't match, display an error message
                        Toast.makeText(Signup.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.signInTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });
    }
}