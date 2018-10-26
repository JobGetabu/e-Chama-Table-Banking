package app.hacela.chamatablebanking.viewmodel;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import app.hacela.chamatablebanking.model.Groups;
import app.hacela.chamatablebanking.model.GroupsContributionDefault;

/**
 * Created by Job on Friday : 10/26/2018.
 */
public class NewChamaViewModel extends ViewModel {

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private MediatorLiveData<Integer> currentStep;
    private MediatorLiveData<Groups> groupsMediatorLiveData ;
    private MediatorLiveData<GroupsContributionDefault> groupsContributionDefaultMediatorLiveData ;

    public NewChamaViewModel() {

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        currentStep = new MediatorLiveData<>();
    }


    public MediatorLiveData<Integer> getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Integer currentStep) {
        this.currentStep.setValue(currentStep);
    }
}
