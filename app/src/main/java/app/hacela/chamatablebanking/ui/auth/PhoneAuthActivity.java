package app.hacela.chamatablebanking.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import app.hacela.chamatablebanking.BuildConfig;
import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.model.Users;
import app.hacela.chamatablebanking.ui.MainActivity;
import app.hacela.chamatablebanking.util.AppStatus;
import app.hacela.chamatablebanking.util.DoSnack;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

import static app.hacela.chamatablebanking.util.Constants.GROUPSCOL;
import static app.hacela.chamatablebanking.util.Constants.GROUPSMEMBERSCOL;
import static app.hacela.chamatablebanking.util.Constants.GROUP_ID_PREFS;
import static app.hacela.chamatablebanking.util.Constants.GROUP_NAME_PREFS;
import static app.hacela.chamatablebanking.util.Constants.GROUP_ROLE_PREFS;
import static app.hacela.chamatablebanking.util.Constants.USERCOL;

public class PhoneAuthActivity extends AppCompatActivity {

    private static final String TAG = "phone";
    public static final int RC_SIGN_IN = 1001;

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    @BindView(R.id.ph_number)
    TextInputLayout phNumber;
    @BindView(R.id.ph_code)
    TextInputLayout phCode;
    @BindView(R.id.ph_continue)
    Button phContinue;
    @BindView(R.id.ph_google)
    ImageView phGoogle;
    @BindView(R.id.signup_head)
    TextView signupHead;
    @BindView(R.id.ph_resend)
    Button phResend;


    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private GoogleSignInClient mGoogleSignInClient;
    private DoSnack doSnack;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private SharedPreferences mSharedPreferences;
    private PhoneNumberUtil mPhoneNumberUtil;
    private String mPhoneNum;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        doSnack = new DoSnack(this, PhoneAuthActivity.this);
        sharedPreferencesEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mPhoneNumberUtil = PhoneNumberUtil.createInstance(this);

        //TODO: REMOVE: TESTING PURPOSES ONLY
        if (BuildConfig.DEBUG) {

            // The test phone number and code should be whitelisted in the console.
            String phoneNumber = "+254711223344";
            String smsCode = "123456";


            FirebaseAuthSettings firebaseAuthSettings = mAuth.getFirebaseAuthSettings();

            // Configure faking the auto-retrieval with the whitelisted numbers.
            firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber, smsCode);
        }
    }

    private boolean isValidMobile(String phone) {
        if (phone.isEmpty()) {
            return false;
        }
        return Patterns.PHONE.matcher(phone).matches();
    }

    @OnClick(R.id.logo)
    public void onLogoClicked() {
    }

    private void tweakUIInit() {

        //ui editing
        phNumber.setVisibility(View.VISIBLE);
        phGoogle.setVisibility(View.VISIBLE);

        phCode.setVisibility(View.GONE);
        phResend.setVisibility(View.GONE);

        signupHead.setText(R.string.enter_phonenumber);
        phContinue.setText(R.string.continue_text);

    }

    private void tweakUICodeIncoming() {

        //ui editing: number entered
        phNumber.setVisibility(View.GONE);
        phGoogle.setVisibility(View.GONE);

        phCode.setVisibility(View.VISIBLE);
        phResend.setVisibility(View.VISIBLE);

        signupHead.setText(R.string.enter_sms_code);
        phContinue.setText(R.string.verify_text);

    }

    private void tweakUISmsFailed() {

        //ui editing
        phNumber.setVisibility(View.VISIBLE);
        phGoogle.setVisibility(View.VISIBLE);

        phCode.setVisibility(View.GONE);
        phResend.setVisibility(View.GONE);

        signupHead.setText(R.string.enter_phonenumber);
        phContinue.setText(R.string.continue_text);

    }

    @OnClick(R.id.ph_continue)
    public void onPhContinueClicked() {

        if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {

            doSnack.showSnackbar(getString(R.string.you_offline), getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onPhContinueClicked();
                }
            });

            return;
        }

        String CN = getString(R.string.continue_text);
        String VR = getString(R.string.verify_text);

      /*  final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF4081"));
        pDialog.setTitleText("SMS sent" + "to " + mPhoneNum);
        pDialog.setContentText("Enter code to continue");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(true);
        pDialog.show();*/


        if (phContinue.getText().equals(CN)) {
            if (validatePhone()) {

                tweakUICodeIncoming();
                doSnack.showShortSnackbar("SMS sent" + " to : " + mPhoneNum);
                startPhoneNumberVerification(mPhoneNum);
            }
        }

        if (phContinue.getText().equals(VR)) {
            if (validateCode()) {

                String code = phCode.getEditText().getText().toString().trim();

                if (mVerificationId != null) {

                    verifyPhoneNumberWithCode(mVerificationId, code);
                }
            }
        }

    }

    @OnClick(R.id.ph_google)
    public void onPhGoogleClicked() {
        if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {

            doSnack.showSnackbar(getString(R.string.you_all_set), getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onPhGoogleClicked();
                }
            });

            return;
        }

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private boolean validatePhone() {
        boolean valid = true;

        String phone = phNumber.getEditText().getText().toString();

        PhoneNumberUtil mPhoneNumberUtil = PhoneNumberUtil.createInstance(this);
        Phonenumber.PhoneNumber kenyaNumberProto = null;
        try {
            kenyaNumberProto = mPhoneNumberUtil.parse(phone, "KE");
        } catch (NumberParseException e) {
            Log.e(TAG, "validate: NumberParseException was thrown: ", e);
        }

        if (phone.isEmpty() || !mPhoneNumberUtil.isValidNumber(kenyaNumberProto)) {
            phNumber.setError("enter a valid phone number");
            valid = false;
        } else {
            phNumber.setError(null);
            Log.d(TAG, "validatePhone: " + kenyaNumberProto.getCountryCode() + "" + kenyaNumberProto.getNationalNumber());
            mPhoneNum = "+254" + kenyaNumberProto.getNationalNumber();
        }


        return valid;
    }

    private boolean validateCode() {
        boolean valid = true;

        String code = phCode.getEditText().getText().toString();

        //TODO: verify sent code too

        if (code.isEmpty()) {
            phCode.setError("invalid code");
            valid = false;
        } else {
            phCode.setError(null);
        }

        return valid;
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF4081"));
        pDialog.setTitleText("Logging in...");
        pDialog.setCancelable(false);
        pDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            final String device_token = FirebaseInstanceId.getInstance().getToken();
                            final String mCurrentUserid = mAuth.getCurrentUser().getUid();

                            // refactor this not to write to DB each time...check if account exists

                            DocumentReference docReference = mFirestore.collection(USERCOL).document(mCurrentUserid);
                            docReference.get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                                    Map<String, Object> updateTokenMap = new HashMap<>();
                                                    updateTokenMap.put("devicetoken", device_token);

                                                    //update token only
                                                    updateTokenOnly(mCurrentUserid, pDialog, updateTokenMap);

                                                } else {
                                                    Log.d(TAG, "No such document");

                                                    //logging in with no pre account
                                                    //region create fresh account

                                                    pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                    pDialog.setTitleText("Account doesn't exists! \n Creating one...");

                                                    //write to db
                                                    writingToUsers(pDialog, device_token, user, mCurrentUserid);

                                                    //endregion
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            pDialog.dismissWithAnimation();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(android.R.id.content), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void writingToUsers(final SweetAlertDialog pDialog, String device_token, FirebaseUser user, String mCurrentUserid) {

        // Set the value of 'Users'
        DocumentReference usersRef = mFirestore.collection(USERCOL).document(mCurrentUserid);

        String usern = user.getDisplayName();
        String pp = String.valueOf(user.getPhotoUrl().toString());

        Users users = new Users(usern, device_token, pp);
        usersRef.set(users)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pDialog.dismissWithAnimation();
                        sendUserToMainActivity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pDialog.dismiss();
                doSnack.errorPrompt("Oops...", e.getMessage());
            }
        });
    }

    private void updateTokenOnly(String mCurrentUserid,
                                 final SweetAlertDialog pDialog, Map<String, Object> updateTokenMap) {

        loadYourGroup();

        // Set the value of 'Users'
        DocumentReference usersRef = mFirestore.collection(USERCOL).document(mCurrentUserid);

        usersRef.set(updateTokenMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pDialog.dismissWithAnimation();
                        sendUserToMainActivity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pDialog.dismiss();
                doSnack.errorPrompt("Oops...", e.getMessage());
            }
        });
    }

    private void sendUserToMainActivity() {
        Intent main = new Intent(this, MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(main);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
                Snackbar.make(findViewById(android.R.id.content), "Google sign in failed", Snackbar.LENGTH_LONG).show();
            }
        }

    }

    private void loadYourGroup() {

        mFirestore.collection(GROUPSMEMBERSCOL).document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                //chama exists
                                mFirestore.collection(GROUPSMEMBERSCOL).document(mAuth.getCurrentUser().getUid())
                                        .get()
                                        .addOnSuccessListener(PhoneAuthActivity.this, new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                String mGroupId = documentSnapshot.getString("groupid");
                                                String mUserRole = documentSnapshot.getString("userrole");
                                                findGroupName(mGroupId);

                                                sharedPreferencesEditor.putString(GROUP_ID_PREFS, mGroupId);
                                                sharedPreferencesEditor.putString(GROUP_ROLE_PREFS, mUserRole);
                                                sharedPreferencesEditor.apply();
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    private void findGroupName(String mGroupId) {
        mFirestore.collection(GROUPSCOL).document(mGroupId)
                .get(Source.DEFAULT)
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            sharedPreferencesEditor.putString(GROUP_NAME_PREFS, task.getResult().getString("groupname"));
                            sharedPreferencesEditor.apply();
                        }
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verificaiton without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
            // [START_EXCLUDE silent]
            mVerificationInProgress = false;
            // [END_EXCLUDE]

            // [START_EXCLUDE silent]
            // Update the UI and attempt sign in with the phone credential
            updateUI(STATE_VERIFY_SUCCESS, phoneAuthCredential);
            // [END_EXCLUDE]
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e);
            // [START_EXCLUDE silent]
            mVerificationInProgress = false;
            // [END_EXCLUDE]

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // [START_EXCLUDE]
                //mPhoneNumberField.setError("Invalid phone number.");
                doSnack.showSnackbar(getString(R.string.invalid_phone_num));
                // [END_EXCLUDE]
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // [START_EXCLUDE]
                //Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.", Snackbar.LENGTH_SHORT).show();
                doSnack.showSnackbar(getString(R.string.quota_exceeded));
                // [END_EXCLUDE]
            }

            // Show a message and update the UI
            // [START_EXCLUDE]
            updateUI(STATE_VERIFY_FAILED);
            // [END_EXCLUDE]
        }

        @Override
        public void onCodeSent(String verificationId,
                               PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;

            // [START_EXCLUDE]
            // Update UI
            updateUI(STATE_CODE_SENT);
            // [END_EXCLUDE]
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
        if (mVerificationInProgress) {
            tweakUICodeIncoming();
        }
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks

        doSnack.showShortSnackbar("SMS Resent");
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF4081"));
        pDialog.setTitleText("Logging in...");
        pDialog.setCancelable(false);
        pDialog.show();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential: success");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            updateUI(STATE_SIGNIN_SUCCESS, user, null, pDialog);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                //mVerificationField.setError("Invalid code.");
                                doSnack.showShortSnackbar("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED, pDialog);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }

    @OnClick(R.id.ph_resend)
    public void onResendSmsViewClicked() {
        //resend sms click
        if (mPhoneNum != null && mResendToken != null) {

            resendVerificationCode(mPhoneNum, mResendToken);
        } else {

            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null, null);
    }

    private void updateUI(int uiState, final SweetAlertDialog pDialog) {
        updateUI(uiState, mAuth.getCurrentUser(), null, pDialog);
    }

    /*private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }*/

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred, null);
    }

    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred, final SweetAlertDialog pDialog) {
        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button

                tweakUIInit();
                break;
            case STATE_CODE_SENT:
                // Code sent state, show the verification field, the

                //tweakUICodeIncoming();
                doSnack.showShortSnackbar(getString(R.string.status_code_sent));

                break;
            case STATE_VERIFY_FAILED:
                // Verification has failed, show all options

                tweakUISmsFailed();
                doSnack.showSnackbar(R.string.status_verification_failed, R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPhContinueClicked();
                    }
                });
                break;
            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
                //disableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
                //mDetailText.setText(R.string.status_verification_succeeded);
                tweakUICodeIncoming();

                // Set the verification text based on the credential
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        //mVerificationField.setText(cred.getSmsCode());

                        phCode.getEditText().setText(cred.getSmsCode());

                    } else {
                        //mVerificationField.setText(R.string.instant_validation);
                        doSnack.showShortSnackbar(getString(R.string.instant_validation));
                    }
                }

                break;
            case STATE_SIGNIN_FAILED:
                // No-op, handled by sign-in check

                if (pDialog != null) {
                    pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    pDialog.setTitle(getString(R.string.status_verification_failed));
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
                }

                tweakUISmsFailed();
                doSnack.showSnackbar(R.string.status_verification_failed, R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPhContinueClicked();
                    }
                });

                break;
            case STATE_SIGNIN_SUCCESS:
                tweakUICodeIncoming();
                // Np-op, handled by sign-in check
                if (pDialog != null) {
                    pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.dismissWithAnimation();

                }
                sendUserToMainActivity();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            sendUserToMainActivity();
        }
    }
}
