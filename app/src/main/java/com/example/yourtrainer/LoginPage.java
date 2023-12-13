package com.example.yourtrainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {
    EditText emailET, passwordET;
    Button signInBtn;
    TextView signUpLink;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        firebaseAuth=FirebaseAuth.getInstance();
        emailET=findViewById(R.id.emailET);
        passwordET=findViewById(R.id.passwordET);
        signInBtn=findViewById(R.id.signInBtn);
        signUpLink=findViewById(R.id.signUpLink);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseUser != null){
                    Toast.makeText(LoginPage.this, "You are logged in ", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginPage.this, NavigationPage.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(LoginPage.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                if(email.isEmpty()){
                    emailET.setError("Please enter your email!");
                    emailET.requestFocus();

                }
                else if(password.isEmpty()){
                    passwordET.setError("Please enter your password!");
                    passwordET.requestFocus();
                }
                else if(email.isEmpty() && password.isEmpty()){
                    Toast.makeText(LoginPage.this,"Fields are empty!",Toast.LENGTH_SHORT);
                }
                else if(!email.isEmpty() && !password.isEmpty()){
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginPage.this,"Login Error, Please Login Again",Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Intent inToHome = new Intent(LoginPage.this, NavigationPage.class);
                                startActivity(inToHome);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginPage.this,"Error Occurred",Toast.LENGTH_SHORT).show();

                }
            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginPage.this, SignUpPage.class);
                startActivity(i);
            }
        });
    }
}
