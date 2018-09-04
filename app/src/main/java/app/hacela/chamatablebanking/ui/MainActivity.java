package app.hacela.chamatablebanking.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Arrays;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.util.ImageProcessor;
import app.hacela.chamatablebanking.viewmodel.MainViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

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
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private ExpandingList mExpandingList;
    private ImageProcessor imageProcessor;

    private MainViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(bar);

        //firebase
        auth = FirebaseAuth.getInstance();

        // View model
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mExpandingList = findViewById(R.id.expanding_list_main);
        createItems();

        imageProcessor = new ImageProcessor(this);

        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user == null) {
                    // Sign in logic here.
                    creatingAuthIntent();
                } else {
                    //play with auth user id

                    setUpUserUI();
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
                        .setIsSmartLockEnabled(false, true)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.EmailBuilder().build()))
                        .build(),
                RC_SIGN_IN);

        finish();

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
        addItem("Contribution", new String[]{"House", "Boat", "Candy", "Collection", "Sport", "Ball", "Head"}, R.color.pink, R.drawable.group_bg);
        addItem("Loans", new String[]{"Dog", "Horse", "Boat"}, R.color.greenb, R.drawable.loan_bg);
        addItem("Fines", new String[]{"Cat"}, R.color.greenb3, R.drawable.profit_bg);
        addItem("Projects", new String[]{"Parrot", "Elephant", "Coffee"}, R.color.purpleb, R.drawable.proj_bg);

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
        ((TextView) view.findViewById(R.id.e_gs_details)).setText(subTitle);
        ((TextView) view.findViewById(R.id.e_gs_date)).setText("12/08/2018");
        ((TextView) view.findViewById(R.id.e_gs_money)).setText("+ Ksh 2,000");

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
                startActivity(new Intent(this, MainActivity.class));

                //finish();


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

    private void setUpUserUI() {
        userInfoUsername.setText(auth.getCurrentUser().getDisplayName());
        imageProcessor.setMyImage(userInfoImage, auth.getCurrentUser().getPhotoUrl().toString());
    }

}
