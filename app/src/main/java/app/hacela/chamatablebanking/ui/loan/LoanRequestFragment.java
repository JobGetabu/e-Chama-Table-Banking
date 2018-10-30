package app.hacela.chamatablebanking.ui.loan;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;

import app.hacela.chamatablebanking.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;



public class LoanRequestFragment extends BottomSheetDialogFragment {

    @BindView(R.id.pay_phonenumber)
    TextView payPhonenumber;
    @BindView(R.id.pay_username)
    TextView payUsername;
    @BindView(R.id.pay_textamount)
    TextView payTextamount;
    @BindView(R.id.pay_amountinput)
    TextInputLayout payAmountinput;
    @BindView(R.id.pay_editImg)
    ImageButton editImagbtn;

    private View mRootView;

    public static final String TAG = "PayFragment";
    private String userOnlineName = "";


    //starter progress
    private SweetAlertDialog pDialog;

    public LoanRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.loanrequest_checkout, container, false);
        ButterKnife.bind(this, mRootView);

        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textWatcher();


    }


    @OnClick(R.id.pay_amountinput)
    public void onPayAmountinputClicked() {
    }

    @OnClick(R.id.pay_paybtn)
    public void onPayPaybtnClicked() {

        if (validateOnPay()){

            new AlertDialog.Builder(getContext())
                    .setTitle("Confirm Loan Application")
                    .setCancelable(true)
                    .setMessage(R.string.sample_loan_apply_text)
                    .setPositiveButton("Get Loan", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();

                            //processing dialogue
                            pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.setCancelable(false);
                            pDialog.setContentText("Processing Loan Request");
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

    private void timer(){
        //1 minute
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                //ticking
            }

            public void onFinish() {

                pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                pDialog.setCancelable(false);
                pDialog.setTitleText("Loan Approved ");
                pDialog.setContentText("Wait for mpesa text");
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


    @OnClick({R.id.pay_editImg,R.id.pay_textamount})
    public void onPayEditIcon() {
        payAmountinput.setVisibility(View.VISIBLE);
        String am = payTextamount.getText().toString();
        String newstr = am.replaceAll("KES ", "")
                .replaceAll("/-", "")
                .replaceAll(",", "");
        payAmountinput.getEditText().setText(newstr);
        payTextamount.setVisibility(View.GONE);
        editImagbtn.setVisibility(View.GONE);
    }

    @OnClick({R.id.pay_phonenumber, R.id.pay_username, R.id.kngdpay,
            R.id.kjbsavk6pay, R.id.klsdnldvkj, R.id.main_sdbjldv,
            R.id.textView6pay})
    public void onHidePayInputField() {
        payAmountinput.setVisibility(View.GONE);
        payTextamount.setVisibility(View.VISIBLE);
        editImagbtn.setVisibility(View.VISIBLE);
        String am = payAmountinput.getEditText().getText().toString();
        //payTextamount.setText("KES " + am + "/-");
        double temp = 0;
        try {
            temp = Double.parseDouble(am);
        } catch (Exception e) {
            Log.e(TAG, "onHideInputField: ", e);
        }
        payTextamount.setText(formatMyMoney(temp) + "/-");
    }

    private void textWatcher() {

        payAmountinput.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (-1 != payAmountinput.getEditText().getText().toString().indexOf("\n")) {

                    payAmountinput.setVisibility(View.GONE);
                    payTextamount.setVisibility(View.VISIBLE);
                    editImagbtn.setVisibility(View.VISIBLE);
                    String am = payAmountinput.getEditText().getText().toString();

                    //payTextamount.setText("KES " + am + "/-");
                    double temp = 0;
                    try {
                        temp = Double.parseDouble(am);
                    } catch (Exception e) {
                        Log.e(TAG, "onHideInputField: ", e);
                    }
                    payTextamount.setText(formatMyMoney(temp) + "/-");
                }
            }
        });
    }

    private void showWaitDialogue() {

        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#f9ab60"));
        pDialog.setTitleText("Simulating M-pesa Transaction ...");
        pDialog.setCancelable(true);
        pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (userOnlineName.isEmpty()) {
                    dismiss();
                }
            }
        });
        pDialog.show();
    }


    private boolean validateOnPay() {

        boolean valid = true;

        String am = payAmountinput.getEditText().getText().toString();

        if (am.isEmpty() || am.equals("0")) {
            payAmountinput.setError("Amount is not valid");

            payTextamount.setVisibility(View.GONE);
            editImagbtn.setVisibility(View.GONE);
            payAmountinput.setVisibility(View.VISIBLE);

            valid = false;
        } else {
            payTextamount.setVisibility(View.VISIBLE);
            editImagbtn.setVisibility(View.VISIBLE);
            payAmountinput.setVisibility(View.GONE);
            payAmountinput.setError(null);
        }

        if (!am.isEmpty()) {
            if (Double.parseDouble(am) < 10) {
                payAmountinput.setError("Amount must be greater than 10");

                payTextamount.setVisibility(View.GONE);
                editImagbtn.setVisibility(View.GONE);
                payAmountinput.setVisibility(View.VISIBLE);

                valid = false;
            } else {
                payTextamount.setVisibility(View.VISIBLE);
                editImagbtn.setVisibility(View.VISIBLE);
                payAmountinput.setVisibility(View.GONE);
                payAmountinput.setError(null);
            }
        }

        if (!am.isEmpty()) {
            if (Double.parseDouble(am) > 6000) {
                payAmountinput.setError("Amount must be less than 6000");

                payTextamount.setVisibility(View.GONE);
                editImagbtn.setVisibility(View.GONE);
                payAmountinput.setVisibility(View.VISIBLE);

                valid = false;
            } else {
                payTextamount.setVisibility(View.VISIBLE);
                editImagbtn.setVisibility(View.VISIBLE);
                payAmountinput.setVisibility(View.GONE);
                payAmountinput.setError(null);
            }
        }

        return valid;
    }

    public String formatMyMoney(Double money) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        Log.d(TAG, "formatMyMoney: " + formatter.format(money));
        return String.format("KES %,.0f", money);
    }
}
