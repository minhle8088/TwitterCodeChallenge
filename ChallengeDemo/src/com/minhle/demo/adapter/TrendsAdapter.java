package com.minhle.demo.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minhle.demo.activity.R;
import com.minhle.demo.common.Trend;

public class TrendsAdapter extends BaseAdapter {
	
	private List<Trend> trends;
	private Context context;
	
	
	public TrendsAdapter(final Context iccontext, List<Trend> ictrends) {
		super();
		
		this.context = iccontext;
		this.trends = ictrends;

	}

	@Override
	public int getCount() {

		return trends.size();
	}

	@Override
	public Object getItem(final int index) {

		return trends.get(index);
	}

	@Override
	public long getItemId(final int index) {

		return index;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {

		View row = convertView;
		if ( row == null )
		{
			final LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = vi.inflate(R.layout.trend_item, null);
		}

		Trend topic = (Trend) getItem(pos);

		final TextView topicName = (TextView)row.findViewById(R.id.trend_name);
		topicName.setText(topic.getName());
		
		final TextView urlName = (TextView)row.findViewById(R.id.trend_url);
		urlName.setText(topic.getUrl());

		return row;
	}

}
