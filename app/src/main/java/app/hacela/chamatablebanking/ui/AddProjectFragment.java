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
public class AddProjectFragment extends BottomSheetDialogFragment {
    public static final String TAG = "AddProjectFragment";

    @BindView(R.id.fapr_ll1)
    LinearLayout faprLl1;
    @BindView(R.id.fapr_et2)
    TextInputLayout projectamount;
    @BindView(R.id.fapr_et1)
    TextInputLayout projectTitle;
    @BindView(R.id.fapr_et3)
    TextInputLayout projectDeadline;
    @BindView(R.id.fapr_continue_btn)
    MaterialButton faprContinueBtn;
    Unbinder unbinder;

    //starter progress
    private SweetAlertDialog pDialog;

    public AddProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_project, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
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
    @OnClick(R.id.fapr_continue_btn)
    public void onFaprContinueBtnClicked() {
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

    private boolean validateOnPay() {
        boolean valid = true;

        return valid;
    }

}
