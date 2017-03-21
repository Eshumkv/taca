package thomasmore.be.travelcommunicationassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EditRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);
        setupActionbar();

        if (getIntent().hasExtra("name")) {
            final TextView name = (TextView) findViewById(R.id.name);
            final TextView password = (TextView) findViewById(R.id.password);

            name.setText(getIntent().getStringExtra("name"));
            password.setText("********");
        }
    }

    private void setupActionbar() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ViewGroup actionbarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.actionbar_room_edit,
                null);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setCustomView(actionbarLayout);

        final LinearLayout save = (LinearLayout) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RoomActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, RoomActivity.class);
        startActivity(intent);
        finish();
    }
}
