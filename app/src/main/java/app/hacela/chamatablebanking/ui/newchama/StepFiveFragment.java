package app.hacela.chamatablebanking.ui.newchama;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
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
import java.util.HashMap;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.model.Groups;
import app.hacela.chamatablebanking.model.GroupsAccount;
import app.hacela.chamatablebanking.model.GroupsContributionDefault;
import app.hacela.chamatablebanking.model.GroupsMembers;
import app.hacela.chamatablebanking.ui.MainActivity;
import app.hacela.chamatablebanking.util.AppStatus;
import app.hacela.chamatablebanking.util.DoSnack;
import app.hacela.chamatablebanking.viewmodel.NewChamaViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;
import static app.hacela.chamatablebanking.util.Constants.GROUPSACCOUNTCOL;
import static app.hacela.chamatablebanking.util.Constants.GROUPSCOL;
import static app.hacela.chamatablebanking.util.Constants.GROUPSCONTRIBUTIONDEFAULTCOL;
import static app.hacela.chamatablebanking.util.Constants.GROUPSMEMBERSCOL;
import static app.hacela.chamatablebanking.util.Constants.GROUP_ID_PREFS;
import static app.hacela.chamatablebanking.util.Constants.GROUP_NAME_PREFS;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepFiveFragment extends Fragment {

    private static final int REQUEST_INVITE = 246522;
    private static final String TAG = "FinalStep";

    @BindView(R.id.st_5_invite)
    MaterialButton st5Invite;
    @BindView(R.id.st_5_inviteskip_btn)
    TextView st5InviteskipBtn;
    Unbinder unbinder;

    private DoSnack doSnack;
    private NewChamaViewModel model;
    private SharedPreferences.Editor sharedPreferencesEditor;

    //firebase
    private FirebaseAuth auth;
    private FirebaseFirestore mFirestore;
    private FirebaseStorage storageReference;
    private StorageReference mImageReference;


    public StepFiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_five, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //firebase
        auth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance();

        model = ViewModelProviders.of(getActivity()).get(NewChamaViewModel.class);
        doSnack = new DoSnack(getContext(), getActivity());
        sharedPreferencesEditor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void saveButtonClick(final boolean isInvite) {
        if (!AppStatus.getInstance(getContext()).isOnline()) {

            doSnack.showSnackbar("You're offline", "Retry", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSt5InviteClicked();
                }
            });

            return;
        }

        //dialogue
        final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF5521"));
        pDialog.setTitleText(getContext().getString(R.string.creating_new_chama_group));
        pDialog.setContentText("Just a moment...");
        pDialog.setCancelable(false);
        pDialog.show();

        //TODO: Create chama. Send to invite screen

        final Groups groups = model.getGroupsMediatorLiveData().getValue();
        GroupsContributionDefault grContrDflt = model.getGroupsContributionDefaultMediatorLiveData().getValue();
        GroupsMembers mem = model.getGroupsMembersMediatorLiveData().getValue();
        //Group Account
        GroupsAccount groupsAccount = new GroupsAccount(0, 0);

        final String groupid = mFirestore.collection(GROUPSCOL).document().getId();

        //group data
        HashMap<String, Object> groupMap = new HashMap<>();
        groupMap.put("groupid", groupid);
        groupMap.put("groupname", groups.getGroupname());
        groupMap.put("groupdescription", groups.getGroupdescription());
        groupMap.put("createdate", FieldValue.serverTimestamp());
        groupMap.put("createbyid", auth.getCurrentUser().getUid());
        groupMap.put("createbyname", auth.getCurrentUser().getDisplayName());

        //Group Members
        GroupsMembers groupsMembers = new GroupsMembers(groupid, true, auth.getCurrentUser().getUid(),
                auth.getCurrentUser().getDisplayName(), "admin", mem.getRoledescription());

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

        if (groups.getPhotourl() != null) {

            uploadWithPhoto(groupid, groupMap, batch, pDialog,isInvite,groups.getGroupname());

        } else {

            groupMap.put("photourl", "");

            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        refreshPrefs(groupid,groups.getGroupname());
                        String m = "You're now set";
                        if (isInvite) {
                            m = "You're now set, Invite your members";
                        }

                        pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        pDialog.setCancelable(true);
                        pDialog.setTitleText("Saved Successfully");
                        pDialog.setContentText(m);
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();


                                if (isInvite) {
                                    sendToInviteScreen(groupid,groups.getGroupname());

                                } else {
                                    sendToMain();
                                }

                            }
                        });

                    } else {

                        pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        pDialog.setCancelable(true);
                        pDialog.setTitleText(getString(R.string.error_occured));
                        pDialog.setContentText(task.getException().getMessage());
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                    sendToMain();
                            }
                        });
                    }
                }
            });
        }
    }

    private void uploadWithPhoto(final String groupid, final HashMap<String, Object> groupMap,
                                 final WriteBatch batch, final SweetAlertDialog pDialog, final boolean isInvite, final String groupname) {

        mImageReference = storageReference.getReference(groupid+"/image.jpg");

        Bitmap bitmap = model.getBitmapMediatorLiveData().getValue();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = mImageReference.putBytes(data);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {

                    pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    pDialog.setCancelable(true);
                    pDialog.setTitleText(getString(R.string.error_occured));
                    pDialog.setContentText(task.getException().getMessage());
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            sendToMain();
                        }
                    });
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

                                refreshPrefs(groupid,groupname);
                                String m = "You're now set";
                                if (isInvite) {
                                    m = "You're now set, Invite your members";
                                }

                                pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.setCancelable(true);
                                pDialog.setTitleText("Saved Successfully");
                                pDialog.setContentText(m);
                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();


                                        if (isInvite) {
                                            sendToInviteScreen(groupid,groupname);

                                        } else {
                                            sendToMain();
                                        }

                                    }
                                });

                            } else {

                                pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                pDialog.setCancelable(true);
                                pDialog.setTitleText(getString(R.string.error_occured));
                                pDialog.setContentText(task.getException().getMessage());
                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        sendToMain();
                                    }
                                });
                            }
                        }
                    });


                }else {
                    pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    pDialog.setCancelable(true);
                    pDialog.setTitleText(getString(R.string.error_occured));
                    pDialog.setContentText(task.getException().getMessage());
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            sendToMain();
                        }
                    });
                }
            }
        });
    }

    @OnClick(R.id.st_5_invite)
    public void onSt5InviteClicked() {

        saveButtonClick(true);

    }

    private void sendToMain() {
        Intent main = new Intent(getActivity(), MainActivity.class);
        startActivity(main);
        getActivity().finish();
    }

    private void sendToInviteScreen( String grID, String grName) {

        String link = "https://chamatablebanking.page.link/gr/?invitedto=" + grID;

        Intent intent = new AppInviteInvitation
                .IntentBuilder(getString(R.string.invitation_title))
                .setMessage("Join " + grName)
                .setDeepLink(Uri.parse(link))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();

        startActivityForResult(intent, REQUEST_INVITE);
    }

    @OnClick(R.id.st_5_inviteskip_btn)
    public void onSt5InviteskipBtnClicked() {

        saveButtonClick(false);
    }

    private void refreshPrefs(String grID, String grName){

        sharedPreferencesEditor.putString(GROUP_ID_PREFS, grID);
        sharedPreferencesEditor.putString(GROUP_NAME_PREFS, grName);
        sharedPreferencesEditor.apply();
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
                    //TODO: use topalerts lib
                    Toast.makeText(getContext(), ids.length+" sent invitation", Toast.LENGTH_SHORT).show();

            } else {
                    Toast.makeText(getContext(), "no sent invitation", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
