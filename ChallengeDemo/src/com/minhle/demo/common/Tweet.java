package com.minhle.demo.common;

import android.os.Parcel;
import android.os.Parcelable;

public class Tweet implements Parcelable {
	
	private long tweetId;
	private String imageUrl;
	private String profileName;
	private String tweetMessage;

	public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
		
		public Tweet createFromParcel(Parcel in) {
			return new Tweet(in);
		}

		public Tweet[] newArray(int size) {
			return new Tweet[size];
		}
	};

	private Tweet(Parcel in) {
		readFromParcel(in);
	}
	
	public Tweet(long TweetId,String imageUrl, String profileName, String TweetMessage) {
		super();
		
		this.tweetId = TweetId;
		this.imageUrl = imageUrl;
		this.profileName = profileName;
		this.tweetMessage = TweetMessage;
	}
	
	public long getTweetId() {
		return tweetId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getProfileName() {
		return profileName;
	}

	public String getTweetMessage() {
		return tweetMessage;
	}

	public void writeToParcel(Parcel out, int flags) {
		
		out.writeLong(tweetId);
		out.writeString(imageUrl);
		out.writeString(profileName);
		out.writeString(tweetMessage);
	}

	public void readFromParcel(Parcel in) {
		this.tweetId = in.readLong();
		this.imageUrl = in.readString();
		this.profileName = in.readString();
		this.tweetMessage = in.readString();
	}

	public int describeContents() {
		return 0;
	}

}
