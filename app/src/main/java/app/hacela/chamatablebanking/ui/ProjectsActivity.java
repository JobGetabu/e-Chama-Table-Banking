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
import butterknife.ButterKnife;

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
        ButterKnife.bind(this);

        setSupportActionBar(projects_toolbar);
        getSupportActionBar().setTitle("Group Projects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        projects_recycler.setHasFixedSize(true);
        projects_recycler.setLayoutManager(new LinearLayoutManager(this));
        dummyTests = new ArrayList<>();
        dummyTests.add(new DummyTest("Chicken Farm", "20,000", "7,000", "Job    + 5,000\nAbedy  + 2,000", R.drawable.hacela, "Projects Worth", "Total Amount so far", "Contributions paid so far"));
        dummyTests.add(new DummyTest("Old Game Mutukanio Farm", "15,000", "7,300", "Lawrence    + 5,000\nAbedy  + 2,300", R.drawable.hacela, "Projects Worth", "Total Amount so far", "Contributions paid so far"));
        dummyTests.add(new DummyTest("Incubators hub", "10,000", "6,000", "Mama Jane    + 4,000\nAbedy  + 2,000", R.drawable.hacela, "Projects Worth", "Total Amount so far", "Contributions paid so far"));
        dummyTests.add(new DummyTest("Children's Fund", "13,650", "7,000", "Job    + 5,000\nAbedy  + 2,000", R.drawable.hacela, "Projects Worth", "Total Amount so far", "Contributions paid so far"));
        dummyTests.add(new DummyTest("Biogas Project", "9,000", "7,200", "Job    + 5,000\nAbedy  + 2,200", R.drawable.hacela, "Projects Worth", "Total Amount so far", "Contributions paid so far"));

        adapter = new DummyTestAdapter(dummyTests, this);
        projects_recycler.setAdapter(adapter);
    }
}
