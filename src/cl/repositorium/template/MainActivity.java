package cl.repositorium.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	 public final static String EXTRA_MESSAGE = "cl.repositorium.template.MESSAGE";
	 public static BasicHttpContext mHttpContext = null;
	 /*
	  * Home,
	  * Search,
	  * Form (to add documents)
	  * Results (to show search and form results).
	  */
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if(intent!=null){
        	//recibimos un intent
        	String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        	TextView error_alert = (TextView) findViewById(R.id.login_error);
        	error_alert.setText(message);
        }
        
        //inicializamos el contexto para almacenar las cookies 
    	mHttpContext = new BasicHttpContext();
    	CookieStore mCookieStore = new BasicCookieStore();        
    	mHttpContext.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    /** Called when the user clicks the Send button */
    public void login(View view) {
        // Do something in response to button
    	EditText usernameField = (EditText) findViewById(R.id.username);
    	EditText passwordField = (EditText) findViewById(R.id.password);
    	String username = usernameField.getText().toString();
    	String password = passwordField.getText().toString();
    	
    	HttpClient httpclient = new DefaultHttpClient();
    	//temporalmente dejamos la respuesta en null
    	HttpResponse response = null;
    	//realizamos una llamada a la URL correspondiente para loggear al usuario
    	HttpPost httppost = new HttpPost("http://www.repositorium.cl/api/v1.0/users/login");
    	
    	
    	String algo = null;
    	try {
            //vamos a agregar el username y password al POST que realizaremos luego
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("email", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            //realizamos la llamada POST
            response = httpclient.execute(httppost, mHttpContext);
            algo = inputStreamToString(response.getEntity().getContent());
            
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if(response == null || response.getStatusLine().getStatusCode() != 200){
    		//Si el login no fue exitoso
    		Intent intent = new Intent(this, MainActivity.class);
    		intent.putExtra(EXTRA_MESSAGE, "Error el intentar autentificar. Comprueba que tu username y password son correctos");
    		startActivity(intent);
    	}else{
    		//Si el login fue exitoso
	    	Intent intent = new Intent(this, SelectAction.class);
	    	intent.putExtra(EXTRA_MESSAGE, username+" - "+password+" - "+algo);
	    	startActivity(intent);
    	}
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
