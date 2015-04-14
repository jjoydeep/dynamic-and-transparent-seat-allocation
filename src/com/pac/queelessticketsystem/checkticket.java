package com.pac.queelessticketsystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class checkticket extends Activity{

	ticketAdapter ticketAdapter=null;
	EditText edTicId;
	Button btnCheck;
	ListView lvTic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkticket);
		edTicId=(EditText)findViewById(R.id.edticId);
		btnCheck=(Button)findViewById(R.id.btnCheck);
		lvTic=(ListView)findViewById(R.id.listView1);
		btnCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!edTicId.getText().toString().equals(""))
				{
					if (ticketAdapter == null) {
						ticketAdapter = new ticketAdapter(checkticket.this, 0);
					}
					try 
					{
						HttpClient httpclient = new DefaultHttpClient();
					    HttpPost httppost = new HttpPost("http://192.168.43.99/queeue/checktcket.php"); 
					    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);       
				        nameValuePairs.add(new BasicNameValuePair("ticId",edTicId.getText().toString().trim()));
				        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				        HttpResponse response = httpclient.execute(httppost);
					    InputStream is = response.getEntity().getContent();
					    String res= convertStreamToString(is);
					    Log.d("msg",res);
					    JSONObject jsonResponse = new JSONObject(res);
				        JSONObject object = jsonResponse.getJSONObject("msg");  
				        if(!object.toString().trim().contains("null"))
				        {
				        JSONArray jArray = object.getJSONArray("message"); 
				        
				        	for(int i=0;i<jArray.length();i++)
				        	{
				        		Log.d("Src",jArray.getString(i));
				        		ticketAdapter.add(jArray.getString(i));
				        	}
				        	lvTic.setAdapter(ticketAdapter);
				        }
				        else
				        	Toast.makeText(getApplicationContext(), "Invalid Ticket ID",Toast.LENGTH_LONG).show();
					} 
					catch (ClientProtocolException ei) 
					{
					} catch (IOException e) {
					}
					catch(Exception e)
					{
						Log.e("log_tag", "Error converting result "+e.toString());
					}
					
				}
			}
		});
	}
	public static String convertStreamToString(InputStream is) 
	{
	       BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	       StringBuilder sb = new StringBuilder();
	       String line = null;
	       try 
	       {
	           while ((line = reader.readLine()) != null) 
	           {
	               sb.append(line + "\n");
	           }
	       } 
	       catch (IOException e) 
	       {
	           e.printStackTrace();
	       }
	       finally 
	       {
	           try 
	           {
	               is.close();
	           }
	           catch (IOException e) 
	           {
	               e.printStackTrace();
	           }
	       }
	       return sb.toString();
	}
}
