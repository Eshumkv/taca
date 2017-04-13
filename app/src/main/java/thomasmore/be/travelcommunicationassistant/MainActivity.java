package thomasmore.be.travelcommunicationassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), NavigationDrawerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final EditText loginText = (EditText) findViewById(R.id.login);
        loginText.requestFocus();
        loginText.clearFocus();
    }
}
