package thomasmore.be.travelcommunicationassistant;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import thomasmore.be.travelcommunicationassistant.adapter.FixedTabsPerAdapter;

public class RoomActivity extends AppCompatActivity implements CreatedRoomFragment.OnCreatedRoomFragmentListener, AvailableRoomFragment.OnAvailableRoomListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        setupActionbar(0);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pager = new FixedTabsPerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Created")) {
                    setupActionbar(0);
                } else {
                    setupActionbar(1);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupActionbar(int tab) {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ViewGroup actionbarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.actionbar_room,
                null);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setCustomView(actionbarLayout);

        if (tab == 0) { // "Created"
            final ImageButton addButton = (ImageButton)findViewById(R.id.actionbar_add);
            final ImageButton searchButton = (ImageButton)findViewById(R.id.actionbar_search);
            addButton.setVisibility(View.VISIBLE);
            searchButton.setVisibility(View.GONE);
        } else {
            final ImageButton addButton = (ImageButton)findViewById(R.id.actionbar_add);
            final ImageButton searchButton = (ImageButton)findViewById(R.id.actionbar_search);
            addButton.setVisibility(View.GONE);
            searchButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreatedRoomFragmentInteraction(String name) {
        Intent intent = new Intent(getBaseContext(), EditRoomActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAvailableRoomInteraction(String name) {
        Toast.makeText(this, "Available: " + name, Toast.LENGTH_SHORT).show();
    }

    public void goHome(View v) {
        Intent intent = new Intent(this, NavigationDrawerActivity.class);
        startActivity(intent);
        finish();
    }

    public void goSearch(View v) {
        Intent intent = new Intent(this, SearchRoomActivity.class);
        startActivity(intent);
        finish();
    }

    public void goAdd(View v) {
        Intent intent = new Intent(this, EditRoomActivity.class);
        startActivity(intent);
        finish();
    }
}
