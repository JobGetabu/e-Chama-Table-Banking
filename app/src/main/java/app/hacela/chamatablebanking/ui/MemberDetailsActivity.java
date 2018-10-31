package app.hacela.chamatablebanking.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.adapter.DummyTest;
import app.hacela.chamatablebanking.adapter.DummyTestAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MemberDetailsActivity extends AppCompatActivity {

    @BindView(R.id.members_tool_bar)
    Toolbar members_tool_bar;
    @BindView(R.id.member_recycler)
    RecyclerView member_recycler;

    private RecyclerView.Adapter adapter;
    private List<DummyTest> dummyTests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);
        ButterKnife.bind(this);

        setSupportActionBar(members_tool_bar);
        getSupportActionBar().setTitle("Members");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        member_recycler.setHasFixedSize(true);
        member_recycler.setLayoutManager(new LinearLayoutManager(this));
        dummyTests = new ArrayList<>();
        dummyTests.add(new DummyTest("Job Getabu", "0700243243", "1500", "No records for now", R.drawable.hacela));
        dummyTests.add(new DummyTest("Abedy Ng'ang'a", "0711224354", "2000", "0", R.drawable.avatar_placeholder));
        dummyTests.add(new DummyTest("Lawrence Maluki", "0703432321", "1200", "0", R.drawable.ic_receivecash));
        dummyTests.add(new DummyTest("Mama Njambi", "0725654343", "1000", "0", R.drawable.ic_edit));
        dummyTests.add(new DummyTest("Baba Milly", "0776543453", "1600", "0", R.drawable.avatar_placeholder));

        adapter = new DummyTestAdapter(dummyTests, this);
        member_recycler.setAdapter(adapter);
    }
}
