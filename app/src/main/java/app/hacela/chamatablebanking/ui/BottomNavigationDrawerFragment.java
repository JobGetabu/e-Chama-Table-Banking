package app.hacela.chamatablebanking.ui;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.NavigationView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.viewmodel.MainViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static app.hacela.chamatablebanking.util.Constants.GROUPSCOL;

/**
 * Created by Job on Monday : 9/3/2018.
 */
public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {

    public static final String TAG = "BtmNavFrag";
    private static final int REQUEST_INVITE = 34655;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    Unbinder unbinder;

    //firebase
    private FirebaseAuth auth;
    private FirebaseFirestore mFirestore;

    private ProgressDialog progressDialog;
    private MainViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_bottomsheet, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //firebase
        auth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        // View model
        MainViewModel.Factory factory = new MainViewModel.Factory(
                getActivity().getApplication(), auth, mFirestore);

        mViewModel = ViewModelProviders.of(getActivity(), factory)
                .get(MainViewModel.class);


        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    int id = item.getItemId();
                    switch (id) {
                        case R.id.nav_home:
                            dismiss();
                            return true;
                        case R.id.nav_logout:
                            Toast.makeText(getContext(), "Signing you out", Toast.LENGTH_SHORT).show();

                            AuthUI.getInstance()
                                    .signOut(getContext())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // user is now signed out
                                            auth.signOut();

                                        }
                                    });

                            dismiss();
                            return true;
                        case R.id.nav_createnewgroup:
                            getActivity().startActivity(new Intent(getContext(), CreateChamaActivity.class));
                            dismiss();
                            return true;

                        case R.id.nav_invitemember:

                            progressDialog = new ProgressDialog(getContext());
                            progressDialog.setCancelable(true);
                            progressDialog.show();
                            progressDialog.setMessage("Just a moment...");
                            sendToInviteScreen();
                            dismiss();
                            return true;
                    }
                    return false;
                }
            };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //Todo extra email template setup
    //Manage and control all your chama activities from your phone  groupdisplayname

    private void sendToInviteScreen() {

        final String grID = mViewModel.getGlobalGroupIdMediatorLiveData().getValue();

        if (!TextUtils.isEmpty(grID)) {



            mFirestore.collection(GROUPSCOL).document(grID)
                    .get(Source.CACHE)
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                String grName = (task.getResult().getString("groupname"));

                                String link = "https://chamatablebanking.page.link/gr/?invitedto=" + grID;
                                String link2 = "https://chamatablebanking.page.link/gr";
                                String link3 = buildDeepLink();

                                Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                                        .setMessage("Join " + grName)
                                        .setDeepLink(Uri.parse(link))
                                        //.setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                                        .setCallToActionText(getString(R.string.invitation_cta))
                                        .build();



                                startActivityForResult(intent, REQUEST_INVITE);
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);

                }

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }


            } else {

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        }
    }

    //create own dynamic link

    public String buildDeepLink() {

        String deepLink = "http://jobgetabu.me/e-Chama-Table-banking-Web";
        boolean isAd = false;

        // Get the unique appcode for this app.
        String appCode = getContext().getString(R.string.app_code);

        // Get this app's package name.
        String packageName = getContext().getPackageName();
        String queryParamters = "";
        try {
            queryParamters = generateQueryParameters();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(queryParamters)) {
            deepLink = deepLink + queryParamters;
        }
        // Build the link with all required parameters
        Uri.Builder builder = new Uri.Builder()
                .scheme("https")
                .authority(appCode + ".app.goo.gl")
                .path("/")
                .appendQueryParameter("link", deepLink)
                .appendQueryParameter("apn", packageName);

        // If the deep link is used in an advertisement, this value must be set to 1.
        if (isAd) {
            builder.appendQueryParameter("ad", "1");
        }

        // Minimum version is optional.
        /*
        if (minVersion > 0) {
            builder.appendQueryParameter("amv", Integer.toString(minVersion));
        }

        if (!TextUtils.isEmpty(androidLink)) {
            builder.appendQueryParameter("al", androidLink);
        }

        if (!TextUtils.isEmpty(playStoreAppLink)) {
            builder.appendQueryParameter("afl", playStoreAppLink);
        }
        */

        // Return the completed deep link.
        return builder.build().toString();
    }

    private String generateQueryParameters() throws UnsupportedEncodingException {
        StringBuilder queryParameters = new StringBuilder();
        //server purposes
        queryParameters.append("?*code*");

        Map<String,String> customParameters = new HashMap<>();
        if (!customParameters.isEmpty()) {
            for (Map.Entry<String, String> parameter : customParameters.entrySet()) {
                queryParameters.append(String.format("&%1s=%2s", parameter.getKey(), parameter.getValue()));
            }
        }
        return URLEncoder.encode(queryParameters.toString(), "UTF-8");
    }

}
