package ph.edu.dlsu.mobidev.machineprojectv1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private Button btnSignIn;
    private EditText etEmail, etPassword;
    private TextView tvSignUp;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            //user is logged in
            finish();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }

        tvSignUp = (TextView) findViewById(R.id.tv_sign_up); //bind to text view
        etEmail = (EditText) findViewById(R.id.et_login_email);
        etPassword = (EditText) findViewById(R.id.et_login_password);
        btnSignIn = (Button) findViewById(R.id.btn_log_in);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_login);

        tvSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);

    }

    public void userLogin(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(email.isEmpty()){
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            //start home activity
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Invalid user credentials"
                                    , Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.tv_sign_up:
                startActivity(new Intent(this, SignupActivity.class));
                break;
            case R.id.btn_log_in:
                userLogin();
        }
    }





}
