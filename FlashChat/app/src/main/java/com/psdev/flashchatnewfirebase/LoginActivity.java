package com.psdev.flashchatnewfirebase;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button mSiginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        mPasswordView = (EditText) findViewById(R.id.login_password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mSiginButton = (Button) findViewById(R.id.login_sign_in_button);
        mSiginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    public void registerNewUser(View v) {
        Intent intent = new Intent(this, com.psdev.flashchatnewfirebase.RegisterActivity.class);
        finish();
        startActivity(intent);
    }

    private void attemptLogin() {
        String user = mEmailView.getText().toString();
        String pass = mPasswordView.getText().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Log.d("FlashChat", "Problem signing in " + task.getException());

                    final AlertDialog.Builder b = new AlertDialog.Builder(LoginActivity.this);
                    b.setCancelable(false);
                    b.setMessage("Unable to login: " + task.getException().getMessage());
                    b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    b.show();
                    return;
                }

                Intent intent = new Intent(LoginActivity.this, MainChatActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}