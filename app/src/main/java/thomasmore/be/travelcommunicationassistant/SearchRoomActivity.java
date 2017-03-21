package thomasmore.be.travelcommunicationassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Arrays;

import thomasmore.be.travelcommunicationassistant.adapter.FoundRoomAdapter;

public class SearchRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_room);
        setupActionbar();

        final Button searchButton = (Button) findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        String[] rooms = new String[] {
                "Ivan's rooms", "Test Room"
        };

        final ListView list = (ListView)findViewById(R.id.found_rooms);
        final FoundRoomAdapter adapter = new FoundRoomAdapter(
                this,
                Arrays.asList(rooms));
        list.setAdapter(adapter);
    }

    private void setupActionbar() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ViewGroup actionbarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.actionbar_room_search,
                null);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setCustomView(actionbarLayout);

        final LinearLayout add = (LinearLayout) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RoomActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
