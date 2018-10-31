package app.hacela.chamatablebanking.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

import app.hacela.chamatablebanking.R;
import app.hacela.chamatablebanking.adapter.DummyTest;
import app.hacela.chamatablebanking.adapter.DummyTestAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoansActivity extends AppCompatActivity {
    @BindView(R.id.loans_toolbar)
    Toolbar loans_toolbar;
    @BindView(R.id.loans_recycler)
    RecyclerView loans_recycler;
   /* @BindView(R.id.amount_owing)
    ListView amount_owing;*/

    private RecyclerView.Adapter adapter;
    private List<DummyTest> dummyTests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loans);
        ButterKnife.bind(this);

        setSupportActionBar(loans_toolbar);
        getSupportActionBar().setTitle("Active Loans");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loans_recycler.setHasFixedSize(true);
        loans_recycler.setLayoutManager(new LinearLayoutManager(this));
        dummyTests = new ArrayList<>();
        dummyTests.add(new DummyTest("Job Getabu", "0700243243", "1500", "0", R.drawable.hacela));
        dummyTests.add(new DummyTest("Abedy Ng'ang'a", "0711224354", "2000", "0", R.drawable.avatar_placeholder));
        dummyTests.add(new DummyTest("Lawrence Maluki", "0703432321", "1200", "0", R.drawable.ic_receivecash));
        dummyTests.add(new DummyTest("Mama Njambi", "0725654343", "1000", "0", R.drawable.ic_edit));
        dummyTests.add(new DummyTest("Baba Milly", "0776543453", "1600", "0", R.drawable.avatar_placeholder));

        adapter = new DummyTestAdapter(dummyTests, this);
        loans_recycler.setAdapter(adapter);

        /*String[] myContribs = {"1/2/2018 - +500", "1/3/2018 - +700", "1/8/2018 - +500", "1/17/2018 - +500"};
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, R.layout.list_items, myContribs);
        amount_owing.setAdapter(listAdapter);*/

    }

}
