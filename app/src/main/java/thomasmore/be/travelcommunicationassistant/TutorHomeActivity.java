package thomasmore.be.travelcommunicationassistant;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TutorHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_home);
        setupActionbar();

        final Button contactsButton = (Button) findViewById(R.id.button_contacts);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ContactActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final Button roomsButton = (Button) findViewById(R.id.button_rooms);
        roomsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RoomActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final Button editTutorButton = (Button) findViewById(R.id.button_tutor_settings);
        editTutorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), TutorDetailsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final Button treeviewButton = (Button) findViewById(R.id.button_treeview);
        treeviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), TreeviewActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final Button keywordButton = (Button) findViewById(R.id.button_edit_keywords);
        keywordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), KeywordActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void setupActionbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ViewGroup actionbarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.actionbar_treeview,
                null);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setCustomView(actionbarLayout);
    }
}
