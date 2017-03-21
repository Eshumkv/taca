package thomasmore.be.travelcommunicationassistant;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import thomasmore.be.travelcommunicationassistant.adapter.FixedTabsPerAdapter;
import thomasmore.be.travelcommunicationassistant.adapter.TreeviewTabsPager;
import thomasmore.be.travelcommunicationassistant.fragments.CategoryFragment;
import thomasmore.be.travelcommunicationassistant.fragments.PictogramFragment;
import thomasmore.be.travelcommunicationassistant.fragments.StartFragment;

public class TreeviewActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, PictogramFragment.OnFragmentInteractionListener, CategoryFragment.OnFragmentInteractionListener, StartFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treeview);
        setupActionbar();

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pager = new TreeviewTabsPager(getSupportFragmentManager());
        viewPager.setAdapter(pager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_treeview, popup.getMenu());
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
            case R.id.action_edit_keywords:
                Intent intent2 = new Intent(this, KeywordActivity.class);
                startActivity(intent2);
                finish();
                return true;
            default:
                return false;
        }
    }
}
