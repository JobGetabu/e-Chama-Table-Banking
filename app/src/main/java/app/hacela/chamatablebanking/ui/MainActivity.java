package app.hacela.chamatablebanking.ui;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;
import java.util.Random;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.datasource.GroupsMembers;
import app.hacela.chamatablebanking.datasource.Users;
import app.hacela.chamatablebanking.util.ImageProcessor;
import app.hacela.chamatablebanking.viewmodel.MainViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static app.hacela.chamatablebanking.util.Constants.GROUPSCOL;
import static app.hacela.chamatablebanking.util.Constants.GROUPSMEMBERSCOL;
import static app.hacela.chamatablebanking.util.Constants.USERCOL;

public class MainActivity extends AppCompatActivity {

    // Choose an arbitrary request code value
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "MainA";

    @BindView(R.id.main_bar)
    BottomAppBar bar;
    @BindView(R.id.main_fab)
    FloatingActionButton fab;
    @BindView(R.id.user_info_username)
    TextView userInfoUsername;
    @BindView(R.id.user_info_role)
    TextView userInfoRole;
    @BindView(R.id.user_info_group)
    TextView userInfoGroup;
    @BindView(R.id.user_info_image)
    CircleImageView userInfoImage;
    @BindView(R.id.user_info_img_notification)
    ImageButton userInfoImgNotification;

    //firebase
    private FirebaseAuth auth;
    private FirebaseFirestore mFirestore;

    private ExpandingList mExpandingList;
    private ImageProcessor imageProcessor;

    private MainViewModel mViewModel;
    private String mGroupId;
    private String mUserRole;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(bar);
        imageProcessor = new ImageProcessor(this);


        //firebase
        auth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user == null) {
                    // Sign in logic here.
                    creatingAuthIntent();
                } else {
                    //play with auth user id


                    // View model
                    MainViewModel.Factory factory = new MainViewModel.Factory(
                            MainActivity.this.getApplication(), auth, mFirestore);

                    mViewModel = ViewModelProviders.of(MainActivity.this, factory)
                            .get(MainViewModel.class);

                    //read db data
                    mExpandingList = findViewById(R.id.expanding_list_main);
                    createItems();

                    firstTimeUser();
                    loadYourGroup();

                    //observers
                    userUIObserver();

                }
            }
        });
    }

    //global var groupid
    private void loadYourGroup() {
        //mGroupId;
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setMessage(getString(R.string.dialog_loading_group));

        mFirestore.collection(GROUPSMEMBERSCOL).document(auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (!document.exists()) {
                                progressDialog.dismiss();

                                new AlertDialog.Builder(MainActivity.this)
                                        .setCancelable(false)
                                        .setTitle(R.string.not_in_agroup)
                                        .setMessage("Join or Create a micro savings group to continue")
                                        .setNegativeButton("Join one", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {


                                                Snackbar.make(findViewById(android.R.id.content),
                                                        R.string.chama_link_txt, Snackbar.LENGTH_INDEFINITE)
                                                        .setAction("Ok", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                finish();
                                                            }
                                                        })
                                                        .show();

                                                dialog.dismiss();
                                            }
                                        })
                                        .setPositiveButton("Create Group", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                startActivity(new Intent(MainActivity.this, CreateChamaActivity.class));
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNeutralButton("Later", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                dialogInterface.dismiss();
                                                finish();
                                            }
                                        })
                                        .show();

                            } else {

                                //chama exists
                                mFirestore.collection(GROUPSMEMBERSCOL).document(auth.getCurrentUser().getUid())
                                        .get()
                                        .addOnSuccessListener(MainActivity.this, new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                mGroupId = documentSnapshot.getString("groupid");
                                                mUserRole = documentSnapshot.getString("userrole");
                                                mViewModel.setGlobalGroupIdMediatorLiveData(mGroupId);
                                                progressDialog.dismiss();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        //handle errors

                                        progressDialog.dismiss();

                                        new AlertDialog.Builder(MainActivity.this)
                                                .setCancelable(false)
                                                .setTitle(R.string.error_occured)
                                                .setMessage(e.getMessage())
                                                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {

                                                        dialog.dismiss();
                                                        finish();
                                                    }
                                                });
                                    }
                                });
                            }
                        } else {
                            //handle errors

                            progressDialog.dismiss();

                            new AlertDialog.Builder(MainActivity.this)
                                    .setCancelable(false)
                                    .setTitle(R.string.error_occured)
                                    .setMessage(task.getException().getMessage())
                                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            dialog.dismiss();
                                            finish();
                                        }
                                    });
                        }
                    }
                });

    }

    private void firstTimeUser() {

        mFirestore.collection(USERCOL).document(auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: ");
                            } else {
                                Log.d(TAG, "No such document... creating");

                                String device_token = FirebaseInstanceId.getInstance().getToken();
                                String mCurrentUserid = auth.getCurrentUser().getUid();

                                Users users = new Users(mCurrentUserid, device_token, auth.getCurrentUser().getPhotoUrl().toString());

                                mFirestore.collection(USERCOL).document(auth.getCurrentUser().getUid())
                                        .set(users);

                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user == null) {
                    // Sign in logic here.
                    creatingAuthIntent();
                }
            }
        });
    }

    private void creatingAuthIntent() {

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.avatar_placeholder)
                        .setTheme(R.style.AppTheme)
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.EmailBuilder().build()))
                        .build(),
                RC_SIGN_IN);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.bottom_nav_drawer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                BottomNavigationDrawerFragment bottomNavDrawerFragment = new BottomNavigationDrawerFragment();
                bottomNavDrawerFragment.show(getSupportFragmentManager(), BottomNavigationDrawerFragment.TAG);
                break;
        }

        return true;
    }

    private void createItems() {
        addItem("Contribution", new String[]{"Marian Njoro", "Man Solo", "Wairimu Tess", "Collins Omondi", "Job Getabu", "Leopord the", "Ule Mathe", "John timba", "Kaschana Mresh", "Babayoko Kizee"}, R.color.pink, R.drawable.group_bg);
        addItem("Loans", new String[]{"Collins Omondi", "Job Getabu", "Man Solo"}, R.color.greenb, R.drawable.loan_bg);
        addItem("Fines", new String[]{"Marian Njoro", "Job Getabu", "Leopord the", "Ule Mathe"}, R.color.greenb3, R.drawable.profit_bg);
        addItem("Projects", new String[]{"Chicken farm", "Cow Farming", "Coffee plantation"}, R.color.purpleb, R.drawable.proj_bg);

    }

    private void addItem(String title, String[] subItems, int colorRes, int iconRes) {
        //Let's create an item with R.layout.expanding_layout
        final ExpandingItem item = mExpandingList.createNewItem(R.layout.expanding_layout);

        //If item creation is successful, let's configure it
        if (item != null) {
            item.setIndicatorColorRes(colorRes);
            item.setIndicatorIconRes(iconRes);
            //It is possible to get any view inside the inflated layout. Let's set the text in the item
            ((TextView) item.findViewById(R.id.e_g_item)).setText(title);

            //We can create items in batch.
            item.createSubItems(subItems.length);
            for (int i = 0; i < item.getSubItemsCount(); i++) {
                //Let's get the created sub item by its index
                final View view = item.getSubItemView(i);

                //Let's set some values in
                configureSubItem(item, view, subItems[i]);
            }


           /*
            item.findViewById(R.id.add_more_sub_items).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInsertDialog(new OnItemCreated() {
                        @Override
                        public void itemCreated(String title) {
                            View newSubItem = item.createSubItem();
                            configureSubItem(item, newSubItem, title);
                        }
                    });
                }
            });


            item.findViewById(R.id.remove_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExpandingList.removeItem(item);
                }
            });
            */
        }
    }

    private void configureSubItem(final ExpandingItem item, final View view, String subTitle) {
        Random random = new Random();
        int a = random.nextInt(9);
        int b = random.nextInt(10);
        int c = random.nextInt(10);
        int d = random.nextInt(10);

        ((TextView) view.findViewById(R.id.e_gs_details)).setText(subTitle);
        ((TextView) view.findViewById(R.id.e_gs_date)).setText(c+"/08/2018");

        String cash = "+ Ksh"+a+","+b+c+d;

        ((TextView) view.findViewById(R.id.e_gs_money)).setText(cash);

       /* view.findViewById(R.id.remove_sub_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.removeSubItem(view);
            }
        });*/
    }

    private void showInsertDialog(final OnItemCreated positive) {
        final EditText text = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(text);
        builder.setTitle(R.string.enter_title);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                positive.itemCreated(text.getText().toString());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }

    @OnClick(R.id.main_fab)
    public void onFabViewClicked() {
    }

    interface OnItemCreated {
        void itemCreated(String title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {

                Toast.makeText(this, "" + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = getIntent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                showSnackbar(R.string.unknown_error);
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    private void showSnackbar(int text) {
        Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG).show();
    }

    private void userUIObserver() {
        userInfoUsername.setText(auth.getCurrentUser().getDisplayName());
        imageProcessor.setMyImage(userInfoImage, auth.getCurrentUser().getPhotoUrl().toString());

        mViewModel.getGroupsMembersMediatorLiveData().observe(this, new Observer<GroupsMembers>() {
            @Override
            public void onChanged(@Nullable GroupsMembers groupsMembers) {
                if (groupsMembers != null) {
                    userInfoRole.setText(groupsMembers.getUserrole());
                    mFirestore.collection(GROUPSCOL).document(groupsMembers.getGroupid())
                            .get(Source.DEFAULT)
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        userInfoGroup.setText(task.getResult().getString("groupname"));
                                    }
                                }
                            });
                }
            }
        });
    }

    private void loadUIPerRole() {

    }

}
