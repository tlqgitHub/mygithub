//package com.huatandm.meetme.adapter;
//
//import java.util.List;
//
//import com.huatandm.meetme.R;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//public class MyPartivipantAdapter extends BaseAdapter {
//
//	Context context;
//	List<String[]> list;
//	
////	public MyPartivipantAdapter(Context context, List<String[]> list) {
////		// TODO Auto-generated constructor stub
////		this.context = context;
////		this.list = list;
////	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return list.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		if (convertView == null)
//			convertView = LayoutInflater.from(context).inflate(
//					R.layout.textview, null);
//		TextView text = (TextView)convertView.findViewById(R.id.textname);
//		text.setText(list.get(position)[0]);
//		return null;
//	}
//
// }
