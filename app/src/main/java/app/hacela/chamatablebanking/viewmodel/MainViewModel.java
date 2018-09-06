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

import app.hacela.chamatablebanking.datasource.Users;
import app.hacela.chamatablebanking.repository.FirebaseDocumentLiveData;

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

    //mediators
    private MediatorLiveData<Users> usersMediatorLiveData = new MediatorLiveData<>();

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
    }

    private void workOnUsersLiveData() {

        usersMediatorLiveData.addSource(mUserLiveData, new Observer<DocumentSnapshot>() {
            @Override
            public void onChanged(@Nullable DocumentSnapshot documentSnapshot) {

                if (documentSnapshot != null){



                }else {
                    usersMediatorLiveData.postValue(null);
                }
            }
        });

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
