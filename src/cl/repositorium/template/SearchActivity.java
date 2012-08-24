package cl.repositorium.template;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class SearchActivity extends Activity {
	public static final JSONObject challenge = null;

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
    	EditText searchField = (EditText) findViewById(R.id.searchField);
    	String query = searchField.getText().toString();
    	
    	HttpClient httpclient = new DefaultHttpClient();
    	//temporalmente dejamos la respuesta en null
    	HttpResponse response = null;
    	
    	try {
    		String url = String.format("http://www.repositorium.cl/api/v1.0/repositories/22/search:%s",query.trim());
    		HttpGet httpget = new HttpGet(url);
            //realizamos la llamada GET recordando agregar el contexto para las cookies
            response = httpclient.execute(httpget, MainActivity.mHttpContext);
            
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if(response == null || response.getStatusLine().getStatusCode() != 200){
    		//Si la busqueda fall—
    		Intent intent = new Intent(this, DisplayMessageActivity.class);
    		intent.putExtra(MainActivity.EXTRA_MESSAGE, "Error el intentar buscar. Compruebe la URL");
    		startActivity(intent);
    	}else{
    		//Si la busqueda fue exitosa
    		try {
				//convertimos la respuesta en JSON
    			String jsonString = MainActivity.inputStreamToString(response.getEntity().getContent());
    			
    			if(jsonString.length() < 3){
    				
    				Intent intent = new Intent(this, DisplayMessageActivity.class);
	        		intent.putExtra(MainActivity.EXTRA_MESSAGE, "No se encontraron resultados"+jsonString);
	        		startActivity(intent);
	            //Si no viene pregunta, es porque tenemos un documento como tal
    			}else if(jsonString.startsWith("[")){
    				JSONArray answer = new JSONArray(jsonString);
	            	//mostrar documento
	            	Intent intent = new Intent(this, DisplayMessageActivity.class);
	        		intent.putExtra(MainActivity.EXTRA_MESSAGE, "Titulo: "+((JSONObject) answer.get(0)).getString("title")+
	        				"\nContenido: "+((JSONObject) answer.get(0)).getString("content"));
	        		startActivity(intent);
	            }else{
	            	//recibimos un desafio y debemos mostrar tal vista
	            	Intent intent = new Intent(this, ChallengeActivity.class);
			    	intent.putExtra("jsonString", jsonString);
			    	startActivity(intent);
	            }
		    	
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}
