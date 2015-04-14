
package com.pac.queelessticketsystem;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class BookTicketAdmin extends Activity {

	
	Spinner SpnSource, spnDestination, SpnTimming;
	TextView tvFair;
	Button btnBookTicketAdmin, btnBack;
	ArrayAdapter<String> adapter;
	ArrayAdapter<String> adapter1;
	ArrayAdapter<String> adapter2;
	ArrayList<String> sourcelst = new ArrayList<String>();
	ArrayList<String> destlst = new ArrayList<String>();
	ArrayList<String> fairlst = new ArrayList<String>();
	ArrayList<String> Childfairlst = new ArrayList<String>();
	ArrayList<String> timelst = new ArrayList<String>();
	ArrayList<String> idlst = new ArrayList<String>();
	String perpersnfair, childfair;
	EditText edAdult, edchild;
	String source,destination,timming;
	private TextView tvDisplayDate;
	Button btnChangeDate;
	private int year;
	private int month;
	private int day;
	Button btnFetchtime;
	LinearLayout lvdisplay;
	String Id="";
	ImageView imgBarcode;
	boolean check=false;
	static final int DATE_DIALOG_ID = 999;
	EditText edMail;
	String userName="";
	@SuppressWarnings("deprecation")
	@SuppressLint({ "WorldReadableFiles",  "UseValueOf" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookticketadmin);
		Intent intent=getIntent();
		userName=intent.getStringExtra("userID");
		
		edMail=(EditText)findViewById(R.id.edSendMail);
		SpnSource = (Spinner) findViewById(R.id.spinner1);
		spnDestination = (Spinner) findViewById(R.id.spinner2);
		SpnTimming = (Spinner) findViewById(R.id.spinner3);
		tvFair = (TextView) findViewById(R.id.tvFairs);
		btnBack = (Button) findViewById(R.id.btnback);
		btnBookTicketAdmin = (Button) findViewById(R.id.btnBook);
		edAdult = (EditText) findViewById(R.id.edAdult);
		edchild = (EditText) findViewById(R.id.edChild);
		adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		imgBarcode = (ImageView) findViewById(R.id.imgBarcode);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SpnTimming.setAdapter(adapter2);
		lvdisplay=(LinearLayout)findViewById(R.id.lvDetails);
		btnFetchtime=(Button)findViewById(R.id.btnGetTimming);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SpnSource.setAdapter(adapter);
		adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		tvDisplayDate = (TextView) findViewById(R.id.tvDate);

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH)+1;
		day = c.get(Calendar.DAY_OF_MONTH);

		// set current date into textview
		tvDisplayDate.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(year).append("-").append((month<10?("0"+month):(month))).append("-")
				.append(day).append(" "));
			
		btnChangeDate = (Button) findViewById(R.id.btnChangeDate);

		btnChangeDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID);

			}

		});
		
		
		
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnDestination.setAdapter(adapter1);
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://192.168.43.99/queeue/destntn.php");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			InputStream is = response.getEntity().getContent();
			String res = convertStreamToString(is);
			JSONObject jsonResponse = new JSONObject(res);
			JSONObject object = jsonResponse.getJSONObject("msg");
			JSONArray jArray = object.getJSONArray("message");
			for (int i = 0; i < jArray.length(); i++) {
				Log.d("Src", jArray.getString(i));
				sourcelst.add(jArray.getString(i));
				adapter.add(jArray.getString(i));
			}
			timelst.clear();
			adapter2.clear();
			timelst.add("No Result");
			adapter2.add("No Result");
			adapter2.notifyDataSetChanged();
		} catch (ClientProtocolException ei) {
		} catch (IOException e) {
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}
		SpnSource
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> adView,
							View view, int position, long id) {
						try {
							timelst.clear();
							adapter2.clear();
							timelst.add("No Result");
							adapter2.add("No Result");
							adapter2.notifyDataSetChanged();
							lvdisplay.setVisibility(View.GONE);
							source = (String) adView
									.getItemAtPosition(position).toString()
									.trim();
							Log.d("src", source);
							destlst.clear();
							fairlst.clear();
							adapter1.clear();
							HttpClient httpclient = new DefaultHttpClient();
							HttpPost httppost = new HttpPost(
									"http://192.168.43.99/queeue/sorce_dest.php");
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
									5);
							nameValuePairs.add(new BasicNameValuePair("source",
									source.trim()));
							httppost.setEntity(new UrlEncodedFormEntity(
									nameValuePairs));
							HttpResponse response = httpclient
									.execute(httppost);
							InputStream is = response.getEntity().getContent();
							String res = convertStreamToString(is);
							Log.d("Result", res);
							JSONObject jsonResponse = new JSONObject(res);
							JSONObject object = jsonResponse
									.getJSONObject("msg");
							JSONArray jArray = object.getJSONArray("message");
							JSONObject jsonResponse1 = new JSONObject(res);
							JSONObject object1 = jsonResponse1
									.getJSONObject("fair");
							JSONArray jArray1 = object1.getJSONArray("fair1");
							JSONObject jsonResponse2 = new JSONObject(res);
							JSONObject object2 = jsonResponse2
									.getJSONObject("childfair");
							JSONArray jArray2 = object2
									.getJSONArray("childfair1");
							for (int i = 0; i < jArray.length(); i++) {
								destlst.add(jArray.getString(i));
								fairlst.add(jArray1.getString(i));
								adapter1.add(jArray.getString(i));
								Childfairlst.add(jArray2.getString(i));
							}
							tvFair.setText(fairlst.get(0));
							perpersnfair = fairlst.get(0);
							childfair = Childfairlst.get(0);
							adapter1.notifyDataSetChanged();
						} catch (ClientProtocolException ei) {
						} catch (IOException e) {
						} catch (Exception e) {
							Log.e("log_tag",
									"Error converting result " + e.toString());
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
		spnDestination
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> adView,
							View view, int position, long id) {
						timelst.clear();
						adapter2.clear();
						timelst.add("No Result");
						adapter2.add("No Result");
						adapter2.notifyDataSetChanged();
						lvdisplay.setVisibility(View.GONE);
						destination = (String) adView
								.getItemAtPosition(position).toString()
								.trim();
						perpersnfair = fairlst.get(position);
						childfair = Childfairlst.get(position);
						// int
						// fair=(Integer.parseInt(perpersnfair)+Integer.parseInt(childfair));
						int fair = (((Integer.parseInt(perpersnfair)) * (new Integer(
								edAdult.getText().toString().trim()).intValue())) + ((Integer
								.parseInt(childfair)) * (new Integer(edchild
								.getText().toString().trim()).intValue())));
						tvFair.setText("" + fair);						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
		SpnTimming
		.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adView,
					View view, int position, long id) {
//					timming=idlst.get(position);
					if(check==false)
						check=true;
					else
						Id=idlst.get(position);
				}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		edAdult.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// tvFair.setText("");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				// tvFair.setText("");
			}

			@SuppressLint("UseValueOf")
			@Override
			public void afterTextChanged(Editable s) {
				if (edAdult.getText().toString().trim().length() != 0
						&& edchild.getText().toString().trim().length() != 0) {
					int amt = (((Integer.parseInt(perpersnfair)) * (new Integer(
							edAdult.getText().toString().trim()).intValue())) + ((Integer
							.parseInt(childfair)) * (new Integer(edchild
							.getText().toString().trim()).intValue())));
					tvFair.setText("" + amt);

				}
			}
		});
		btnFetchtime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					String time=getCurrentTimeFormat("HH:mm:ss");
					timelst.clear();
					idlst.clear();
					adapter2.clear();
					Log.d("Source", source);
					Log.d("Destination", destination);
					Log.d("Date", tvDisplayDate.getText().toString().trim()+" "+time);
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							"http://192.168.43.99/queeue/booking_date.php");
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							5);
					nameValuePairs.add(new BasicNameValuePair("Source",
							source.trim()));
					nameValuePairs.add(new BasicNameValuePair("Destination",
							destination.trim()));
					nameValuePairs.add(new BasicNameValuePair("Date",
							tvDisplayDate.getText().toString().trim()+" "+time));
					httppost.setEntity(new UrlEncodedFormEntity(
							nameValuePairs));
					HttpResponse response = httpclient
							.execute(httppost);
					InputStream is = response.getEntity().getContent();
					String res = convertStreamToString(is);
					Log.d("Timming Result", res);
					if(!res.trim().equals("failed"))
					{
						JSONObject jsonResponse1 = new JSONObject(res);
						JSONObject object1 = jsonResponse1
								.getJSONObject("time");
						if (!object1.toString().trim().contains("null")) {

							JSONArray jArray = object1
									.getJSONArray("time1");
							for (int i = 0; i < jArray.length(); i++) {
								timelst.add(jArray.getString(i));
								adapter2.add(jArray.getString(i));
								adapter2.notifyDataSetChanged();
							}
						}
						JSONObject object2 = jsonResponse1
								.getJSONObject("id");
						if (!object2.toString().trim().contains("null")) {
							
							JSONArray jArray = object2
									.getJSONArray("id1");
							timming=jArray.getString(0);
							Id=jArray.getString(0);
							for (int i = 0; i < jArray.length(); i++) {
								
								idlst.add(jArray.getString(i));
							}
						}
						lvdisplay.setVisibility(View.VISIBLE);
					}
					else
					{
						timelst.add("No Result");
						adapter2.add("No Result");
						adapter2.notifyDataSetChanged();
					}
					

				} catch (ClientProtocolException ei) {
				} catch (IOException e) {
				} catch (Exception e) {
					Log.e("log_tag",
							"Error converting result " + e.toString());
					timelst.add("No Result");
					adapter2.add("No Result");
				}
				
			}
		});
		edchild.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (edchild.getText().toString().trim().length() != 0
						&& edAdult.getText().toString().trim().length() != 0) {
					int amt = (((Integer.parseInt(perpersnfair)) * (new Integer(
							edAdult.getText().toString().trim()).intValue())) + ((Integer
							.parseInt(childfair)) * (new Integer(edchild
							.getText().toString().trim()).intValue())));
					tvFair.setText("" + amt);

				}

			}
		});
		btnBookTicketAdmin.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("UseValueOf")
			@Override
			public void onClick(View v) {
				
				if(!edMail.getText().toString().trim().equals(""))
				{
				if (!edAdult.getText().toString().trim().equals("")
						&& !edchild.getText().toString().trim().equals("")) {
						try {
							Log.d("ID",Id);
							HttpClient httpclient = new DefaultHttpClient();
							HttpPost httppost = new HttpPost(
									"http://192.168.43.99/queeue/BookTicketAdmin.php");
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
									7);
							nameValuePairs.add(new BasicNameValuePair("source",
									SpnSource.getSelectedItem().toString()));
							nameValuePairs.add(new BasicNameValuePair(
									"destination", spnDestination
											.getSelectedItem().toString()));
							nameValuePairs.add(new BasicNameValuePair("atvm",
									userName));
							nameValuePairs.add(new BasicNameValuePair("nop",
									edAdult.getText().toString().trim()));
							nameValuePairs.add(new BasicNameValuePair(
									"noChild", edchild.getText().toString()
											.trim()));
							nameValuePairs.add(new BasicNameValuePair("fair",
									tvFair.getText().toString().trim()));
							nameValuePairs.add(new BasicNameValuePair("id",
									Id));
							httppost.setEntity(new UrlEncodedFormEntity(
									nameValuePairs));
							HttpResponse response = httpclient
									.execute(httppost);
							InputStream is = response.getEntity().getContent();
							String res = convertStreamToString(is);
							Log.d("Booked Result", res);
							if (res.trim().contains("sucess")) {

								Toast.makeText(
										getApplicationContext(),
										"You have booked your ticket successfully",
										Toast.LENGTH_LONG).show();
								String ticketid=res.trim().substring(6, res.trim().length());
								Log.d("ticketId",ticketid);
								Log.d("ticketId",ticketid);
								// barcode data
								String barcode_data = "http://192.168.43.99/queeue/viewticketbyidqr.php?ticketId="+ticketid;
								
								// barcode image
								Bitmap bitmap = null;

								bitmap = encodeAsBitmap(barcode_data,
										BarcodeFormat.QR_CODE, 150, 150);
								imgBarcode.setImageBitmap(bitmap);
								imgBarcode.setVisibility(View.VISIBLE);

								ByteArrayOutputStream bytes = new ByteArrayOutputStream();
								bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

								//you can create a new file name "test.jpg" in sdcard folder.
								File f = new File(Environment.getExternalStorageDirectory()
								                        + File.separator + ticketid+".jpg");
								f.createNewFile();
								//write the bytes in file
								FileOutputStream fo = new FileOutputStream(f);
								fo.write(bytes.toByteArray());

								Uri imageuri = Uri.fromFile(f); 
								Intent send_report = new Intent(Intent.ACTION_SEND);
                                        send_report.putExtra(Intent.EXTRA_EMAIL, new String[]{ edMail.getText().toString().trim()}); 
                                        send_report.putExtra(Intent.EXTRA_SUBJECT, "Railway Ticket");
                                        send_report.putExtra(Intent.EXTRA_STREAM, imageuri);
                                        send_report.putExtra(Intent.EXTRA_TEXT,"Please find ticket in you attachment");  
                                        send_report.setType("text/plain");
                                        send_report.setType("image/png");
                                        startActivityForResult(Intent.createChooser(send_report, "Choose an Email client"), 77);
								// remember close de FileOutput
								fo.close();
								
							} else {
								Toast.makeText(getApplicationContext(),
										"Opration failed", Toast.LENGTH_LONG)
										.show();
//								Intent intent = new Intent(BookTicketAdmin.this,
//										Home.class);
//								startActivity(intent);
							}
						} catch (ClientProtocolException ei) {
						} catch (IOException e) {
						} catch (Exception e) {
							Log.e("log_tag",
									"Error converting result " + e.toString());
						}

					}
				}
				else
					Toast.makeText(getApplicationContext(),
							"Enter Email ID of Passenger", Toast.LENGTH_LONG).show();
			}
		});
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth+1;
			day = selectedDay;

			tvDisplayDate.setText(new StringBuilder()
			// Month is 0 based, just add 1
			.append(year).append("-").append((month<10?("0"+month):(month))).append("-")
			.append(day).append(" "));

		}
	};

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

	
	@SuppressLint("SimpleDateFormat")
	private String getCurrentTimeFormat(String timeFormat){
		  String time = "";
		  SimpleDateFormat df = new SimpleDateFormat(timeFormat);
		  Calendar c = Calendar.getInstance();
		  time = df.format(c.getTime());
		 
		  return time;
		}
	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	Bitmap encodeAsBitmap(String contents, BarcodeFormat qrCode, int img_width,
			int img_height) throws WriterException {
		String contentsToEncode = contents;
		if (contentsToEncode == null) {
			return null;
		}

		Map<EncodeHintType, Object> hints = null;
		String encoding = guessAppropriateEncoding(contentsToEncode);
		if (encoding != null) {
			hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result;
		try {
			result = writer.encode(contentsToEncode, qrCode, img_width,
					img_height, hints);
		} catch (IllegalArgumentException iae) {
			// Unsupported format
			return null;
		}
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}

}
