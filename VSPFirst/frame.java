public class frame {
	private int frameOwner; 		//1 if assigned to process, 0 if it's a hole
	private int assignedProcess;	//name of process who owns the frame it occupies
	private int startIndex;
	private int endIndex;
	
	public frame() {
		frameOwner = 0;
		assignedProcess = 0;
	}
	public int getFrameOwner() {
		return frameOwner;
	}
	public void setFrameOwner(int NewframeOwner) {
		frameOwner = NewframeOwner;
	}
	public int getAssignedProcess() {
		return assignedProcess;
	}
	public void setAssignedProcess(int NewassignedProcess) {
		assignedProcess = NewassignedProcess;
	}
	public void setIndexInMem(int[] index) {
		this.startIndex = index[0];
		this.endIndex = index[1];
	}
	public int getStartIndex() {return startIndex;}
	public int getEndIndex() {return endIndex;}
}
