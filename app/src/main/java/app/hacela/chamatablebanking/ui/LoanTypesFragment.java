package app.hacela.chamatablebanking.ui;


import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import app.hacela.chamatablebanking.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoanTypesFragment extends BottomSheetDialogFragment {

    public static final String TAG = "LoanTypeFragment";
    @BindView(R.id.flt_ll1)
    LinearLayout fltLl1;
    @BindView(R.id.flt_et2)
    TextInputLayout loanRates;
    @BindView(R.id.flt_et1)
    TextInputLayout loanPeriod;
    @BindView(R.id.flt_et3)
    TextInputLayout loanGuarantors;
    @BindView(R.id.flt_cb1)
    CheckBox cb1;
    @BindView(R.id.flt_continue_btn)
    MaterialButton fapContinueBtn;
    Unbinder unbinder;

    //starter progress
    private SweetAlertDialog pDialog;


    public LoanTypesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loan_types, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.flt_continue_btn)
    public void onFcrContinueBtnClicked() {

        if (validateOnPay()){

            new AlertDialog.Builder(getContext())
                    .setTitle("Confirm Payment Request")
                    .setCancelable(true)
                    .setMessage(R.string.sample_loan_apply_text)
                    .setPositiveButton("Send Request", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();

                            //processing dialogue
                            pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.setCancelable(false);
                            pDialog.setContentText("Processing Payment Request");
                            pDialog.show();
                            timer();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    })
                    .show();

        }
    }

    private void timer() {
        //1 minute
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                //ticking
            }

            public void onFinish() {

                pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                pDialog.setCancelable(false);
                pDialog.setTitleText("Request Sent ");
                pDialog.setContentText("Wait for Request Approved");
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        dismiss();

                    }
                });
            }
        }.start();
    }

    private boolean validateOnPay() {

        boolean valid = true;

        String lp = loanPeriod.getEditText().getText().toString();
        String lr = loanRates.getEditText().getText().toString();
        String lg = loanGuarantors.getEditText().getText().toString();

        if (!cb1.isChecked()){
            loanGuarantors.setVisibility(View.GONE);
        }
        else {
            loanGuarantors.setVisibility(View.VISIBLE);

            if (lg.isEmpty() || lg.equals("0")) {
                loanGuarantors.setError("Number of Guarantors must be 1 or greater");
                loanGuarantors.setVisibility(View.VISIBLE);
                valid = false;
            } else {
                loanGuarantors.setError(null);
            }
        }

        if (lp.isEmpty() || lp.equals("0")) {
            loanPeriod.setError("Period is not valid");
            loanPeriod.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            loanPeriod.setError(null);
        }

        if (lr.isEmpty()) {
            loanRates.setError("Enter valid loan rates");
            loanRates.setVisibility(View.VISIBLE);

            valid = false;
        } else {
            loanRates.setError(null);
        }

        if (!lp.isEmpty()) {
            if (Double.parseDouble(lr) < 7) {
                loanPeriod.setError("Period must be greater than a week");
                loanPeriod.setVisibility(View.VISIBLE);

                valid = false;
            } else {
                loanPeriod.setError(null);
            }
        }

        if (!lp.isEmpty()) {
            if (Double.parseDouble(lp) > 365) {
                loanPeriod.setError("Period must be less than an year");
                loanPeriod.setVisibility(View.VISIBLE);

                valid = false;
            } else {
                loanPeriod.setError(null);
            }
        }

        return valid;
    }
}


