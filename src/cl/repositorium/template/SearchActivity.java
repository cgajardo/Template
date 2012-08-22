package cl.repositorium.template;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class SearchActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search, menu);
        return true;
    }
    
    public void search(View view){
    	//TODO: realizar la busqueda, mostrar desafio en caso de ser necesario
    }
}
