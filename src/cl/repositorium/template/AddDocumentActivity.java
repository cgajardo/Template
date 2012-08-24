package cl.repositorium.template;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class AddDocumentActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_document);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_document, menu);
        return true;
    }
    
    public void save(View view){
    	EditText tituloField = (EditText) findViewById(R.id.docTitle);
    	EditText contenidoField = (EditText) findViewById(R.id.docContent);
    	String titulo = tituloField.getText().toString();
    	String contenido = contenidoField.getText().toString();
    	
    	HttpClient httpclient = new DefaultHttpClient();
    	//temporalmente dejamos la respuesta en null
    	HttpResponse response = null;
    	//realizamos una llamada a la URL correspondiente para loggear al usuario
    	HttpPost httppost = new HttpPost("http://www.repositorium.cl/api/v1.0/repositories/22/documents");
    	try {
            //vamos a agregar el titulo y contenido al POST que realizaremos luego
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("title", titulo));
            nameValuePairs.add(new BasicNameValuePair("content", contenido));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            //realizamos la llamada POST
            response = httpclient.execute(httppost, MainActivity.mHttpContext);
            
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
    		Intent intent = new Intent(this, DisplayMessageActivity.class);
    		intent.putExtra(MainActivity.EXTRA_MESSAGE, "Error el intentar autentificar. Comprueba que tu username y password son correctos");
    		startActivity(intent);
    	}else{
    		//Si el login fue exitoso
	    	Intent intent = new Intent(this, DisplayMessageActivity.class);
	    	intent.putExtra(MainActivity.EXTRA_MESSAGE, "Se agrego el documento");
	    	startActivity(intent);
    	}
    }
}
