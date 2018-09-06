package app.hacela.chamatablebanking.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.datasource.GroupsAccount;
import app.hacela.chamatablebanking.datasource.GroupsContributionDefault;
import app.hacela.chamatablebanking.datasource.GroupsMembers;
import app.hacela.chamatablebanking.viewmodel.CreateChamaViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;

import static app.hacela.chamatablebanking.util.Constants.GROUPSACCOUNTCOL;
import static app.hacela.chamatablebanking.util.Constants.GROUPSCOL;
import static app.hacela.chamatablebanking.util.Constants.GROUPSCONTRIBUTIONDEFAULTCOL;
import static app.hacela.chamatablebanking.util.Constants.GROUPSMEMBERSCOL;

public class CreateChamaActivity extends AppCompatActivity implements VerticalStepperForm {

    private static final String TAG = "CreateChama";
    private static final int PICK_IMAGE_REQUEST = 101;

    @BindView(R.id.vertical_stepper_form)
    VerticalStepperFormLayout verticalStepperForm;


    private TextInputLayout s2Name;
    private ImageView s3Image;
    private MaterialButton s3BtnSelectpic;
    private Spinner st4Role;
    private TextInputLayout s5Entryfee;
    private TextInputLayout s6RegularAmount;
    private EditText s6StartDate;
    private Spinner s6Regular;
    private Spinner s6Daymonth;
    private Spinner s6Dayweek;

    //firebase
    private FirebaseAuth auth;
    private FirebaseFirestore mFirestore;
    private FirebaseStorage storageReference;
    private StorageReference mImageReference;

    private Date mDate;
    private Uri mResultPhotoFile;
    private ProgressDialog progressDialog;
    private ByteArrayOutputStream mBaos = new ByteArrayOutputStream();
    private CreateChamaViewModel chamaViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chama);
        ButterKnife.bind(this);

        chamaViewModel = ViewModelProviders.of(this).get(CreateChamaViewModel.class);


        //firebase
        auth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance();


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

        s6Regular.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (adapterView.getSelectedItemPosition() == 0) {
                    s6Daymonth.setVisibility(View.VISIBLE);
                    s6Dayweek.setVisibility(View.GONE);

                } else {
                    s6Daymonth.setVisibility(View.GONE);
                    s6Dayweek.setVisibility(View.VISIBLE);
                }
                Toast.makeText(CreateChamaActivity.this, adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

                verticalStepperForm.setStepAsCompleted(stepNumber);


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


                final Calendar myCalendar = Calendar.getInstance();


                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                        mDate = myCalendar.getTime();
                        String myFormat = "MM/dd/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

                        s6StartDate.setText(sdf.format(myCalendar.getTime()));
                    }
                };

                s6StartDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO Auto-generated method stub
                        new DatePickerDialog(CreateChamaActivity.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                s6StartDate.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (TextUtils.isEmpty(s6StartDate.getText().toString())) {
                            verticalStepperForm.setStepAsUncompleted(stepNumber, "Select a date for next meeting");
                        } else {
                            verticalStepperForm.setActiveStepAsCompleted();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                break;
        }
    }

    private void formChama() {

        //TODO : check net

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setMessage(getString(R.string.creating_new_chama_group));


        String groupid = mFirestore.collection(GROUPSCOL).document().getId();

        //group data
        HashMap<String, Object> groupMap = new HashMap<>();
        groupMap.put("groupid", groupid);
        groupMap.put("groupname", s2Name.getEditText().getText().toString());
        groupMap.put("createdate", FieldValue.serverTimestamp());
        groupMap.put("createbyid", auth.getCurrentUser().getUid());
        groupMap.put("createbyname", auth.getCurrentUser().getDisplayName());

        //Group Account
        GroupsAccount groupsAccount = new GroupsAccount(0, 0);

        //Group Members
        GroupsMembers groupsMembers = new GroupsMembers(groupid, true, auth.getCurrentUser().getUid(),
                auth.getCurrentUser().getDisplayName(), "admin", st4Role.getSelectedItem().toString());

        //group contribution default
        GroupsContributionDefault grContrDflt = new GroupsContributionDefault();
        grContrDflt.setAmount(Double.parseDouble(s6RegularAmount.getEditText().getText().toString()));
        grContrDflt.setEntryfee(Double.parseDouble(s5Entryfee.getEditText().getText().toString()));
        grContrDflt.setStartdate(mDate);
        grContrDflt.setCycleintervaltype(s6Regular.getSelectedItem().toString());
        if (s6Dayweek.getVisibility() == View.VISIBLE) {
            grContrDflt.setDayofweek(s6Dayweek.getSelectedItem().toString());
            grContrDflt.setDayofmonth("");
        } else {
            grContrDflt.setDayofweek("");
            grContrDflt.setDayofmonth(s6Daymonth.getSelectedItem().toString());
        }


        //no photo selected

        //refs
        DocumentReference GROUPREF = mFirestore.collection(GROUPSCOL).document(groupid);
        DocumentReference GROUPDEFREF = mFirestore.collection(GROUPSCONTRIBUTIONDEFAULTCOL).document(groupid);
        DocumentReference GROUPACCREF = mFirestore.collection(GROUPSACCOUNTCOL).document(groupid);
        DocumentReference GROUPMEMBERSREF = mFirestore.collection(GROUPSMEMBERSCOL).document(auth.getCurrentUser().getUid());

        // Get a new write batch
        WriteBatch batch = mFirestore.batch();
        //upload the group
        batch.set(GROUPREF, groupMap);
        //upload group default
        batch.set(GROUPDEFREF, grContrDflt);
        //upload group member
        batch.set(GROUPMEMBERSREF, groupsMembers);
        //upload group accounts
        batch.set(GROUPACCREF, groupsAccount);



        if (mResultPhotoFile != null) {

            uploadWithPhoto(groupid, groupMap, batch);

        } else {

            groupMap.put("photourl", "");

            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(CreateChamaActivity.this, "Successful !", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {

                        progressDialog.setCancelable(true);
                        progressDialog.setTitle(R.string.error_occured);
                        progressDialog.setMessage(task.getException().getMessage());
                        progressDialog.setCanceledOnTouchOutside(true);

                    }
                }
            });
        }


    }

    private void uploadWithPhoto(String groupid, final HashMap<String, Object> groupMap, final WriteBatch batch) {

        mImageReference = storageReference.getReference(groupid+"/image.jpg");

        s3Image.setDrawingCacheEnabled(true);
        s3Image.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) s3Image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = mImageReference.putBytes(data);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {

                    // Handle failures
                    progressDialog.setCancelable(true);
                    progressDialog.setTitle(R.string.error_occured);
                    progressDialog.setMessage(task.getException().getMessage());
                    progressDialog.setCanceledOnTouchOutside(true);

                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return mImageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){

                    Uri downloadUri = task.getResult();
                    groupMap.put("photourl", downloadUri.toString());

                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                progressDialog.dismiss();
                                Toast.makeText(CreateChamaActivity.this, "Successful !", Toast.LENGTH_SHORT).show();
                                finish();

                            } else {

                                progressDialog.setCancelable(true);
                                progressDialog.setTitle(R.string.error_occured);
                                progressDialog.setMessage(task.getException().getMessage());
                                progressDialog.setCanceledOnTouchOutside(true);

                            }
                        }
                    });


                }else {
                    progressDialog.setCancelable(true);
                    progressDialog.setTitle(R.string.error_occured);
                    progressDialog.setMessage(task.getException().getMessage());
                    progressDialog.setCanceledOnTouchOutside(true);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST) {

            if (data != null) {

                Uri resultUri = data.getData();

                s3Image.setVisibility(View.VISIBLE);
                s3Image.setImageURI(resultUri);
                s3Image.setScaleType(ImageView.ScaleType.CENTER);

                mResultPhotoFile = resultUri;
            }
        }
    }
}
