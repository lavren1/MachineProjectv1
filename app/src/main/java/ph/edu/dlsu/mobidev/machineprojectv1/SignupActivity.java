package ph.edu.dlsu.mobidev.machineprojectv1;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by francis omangayon on 10/26/2017.
 */

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "createUser";
    EditText signup_email, signup_password, signup_repassword;
    Button signup_button;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        signup_email = (EditText) findViewById(R.id.et_signup_email);
        signup_password = (EditText) findViewById(R.id.et_signup_password);
        signup_repassword = (EditText) findViewById(R.id.et_signup_repassword);
        signup_button = (Button) findViewById(R.id.btn_sign_up);

        signup_button.setOnClickListener(this);
    }
    private void registerUser(){
        String email = signup_email.getText().toString().trim();
        String password = signup_password.getText().toString().trim();
        String confpass = signup_repassword.getText().toString().trim();

        if(email.isEmpty()){
            signup_email.setError("Username is required");
            signup_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signup_email.setError("Please enter a valid email");
            signup_email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            signup_password.setError("Password is required");
            signup_password.requestFocus();
            return;
        }
        if(confpass.isEmpty()){
            signup_repassword.setError("Please confirm your password");
            signup_repassword.requestFocus();
            return;
        }
        if(!password.equals(confpass)){
            signup_repassword.setError("Passwords do not match");
            signup_repassword.requestFocus();
            return;
        }

        //gawa progress bar
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "create user with email: "+task.isSuccessful());
                            Toast.makeText(getApplicationContext(), "User Registered Successfully",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_sign_up:
                registerUser();
                break;
        }
    }
}
