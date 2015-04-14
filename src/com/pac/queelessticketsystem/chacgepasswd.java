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
import android.widget.Toast;

public class chacgepasswd extends Activity{

	Button btnUpdate,btnCancel;
	EditText edUserName,edOldpass,ednewPass,edConfirmnewpass;
	String usrname,paswd,newpaswd,confrmnewpass;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changepasswd);
		btnUpdate=(Button)findViewById(R.id.btnUpdate);
		btnCancel=(Button)findViewById(R.id.btnCancel);
		edUserName=(EditText)findViewById(R.id.edUserName);
		edOldpass=(EditText)findViewById(R.id.edOldPass);
		ednewPass=(EditText)findViewById(R.id.edNewPass);
		edConfirmnewpass=(EditText)findViewById(R.id.edConfirmNewPass);
		btnUpdate.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				usrname=edUserName.getText().toString().trim();
				paswd=edOldpass.getText().toString().trim();
				newpaswd=ednewPass.getText().toString().trim();
				confrmnewpass=edConfirmnewpass.getText().toString().trim();
				if(!usrname.equals("") && !paswd.equals("") && !newpaswd.equals("") && !confrmnewpass.equals(""))
				{
					try 
					{
						HttpClient httpclient = new DefaultHttpClient();
					    HttpPost httppost = new HttpPost("http://192.168.43.99/queeue/updatepasswd.php"); 
					    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);       
				        nameValuePairs.add(new BasicNameValuePair("username",usrname));
				        nameValuePairs.add(new BasicNameValuePair("oldpass",paswd));
				        nameValuePairs.add(new BasicNameValuePair("newpass",newpaswd));
				        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				        HttpResponse response = httpclient.execute(httppost);
					    InputStream is = response.getEntity().getContent();
					    String res= convertStreamToString(is);
					    AlertDialog.Builder mainBuilder = new AlertDialog.Builder(chacgepasswd.this);
					    mainBuilder.setMessage(res)
			               .setCancelable(false)
			               .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
			               {
			                   public void onClick(DialogInterface dialog, int id) {
			                	   
			                	Intent intent=new Intent(chacgepasswd.this,Home.class);
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
				else
				{
					Toast.makeText(getApplicationContext(), "Input all values",Toast.LENGTH_LONG).show();
				}
			}
		});
		btnCancel.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
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
