package app.hacela.chamatablebanking.ui.newchama;


import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.hacela.chamatablebanking.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepFiveFragment extends Fragment {


    @BindView(R.id.st_5_invite)
    MaterialButton st5Invite;
    @BindView(R.id.st_5_inviteskip_btn)
    TextView st5InviteskipBtn;
    Unbinder unbinder;

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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.st_5_invite)
    public void onSt5InviteClicked() {
    }

    @OnClick(R.id.st_5_inviteskip_btn)
    public void onSt5InviteskipBtnClicked() {
    }
}
