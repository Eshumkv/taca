package thomasmore.be.travelcommunicationassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

import thomasmore.be.travelcommunicationassistant.adapter.SpinnerAdapter;

public class ContactDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        setupActionbar();

        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("contact");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("contact");
        }

        final TextView textView = (TextView) findViewById(R.id.contact_name);
        textView.setText(newString);
    }

    private void setupActionbar() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ViewGroup actionbarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.actionbar_contact_details,
                null);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setCustomView(actionbarLayout);
    }

    public void goBack(View v) {
        finish();
    }
}
