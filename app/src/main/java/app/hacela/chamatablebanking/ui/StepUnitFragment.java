package app.hacela.chamatablebanking.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.viewmodel.CreateChamaViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepUnitFragment extends Fragment {


    @BindView(R.id.step_unit_unitname)
    TextInputLayout stepUnitUnitname;
    @BindView(R.id.step_unit_unitcode)
    TextInputLayout stepUnitUnitcode;

    @BindView(R.id.step_unit_back)
    TextView stepUnitBack;
    @BindView(R.id.step_unit_next)
    TextView stepUnitNext;

    Unbinder unbinder;

    private CreateChamaViewModel model;
    Calendar mcurrentTime = Calendar.getInstance();

    public StepUnitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_unit, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        model = ViewModelProviders.of(getActivity()).get(CreateChamaViewModel.class);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.step_unit_back)
    public void onStepUnitBackClicked() {
        model.setCurrentStep(0);
    }

    @OnClick(R.id.step_unit_next)
    public void onStepUnitNextClicked() {
        if (validate()) {

            model.setCurrentStep(2);

            String unitname = stepUnitUnitname.getEditText().getText().toString();
            String unitcode = stepUnitUnitcode.getEditText().getText().toString();


            /*model.getLecTeachTimeMediatorLiveData().getValue().setUnitname(unitname);
            model.getLecTeachTimeMediatorLiveData().getValue().setUnitcode(unitcode);*/

        }
    }

    private boolean validate() {
        boolean valid = true;

        String unitname = stepUnitUnitname.getEditText().getText().toString();
        String unitcode = stepUnitUnitcode.getEditText().getText().toString();

        if (unitname.isEmpty()) {
            stepUnitUnitname.setError("enter unit");
            valid = false;
        } else {
            stepUnitUnitname.setError(null);
        }

        if (unitcode.isEmpty()) {
            stepUnitUnitcode.setError("enter unit code");
            valid = false;
        } else {
            stepUnitUnitcode.setError(null);
        }

        return valid;
    }
}
