package app.hacela.chamatablebanking.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import app.hacela.chamatablebanking.datasource.Groups;
import app.hacela.chamatablebanking.datasource.GroupsContributionDefault;

/**
 * Created by Job on Wednesday : 9/5/2018.
 */
public class CreateChamaViewModel extends AndroidViewModel {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private MediatorLiveData<Groups> groupsMediatorLiveData = new MediatorLiveData<>();
    private MediatorLiveData<GroupsContributionDefault> groupsContributionDefaultMediatorLiveData = new MediatorLiveData<>();


    public static final String TAG = "AccountVM";

    public CreateChamaViewModel(@NonNull Application application) {
        super(application);

    }

    public MediatorLiveData<Groups> getGroupsMediatorLiveData() {
        return groupsMediatorLiveData;
    }

    public void setGroupsMediatorLiveData(Groups groupsLiveData) {
        this.groupsMediatorLiveData.setValue(groupsLiveData);
    }

    public MediatorLiveData<GroupsContributionDefault> getGroupsContributionDefaultMediatorLiveData() {
        return groupsContributionDefaultMediatorLiveData;
    }

    public void setGroupsContributionDefaultMediatorLiveData(GroupsContributionDefault groupsContributionDefaultLiveData) {
        this.groupsContributionDefaultMediatorLiveData.setValue(groupsContributionDefaultLiveData);
    }

    /**
     * Factory for instantiating the viewmodel

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
            return (T) new CreateChamaViewModel(mApplication, mAuth, mFirestore);
        }
    }
     */
}
