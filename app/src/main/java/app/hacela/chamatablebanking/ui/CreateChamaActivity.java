package app.hacela.chamatablebanking.ui;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import app.hacela.chamatablebanking.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;

public class CreateChamaActivity extends AppCompatActivity implements VerticalStepperForm {

    private static final String TAG = "CreateChama";
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

    private void initStepViews(){
        s2Name = findViewById(R.id.s2_name);
        s3Image = findViewById(R.id.s3_image);
        s3BtnSelectpic = findViewById(R.id.s3_btn_selectpic);
        st4Role = findViewById(R.id.st_4_role);
        s5Entryfee = findViewById(R.id.s5_entryfee);
        s6RegularAmount = findViewById(R.id.s6_regular_amount);
        s6Regular = findViewById(R.id.s6_regular);
        s6Daymonth = findViewById(R.id.s6_daymonth);
        s6Dayweek = findViewById(R.id.s6_dayweek);

    }

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

        initStepViews();

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
                //verticalStepperForm.setStepAsCompleted(stepNumber);
                break;
            case 2:
                //test
                verticalStepperForm.setStepAsCompleted(stepNumber);
                break;
            case 3:
                //test
                verticalStepperForm.setStepAsCompleted(stepNumber);
                break;
            case 4:
                //test
                verticalStepperForm.setStepAsCompleted(stepNumber);
                break;
            case 5:
                //test
                verticalStepperForm.setStepAsCompleted(stepNumber);
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


        Log.d(TAG, "createNameStep: " + s2Name.getEditText().getText().toString());

        if (TextUtils.isEmpty(s2Name.getEditText().getText().toString())) {
            verticalStepperForm.setActiveStepAsUncompleted("Chama name can't be empty");
        } else {
            verticalStepperForm.setActiveStepAsCompleted();
        }

        return v2;
    }

    private View createPhotoStep() {
        // This step view is generated by inflating a layout XML file
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout v3 =
                (LinearLayout) inflater.inflate(R.layout.step_three_photo, null, false);


        return v3;
    }

    private View createRoleStep() {
        // This step view is generated by inflating a layout XML file
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout v4 =
                (LinearLayout) inflater.inflate(R.layout.step_four_role, null, false);


        return v4;
    }

    private View createFeeStep() {
        // This step view is generated by inflating a layout XML file
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout v5 =
                (LinearLayout) inflater.inflate(R.layout.step_five_fee, null, false);


        return v5;
    }

    private View createIntervalStep() {
        // This step view is generated by inflating a layout XML file
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout v6 =
                (LinearLayout) inflater.inflate(R.layout.step_six_intervals, null, false);


        return v6;
    }
}
