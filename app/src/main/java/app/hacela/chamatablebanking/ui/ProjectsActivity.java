package app.hacela.chamatablebanking.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ramotion.foldingcell.FoldingCell;

import app.hacela.chamatablebanking.R;
import butterknife.BindView;

public class ProjectsActivity extends AppCompatActivity {
    @BindView(R.id.folding_cell)
    FoldingCell fc;
    @BindView(R.id.projects_toolbar)
    Toolbar projects_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        setSupportActionBar(projects_toolbar);
        getSupportActionBar().setTitle("Group Projects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
