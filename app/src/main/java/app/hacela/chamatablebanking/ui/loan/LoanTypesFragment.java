package app.hacela.chamatablebanking.ui.loan;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.hacela.chamatablebanking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoanTypesFragment extends Fragment {


    public LoanTypesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loan_types, container, false);
    }

}
