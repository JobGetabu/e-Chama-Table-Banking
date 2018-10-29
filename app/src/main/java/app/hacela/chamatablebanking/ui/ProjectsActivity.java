package app.hacela.chamatablebanking.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.adapter.DummyTest;
import app.hacela.chamatablebanking.adapter.DummyTestAdapter;
import butterknife.BindView;

public class ProjectsActivity extends AppCompatActivity {

    @BindView(R.id.projects_toolbar)
    Toolbar projects_toolbar;
    @BindView(R.id.projects_recycler)
    RecyclerView projects_recycler;

    private RecyclerView.Adapter adapter;
    private List<DummyTest> dummyTests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        setSupportActionBar(projects_toolbar);
        getSupportActionBar().setTitle("Group Projects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        projects_recycler.setHasFixedSize(true);
        projects_recycler.setLayoutManager(new LinearLayoutManager(this));
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
        projects_recycler.setAdapter(adapter);
    }
}
