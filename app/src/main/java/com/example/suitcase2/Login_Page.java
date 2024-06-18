package com.example.suitcase2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Page extends AppCompatActivity {
    EditText txt_email,txt_password;
    Button btn_login;
    TextView forgot_password,txt_signup;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txt_email=findViewById(R.id.login_email);
        txt_password=findViewById(R.id.login_password);
        btn_login=findViewById(R.id.login_btn);
        forgot_password=findViewById(R.id.forgot);
        txt_signup=findViewById(R.id.signup_txt);

        firebaseAuth=FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=txt_email.getText().toString().trim();
                String password=txt_password.getText().toString().trim();

                if (email.isEmpty()){
                    Toast.makeText(Login_Page.this, "Please Enter Email first", Toast.LENGTH_SHORT).show();
                }
                if (password.isEmpty()){
                    Toast.makeText(Login_Page.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                             if (task.isSuccessful()){
                                 Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                 startActivity(intent);
                                 Toast.makeText(Login_Page.this, "Login comp", Toast.LENGTH_SHORT).show();
                                 finish();
                             }else {
                                 Toast.makeText(Login_Page.this, "Login not comp", Toast.LENGTH_SHORT).show();
                             }
                            }
                        });

            }
        });
    }
}