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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class addbalance extends Activity{

	EditText edAtvm,edAmt;
	Button btnRefill,btnCancel;
	String atvm,refil;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addbal);
		edAmt=(EditText)findViewById(R.id.edAmount);
		edAtvm=(EditText)findViewById(R.id.edAtvmNo);
		btnRefill=(Button)findViewById(R.id.btnRefill);
		btnCancel=(Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnRefill.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				atvm=edAtvm.getText().toString().trim();
				refil=edAmt.getText().toString().trim();
				if(!atvm.equals("") || !refil.equals(""))
				{
					try 
					{
						HttpClient httpclient = new DefaultHttpClient();
					    HttpPost httppost = new HttpPost("http://192.168.43.99/queeue/addbal.php"); 
					    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);       
				        nameValuePairs.add(new BasicNameValuePair("atvm",atvm));
				        nameValuePairs.add(new BasicNameValuePair("amount",refil));
				        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				        HttpResponse response = httpclient.execute(httppost);
					    InputStream is = response.getEntity().getContent();
					    String res= convertStreamToString(is);
					    AlertDialog.Builder mainBuilder = new AlertDialog.Builder(addbalance.this);
					    mainBuilder.setMessage(res)
			               .setCancelable(false)
			               .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
			               {
			                   public void onClick(DialogInterface dialog, int id) {
			                	   
			                	Intent intent=new Intent(addbalance.this,AdminHome.class);
			                	startActivity(intent);
			                   }
			               });
			               
			        AlertDialog mainAlert = mainBuilder.create();
			        mainAlert.show();
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
