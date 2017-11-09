package com.philippegerbeau.pocketcamping.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

        signUpUsername = findViewById(R.id.username);
        signUpEmail = findViewById(R.id.email);
        signUpPassword = findViewById(R.id.password);
        signUpConfirmPassword = findViewById(R.id.confirm);
    }

    public void signUp(View view) {
        if (validForm()) {
            final String username = signUpUsername.getText().toString();
            final String email = signUpEmail.getText().toString();
            String password = signUpPassword.getText().toString();
            fbAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UserProfileChangeRequest profileUpdates =
                                        new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username).build();
                                FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

                                String userID = "";
                                if (fbUser != null) {
                                    fbUser.updateProfile(profileUpdates);
                                    userID = fbUser.getUid();
                                }

                                storeEmail(userID);

                                Toast.makeText(SignUpActivity.this,
                                        getString(R.string.account_created), Toast.LENGTH_SHORT)
                                        .show();

                                toHome();
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
            TextInputLayout usernameParent = findViewById(R.id.username_parent);
            usernameParent.setError(getString(R.string.invalid_username));
        } else if (!validEmail()) {
            TextInputLayout emailParent = findViewById(R.id.email_parent);
            emailParent.setError(getString(R.string.invalid_email));
        } else if (!validPassword()) {
            TextInputLayout passwordParent = findViewById(R.id.password_parent);
            passwordParent.setError(getString(R.string.invalid_password));
        } else if (!validPasswordConfirm()) {
            TextInputLayout confirmParent = findViewById(R.id.confirm_parent);
            confirmParent.setError(getString(R.string.invalid_confirm));
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

    private void storeEmail(String userID) {
        DatabaseReference fbEmailRef = FirebaseDatabase.getInstance()
                .getReference().child("emails");
        String encodedEmail = signUpEmail.getText().toString().replace('.', ',');
        fbEmailRef.child(encodedEmail).setValue(userID);
    }

    private void toHome() {
        Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(i);
    }
}
