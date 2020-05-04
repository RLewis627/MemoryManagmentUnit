public class frame {
	private int frameOwner; 		//1 if assigned to process, 0 if it's a hole
	private int assignedProcess;	//name of process who owns the frame it occupies
	private long segNumber;			//number of segments the process is broken down into
	private int segSize;			//size of the segment
	private int pageNumber;			//number of pages the process is broken down into
	
	public frame() {
		frameOwner = 0;
		assignedProcess = 0;
		pageNumber = 0;
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
	public long getSegNumber() {
		return segNumber;
	}
	public void setSegNumber(int pageNumber) {
		this.segNumber = pageNumber;
	}
	public void setSegNumber(long segment) {
		this.segNumber = segment;
	}
	public void setSegSize(int size) {
		this.segSize = size;
	}
	public int getSegSize() {
		return segSize;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
}