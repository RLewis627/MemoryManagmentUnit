/***************************************************************
* file: hole.java
* author: Rachel Lewis
* class: CS 4310 - Operating Systems
*
* assignment: program 3
* date last modified: 5/3/2020
*
* purpose: This file is the class definition for a hole object.
*
****************************************************************/ 
public class hole {
	private int Size;
	private int start;
	private int end;
	public hole(int size, int startIndex, int endIndex) {
		Size = size;
		start = startIndex;
		end = endIndex;
	}
	public int getSize() {
		return Size;
	}
	public int getStart() {
		return start;
	}
	public int getEnd() {
		return end;
	}
}
