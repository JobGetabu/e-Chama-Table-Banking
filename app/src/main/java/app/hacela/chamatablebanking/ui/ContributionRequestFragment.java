package app.hacela.chamatablebanking.ui;


import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.button.MaterialButton;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import app.hacela.chamatablebanking.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContributionRequestFragment extends BottomSheetDialogFragment {

    public static final String TAG = "ContriFragment";

    @BindView(R.id.fcr_ll1)
    LinearLayout fcrLl1;
    @BindView(R.id.pay_textamount)
    TextView payTextamount;
    @BindView(R.id.pay_editImg)
    ImageButton payEditImg;
    @BindView(R.id.pay_amountinput)
    TextInputLayout payAmountinput;
    @BindView(R.id.fcr_continue_btn)
    MaterialButton fcrContinueBtn;
    Unbinder unbinder;


    //starter progress
    private SweetAlertDialog pDialog;

    public ContributionRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contribution_request, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textWatcher();
    }

    @OnClick(R.id.pay_editImg)
    public void onPayEditImgClicked() {
    }

    @OnClick(R.id.fcr_continue_btn)
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
                            pDialog.setContentText("Processing Contribution Request");
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
                    payEditImg.setVisibility(View.VISIBLE);
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

    private boolean validateOnPay() {

        boolean valid = true;

        String am = payAmountinput.getEditText().getText().toString();

        if (am.isEmpty() || am.equals("0")) {
            payAmountinput.setError("Amount is not valid");

            payTextamount.setVisibility(View.GONE);
            payEditImg.setVisibility(View.GONE);
            payAmountinput.setVisibility(View.VISIBLE);

            valid = false;
        } else {
            payTextamount.setVisibility(View.VISIBLE);
            payEditImg.setVisibility(View.VISIBLE);
            payAmountinput.setVisibility(View.GONE);
            payAmountinput.setError(null);
        }

        if (!am.isEmpty()) {
            if (Double.parseDouble(am) < 10) {
                payAmountinput.setError("Amount must be greater than 10");

                payTextamount.setVisibility(View.GONE);
                payEditImg.setVisibility(View.GONE);
                payAmountinput.setVisibility(View.VISIBLE);

                valid = false;
            } else {
                payTextamount.setVisibility(View.VISIBLE);
                payEditImg.setVisibility(View.VISIBLE);
                payAmountinput.setVisibility(View.GONE);
                payAmountinput.setError(null);
            }
        }

        if (!am.isEmpty()) {
            if (Double.parseDouble(am) > 6000) {
                payAmountinput.setError("Amount must be less than 6000");

                payTextamount.setVisibility(View.GONE);
                payEditImg.setVisibility(View.GONE);
                payAmountinput.setVisibility(View.VISIBLE);

                valid = false;
            } else {
                payTextamount.setVisibility(View.VISIBLE);
                payEditImg.setVisibility(View.VISIBLE);
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

    @OnClick({R.id.pay_editImg,R.id.pay_textamount})
    public void onPayEditIcon() {
        payAmountinput.setVisibility(View.VISIBLE);
        String am = payTextamount.getText().toString();
        String newstr = am.replaceAll("KES ", "")
                .replaceAll("/-", "")
                .replaceAll(",", "");
        payAmountinput.getEditText().setText(newstr);
        payTextamount.setVisibility(View.GONE);
        payEditImg.setVisibility(View.GONE);
    }

    @OnClick({R.id.fcr_tv1, R.id.fcr_tv2, R.id.textView8,
            R.id.fcr_ll1, R.id.fcr_hsv1, R.id.fcr_tv3})
    public void onHidePayInputField() {
        payAmountinput.setVisibility(View.GONE);
        payTextamount.setVisibility(View.VISIBLE);
        payEditImg.setVisibility(View.VISIBLE);
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
