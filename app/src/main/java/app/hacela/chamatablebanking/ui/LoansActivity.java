package app.hacela.chamatablebanking.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ramotion.foldingcell.FoldingCell;

import app.hacela.chamatablebanking.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoansActivity extends AppCompatActivity {
    @BindView(R.id.folding_cell)
    FoldingCell fc;
    @BindView(R.id.loans_toolbar)
    Toolbar loans_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loans);
        ButterKnife.bind(this);

        setSupportActionBar(loans_toolbar);
        getSupportActionBar().setTitle("Active Loans");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @OnClick(R.id.folding_cell)
    public void onFoldClicked(){
        fc.toggle(false);
    }
}
