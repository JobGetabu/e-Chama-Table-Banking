package app.hacela.chamatablebanking.ui.newchama;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.viewmodel.NewChamaViewModel;
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

    private NewChamaViewModel model;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        model = ViewModelProviders.of(getActivity()).get(NewChamaViewModel.class);

        onInit();

        st4Regular.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (adapterView.getSelectedItemPosition() == 0) {
                    st4Daymonth.setVisibility(View.VISIBLE);
                    st4Dayweek.setVisibility(View.GONE);

                } else {
                    st4Daymonth.setVisibility(View.GONE);
                    st4Dayweek.setVisibility(View.VISIBLE);
                }
                Toast.makeText(getContext(), adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void onInit() {
        String sel = st4Regular.getSelectedItem().toString();
        if (sel.equals("Once a Month")){
            st4Daymonth.setVisibility(View.VISIBLE);
            st4Dayweek.setVisibility(View.GONE);
        }else {
            st4Daymonth.setVisibility(View.GONE);
            st4Dayweek.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.st_4_back)
    public void onSt4BackClicked() {
        model.setCurrentStep(3);
    }

    @OnClick(R.id.st_4_next)
    public void onSt4NextClicked() {
        model.setCurrentStep(5);

        //TODO: Save this on the db
    }
}
