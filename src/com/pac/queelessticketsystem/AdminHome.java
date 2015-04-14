package com.pac.queelessticketsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminHome extends Activity {

	Button btnAddBal,btnCheckTic,btnLogout,btnBooktct;
	String userName="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adminop);
		Intent intent=getIntent();
		userName=intent.getStringExtra("userID");
		btnBooktct = (Button) findViewById(R.id.btnBookTicket);
		btnAddBal=(Button)findViewById(R.id.btnAddBal);
		btnCheckTic=(Button)findViewById(R.id.btncheckTic);
		btnAddBal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(AdminHome.this,addbalance.class);
				startActivity(intent);
			}
		});
		btnLogout=(Button)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			Intent intent=new Intent(AdminHome.this,userLogin.class);
			startActivity(intent);
			
			}
		});
		btnBooktct.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			Intent intent=new Intent(AdminHome.this,BookTicketAdmin.class);
			startActivity(intent);
			
			}
		});
		btnCheckTic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(AdminHome.this,checkticket.class);
				startActivity(intent);
			}
		});
	}
}