package app.hacela.chamatablebanking.ui.settings;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import app.hacela.chamatablebanking.R;

public class FeatureUnavailableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_feature_unavailable);

        timer();
    }

    private void timer(){
        //1 minute
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                //ticking
            }

            public void onFinish() {
               finish();
            }
        }.start();
    }
}
