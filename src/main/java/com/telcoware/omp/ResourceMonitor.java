package com.telcoware.omp;

public class ResourceMonitor {
	private String timestamp;
	
	private String type;
	
	private int cpuMax;
	private int cpuAverage;
	
	private int ramMax;
	private int ramAverage;
	
	private int diskMax;
	private int diskAverage;
	
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getCpuMax() {
		return cpuMax;
	}
	public void setCpuMax(int cpuMax) {
		this.cpuMax = cpuMax;
	}
	public int getCpuAverage() {
		return cpuAverage;
	}
	public void setCpuAverage(int cpuAverage) {
		this.cpuAverage = cpuAverage;
	}
	public int getRamMax() {
		return ramMax;
	}
	public void setRamMax(int ramMax) {
		this.ramMax = ramMax;
	}
	public int getRamAverage() {
		return ramAverage;
	}
	public void setRamAverage(int ramAverage) {
		this.ramAverage = ramAverage;
	}
	public int getDiskMax() {
		return diskMax;
	}
	public void setDiskMax(int diskMax) {
		this.diskMax = diskMax;
	}
	public int getDiskAverage() {
		return diskAverage;
	}
	public void setDiskAverage(int diskAverage) {
		this.diskAverage = diskAverage;
	}
	
	
}
