package app.hacela.chamatablebanking.ui.newchama;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import app.hacela.chamatablebanking.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepTwoFragment extends Fragment {


    @BindView(R.id.st_2_role)
    Spinner st2Role;
    @BindView(R.id.st_2_back)
    TextView st2Back;
    @BindView(R.id.st_2_next)
    TextView st2Next;
    Unbinder unbinder;

    public StepTwoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_two, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.st_2_back)
    public void onSt2BackClicked() {
    }

    @OnClick(R.id.st_2_next)
    public void onSt2NextClicked() {
    }
}
