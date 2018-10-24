package app.hacela.chamatablebanking.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import app.hacela.chamatablebanking.R;
import butterknife.BindView;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login_email)
    TextInputLayout loginEmail;
    @BindView(R.id.login_password)
    TextInputLayout loginPassword;
    @BindView(R.id.forgotpass)
    TextView forgotpass;
    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.login_via_google)
    LinearLayout loginViaGoogle;

    private static final String TAG = "login";
    public static final int RC_SIGN_IN = 1001;


    private FirebaseAuth userAuth;
    private FirebaseFirestore mFirestore;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userAuth = FirebaseAuth.getInstance();
        String currentUserID = userAuth.getCurrentUser().getUid();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getEditText().getText().toString().trim();
                String password = loginPassword.getEditText().getText().toString().trim();

                if (email.isEmpty() | password.isEmpty()){
                    if (email.isEmpty()){
                        loginEmail.getEditText().setError("Email is required");
                        loginEmail.requestFocus();
                    }
                    if (password.isEmpty()){
                        loginPassword.getEditText().setError("Password is required");
                        loginPassword.requestFocus();
                    }
                }
                else {
                    userAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent main = new Intent(LoginActivity.this, MainActivity.class);
                                        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(main);
                                    }
                                    else {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });

    }
}
