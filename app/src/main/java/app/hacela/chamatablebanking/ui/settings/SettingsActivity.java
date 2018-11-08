package app.hacela.chamatablebanking.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.ui.AddExpensesFragment;
import app.hacela.chamatablebanking.ui.AddPaymentFragment;
import app.hacela.chamatablebanking.ui.AddProjectFragment;
import app.hacela.chamatablebanking.ui.LoanTypesFragment;
import app.hacela.chamatablebanking.ui.ManageAccountActivity;
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
   /* @BindView(R.id.settings_password)
    MaterialButton settingsPassword;*/
    @BindView(R.id.settings_manage_classes)
    MaterialButton settingsManageClasses;
    @BindView(R.id.settings_current)
    MaterialButton settingsCurrent;
    @BindView(R.id.settings_help)
    MaterialButton settingsHelp;
    @BindView(R.id.settings_faq)
    MaterialButton settingsFaq;
    @BindView(R.id.settings_add_payments)
    MaterialButton settingsAddPayments;
    @BindView(R.id.settings_add_expenses)
    MaterialButton settingsAddExpenses;
    @BindView(R.id.settings_loan_types)
    MaterialButton settingsLoanTypes;
    @BindView(R.id.settings_add_project)
    MaterialButton settingsAddProject;


    private FirebaseAuth mAuth;

    private AddPaymentFragment addPaymentFragment;
    private AddExpensesFragment addExpensesFragment;
    private LoanTypesFragment loanTypesFragment;
    private AddProjectFragment addProjectFragment;

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

        addPaymentFragment = new AddPaymentFragment();
        addExpensesFragment = new AddExpensesFragment();
        loanTypesFragment = new LoanTypesFragment();
        addProjectFragment = new AddProjectFragment();

    }

    @OnClick(R.id.settings_manage_account)
    public void onSettingsManageAccountClicked() {
        Intent manageAccount = new Intent(SettingsActivity.this, ManageAccountActivity.class);
        startActivity(manageAccount);
    }

    @OnClick(R.id.settings_logout)
    public void onSettingsLogoutClicked() {
        mAuth.signOut();
        sendToLogin();
    }

    @OnClick(R.id.settings_add_project)
   public void onAddProjectClicked() {
        addProjectFragment.show(getSupportFragmentManager(), AddProjectFragment.TAG);
    }

    @OnClick(R.id.settings_loan_types)
    public void onAddLoanTypesClicked(){
        loanTypesFragment.show(getSupportFragmentManager(), LoanTypesFragment.TAG);
    }

    @OnClick(R.id.settings_add_payments)
    public void onAddPaymentsClicked(){
        addPaymentFragment.show(getSupportFragmentManager(), AddPaymentFragment.TAG);
    }

    @OnClick(R.id.settings_add_expenses)
    public void onAddExpensesClicked(){
        addExpensesFragment.show(getSupportFragmentManager(), AddExpensesFragment.TAG);
    }

    /*@OnClick(R.id.settings_password)
    public void onPasswordClicked() {
        //Intent intent = new Intent(SettingsActivity.this, PasscodeActivity.class);startActivity(intent);
    }*/

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
