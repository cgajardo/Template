package cl.repositorium.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.TextView;

public class ShowRepos extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_repos);
        
        HttpClient httpclient = new DefaultHttpClient();
    	//temporalmente dejamos la respuesta en null
    	HttpResponse response = null;
    	JSONArray repositories = null;
    	//realizamos una llamada a la URL correspondiente para loggear al usuario
    	HttpGet httpget = new HttpGet("http://www.repositorium.cl/api/v1.0/repositories");
    	try {
            //realizamos la llamada POST
            response = httpclient.execute(httpget);
            //convertimos la respuesta en JSON
            repositories = new JSONArray(inputStreamToString(response.getEntity().getContent()));
            
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//Comenzamos a iterar por los respositorios y los agregamos a la lista de para mostrar
    	String lista = "";
    	for(int i=0;i<repositories.length(); i++){
    		try {
				JSONObject repository = repositories.getJSONObject(i);
				lista += repository.getString("name")+": "+repository.getString("description")+"\n--------\n";
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	TextView textview =  (TextView) findViewById(R.id.list_of_repos);
    	textview.setMovementMethod(new ScrollingMovementMethod());
    	textview.setText(lista);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_show_repos, menu);
        return true;
    }
    
  //funci—n auxiliar para parsear la respuesta que nos entrega la API
    private String inputStreamToString(InputStream is) throws IOException {
        String s = "";
        String line = "";
        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        // Read response until the end
        while ((line = rd.readLine()) != null) { s += line; }
        // Return full string
        return s;
    }
}
