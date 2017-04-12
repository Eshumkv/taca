package thomasmore.be.travelcommunicationassistant;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Arrays;

import thomasmore.be.travelcommunicationassistant.adapter.HomeScreenAdapter;
import thomasmore.be.travelcommunicationassistant.adapter.NavigationAdapter;
import thomasmore.be.travelcommunicationassistant.adapter.misc.HomeScreenValues;
import thomasmore.be.travelcommunicationassistant.misc.Helper;

public class TutorHomeActivity extends AppCompatActivity {
    private static final HomeScreenValues[] values = new HomeScreenValues[] {
            new HomeScreenValues("Messages", R.drawable.ic_message_black_24dp),
            new HomeScreenValues("Rooms", R.drawable.ic_crop_square_black_24dp),
            new HomeScreenValues("Pictograms", R.drawable.ic_stars_black_24dp),
            new HomeScreenValues("Categories", R.drawable.ic_dashboard_black_24dp),
            new HomeScreenValues("Warded Persons", R.drawable.ic_person_black_24dp),
            new HomeScreenValues("Contacts", R.drawable.ic_contacts_black_24dp),
            new HomeScreenValues("", 0), // Needed to have the last item be in the middle.
            new HomeScreenValues("Personal Info", R.drawable.ic_info_black_24dp),
    };

    LinearLayout navList;
    DrawerLayout navLayout;
    ActionBarDrawerToggle navToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_home);
        setupActionbar();

        final GridView grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(new HomeScreenAdapter(this, Arrays.asList(values)));

        // Navigation drawer
        navLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navList = (LinearLayout) findViewById(R.id.navigation_layout);

        ListView listView = (ListView) navList.findViewById(R.id.navigation_list);

        listView.setAdapter(new NavigationAdapter(this, Arrays.asList(Helper.navigationItems)));
        //navList.setOnClickListener();

        navToggle = new ActionBarDrawerToggle(this, navLayout, R.string.activity_main_password, R.string.activity_main_role_warded) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                getActionBar().setTitle("Test");
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
//                getActionBar().setTitle("Open?");
                invalidateOptionsMenu();
            }
        };
        navLayout.setDrawerListener(navToggle);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = navLayout.isDrawerOpen(navList);
        //menu.findItem(R.id.action_home).setVisible(!drawerOpen);

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (navToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle other menu items
        switch(item.getItemId())
        {
            case R.id.action_logout:
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        navToggle.onConfigurationChanged(newConfig);
    }

    private void setupActionbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();

        if (actionbar == null)
            return;

        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
    }

    private View.OnClickListener clickListenerFor(final Class<?> cls) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), cls);
                startActivity(intent);
                finish();
            }
        };
    }
}
