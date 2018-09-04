package app.hacela.chamatablebanking.ui;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import app.hacela.chamatablebanking.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;

public class CreateChamaActivity extends AppCompatActivity implements VerticalStepperForm {

    private static final String TAG = "CreateChama";
    private static final int PICK_IMAGE_REQUEST = 101;

    @BindView(R.id.vertical_stepper_form)
    VerticalStepperFormLayout verticalStepperForm;



    private TextInputLayout s2Name;
    private ImageView s3Image;
    private MaterialButton s3BtnSelectpic;
    private MaterialBetterSpinner st4Role;
    private TextInputLayout s5Entryfee;
    private TextInputLayout s6RegularAmount;
    private MaterialBetterSpinner s6Regular;
    private MaterialBetterSpinner s6Daymonth;
    private MaterialBetterSpinner s6Dayweek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chama);
        ButterKnife.bind(this);


        String[] stepsTitles = getResources().getStringArray(R.array.steps_titles);
        String[] stepsSubtitles = getResources().getStringArray(R.array.steps_subtitles);

        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);

        // Setting up and initializing the form
        VerticalStepperFormLayout.Builder.newInstance(verticalStepperForm, stepsTitles, this, this)
                .stepsSubtitles(stepsSubtitles)
                .materialDesignInDisabledSteps(true) // false by default
                //.showVerticalLineWhenStepsAreCollapsed(true) // false by default
                .primaryColor(colorPrimary)
                .primaryDarkColor(colorPrimaryDark)
                .displayBottomNavigation(true) // It is true by default, so in this case this line is not necessary
                .init();


    }


    @Override
    public View createStepContentView(int stepNumber) {
        // Here we generate the content view of the correspondent step and we return it so it gets
        // automatically added to the step layout (AKA stepContent)
        View view = null;
        switch (stepNumber) {
            case 0:
                view = createIntroStep();
                break;
            case 1:
                view = createNameStep();
                break;
            case 2:
                view = createPhotoStep();
                break;
            case 3:
                view = createRoleStep();
                break;
            case 4:
                view = createFeeStep();
                break;
            case 5:
                view = createIntervalStep();
                break;
        }
        return view;
    }


    @Override
    public void onStepOpening(int stepNumber) {

        switch (stepNumber) {
            case 0:

                //test
                verticalStepperForm.setStepAsCompleted(stepNumber);
                break;
            case 1:
                //test
                stepVerification(1);
                break;
            case 2:
                //test
                stepVerification(2);
                break;
            case 3:
                //test
                stepVerification(3);
                break;
            case 4:
                //test
                stepVerification(4);
                break;
            case 5:
                //test
                stepVerification(5);
                break;
        }
    }

    @Override
    public void sendData() {

    }

    private View createIntroStep() {
        // This step view is generated by inflating a layout XML file
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout v1 =
                (LinearLayout) inflater.inflate(R.layout.step_one_intro, null, false);


        return v1;
    }

    private View createNameStep() {
        // This step view is generated by inflating a layout XML file
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout v2 =
                (LinearLayout) inflater.inflate(R.layout.step_two_name, null, false);

        s2Name = v2.findViewById(R.id.s2_name);

        return v2;
    }

    private View createPhotoStep() {
        // This step view is generated by inflating a layout XML file
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout v3 =
                (LinearLayout) inflater.inflate(R.layout.step_three_photo, null, false);

        s3Image = v3.findViewById(R.id.s3_image);
        s3BtnSelectpic = v3.findViewById(R.id.s3_btn_selectpic);

        s3BtnSelectpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateChamaActivity.this, "Select image of your chama", Toast.LENGTH_SHORT).show();
            }
        });
        return v3;
    }

    private View createRoleStep() {
        // This step view is generated by inflating a layout XML file
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout v4 =
                (LinearLayout) inflater.inflate(R.layout.step_four_role, null, false);

        st4Role = v4.findViewById(R.id.st_4_role);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.chamaroles));

        st4Role.setAdapter(arrayAdapter);

        return v4;
    }

    private View createFeeStep() {
        // This step view is generated by inflating a layout XML file
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout v5 =
                (LinearLayout) inflater.inflate(R.layout.step_five_fee, null, false);

        s5Entryfee = v5.findViewById(R.id.s5_entryfee);
        return v5;
    }

    private View createIntervalStep() {
        // This step view is generated by inflating a layout XML file
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout v6 =
                (LinearLayout) inflater.inflate(R.layout.step_six_intervals, null, false);



        s6RegularAmount = v6.findViewById(R.id.s6_regular_amount);
        s6Regular = v6.findViewById(R.id.s6_regular);
        s6Daymonth = v6.findViewById(R.id.s6_daymonth);
        s6Dayweek = v6.findViewById(R.id.s6_dayweek);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.chamaroles));

        st4Role.setAdapter(arrayAdapter);

        return v6;
    }

    private void stepVerification(final int stepNumber){
        switch (stepNumber) {
            case 1:



                s2Name.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (TextUtils.isEmpty(s2Name.getEditText().getText().toString())) {
                            verticalStepperForm.setStepAsUncompleted(stepNumber,"Chama name can't be empty");
                        } else {
                            verticalStepperForm.setActiveStepAsCompleted();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });

                break;
            case 2:
                //pass
                verticalStepperForm.setStepAsCompleted(stepNumber);
                break;
            case 3:
                //spinner pass
                verticalStepperForm.setStepAsCompleted(stepNumber);
                break;
            case 4:
                //test

                s5Entryfee.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (TextUtils.isEmpty(s5Entryfee.getEditText().getText().toString())) {
                            verticalStepperForm.setStepAsUncompleted(stepNumber,"Entry fee can't be empty");
                        } else {
                            verticalStepperForm.setActiveStepAsCompleted();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });

                break;
            case 5:
                //test

                s6RegularAmount.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (TextUtils.isEmpty(s6RegularAmount.getEditText().getText().toString())) {
                            verticalStepperForm.setStepAsUncompleted(stepNumber,"Regular Contribution can't be empty");
                        } else {
                            verticalStepperForm.setActiveStepAsCompleted();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });

                break;
        }
    }
}
