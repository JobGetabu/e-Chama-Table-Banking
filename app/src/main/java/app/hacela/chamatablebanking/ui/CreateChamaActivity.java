package app.hacela.chamatablebanking.ui;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.File;
import java.util.HashMap;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.datasource.Groups;
import app.hacela.chamatablebanking.datasource.GroupsAccount;
import app.hacela.chamatablebanking.datasource.GroupsContributionDefault;
import app.hacela.chamatablebanking.datasource.GroupsMembers;
import app.hacela.chamatablebanking.util.ImageProcessor;
import app.hacela.chamatablebanking.viewmodel.CreateChamaViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;

import static app.hacela.chamatablebanking.util.Constants.GROUPSCOL;

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
    private TextInputLayout s6StartDate;
    private MaterialBetterSpinner s6Regular;
    private MaterialBetterSpinner s6Daymonth;
    private MaterialBetterSpinner s6Dayweek;

    //firebase
    private FirebaseAuth auth;
    private FirebaseFirestore mFirestore;

    private ImageProcessor imageProcessor;
    private Uri mResultPhotoFile;
    private ProgressDialog progressDialog;
    private CreateChamaViewModel chamaViewModel;
    private Groups groups;
    private GroupsContributionDefault groupsContributionDefault;
    private String selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chama);
        ButterKnife.bind(this);

        imageProcessor = new ImageProcessor(this);
        chamaViewModel = ViewModelProviders.of(this).get(CreateChamaViewModel.class);
        groups = new Groups();
        groupsContributionDefault = new GroupsContributionDefault();

        //firebase
        auth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


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


        //register observers
        uiObserver();
    }

    private void uiObserver() {
        chamaViewModel.getGroupsMediatorLiveData().observe(this, new Observer<Groups>() {
            @Override
            public void onChanged(@Nullable Groups groups) {

                if (groups != null) {

                    s2Name.getEditText().setText(groups.getGroupname());
                    Log.d(TAG, "onChanged: " + groups.toString());
                }
            }
        });
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

                //pass description
                verticalStepperForm.setStepAsCompleted(stepNumber);
                break;
            case 1:
                //test chama name
                stepVerification(1);
                break;
            case 2:
                //test photo picked
                stepVerification(2);
                break;
            case 3:
                //test role
                stepVerification(3);
                break;
            case 4:
                //test entry fee
                stepVerification(4);
                break;
            case 5:
                //test regulars
                stepVerification(5);
                break;
        }
    }

    @Override
    public void sendData() {

        Log.d(TAG, "onSend Data: " + groups.toString());

        formChama();
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

        s3Image.setVisibility(View.GONE);

        s3BtnSelectpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateChamaActivity.this, "Select image of your chama", Toast.LENGTH_SHORT).show();

                //start image intent
                Intent imageIntent = new Intent();
                imageIntent.setType("image/*");
                imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(imageIntent, "Select Chama Picture"), PICK_IMAGE_REQUEST);
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
        s6StartDate = v6.findViewById(R.id.s6_start_date);

        ArrayAdapter<String> arrayAdapterMeetCycles = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.meetingcyles));

        ArrayAdapter<String> arrayAdapterMonthCycles = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.monthcyles));

        ArrayAdapter<String> arrayAdapterWeekCycles = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.weekcyles));

        s6Regular.setAdapter(arrayAdapterMeetCycles);
        s6Daymonth.setAdapter(arrayAdapterMonthCycles);
        s6Dayweek.setAdapter(arrayAdapterWeekCycles);

        s6Daymonth.setVisibility(View.GONE);
        s6Dayweek.setVisibility(View.GONE);

        s6Regular.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                if (adapterView.getSelectedItemPosition() == 0) {
                    s6Daymonth.setVisibility(View.VISIBLE);
                    s6Dayweek.setVisibility(View.GONE);

                } else {
                    s6Daymonth.setVisibility(View.GONE);
                    s6Dayweek.setVisibility(View.VISIBLE);
                }

                String cycleintervaltype = adapterView.getItemAtPosition(i).toString();
            }
        });

        return v6;
    }

    private void stepVerification(final int stepNumber) {
        switch (stepNumber) {
            case 1:


                s2Name.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (TextUtils.isEmpty(s2Name.getEditText().getText().toString())) {
                            verticalStepperForm.setStepAsUncompleted(stepNumber, "Chama name can't be empty");
                        } else {
                            verticalStepperForm.setActiveStepAsCompleted();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                break;
            case 2:
                //pass
                verticalStepperForm.setStepAsCompleted(stepNumber);
                break;
            case 3:
                //spinner pass
                st4Role.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        if (adapterView.isSelected()) {
                            verticalStepperForm.setStepAsUncompleted(stepNumber, "Select an option");
                        } else {
                            verticalStepperForm.setActiveStepAsCompleted();
                            selectedRole = adapterView.getSelectedItem().toString();
                        }

                    }
                });
                break;
            case 4:
                //test

                s5Entryfee.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (TextUtils.isEmpty(s5Entryfee.getEditText().getText().toString())) {
                            verticalStepperForm.setStepAsUncompleted(stepNumber, "Entry fee can't be empty");
                        } else {
                            verticalStepperForm.setActiveStepAsCompleted();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                break;
            case 5:
                //test

                s6RegularAmount.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (TextUtils.isEmpty(s6RegularAmount.getEditText().getText().toString())) {
                            verticalStepperForm.setStepAsUncompleted(stepNumber, "Regular Contribution can't be empty");
                        } else {
                            verticalStepperForm.setActiveStepAsCompleted();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                s6Regular.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        if (adapterView.getSelectedItemPosition() == 0) {

                            s6Daymonth.setVisibility(View.VISIBLE);
                            s6Dayweek.setVisibility(View.GONE);
                        }

                        if (adapterView.getSelectedItemPosition() == 1) {

                            s6Daymonth.setVisibility(View.GONE);
                            s6Dayweek.setVisibility(View.VISIBLE);
                        }

                        if (adapterView.isSelected()) {
                            verticalStepperForm.setStepAsUncompleted(stepNumber, "Select an option");
                        } else {
                            verticalStepperForm.setActiveStepAsCompleted();
                            groupsContributionDefault.setCycleintervaltype(adapterView.getSelectedItem().toString());
                        }

                    }
                });

                if (s6Daymonth.getVisibility() == View.VISIBLE) {
                    s6Daymonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            if (adapterView.isSelected()) {
                                verticalStepperForm.setStepAsUncompleted(stepNumber, "Select a month option");
                            } else {
                                verticalStepperForm.setActiveStepAsCompleted();
                                groupsContributionDefault.setDayofmonth(adapterView.getSelectedItem().toString());
                            }

                        }
                    });
                }

                if (s6Dayweek.getVisibility() == View.VISIBLE) {
                    s6Dayweek.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            if (adapterView.isSelected()) {
                                verticalStepperForm.setStepAsUncompleted(stepNumber, "Select a week option");
                            } else {
                                verticalStepperForm.setActiveStepAsCompleted();
                                groupsContributionDefault.setDayofweek(adapterView.getSelectedItem().toString());
                            }

                        }
                    });
                }

                break;
        }
    }

    private void formChama() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.setMessage(getString(R.string.creating_new_chama_group));

        groups.setGroupname(s2Name.getEditText().getText().toString());
        groupsContributionDefault.setAmount(Double.parseDouble(s6RegularAmount.getEditText().getText().toString()));
        groupsContributionDefault.setEntryfee(Double.parseDouble(s5Entryfee.getEditText().getText().toString()));


        String groupid = mFirestore.collection(GROUPSCOL).document().getId();

        //group data
        HashMap<String, Object> groupMap = new HashMap<>();
        groupMap.put("groupid", groupid);
        groupMap.put("groupname", groups.getGroupname());
        groupMap.put("createdate", FieldValue.serverTimestamp());
        groupMap.put("createbyid", auth.getCurrentUser().getUid());
        groupMap.put("photourl", "");
        groupMap.put("createbyname", auth.getCurrentUser().getDisplayName());

        //Group Account
        GroupsAccount groupsAccount = new GroupsAccount(0, 0);

        //Group Members

        GroupsMembers groupsMembers = new GroupsMembers(groupid, true, auth.getCurrentUser().getUid(),
                auth.getCurrentUser().getDisplayName(), "admin", selectedRole);


        Log.d(TAG, "formChama: " + groups.toString());

        if (mResultPhotoFile != null) {

            uploadWithPhoto();
        } else {

            //no photo selected

        }

        chamaViewModel.setGroupsMediatorLiveData(groups);
    }

    private void uploadWithPhoto() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST) {

            if (data != null) {

                Uri resultUri = data.getData();
                File imagefile = new File(resultUri.getPath());

                s3Image.setVisibility(View.VISIBLE);
                mResultPhotoFile = resultUri;
                s3Image.setImageURI(resultUri);
            }
        }
    }
}
