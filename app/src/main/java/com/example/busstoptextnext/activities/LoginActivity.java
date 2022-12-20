package com.example.busstoptextnext.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.busstoptextnext.R;
import com.google.firebase.auth.FirebaseUser;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = "SignUpActivity";

    // Instantiate XML elements
    private TextView title, desc;
    private EditText username, password;
    private Button signup, login;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        // Link XML elements
        title = findViewById(R.id.login_title);
        desc = findViewById(R.id.login_desc);
        username = findViewById(R.id.username);
        password= findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);

        signup.setOnClickListener(l -> signup());
        login.setOnClickListener(l -> login());
    }

    /**
     * onClick method to start main activity on a successful login
     */
    public void login() {
        try {
            if (username.getText().toString().isBlank()) {
                throw new Exception("Email can't be empty!");
            }
            if (password.getText().toString().isBlank()) {
                throw new Exception("Password can't be empty!");
            }

            firebaseAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG + "Login", "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch(Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * onClick method to start sign up activity on a signup
     */
    public void signup() {
        Intent intent = new Intent(this, SignUpActivity.class);
        // Clear fields
        username.setText("");
        password.setText("");
        startActivity(intent);
    }

    /**
     * Check if user is signed in (non-null) and update UI accordingly.
     */
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null)
            updateUI(currentUser);
    }

    /**
     * updates the UI
     * @param user
     */
    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", user.getDisplayName());
        intent.putExtra("uid", user.getUid());
        intent.putExtra("user-path-firebase", "userData/" + user.getUid());
        // Clear fields
        username.setText("");
        password.setText("");
        startActivity(intent);
    }
}