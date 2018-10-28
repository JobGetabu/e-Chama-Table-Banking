package app.hacela.chamatablebanking.ui.newchama;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.model.Groups;
import app.hacela.chamatablebanking.model.GroupsContributionDefault;
import app.hacela.chamatablebanking.viewmodel.NewChamaViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepThreeFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 101;
    private static final String TAG = "st3";

    @BindView(R.id.st_3_pic)
    ImageView st3Pic;
    @BindView(R.id.st_3_pic_btn)
    MaterialButton st3PicBtn;
    @BindView(R.id.st_3_minfee)
    TextInputLayout st3Minfee;
    @BindView(R.id.st_3_back)
    TextView st3Back;
    @BindView(R.id.st_3_next)
    TextView st3Next;

    Unbinder unbinder;

    private NewChamaViewModel model;
    private String photourl;
    private Uri mResultPhotoFile;

    public StepThreeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_three, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        model = ViewModelProviders.of(getActivity()).get(NewChamaViewModel.class);
        photourl = "";

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.st_3_pic_btn)
    public void onSt3PicBtnClicked() {

        //start image intent
        Intent imageIntent = new Intent();
        imageIntent.setType("image/*");
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(imageIntent, "Select Chama group photo"), PICK_IMAGE_REQUEST);
    }

    @OnClick(R.id.st_3_back)
    public void onSt3BackClicked() {
        model.setCurrentStep(2);
    }

    @OnClick(R.id.st_3_next)
    public void onSt3NextClicked() {

        if (validate()) {

            model.setCurrentStep(4);

            Groups xx = model.getGroupsMediatorLiveData().getValue();
            GroupsContributionDefault dd =  model.getGroupsContributionDefaultMediatorLiveData().getValue();

            if (mResultPhotoFile != null){
                xx.setPhotourl("true");

                //pass up image
                st3Pic.setDrawingCacheEnabled(true);
                st3Pic.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) st3Pic.getDrawable()).getBitmap();
                model.setBitmapMediatorLiveData(bitmap);
            }

            String minfee = st3Minfee.getEditText().getText().toString();
            dd.setEntryfee(Double.parseDouble(minfee));

        }
    }

    private boolean validate() {
        boolean valid = true;

        String minfee = st3Minfee.getEditText().getText().toString();

        if (minfee.isEmpty()) {
            st3Minfee.setError("enter min fee");
            valid = false;
        } else {
            st3Minfee.setError(null);
        }

        return valid;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST) {

            if (data != null) {

                Uri resultUri = data.getData();

                st3Pic.setVisibility(View.VISIBLE);
                st3Pic.setImageURI(resultUri);

                st3PicBtn.setText(R.string.change_photo);

                mResultPhotoFile = resultUri;
            }else {
                mResultPhotoFile = null;
                st3Pic.setVisibility(View.GONE);
                st3PicBtn.setText(R.string.select_picture);
            }
        }
    }
}
