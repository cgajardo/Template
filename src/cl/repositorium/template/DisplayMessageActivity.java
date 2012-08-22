package cl.repositorium.template;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity {
	// Login, a Home, a Search, a Form (to add documents), and  Results (to show search and form results)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        
        Intent intent = getIntent();
        String message = intent.getStringExtra(AddDocumentActivity.MESSAGE);
        
        // Create the text view
        TextView textView = (TextView) findViewById(R.id.textMessage);
        textView.setText(message);
        
    }
    
    public void back(View view){
    	Intent intent = new Intent(this, SelectAction.class);
		startActivity(intent);
    }

}
