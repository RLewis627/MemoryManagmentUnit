public class frame {
	private int frameOwner; 		//1 if assigned to process, 0 if it's a hole
	private int assignedProcess;	//name of process who owns the frame it occupies
	private int pageNumber;			//number of pages the process is broken down into
	
	public frame() {
		frameOwner = 0;
		assignedProcess = 0;
		pageNumber = 0;
	}
	public int getFrameOwner() {
		return frameOwner;
	}
	public void setFrameOwner(int frameOwner) {
		this.frameOwner = frameOwner;
	}
	public int getAssignedProcess() {
		return assignedProcess;
	}
	public void setAssignedProcess(int assignedProcess) {
		this.assignedProcess = assignedProcess;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
}
