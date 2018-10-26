package app.hacela.chamatablebanking.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.ramotion.foldingcell.FoldingCell;

import app.hacela.chamatablebanking.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupContributionsActivity extends AppCompatActivity {
    @BindView(R.id.folding_cell)
    FoldingCell fc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_contributions);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.folding_cell)
    public void onFoldClicked(){
        fc.toggle(false);
    }
}
