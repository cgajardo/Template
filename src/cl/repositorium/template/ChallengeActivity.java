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
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class ChallengeActivity extends Activity {
	private String docId;
	private String challengeId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        
        Bundle b = getIntent().getExtras();
        String json = b.getString("jsonString");
        try {
			JSONObject data = new JSONObject(json);
			RadioButton A = (RadioButton)findViewById(R.id.radio0);
			RadioButton B = (RadioButton)findViewById(R.id.radio1);
			TextView question = (TextView)findViewById(R.id.question);
			TextView chiste = (TextView)findViewById(R.id.chiste);
			
			JSONArray chistes = data.getJSONArray("documents");
			A.setText(data.getString("answera"));
			B.setText(data.getString("answerb"));
			question.setText(data.getString("question"));
			/* sabemos que por regla de este repositorio un desaf’o constar‡ solo de 1 documento
			 * y por lo tanto podemos con tranquilidad solicitar el primer elemento del arreglo
			 */
			chiste.setText("Titulo: "+chistes.getJSONObject(0).getString("title")+
						"\nTexto: "+chistes.getJSONObject(0).getString("content"));
			docId = chistes.getJSONObject(0).getString("id");
			challengeId = data.getString("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //parseo el json y lo pongo en la vista
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_challenge, menu);
        return true;
    }
    
    public void submit(View view){
    	//enviar la respuesta
    	//si recibimos challenge, nuevo intent aqu’ mismo. Si no, mostramos respuesta.
    	RadioButton A = (RadioButton)findViewById(R.id.radio0);
		
    	String response;
		if(A.isChecked()) response = "a";
		else response = "b";
    	
    	//TODO: submit
    	
    	HttpClient httpclient = new DefaultHttpClient();
    	//temporalmente dejamos la respuesta en null
    	HttpResponse userResponse = null;
    	//realizamos una llamada a la URL correspondiente para loggear al usuario
    	HttpPost httppost = new HttpPost("http://www.repositorium.cl/api/v1.0/repositories/22/challenge");
    	String respuesta = null;
    	try {
            //vamos a agregar el titulo y contenido al POST que realizaremos luego
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("id", challengeId));
            nameValuePairs.add(new BasicNameValuePair(docId+"", response));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            //realizamos la llamada POST
            userResponse = httpclient.execute(httppost, MainActivity.mHttpContext);
            respuesta = inputStreamToString(userResponse.getEntity().getContent());
          //recibimos un desafio y debemos mostrar tal vista
            
            JSONArray answer = new JSONArray(respuesta);
        	//mostrar documento
        	Intent intent = new Intent(this, DisplayMessageActivity.class);
    		intent.putExtra(MainActivity.EXTRA_MESSAGE, "Titulo: "+((JSONObject) answer.get(0)).getString("title")+
    				"\nContenido: "+((JSONObject) answer.get(0)).getString("content"));
    		startActivity(intent);
            
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
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
