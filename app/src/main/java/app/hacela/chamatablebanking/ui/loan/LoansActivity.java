package app.hacela.chamatablebanking.ui.loan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class LoansActivity extends AppCompatActivity {
    @BindView(R.id.loans_toolbar)
    Toolbar loans_toolbar;
    @BindView(R.id.loans_recycler)
    RecyclerView loans_recycler;

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
        loans_recycler.setAdapter(adapter);

    }

}
