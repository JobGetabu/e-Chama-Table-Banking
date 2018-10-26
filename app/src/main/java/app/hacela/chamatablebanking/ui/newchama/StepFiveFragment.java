package app.hacela.chamatablebanking.ui.newchama;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.model.Groups;
import app.hacela.chamatablebanking.model.GroupsContributionDefault;
import app.hacela.chamatablebanking.model.GroupsMembers;
import app.hacela.chamatablebanking.viewmodel.NewChamaViewModel;
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

    private NewChamaViewModel model;


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

        model = ViewModelProviders.of(getActivity()).get(NewChamaViewModel.class);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.st_5_invite)
    public void onSt5InviteClicked() {

        //TODO: Create chama. Send to invite screen

        Groups groups = model.getGroupsMediatorLiveData().getValue();
        GroupsContributionDefault grContrDflt = model.getGroupsContributionDefaultMediatorLiveData().getValue();
        GroupsMembers members = model.getGroupsMembersMediatorLiveData().getValue();

        Toast.makeText(getContext(), ""+groups.toString()+grContrDflt.toString()+members.toString(), Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.st_5_inviteskip_btn)
    public void onSt5InviteskipBtnClicked() {

        //TODO: Create chama. Send to home screen
    }
}
