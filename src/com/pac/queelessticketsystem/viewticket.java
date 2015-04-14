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
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("unused")
public class viewticket extends Activity implements OnItemClickListener{

	ticketAdapter ticketAdapter=null;
	ListView lvTickets;
	String tid,fair,noOfSeat,AtvmNo;
	ArrayList<String> id=new ArrayList<String>();
	@SuppressLint("WorldReadableFiles")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewticket);
		lvTickets=(ListView)findViewById(R.id.lvTickets);
		lvTickets.setOnItemClickListener(this);
		if (ticketAdapter == null) {
			ticketAdapter = new ticketAdapter(viewticket.this, 0);
		}
		SharedPreferences myPrefs = viewticket.this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
		AtvmNo = myPrefs.getString("AtvmNo", null);
		
		ViewTicket regi=new ViewTicket(viewticket.this);
		regi.execute("http://192.168.43.99/queeue/viewtcket.php");
		
		
	}
	public class ViewTicket extends AsyncTask<String, Void,String>
	{		
		Context context;
		ProgressDialog progressDialog;
		public ViewTicket(Context context) {
			this.context = context;
			progressDialog = new ProgressDialog(context);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setMessage("Fetching ticket, please wait...");
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
				lvTickets.setAdapter(ticketAdapter);
			}
		}
	}
		
		public String loginOutput(String url)
		{
			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
    		    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
    		    nameValuePairs.add(new BasicNameValuePair("atvm",AtvmNo));
    	        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = httpClient.execute(httpPost,
						responseHandler);
				Log.d("Response",responseBody);    	       
				JSONObject jsonResponse = new JSONObject(responseBody);
    	        JSONObject object = jsonResponse.getJSONObject("msg");    	       
	    	        if(!object.toString().trim().contains("null"))
	    	        {
		    	        JSONArray jArray = object.getJSONArray("message");
		    	        for(int i=0;i<jArray.length();i++)
		    	        {
		    	        	Log.d("Src",jArray.getString(i));
		    	        	ticketAdapter.add(jArray.getString(i));
		    	        	String str=jArray.getString(i);
		    	    		String []Details=str.split(",");
		    	    		id.add(Details[0].trim());
		    	        }
	    	        }
    	    	} 
				catch (Exception e) {
    	    		e.printStackTrace();
    	    	}
			return null;
		}
		
		
		public class CancelTicket extends AsyncTask<String, Void,String>
		{		
			Context context;
			ProgressDialog progressDialog;
			public CancelTicket(Context context) {
				this.context = context;
				progressDialog = new ProgressDialog(context);
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog.setMessage("Cancelling ticket, please wait...");
				progressDialog.setCancelable(false);
				progressDialog.show();
			}

			@Override
			protected String doInBackground(String... urls) {
				String output = null;
				for (String url : urls) {
					output = CancelTecketOutput(url);
				}
				return output;
			}
			@Override
			protected void onPostExecute(String output) {
				super.onPostExecute(output);
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
					lvTickets.setAdapter(ticketAdapter);
				}
			}
		}
			@SuppressLint("WorldReadableFiles")
			public String CancelTecketOutput(String url)
			{
				try {
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(url);
	    		    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
	    		    nameValuePairs.add(new BasicNameValuePair("tid",tid));
	    	        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	    	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
					String responseBody = httpClient.execute(httpPost,
							responseHandler);
					Log.d("Response",responseBody);
					finish();
					Intent intent=new Intent(viewticket.this,viewticket.class);
					startActivity(intent);
//					JSONObject jsonResponse = new JSONObject(responseBody);
//	    	        JSONObject object = jsonResponse.getJSONObject("msg");    	       
//		    	        if(!object.toString().trim().contains("null"))
//		    	        {
//			    	        JSONArray jArray = object.getJSONArray("message");
//			    	        for(int i=0;i<jArray.length();i++)
//			    	        {
//			    	        	Log.d("Src",jArray.getString(i));
//			    	        	ticketAdapter.add(jArray.getString(i));
//			    	        }
//		    	        }
	    	    	} 
					catch (Exception e) {
	    	    		e.printStackTrace();
	    	    	}
				return null;
			}
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			tid=id.get(arg2);
			AlertDialog.Builder mainBuilder = new AlertDialog.Builder(viewticket.this);
		    mainBuilder.setMessage("Do you wish to cancel this ticket???")
               .setCancelable(false)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() 
               {
                   public void onClick(DialogInterface dialog, int id) {
                	   
                	CancelTicket cancelTicket=new CancelTicket(viewticket.this);
                	cancelTicket.execute("http://192.168.43.99/queeue/cancel_ticket.php");
                	
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() 
               {
                   public void onClick(DialogInterface dialog, int id) {
                	   
                	dialog.dismiss();
                   }
               });
//               
        AlertDialog mainAlert = mainBuilder.create();
        mainAlert.show();
		}
}
