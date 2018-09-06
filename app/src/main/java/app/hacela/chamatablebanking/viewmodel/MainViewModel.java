package app.hacela.chamatablebanking.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by Job on Tuesday : 9/4/2018.
 */
public class MainViewModel extends AndroidViewModel {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    public MainViewModel(@NonNull Application application,FirebaseAuth mAuth,FirebaseFirestore mFirestore) {
        super(application);

        this.mAuth = mAuth;
        this.mFirestore = mFirestore;
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
