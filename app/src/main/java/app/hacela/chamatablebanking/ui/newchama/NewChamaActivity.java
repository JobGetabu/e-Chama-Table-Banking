package app.hacela.chamatablebanking.ui.newchama;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.shuhart.stepview.StepView;

import java.util.Arrays;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.adapter.AddChamaPagerAdapter;
import app.hacela.chamatablebanking.adapter.NoSwipePager;
import app.hacela.chamatablebanking.ui.ChamaExistsActivity;
import app.hacela.chamatablebanking.viewmodel.NewChamaViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

import static app.hacela.chamatablebanking.util.Constants.GROUP_ID_PREFS;
import static app.hacela.chamatablebanking.util.Constants.GROUP_NAME_PREFS;
import static app.hacela.chamatablebanking.util.Constants.GROUP_ROLE_PREFS;

public class NewChamaActivity extends AppCompatActivity {

    @BindView(R.id.add_chama_step_view)
    StepView addChamaStepView;
    @BindView(R.id.add_chama_noswipepager)
    NoSwipePager addChamaNoswipepager;

    private AddChamaPagerAdapter pagerAdapter;
    private NewChamaViewModel model;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chama);
        ButterKnife.bind(this);

        //see if has user

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (preChamaExists()) {
            startActivity(new Intent(this, ChamaExistsActivity.class));
            finish();
            return;

        } else {
            // continue
        }

        pagerAdapter = new AddChamaPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragments(new StepInitFragment());
        pagerAdapter.addFragments(new StepOneFragment());
        pagerAdapter.addFragments(new StepTwoFragment());
        pagerAdapter.addFragments(new StepThreeFragment());
        pagerAdapter.addFragments(new StepFourFragment());
        pagerAdapter.addFragments(new StepFiveFragment());

        addChamaNoswipepager.setAdapter(pagerAdapter);
        addChamaNoswipepager.setPagingEnabled(false);
        addChamaNoswipepager.setOffscreenPageLimit(3);


        addChamaStepView.getState()
                .steps(Arrays.asList(getResources().getStringArray(R.array.addchama_steps)))
                .commit();

        model = ViewModelProviders.of(this).get(NewChamaViewModel.class);

        model.getCurrentStep().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer != null){
                    addChamaNoswipepager.setCurrentItem(integer);
                    addChamaStepView.go(integer, true);
                    //addClassStepView.done(true);
                }
            }
        });
    }

    private boolean preChamaExists() {
        String gid = mSharedPreferences.getString(GROUP_ID_PREFS, null);
        String gname = mSharedPreferences.getString(GROUP_NAME_PREFS, null);
        String role = mSharedPreferences.getString(GROUP_ROLE_PREFS, null);

        if (gid != null && gname != null && role != null) {

            if (role.equals("admin")) {
                return true;
            }
        }
        return false;
    }
}
