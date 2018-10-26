package app.hacela.chamatablebanking.ui.newchama;


import android.app.Fragment;
import android.os.Bundle;
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
public class StepFourFragment extends Fragment {


    @BindView(R.id.st_4_regular)
    Spinner st4Regular;
    @BindView(R.id.st_4_daymonth)
    Spinner st4Daymonth;
    @BindView(R.id.st_4_dayweek)
    Spinner st4Dayweek;
    @BindView(R.id.st_4_back)
    TextView st4Back;
    @BindView(R.id.st_4_next)
    TextView st4Next;
    Unbinder unbinder;

    public StepFourFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_four, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.st_4_back)
    public void onSt4BackClicked() {
    }

    @OnClick(R.id.st_4_next)
    public void onSt4NextClicked() {
    }
}
