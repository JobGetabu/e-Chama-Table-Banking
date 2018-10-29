package app.hacela.chamatablebanking.ui.auth;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.widget.Button;
import android.widget.ImageView;

import app.hacela.chamatablebanking.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneAuthActivity extends AppCompatActivity {

    @BindView(R.id.ph_number)
    TextInputLayout phNumber;
    @BindView(R.id.ph_code)
    TextInputLayout phCode;
    @BindView(R.id.ph_continue)
    Button phContinue;
    @BindView(R.id.ph_google)
    ImageView phGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        ButterKnife.bind(this);
    }

    private boolean isValidMobile(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

    @OnClick(R.id.logo)
    public void onLogoClicked() {
    }

    @OnClick(R.id.ph_continue)
    public void onPhContinueClicked() {
    }

    @OnClick(R.id.ph_google)
    public void onPhGoogleClicked() {
    }
}
