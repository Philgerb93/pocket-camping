package com.philippegerbeau.pocketcamping.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.philippegerbeau.pocketcamping.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private static final int PASSWORD_MIN_LENGTH = 6;

    private FirebaseAuth fbAuth;

    private EditText signUpUsername;
    private EditText signUpEmail;
    private EditText signUpPassword;
    private EditText signUpConfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fbAuth = FirebaseAuth.getInstance();

        signUpUsername = (EditText) findViewById(R.id.sign_up_username);
        signUpEmail = (EditText) findViewById(R.id.sign_up_email);
        signUpPassword = (EditText) findViewById(R.id.sign_up_password);
        signUpConfirmPassword = (EditText) findViewById(R.id.sign_up_confirm_password);
    }

    public void signUp(View view) {
        if (validForm()) {
            String email = signUpEmail.getText().toString();
            String password = signUpPassword.getText().toString();
            fbAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this,
                                        getString(R.string.account_created), Toast.LENGTH_SHORT)
                                        .show();
                                Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(SignUpActivity.this, getString(R.string.auth_failed),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private boolean validForm() {
        boolean valid = false;

        if (!validName()) {
            signUpUsername.setError(getString(R.string.invalid_username));
        } else if (!validEmail()) {
            signUpEmail.setError(getString(R.string.invalid_email));
        } else if (!validPassword()) {
            signUpPassword.setError(getString(R.string.invalid_password));
        } else if (!validPasswordConfirm()) {
            signUpConfirmPassword.setError(getString(R.string.invalid_confirm));
        } else {
            valid = true;
        }
        return valid;
    }

    private boolean validName() {
        return signUpUsername.getText().toString().trim().length() > 0;
    }

    private boolean validEmail() {
        String regex = "\\S+@\\S+.\\S+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(signUpEmail.getText().toString().trim());
        return matcher.matches();
    }

    private boolean validPassword() {
        return signUpPassword.getText().toString().trim().length() >= PASSWORD_MIN_LENGTH;
    }

    private boolean validPasswordConfirm() {
        return signUpPassword.getText().toString().trim().equals(
                signUpConfirmPassword.getText().toString().trim());
    }
}
