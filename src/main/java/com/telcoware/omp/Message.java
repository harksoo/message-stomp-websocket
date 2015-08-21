package com.telcoware.omp;

import java.util.Date;

public class Message {
	
	private Date time;
	
	private String id;
	private String message;

	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	
	@Override
	public String toString() {
		return "Message [time=" + time + ", id=" + id + ", message=" + message + "]";
	}

}
