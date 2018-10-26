package app.hacela.chamatablebanking.ui.newchama;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.shuhart.stepview.StepView;

import java.util.Arrays;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.adapter.AddChamaPagerAdapter;
import app.hacela.chamatablebanking.adapter.NoSwipePager;
import app.hacela.chamatablebanking.viewmodel.NewChamaViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewChamaActivity extends AppCompatActivity {

    @BindView(R.id.add_chama_step_view)
    StepView addChamaStepView;
    @BindView(R.id.add_chama_noswipepager)
    NoSwipePager addChamaNoswipepager;

    private AddChamaPagerAdapter pagerAdapter;
    private NewChamaViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chama);
        ButterKnife.bind(this);

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
}
