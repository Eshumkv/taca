package thomasmore.be.travelcommunicationassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.Arrays;

import thomasmore.be.travelcommunicationassistant.adapter.SpinnerAdapter;

public class TutorDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_details);
        setupActionbar();

        String[] groupItems = new String[] {
                "English", "Russian"
        };

        final Spinner spinner = (Spinner) findViewById(R.id.lanugage);
        spinner.setAdapter(new SpinnerAdapter(this, Arrays.asList(groupItems)));

        final LinearLayout save = (LinearLayout) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), TutorHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupActionbar() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ViewGroup actionbarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.actionbar_tutor_details,
                null);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setCustomView(actionbarLayout);
    }

    public void goHome(View v) {
        Intent intent = new Intent(getBaseContext(), TutorHomeActivity.class);
        startActivity(intent);
        finish();}
}
