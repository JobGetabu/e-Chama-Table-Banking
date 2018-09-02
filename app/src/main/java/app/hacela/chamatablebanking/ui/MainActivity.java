package app.hacela.chamatablebanking.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import app.hacela.chamatablebanking.R;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }

}
