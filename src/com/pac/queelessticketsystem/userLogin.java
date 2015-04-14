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
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class userLogin extends Activity {
	EditText edUserName,edPasswd;
	Button btnLogin,btnReg,btnExit;
	CheckBox cbtype;
	String userId,Passwd,type="user";
	String FR="";
	String FR1="";
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnLogin=(Button)findViewById(R.id.btnLogin);
		btnReg=(Button)findViewById(R.id.btnRegister);
		btnExit=(Button)findViewById(R.id.btnCancel);
		edUserName=(EditText)findViewById(R.id.edUserName);
		edPasswd=(EditText)findViewById(R.id.edPasswd);
		cbtype=(CheckBox)findViewById(R.id.checkBox1);
		
		int SDK_INT = android.os.Build.VERSION.SDK_INT;

		if (SDK_INT>8){

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 

		}

		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userId=edUserName.getText().toString().trim();
				Passwd=edPasswd.getText().toString().trim();
				if(!userId.equals("") && !Passwd.equals(""))
				{
					CheckLogin checkLogin=new CheckLogin(userLogin.this);
					checkLogin.execute("http://192.168.43.99/queeue/login.php");
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Input Values",Toast.LENGTH_LONG).show();
				}
			}
		});
		btnReg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub 
				Intent intent=new Intent(userLogin.this, register.class);
				startActivity(intent);
			}
		});
		btnExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		cbtype.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked==true)
				{
					type="admin";
				}
				else
					type="user";
			}
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	public class CheckLogin extends AsyncTask<String, Void,String>
	{		
		Context context;
		ProgressDialog progressDialog;
		public CheckLogin(Context context) {
			this.context = context;
			progressDialog = new ProgressDialog(context);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setMessage("Validating Login, please wait...");
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
					if(type.equals("admin"))
					{
						Intent intent=new Intent(userLogin.this, AdminHome.class);
						intent.putExtra("userID",edUserName.getText().toString().trim());
						startActivity(intent);
					}
					else
					{
						Intent intent=new Intent(userLogin.this, Home.class);
						startActivity(intent);
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Invalid Login",Toast.LENGTH_LONG).show();
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
    	        nameValuePairs.add(new BasicNameValuePair("name", userId));
    	        nameValuePairs.add(new BasicNameValuePair("pass", Passwd)); 
    	        nameValuePairs.add(new BasicNameValuePair("type", type)); 
    	        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = httpClient.execute(httpPost,
						responseHandler);
				Log.d("Response",responseBody);    	       
				JSONObject jsonResponse = new JSONObject(responseBody);
    	        JSONObject object = jsonResponse.getJSONObject("msg");    	       
    	        FR = object.getString("message");
    	        JSONObject object2 = jsonResponse.getJSONObject("msg1");    	       
    	        String bal = object2.getString("message1");
    	        JSONObject object1 = jsonResponse.getJSONObject("status");
    	        FR1 = object1.getString("value");    	        
    	        if(FR1.equalsIgnoreCase("true"))
	        	{    	 
    	        	SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
	                SharedPreferences.Editor prefsEditor = myPrefs.edit();
	                prefsEditor.putString("AtvmNo",FR);
	                prefsEditor.putString("Bal",bal);
	                prefsEditor.commit();
	                Log.d("Atvm ",FR);
	                Log.d("Bal ",bal);
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
