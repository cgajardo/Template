package cl.repositorium.template;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class SelectAction extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_action);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_select_action, menu);
        return true;
    }
    
    
    public void go_repositories(View view) {
    	Intent intent = new Intent(this, ShowRepos.class);
		startActivity(intent);
    }
    
    public void go_search(View view){
    	//TODO: enviar a la vista de busqueda
    }
    
    public void go_add(View view){
    	//TODO: enviar a la vista de agregaci—n
    }
    
}
