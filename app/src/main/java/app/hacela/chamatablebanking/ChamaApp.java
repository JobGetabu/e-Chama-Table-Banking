package app.hacela.chamatablebanking;

import android.support.multidex.MultiDexApplication;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

/**
 * Created by Job on Monday : 9/3/2018.
 */
public class ChamaApp extends MultiDexApplication {

    FirebaseFirestore mFirestore;

    @Override
    public void onCreate() {
        super.onCreate();
        //firebase init

        mFirestore = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);

    }
}
