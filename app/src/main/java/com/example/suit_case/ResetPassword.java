package com.example.suit_case;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.suit_case.databinding.ActivityResetPasswordBinding;

public class ResetPassword extends AppCompatActivity {

    ActivityResetPasswordBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new DatabaseHelper(this);

        binding.resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the email entered by the user
                String email = binding.forgotEmail.getText().toString();

                // Check if the entered email is associated with any account
                Boolean checkEmail = databaseHelper.checkEmail(email);
                if (checkEmail == true){

                    // If the email is associated with an account, proceed to the next step
                    Intent intent = new Intent(getApplicationContext(),ResetPassword2.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    // If the email is not associated with any account, display an error message
                    Toast.makeText(ResetPassword.this, "Email Doesn't Associate with any Account", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.returnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }
}