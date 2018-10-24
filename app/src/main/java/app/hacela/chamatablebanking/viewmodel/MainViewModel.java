package app.hacela.chamatablebanking.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import app.hacela.chamatablebanking.appexecutor.DefaultExecutorSupplier;
import app.hacela.chamatablebanking.datasource.Groups;
import app.hacela.chamatablebanking.datasource.GroupsAccount;
import app.hacela.chamatablebanking.datasource.GroupsMembers;
import app.hacela.chamatablebanking.datasource.Users;
import app.hacela.chamatablebanking.repository.FirebaseDocumentLiveData;

import static app.hacela.chamatablebanking.util.Constants.GROUPSACCOUNTCOL;
import static app.hacela.chamatablebanking.util.Constants.GROUPSCOL;
import static app.hacela.chamatablebanking.util.Constants.GROUPSMEMBERSCOL;
import static app.hacela.chamatablebanking.util.Constants.USERCOL;

/**
 * Created by Job on Tuesday : 9/4/2018.
 */
public class MainViewModel extends AndroidViewModel {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private String currentUserId;

    public static final String TAG = "AccountVM";

    //few db references
    private DocumentReference userRef;
    private DocumentReference userMemberRef;
    private DocumentReference groupRef;
    private DocumentReference groupAccountRef;

    //live datas
    private FirebaseDocumentLiveData mUserLiveData;
    private FirebaseDocumentLiveData mUserMemberLiveData;
    private FirebaseDocumentLiveData mGroupAccountLiveData;
    private FirebaseDocumentLiveData mGroupsLiveData;

    //mediators
    private MediatorLiveData<String> globalGroupIdMediatorLiveData = new MediatorLiveData<>();
    private MediatorLiveData<Users> usersMediatorLiveData = new MediatorLiveData<>();
    private MediatorLiveData<GroupsMembers> groupsMembersMediatorLiveData = new MediatorLiveData<>();
    private MediatorLiveData<GroupsAccount> groupsAccountMediatorLiveData = new MediatorLiveData<>();
    private MediatorLiveData<Groups> groupsMediatorLiveData = new MediatorLiveData<>();


    public MainViewModel(@NonNull Application application,FirebaseAuth mAuth,FirebaseFirestore mFirestore) {
        super(application);

        this.mAuth = mAuth;
        this.mFirestore = mFirestore;
        this.currentUserId = mAuth.getCurrentUser().getUid();

        //init db refs 
        userRef = mFirestore.collection(USERCOL).document(currentUserId);
        userMemberRef = mFirestore.collection(GROUPSMEMBERSCOL).document(currentUserId);


        //init livedatas
        mUserLiveData = new FirebaseDocumentLiveData(userRef);
        mUserMemberLiveData = new FirebaseDocumentLiveData(userMemberRef);

        // Set up the MediatorLiveData to convert DataSnapshot objects into POJO objects
        workOnUsersLiveData();
        workOnUsersMembers();
        //workOnGroupAccount();
    }

    public String formatMyMoney(Double money){
        return String.format("Ksh %,.2f", money);
    }

    private void workOnUsersLiveData() {

        usersMediatorLiveData.addSource(mUserLiveData, new Observer<DocumentSnapshot>() {
            @Override
            public void onChanged(@Nullable final DocumentSnapshot documentSnapshot) {

                if (documentSnapshot != null){

                    DefaultExecutorSupplier.getInstance().forBackgroundTasks()
                            .submit(new Runnable() {
                                @Override
                                public void run() {

                                    usersMediatorLiveData.postValue(documentSnapshot.toObject(Users.class));

                                }
                            });

                }else {
                    usersMediatorLiveData.postValue(null);
                }
            }
        });

    }

    private void workOnUsersMembers(){

        groupsMembersMediatorLiveData.addSource(mUserMemberLiveData, new Observer<DocumentSnapshot>() {
            @Override
            public void onChanged(@Nullable final DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null){

                    DefaultExecutorSupplier.getInstance().forBackgroundTasks()
                            .submit(new Runnable() {
                                @Override
                                public void run() {
                                    groupsMembersMediatorLiveData.postValue(documentSnapshot.toObject(GroupsMembers.class));
                                }
                            });
                }else {
                    groupsMembersMediatorLiveData.postValue(null);
                }
            }
        });
    }

    public void workOnGroupsAccount(String gID){
        //init db refs
        groupAccountRef = mFirestore.collection(GROUPSACCOUNTCOL).document(gID);
        //init livedatas
        mGroupAccountLiveData = new FirebaseDocumentLiveData(groupAccountRef);

        groupsAccountMediatorLiveData.addSource(mGroupAccountLiveData, new Observer<DocumentSnapshot>() {
            @Override
            public void onChanged(@Nullable final DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null){
                    DefaultExecutorSupplier.getInstance().forBackgroundTasks()
                            .submit(new Runnable() {
                                @Override
                                public void run() {
                                    groupsAccountMediatorLiveData.postValue(documentSnapshot.toObject(GroupsAccount.class));
                                }
                            });
                }else {
                    groupsAccountMediatorLiveData.postValue(null);
                }
            }
        });
    }

    public void workOnGroups(String gID){
        //init db refs
        groupRef = mFirestore.collection(GROUPSCOL).document(gID);
        //init livedatas
        mGroupsLiveData = new FirebaseDocumentLiveData(groupRef);

        groupsMediatorLiveData.addSource(mGroupsLiveData, new Observer<DocumentSnapshot>() {
            @Override
            public void onChanged(@Nullable final DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null){
                    DefaultExecutorSupplier.getInstance().forBackgroundTasks()
                            .submit(new Runnable() {
                                @Override
                                public void run() {
                                    groupsMediatorLiveData.postValue(documentSnapshot.toObject(Groups.class));
                                }
                            });
                }else {
                    groupsMediatorLiveData.postValue(null);
                }
            }
        });
    }




    public MediatorLiveData<Users> getUsersMediatorLiveData() {
        return usersMediatorLiveData;
    }

    public MediatorLiveData<GroupsMembers> getGroupsMembersMediatorLiveData() {
        return groupsMembersMediatorLiveData;
    }

    public MediatorLiveData<String> getGlobalGroupIdMediatorLiveData() {
        return globalGroupIdMediatorLiveData;
    }

    public void setGlobalGroupIdMediatorLiveData(String globalGroupIdLiveData) {
        this.globalGroupIdMediatorLiveData.setValue(globalGroupIdLiveData);
    }

    public MediatorLiveData<GroupsAccount> getGroupsAccountMediatorLiveData() {
        return groupsAccountMediatorLiveData;
    }

    public MediatorLiveData<Groups> getGroupsMediatorLiveData() {
        return groupsMediatorLiveData;
    }

    /**
     * Factory for instantiating the viewmodel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private FirebaseAuth mAuth;
        private FirebaseFirestore mFirestore;

        public Factory(@NonNull Application mApplication, FirebaseAuth mAuth, FirebaseFirestore mFirestore) {
            this.mApplication = mApplication;
            this.mAuth = mAuth;
            this.mFirestore = mFirestore;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MainViewModel(mApplication, mAuth, mFirestore);
        }
    }

}
