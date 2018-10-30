package app.hacela.chamatablebanking.ui.auth;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.model.Users;
import app.hacela.chamatablebanking.ui.MainActivity;
import app.hacela.chamatablebanking.util.AppStatus;
import app.hacela.chamatablebanking.util.DoSnack;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static app.hacela.chamatablebanking.util.Constants.GROUPSCOL;
import static app.hacela.chamatablebanking.util.Constants.GROUPSMEMBERSCOL;
import static app.hacela.chamatablebanking.util.Constants.GROUP_ID_PREFS;
import static app.hacela.chamatablebanking.util.Constants.GROUP_NAME_PREFS;
import static app.hacela.chamatablebanking.util.Constants.GROUP_ROLE_PREFS;
import static app.hacela.chamatablebanking.util.Constants.SETTINGS_LOGIN_PHONE_PREFS;
import static app.hacela.chamatablebanking.util.Constants.USERCOL;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login_email)
    TextInputLayout loginEmail;
    @BindView(R.id.login_password)
    TextInputLayout loginPassword;
    @BindView(R.id.login_forgotpass)
    MaterialButton forgotpass;
    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.login_via_google_image)
    ImageView google_image;
    @BindView(R.id.create_account)
    MaterialButton create_account;
    @BindView(R.id.background_dim_login)
    View dim;

    private static final String TAG = "login";
    public static final int RC_SIGN_IN = 1001;


    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private GoogleSignInClient mGoogleSignInClient;
    private DoSnack doSnack;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        doSnack = new DoSnack(this, LoginActivity.this);
        sharedPreferencesEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            sendUserToMainActivity();
        }

       /* if (checkPrefLogin()){
            sendToPhoneAuth();
        }*/

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
        String pp = user.getPhotoUrl().toString();

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
        Intent main = new Intent(LoginActivity.this, MainActivity.class);
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

    @OnClick(R.id.login_button)
    public void onLoginButtonClicked() {

        if (validate()) {

            if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {

                doSnack.showSnackbar(getString(R.string.you_offline), getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onLoginForgotpassClicked();
                    }
                });

                return;
            }

            String email = loginEmail.getEditText().getText().toString();
            String password = loginPassword.getEditText().getText().toString();

            if (validate()) {

                final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF4081"));
                pDialog.setTitleText("Logging in...");
                pDialog.setCancelable(false);
                pDialog.show();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> authtask) {
                                if (authtask.isSuccessful()) {

                                    //login successful

                                    //update device token
                                    String devicetoken = FirebaseInstanceId.getInstance().getToken();

                                    Map<String, Object> updateTokenMap = new HashMap<>();
                                    updateTokenMap.put("devicetoken", devicetoken);

                                    String mCurrentUserid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    updateTokenOnly(mCurrentUserid, pDialog, updateTokenMap);


                                } else {
                                    pDialog.dismiss();
                                    doSnack.UserAuthToastExceptions(authtask);
                                }
                            }
                        });
            }
        }
    }

    @OnClick(R.id.login_forgotpass)
    public void onLoginForgotpassClicked() {
        if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {

            doSnack.showSnackbar(getString(R.string.you_offline), getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLoginForgotpassClicked();
                }
            });

            return;
        }

        final String email = loginEmail.getEditText().getText().toString();
        if (email.isEmpty() || !isEmailValid(email)) {
            loginEmail.setError("enter valid email");
            return;
        } else {
            loginEmail.setError(null);
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            doSnack.showSnackbar("Email sent to " + email, "Check", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                                    try {
                                        //startActivity(intent);
                                        startActivity(Intent.createChooser(intent, getString(R.string.chooseEmailClient)));
                                    } catch (ActivityNotFoundException e) { }
                                }
                            });
                        }else {
                            doSnack.showShortSnackbar("You're not registered :(");
                        }
                    }
                });

    }

    @OnClick(R.id.create_account)
    public void onCreateAccountClicked() {
        startActivity(new Intent(this,SignUpActivity.class));
    }

    @OnClick(R.id.logo)
    public void onLogoClicked() {
        doSnack.showShortSnackbar(getString(R.string.chama_create_txt));
    }

    @OnClick(R.id.login_via_google_image)
    public void onLoginViaGoogleImageClicked() {
        if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {

            doSnack.showSnackbar("You're offline", "Retry", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLoginViaGoogleImageClicked();
                }
            });

            return;
        }

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validate() {
        boolean valid = true;

        String email = loginEmail.getEditText().getText().toString();
        String password = loginPassword.getEditText().getText().toString();

        if (email.isEmpty() || !isEmailValid(email)) {
            loginEmail.setError("enter valid email");
            valid = false;
        } else {
            loginEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            loginPassword.setError("at least 6 characters");
            valid = false;
        } else {
            loginPassword.setError(null);
        }

        return valid;
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
                                        .addOnSuccessListener(LoginActivity.this, new OnSuccessListener<DocumentSnapshot>() {
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

    private boolean checkPrefLogin() {
        if (mSharedPreferences.getBoolean(SETTINGS_LOGIN_PHONE_PREFS,true)){
            return true;
        }
        return false;
    }

    private void sendToPhoneAuth(){
        Intent pp = new Intent(LoginActivity.this, PhoneAuthActivity.class);
        pp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(pp);
        finish();
    }
}
