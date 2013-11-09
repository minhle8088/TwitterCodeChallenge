package com.minhle.demo.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minhle.demo.activity.R;
import com.minhle.demo.common.Tweet;

public class TweetsAdapter extends BaseAdapter {

	private List<Tweet> tweets;
	private Context context;	
	
	public TweetsAdapter(final Context iccontext,  List<Tweet> ictweets )
	{
		super();
		
		this.tweets = ictweets;
		this.context = iccontext;
	}
	
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{
		View row = convertView;
		
		if ( row == null )
		{
			final LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = vi.inflate(R.layout.tweet_item, null);
		}

		Tweet tweet = (Tweet) getItem(position);
		
		final TextView profileName = (TextView)row.findViewById(R.id.profileName);
		profileName.setText(tweet.getProfileName());

		final TextView twitMessage = (TextView)row.findViewById(R.id.tweetMessage);
		twitMessage.setText(tweet.getTweetMessage());

		return row;
	}
	
	public int getCount()
	{
		return tweets.size();
	}
	
	public Object getItem(final int index)
	{	
		return tweets.get(index);
	}

	public long getItemId(final int index)
	{
		return index;
	}

}
