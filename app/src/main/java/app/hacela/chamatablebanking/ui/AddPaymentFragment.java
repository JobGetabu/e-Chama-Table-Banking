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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.hacela.chamatablebanking.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPaymentFragment extends BottomSheetDialogFragment {
    public static final String TAG = "AddPayFragment";

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fap_continue_btn)
    public void onFapContinueBtnClicked() {

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

        String am = payTextamount.getEditText().getText().toString();
        String det = payDetails.getEditText().getText().toString();

        if (am.isEmpty() || am.equals("0")) {
            payTextamount.setError("Amount is not valid");


            payTextamount.setVisibility(View.VISIBLE);

            valid = false;
        } else {

//            payTextamount.setVisibility(View.GONE);
            payTextamount.setError(null);
        }

        if (det.isEmpty()) {
            payTextamount.setError("Please provide some details");
            payTextamount.setVisibility(View.VISIBLE);

            valid = false;
        } else {

//            payTextamount.setVisibility(View.GONE);
            payTextamount.setError(null);
        }

        if (!am.isEmpty()) {
            if (Double.parseDouble(am) < 10) {
                payTextamount.setError("Amount must be greater than 10");


                payTextamount.setVisibility(View.VISIBLE);

                valid = false;
            } else {

//                payTextamount.setVisibility(View.GONE);
                payTextamount.setError(null);
            }
        }

        if (!am.isEmpty()) {
            if (Double.parseDouble(am) > 6000) {
                payTextamount.setError("Amount must be less than 6000");
                payTextamount.setVisibility(View.VISIBLE);

                valid = false;
            } else {
//                payTextamount.setVisibility(View.GONE);
                payTextamount.setError(null);
            }
        }

        return valid;
    }
}
