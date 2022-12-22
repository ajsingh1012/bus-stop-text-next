package com.example.busstoptextnext.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.busstoptextnext.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private final String TAG = "SignUpActivity";

    // Instantiate XML elements
    EditText email, password, name, phone;
    Button signup;
    Button cancel;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private CollectionReference usersCollection;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        // Link XML elements
        name = findViewById(R.id.person_name);
        phone = findViewById(R.id.local_phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        cancel = findViewById(R.id.cancel_button);
        this.context = SignUpActivity.this;

        cancel.setOnClickListener(view -> finish());

        signup.setOnClickListener(view -> {
            try {
                if (name.getText().toString().isEmpty()) {
                    throw new Exception ("Name can't be empty!");
                }
                if (phone.getText().toString().isEmpty()) {
                    throw new Exception ("Phone can't be empty!");
                }
                if (phone.getText().toString().length() < 5) {
                    throw new Exception ("Phone must be at least 5 digits!");
                }
                if (email.getText().toString().isEmpty()) {
                    throw new Exception ("Email can't be empty!");
                }
                if (password.getText().toString().isEmpty()) {
                    throw new Exception ("Password can't be empty!");
                }
                if (password.getText().toString().length() < 6) {
                    throw new Exception ("Password must be at least 6 characters!");
                }
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(SignUpActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name.getText().toString())
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");

                                                db = FirebaseFirestore.getInstance();
                                                Map<String, Object> data = new HashMap<>();
                                                data.put("localNo", phone.getText().toString());
                                                db.collection("userData").document(user.getUid()).set(data);

                                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                intent.putExtra("username", user.getDisplayName());
                                                intent.putExtra("uid", user.getUid());
                                                intent.putExtra("user-path-firebase", "userData/" + user.getUid());
                                                // Clear fields
                                                name.setText("");
                                                phone.setText("");
                                                email.setText("");
                                                password.setText("");
                                                startActivity(intent);
                                                finish();
                                            }
                                        });


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Invalid email!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (Exception e) {
                Toast.makeText(SignUpActivity.this, e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }

        });
    }
}