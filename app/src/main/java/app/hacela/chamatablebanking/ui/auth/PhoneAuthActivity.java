package app.hacela.chamatablebanking.ui.auth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import app.hacela.chamatablebanking.R;

public class PhoneAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
