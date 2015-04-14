package com.pac.queelessticketsystem;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class register extends Activity {

	EditText edUserName,edPasswd,edConfrmPasswd,edAtvmNo;
	Button btnReg,btnCancel;
	String usrname,passwd,cmfrmpswd,atvm; 
	String FR,FR1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		edUserName=(EditText)findViewById(R.id.edUserName);
		edPasswd=(EditText)findViewById(R.id.edPasswd);
		edConfrmPasswd=(EditText)findViewById(R.id.edConfirmPasswd);
		edAtvmNo=(EditText)findViewById(R.id.edAtvmNo);
		btnReg=(Button)findViewById(R.id.btnRegister);
		btnCancel=(Button)findViewById(R.id.btnCancel);
		btnReg.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				usrname=edUserName.getText().toString().trim();
				passwd=edPasswd.getText().toString().trim();
				cmfrmpswd=edConfrmPasswd.getText().toString().trim();
				atvm=edAtvmNo.getText().toString().trim();
				if(!usrname.equals("") && !passwd.equals("") && !cmfrmpswd.equals("") && !atvm.equals(""))
				{
					if(passwd.equals(cmfrmpswd))
					{
						Registration regi=new Registration(register.this);
						regi.execute("http://192.168.43.99/queeue/reg.php");
					}
					else
						Toast.makeText(getApplicationContext(), "Password not matching",Toast.LENGTH_LONG).show();
				} 
				else
				{
					Toast.makeText(getApplicationContext(), "Input all values",Toast.LENGTH_LONG).show();
				}
			}
		});
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	public class Registration extends AsyncTask<String, Void,String>
	{		
		Context context;
		ProgressDialog progressDialog;
		public Registration(Context context) {
			this.context = context;
			progressDialog = new ProgressDialog(context);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setMessage("Registering new user, please wait...");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... urls) {
			String output = null;
			for (String url : urls) {
				output = loginOutput(url);
			}
			return output;
		}
		@Override
		protected void onPostExecute(String output) {
			super.onPostExecute(output);
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
				if(FR1.equalsIgnoreCase("true"))
				{
						Intent intent=new Intent(register.this,userLogin.class);
						startActivity(intent);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Username or Atvm No Already selected",Toast.LENGTH_LONG).show();
				}
			}
		}
	}
		@SuppressLint("WorldReadableFiles")
		public String loginOutput(String url)
		{
			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
    		    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
    	        nameValuePairs.add(new BasicNameValuePair("name", usrname));
    	        nameValuePairs.add(new BasicNameValuePair("pass", passwd));
    	        nameValuePairs.add(new BasicNameValuePair("atvm", atvm));
    	        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = httpClient.execute(httpPost,
						responseHandler);
				Log.d("Response",responseBody);    	       
				JSONObject jsonResponse = new JSONObject(responseBody);
    	        JSONObject object = jsonResponse.getJSONObject("msg");    	       
    	        FR = object.getString("message");
    	        JSONObject object1 = jsonResponse.getJSONObject("status");
    	        FR1 = object1.getString("value");    	        
    	        if(FR1.equalsIgnoreCase("true"))
	        	{    	 
    	        	SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
	                SharedPreferences.Editor prefsEditor = myPrefs.edit();
	                prefsEditor.putString("AtvmNo",FR);
	                prefsEditor.commit();
	                Log.d("Atvm ",FR);
	                
	                return "Success";
    	        }
    	         else
    	        {
    	        	return "invalid";
    	       // 	Toast.makeText(getApplicationContext(), "Invalid username or password",Toast.LENGTH_LONG).show();
    	        }
    	    	} 
				catch (Exception e) {
    	    		e.printStackTrace();
    	    	}
			return null;
		}
}
