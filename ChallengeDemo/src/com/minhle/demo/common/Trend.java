package com.minhle.demo.common;

public class Trend {
	
	private String name;
	private String query;
	private String url;
	
	public Trend(String tname, String tquery, String turl) {
		this.name = tname;
		this.query = tquery;
		this.url = turl.replace("http://twitter.com/search?", "https://api.twitter.com/1.1/search/tweets.json?");
	}
	
	public String getName() {
		return name;
	}
	
	public String getQuery() {
		return query;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}  
	
	public void setUrl(String url) {
		this.url = url;
	}

}
