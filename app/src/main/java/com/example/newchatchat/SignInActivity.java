package com.example.newchatchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.newchatchat.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    private BeginSignInRequest signInRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Sign Process Starts.");
        progressDialog.setMessage("Check all the credentials....");

        auth = FirebaseAuth.getInstance();

        // google sign in process

//        signInRequest = BeginSignInRequest.builder()
//                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                        .setSupported(true)
//                        // Your server's client ID, not your Android client ID.
//                        .setServerClientId(getString(R.string.default_web_client_id))
//                        // Only show accounts previously used to sign in.
//                        .setFilterByAuthorizedAccounts(true)
//                        .build())
//                .build();


        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              if(binding.email.getText().toString().isEmpty())
              {
                  binding.email.setError("Enter email first");
                  return;
              }
                if(binding.password.getText().toString().isEmpty())
                {
                    binding.password.setError("Enter password first");
                    return;
                }
                progressDialog.show();
                auth.signInWithEmailAndPassword(binding.email.getText().toString(), binding.password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            funIsOnline();
                            Intent intent = new Intent(SignInActivity.this, ChatActivity.class);
                            startActivity(intent);
                        } else
                            Toast.makeText(SignInActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    private void funIsOnline() {
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                    }
                });
            }
        });
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(SignInActivity.this, ChatActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK|intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
public  void gotSingup(View view){
        Intent intent=new Intent(SignInActivity.this,MainActivity.class);
        startActivity(intent);
}
}