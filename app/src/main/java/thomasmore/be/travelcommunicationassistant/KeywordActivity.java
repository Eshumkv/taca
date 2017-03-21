package thomasmore.be.travelcommunicationassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import thomasmore.be.travelcommunicationassistant.adapter.CreatedRoomAdapter;
import thomasmore.be.travelcommunicationassistant.adapter.KeywordAdapter;

public class KeywordActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword);
        setupActionbar();

        ArrayList<String> words = new ArrayList<>();

        for (int i = 1; i <= 49; i++) {
            words.add("Keyword " + i);
        }

        final ListView list = (ListView) findViewById(R.id.list);
        final KeywordAdapter adapter = new KeywordAdapter(this, words);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), EditKeywordActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupActionbar() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ViewGroup actionbarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.actionbar_treeview,
                null);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setCustomView(actionbarLayout);
    }

    public void goHome(View v) {
        Intent intent = new Intent(this, TutorHomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(this, TutorHomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }
}
