package app.hacela.chamatablebanking.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import app.hacela.chamatablebanking.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageAccountActivity extends AppCompatActivity {

    @BindView(R.id.ma_toolbar)
    Toolbar ma_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        ButterKnife.bind(this);

        setSupportActionBar(ma_toolbar);
        getSupportActionBar().setTitle("Manage Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
