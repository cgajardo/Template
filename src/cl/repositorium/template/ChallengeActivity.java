package cl.repositorium.template;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ChallengeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_challenge, menu);
        return true;
    }
}
