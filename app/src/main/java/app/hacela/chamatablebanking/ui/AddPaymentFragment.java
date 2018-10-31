package app.hacela.chamatablebanking.ui;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.hacela.chamatablebanking.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPaymentFragment extends BottomSheetDialogFragment {
    public static final String TAG = "ContriFragment";

    @BindView(R.id.fap_ll1)
    LinearLayout fapLl1;
    @BindView(R.id.fap_et2)
    TextInputLayout payTextamount;
    @BindView(R.id.fap_et1)
    TextInputLayout payDetails;
    @BindView(R.id.fap_continue_btn)
    MaterialButton fapContinueBtn;
    Unbinder unbinder;

    //starter progress
    private SweetAlertDialog pDialog;

    public AddPaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_payment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

}
