package thomasmore.be.travelcommunicationassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.Arrays;

import thomasmore.be.travelcommunicationassistant.adapter.HomeScreenAdapter;
import thomasmore.be.travelcommunicationassistant.adapter.misc.HomeScreenValues;

public class TutorHomeActivity extends AppCompatActivity {
    private static final HomeScreenValues[] values = new HomeScreenValues[] {
            new HomeScreenValues("Messages", R.drawable.ic_message_black_24dp),
            new HomeScreenValues("Rooms", R.drawable.ic_add_black_24dp),
            new HomeScreenValues("Pictograms", R.drawable.ic_add_black_24dp),
            new HomeScreenValues("Categories", R.drawable.ic_add_black_24dp),
            new HomeScreenValues("Warded Persons", R.drawable.ic_add_black_24dp),
            new HomeScreenValues("Contacts", R.drawable.ic_add_black_24dp),
            new HomeScreenValues("", 0),
            new HomeScreenValues("Personal Info", R.drawable.ic_add_black_24dp),
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_home);
        setupActionbar();

        final GridView grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(new HomeScreenAdapter(this, Arrays.asList(values)));
    }


    private void setupActionbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ViewGroup actionbarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.actionbar_tutor_home,
                null);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setCustomView(actionbarLayout);

        final ImageButton logout = (ImageButton) actionbarLayout.findViewById(R.id.logout);
        final ImageButton menu = (ImageButton) actionbarLayout.findViewById(R.id.menu);

        logout.setOnClickListener(clickListenerFor(MainActivity.class));
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
