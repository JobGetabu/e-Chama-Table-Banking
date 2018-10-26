package app.hacela.chamatablebanking.ui.newchama;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class StepThreeFragment extends Fragment {


    @BindView(R.id.st_3_pic)
    ImageView st3Pic;
    @BindView(R.id.st_3_pic_btn)
    MaterialButton st3PicBtn;
    @BindView(R.id.st_3_minfee)
    TextInputLayout st3Minfee;
    @BindView(R.id.st_3_back)
    TextView st3Back;
    @BindView(R.id.st_3_next)
    TextView st3Next;

    Unbinder unbinder;

    private CreateChamaViewModel model;

    public StepThreeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_three, container, false);
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

    @OnClick(R.id.st_3_pic_btn)
    public void onSt3PicBtnClicked() {
    }

    @OnClick(R.id.st_3_back)
    public void onSt3BackClicked() {
        model.setCurrentStep(2);
    }

    @OnClick(R.id.st_3_next)
    public void onSt3NextClicked() {

        if (validate()) {

            model.setCurrentStep(4);
        }
    }

    private boolean validate() {
        boolean valid = true;

        String minfee = st3Minfee.getEditText().getText().toString();

        if (minfee.isEmpty()) {
            st3Minfee.setError("enter min fee");
            valid = false;
        } else {
            st3Minfee.setError(null);
        }

        return valid;
    }
}
