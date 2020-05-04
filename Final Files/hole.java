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