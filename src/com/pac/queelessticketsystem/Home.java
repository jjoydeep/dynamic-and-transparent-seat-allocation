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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class Home extends Activity {

	Button btnBooktct, btnChgePasswd, btnCheckBal, btnLogout, btnCancelTicket;
	

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainview);
		btnBooktct = (Button) findViewById(R.id.btnBookTicket);
		btnCheckBal = (Button) findViewById(R.id.btnCheckBal);
		btnChgePasswd = (Button) findViewById(R.id.btnChangePasswd);
		btnLogout = (Button) findViewById(R.id.btnLogout);
		btnCancelTicket = (Button) findViewById(R.id.btnCancelTicket);
		btnCancelTicket.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Home.this, viewticket.class);
				startActivity(intent);
			}
		});
		
		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Home.this, userLogin.class);
				startActivity(intent);

			}
		});
		btnBooktct.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Home.this, Bookticket.class);
				startActivity(intent);
				 
			}
		});
		
		btnCheckBal.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("WorldReadableFiles")
			@Override
			public void onClick(View v) {
				try {
					SharedPreferences myPrefs = Home.this.getSharedPreferences(
							"myPrefs", MODE_WORLD_READABLE);
					String atvm = myPrefs.getString("AtvmNo", null);
					Log.d("atvm", atvm);
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							"http://192.168.43.99/queeue/fetchbal.php");
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							5);
					nameValuePairs.add(new BasicNameValuePair("atvm", atvm
							.trim()));
					// Log.d("dist",dist_id);
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					InputStream is = response.getEntity().getContent();
					String res = convertStreamToString(is);
					AlertDialog.Builder mainBuilder = new AlertDialog.Builder(
							Home.this);
					mainBuilder
							.setMessage("Your Balance is : " + res)
							.setCancelable(false)
							.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {

										}
									});

					AlertDialog mainAlert = mainBuilder.create();
					mainAlert.show();
				} catch (ClientProtocolException ei) {
				} catch (IOException e) {
				} catch (Exception e) {
					Log.e("log_tag", "Error converting result " + e.toString());
				}
			}
		});
		btnChgePasswd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Home.this, chacgepasswd.class);
				startActivity(intent);

			}
		});
	}

	

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
