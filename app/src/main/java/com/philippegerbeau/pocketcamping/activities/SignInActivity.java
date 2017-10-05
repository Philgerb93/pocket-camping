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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philippegerbeau.pocketcamping.R;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser;
    private DatabaseReference fbRootRef;

    private EditText signInEmail;
    private EditText signInPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();
        fbRootRef = FirebaseDatabase.getInstance().getReference();

        signInEmail = (EditText) findViewById(R.id.email);
        signInPassword = (EditText) findViewById(R.id.password);
    }

    public void signIn(View view) {
        if (validForm()) {
            String email = signInEmail.getText().toString();
            String password = signInPassword.getText().toString();

            fbAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            System.out.println("WERE IN");
                            if (task.isSuccessful() && fbUser != null) {
                                nextActivity();
                                finish();
                            } else {
                                Toast.makeText(SignInActivity.this, getString(R.string.auth_failed),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validForm() {
        return signInEmail.getText().toString().length() > 0 &&
                signInPassword.getText().toString().length() > 0;
    }

    private void nextActivity() {
        String userID = fbUser.getUid();
        fbRootRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Intent i;
                if (snapshot.hasChild("stayID")) {
                    i = new Intent(SignInActivity.this, HomeActivity.class);
                } else {
                    i = new Intent(SignInActivity.this, NoStayActivity.class);
                }
                startActivity(i);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void toSignUp(View view) {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }
}
