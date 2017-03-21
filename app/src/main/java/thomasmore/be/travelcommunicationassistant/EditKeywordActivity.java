package thomasmore.be.travelcommunicationassistant;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

public class EditKeywordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_keyword);
        setupActionbar();
    }

    private void setupActionbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ViewGroup actionbarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.actionbar_pictogram_edit,
                null);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setCustomView(actionbarLayout);
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, KeywordActivity.class);
        startActivity(intent);
        finish();
    }
}
