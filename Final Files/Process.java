/***************************************************************
* file: Process.java
* author: Rachel Lewis
* class: CS 4310 - Operating Systems
*
* assignment: program 3
* date last modified: 5/3/2020
*
* purpose: This file is the class definition for a Process object.
*
****************************************************************/ 
public class Process {
	private int processId;
	private int arrival;
	private long lifetime;
	private int processSize;
	private int numSegments;
	private int[] segmentSizes;
	private double pageSize;
	private long arrivalInMemory;
	private boolean isActive;
	private long timeFinished;

	public int getProcessId() {return processId;}
	public int getArrival() {return arrival;}
	public long getLifetime() {return lifetime;}
	public int getProcessSize() {return processSize;}
	public int getNumSegments() {return numSegments;}
	public int[] getSegmentSizes() {return segmentSizes;}
	public double getPageSize() {return pageSize;}
	public boolean getIsActive() {return isActive;}
	public long getTimeFinished() {return timeFinished;}
	public long getArrivalInMemory() {return arrivalInMemory;}
	
	public void setIsActive(boolean isActive) {this.isActive = isActive;}
	public void setTimeFinished(long time) {this.timeFinished = time;}
	public void setArrivalInMemory(long time) {this.arrivalInMemory = time;}

	public Process(int pID, int arrivalInMem, int lifetimeInMem, int numOfSeg, int pSize, int frameSize, int[] segSizes) {
		processId = pID;
		arrival = arrivalInMem;
		lifetime = lifetimeInMem;
		numSegments = numOfSeg;
		processSize = pSize;
		pageSize = frameSize;
		segmentSizes = segSizes;
	}
}
