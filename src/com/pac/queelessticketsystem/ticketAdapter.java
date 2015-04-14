package com.pac.queelessticketsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



public class ticketAdapter extends ArrayAdapter<String>{

	Context context;
	public ticketAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context=context;
	}

	public ticketAdapter(Context context,	int textViewResourceId, String[] objects) {
		super(context,textViewResourceId, objects);
		this.context=context;
	}
	


	@Override
	public View getView(int position,View convertView,ViewGroup parent)
	{
		if(convertView==null)
		{
			convertView=LayoutInflater.from(getContext()).inflate(R.layout.ticketdetails,null);
		}
		String str=getItem(position);
		String []Details=str.split(",");
		TextView tvDate=(TextView)convertView.findViewById(R.id.tvDate);
		TextView tvTicId=(TextView)convertView.findViewById(R.id.tvTicId);
		TextView tvsrc=(TextView)convertView.findViewById(R.id.tvsrc);
		TextView tvDesc=(TextView)convertView.findViewById(R.id.tvDestn);
		TextView tvFair=(TextView)convertView.findViewById(R.id.tvFair);
		TextView tvNop=(TextView)convertView.findViewById(R.id.tvNop);
		TextView tvBus=(TextView)convertView.findViewById(R.id.tvBusNo);
		TextView tvNoChild=(TextView)convertView.findViewById(R.id.tvNoChild);
		TextView tvSeatNo=(TextView)convertView.findViewById(R.id.tvSeatNo);
		tvDate.setText(""+Details[1]);
		tvTicId.setText(""+Details[0]);
		tvsrc.setText(""+Details[2]);
		tvDesc.setText(""+Details[3]);
		tvFair.setText(""+Details[4]);
		tvNop.setText(""+Details[5]);
		tvBus.setText(""+Details[6]);
		tvNoChild.setText(""+Details[7]);
		tvSeatNo.setText(""+Details[8]);
		return convertView;
	}

}
