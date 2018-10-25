package app.hacela.chamatablebanking.ui.newchama;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.viewmodel.CreateChamaViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepOneFragment extends Fragment {


    @BindView(R.id.step_one_grname)
    TextInputLayout stepgrname;
    @BindView(R.id.step_one_grdesc)
    TextInputLayout stepgrdesc;

    @BindView(R.id.step_unit_back)
    TextView stepUnitBack;
    @BindView(R.id.step_unit_next)
    TextView stepUnitNext;

    Unbinder unbinder;

    private CreateChamaViewModel model;


    public StepOneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_one, container, false);
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

            String grname = stepgrname.getEditText().getText().toString();
            String grdesc = stepgrdesc.getEditText().getText().toString();


            /*model.getLecTeachTimeMediatorLiveData().getValue().setUnitname(unitname);
            model.getLecTeachTimeMediatorLiveData().getValue().setUnitcode(unitcode);*/

        }
    }

    private boolean validate() {
        boolean valid = true;

        String grname = stepgrname.getEditText().getText().toString();
        String grdesc = stepgrdesc.getEditText().getText().toString();

        if (grname.isEmpty()) {
            stepgrname.setError("enter name");
            valid = false;
        } else {
            stepgrname.setError(null);
        }

        if (grdesc.isEmpty()) {
            stepgrdesc.setError("enter description");
            valid = false;
        } else {
            stepgrdesc.setError(null);
        }

        return valid;
    }
}
