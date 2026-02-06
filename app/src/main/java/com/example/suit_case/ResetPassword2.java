package com.example.suit_case;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.suit_case.databinding.ActivityResetPassword2Binding;

public class ResetPassword2 extends AppCompatActivity {

    ActivityResetPassword2Binding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPassword2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new DatabaseHelper(this);

        // Retrieve the email entered by the user while resetting the password
        Intent intent = getIntent();
        binding.UserEmail.setText(intent.getStringExtra("email"));

        binding.resetPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the email and new password entered by the user
                String email = binding.UserEmail.getText().toString();
                String password = binding.resetPassword.getText().toString();
                String confirmPassword = binding.confirmResetPassword.getText().toString();
                // Check if the entered passwords match
                if (password.equals(confirmPassword)) {
                    Boolean checkPasswordReset = databaseHelper.resetPassword(email, password);
                    if (checkPasswordReset == true) {
                        // If the password is successfully changed, navigate back to the login screen
                        Intent i = new Intent(getApplicationContext(), Login.class);
                        startActivity(i);
                        Toast.makeText(ResetPassword2.this, "Password Changed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ResetPassword2.this, "Password Reset Failed", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // If the entered passwords don't match, display an error message
                    Toast.makeText(ResetPassword2.this, "Passwords Not Matched", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.returnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), Login.class);
                startActivity(intent1);
            }
        });

    }
}