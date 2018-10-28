package app.hacela.chamatablebanking.onboarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.ui.MainActivity;
import app.hacela.chamatablebanking.util.DoSnack;

import static app.hacela.chamatablebanking.util.Constants.FIRSTINSTALL_PREFS;

public class OnBoardingActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private SharedPreferences msharedPreferences;
    private DoSnack doSnack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding);

        sharedPreferencesEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        msharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        firstInstallCheck();
        fragmentManager = getSupportFragmentManager();
        doSnack = new DoSnack(this, OnBoardingActivity.this);

        final PaperOnboardingFragment onBoardingFragment = PaperOnboardingFragment.newInstance(getDataForOnboarding());

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, onBoardingFragment);
        fragmentTransaction.commit();

        onBoardingFragment.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
            @Override
            public void onRightOut() {
                Intent intent = new Intent(OnBoardingActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                doSnack.showSnackbar(getString(R.string.you_all_set));
                finish();


                sharedPreferencesEditor.putBoolean(FIRSTINSTALL_PREFS, true);
                sharedPreferencesEditor.apply();
            }
        });
    }

    private ArrayList<PaperOnboardingPage> getDataForOnboarding() {
        // prepare data
        PaperOnboardingPage scr1 = new PaperOnboardingPage(getString(R.string.board1), getString(R.string.board1dsc),
                Color.parseColor("#5889b6"), R.drawable.banks, R.drawable.key);
        PaperOnboardingPage scr2 = new PaperOnboardingPage(getString(R.string.board2), getString(R.string.board2dsc),
                Color.parseColor("#46aeb4"), R.drawable.banks, R.drawable.key);
        PaperOnboardingPage scr3 = new PaperOnboardingPage(getString(R.string.board3), getString(R.string.board3dsc),
                Color.parseColor("#7035ae"), R.drawable.banks, R.drawable.key);
        PaperOnboardingPage scr4 = new PaperOnboardingPage(getString(R.string.board4), getString(R.string.board4dsc),
                Color.parseColor("#466cb4"), R.drawable.banks, R.drawable.key);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr3);
        elements.add(scr4);
        return elements;
    }

    private void firstInstallCheck() {
        if (msharedPreferences.getBoolean(FIRSTINSTALL_PREFS, false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
