package app.hacela.chamatablebanking.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.adapter.DummyTest;
import app.hacela.chamatablebanking.adapter.DummyTestAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupContributionsActivity extends AppCompatActivity {
    @BindView(R.id.folding_cell)
    FoldingCell fc;
    @BindView(R.id.members_tool_bar)
    Toolbar members_tool_bar;
    @BindView(R.id.member_recycler)
    RecyclerView member_recycler;

    private RecyclerView.Adapter adapter;
    private List<DummyTest> dummyTests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_contributions);
        ButterKnife.bind(this);

        setSupportActionBar(members_tool_bar);
        getSupportActionBar().setTitle("Members");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        member_recycler.setHasFixedSize(true);
        member_recycler.setLayoutManager(new LinearLayoutManager(this));
        dummyTests = new ArrayList<>();

        for (int i = 0; i <= 10; i++){
            DummyTest dummyTest = new DummyTest(
                    "Member" + i + 1,
                    "070011223344",
                    "1500",
                    "500"
            );

            dummyTests.add(dummyTest);
        }

        adapter = new DummyTestAdapter(dummyTests, this);
        member_recycler.setAdapter(adapter);

    }


    @OnClick(R.id.folding_cell)
    public void onFoldClicked(){
        fc.toggle(false);
    }
}
