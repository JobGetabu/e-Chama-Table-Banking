package app.hacela.chamatablebanking.ui.settings;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import app.hacela.chamatablebanking.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.settings_toolbar)
    Toolbar settingsToolbar;

    @BindView(R.id.settings_manage_account)
    MaterialButton settingsManageAccount;
    @BindView(R.id.settings_logout)
    MaterialButton settingsLogout;
    @BindView(R.id.settings_password)
    MaterialButton settingsPassword;
    @BindView(R.id.settings_manage_classes)
    MaterialButton settingsManageClasses;
    @BindView(R.id.settings_current)
    MaterialButton settingsCurrent;
    @BindView(R.id.settings_help)
    MaterialButton settingsHelp;
    @BindView(R.id.settings_faq)
    MaterialButton settingsFaq;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(settingsToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_toolbar_back));

        //firebase
        mAuth = FirebaseAuth.getInstance();

    }

    @OnClick(R.id.settings_manage_account)
    public void onSettingsManageAccountClicked() {

    }

    @OnClick(R.id.settings_logout)
    public void onSettingsLogoutClicked() {
        mAuth.signOut();

        sendToLogin();
    }

    @OnClick(R.id.settings_password)
    public void onPasswordClicked() {
        //Intent intent = new Intent(SettingsActivity.this, PasscodeActivity.class);startActivity(intent);
    }

    private void sendToLogin() {

    }

    @OnClick(R.id.settings_manage_classes)
    public void onViewAddClassClicked() {

    }

    @OnClick(R.id.settings_current)
    public void onSettingsCurrentClicked() {

    }

    @OnClick(R.id.settings_help)
    public void onSettingsHelpClicked() {
        //Constants.createEmailIntent(this,R.string.dev_email,R.string.dev_subject, "Case Id \n"+mAuth.getCurrentUser().getUid()+" \n \n "+mAuth.getCurrentUser().getDisplayName()+"\n");
    }

    @OnClick(R.id.settings_faq)
    public void onSettingsFaqClicked() {
        //Intent fIntent = new Intent(this, FaqActivity.class); startActivity(fIntent);

    }
}
