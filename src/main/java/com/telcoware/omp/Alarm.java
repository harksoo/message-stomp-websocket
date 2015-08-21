package com.telcoware.omp;

public class Alarm {
	
//	private String id;
	private String system;
	private String upper;
	private String lower;
	private String item;
	private String grade;
	private String timestamp;
	private String log;
	
	public String getId() {
		return String.format("%s-%s-%s-%s", system, upper, lower, item);
	}
	
//	public void setId(String id) {
//		this.id = id;
//	}
	
	public String getSystem() {
		return system;
	}
	
	public void setSystem(String system) {
		this.system = system;
	}
	public String getUpper() {
		return upper;
	}
	public void setUpper(String upper) {
		this.upper = upper;
	}
	public String getLower() {
		return lower;
	}
	public void setLower(String lower) {
		this.lower = lower;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}

	
	

}
