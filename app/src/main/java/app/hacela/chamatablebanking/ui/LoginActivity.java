package app.hacela.chamatablebanking.ui;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import app.hacela.chamatablebanking.R;
import butterknife.BindView;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.signup_email)
    TextInputLayout loginEmail;
    @BindView(R.id.signup_password)
    TextInputLayout loginPassword;
    @BindView(R.id.forgotpass)
    TextView forgotpass;
    @BindView(R.id.signup_button)
    Button loginButton;
    @BindView(R.id.login_via_google)
    LinearLayout loginViaGoogle;

    private static final String TAG = "login";
    public static final int RC_SIGN_IN = 1001;


    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
