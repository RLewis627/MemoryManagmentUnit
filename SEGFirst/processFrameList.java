public class processFrameList {
	private frame[] listOfFrames;
	private int numOfFrames;

	public processFrameList(int numOfPages) {
		listOfFrames = new frame[numOfPages];	//array 2000 frames
		numOfFrames = numOfPages;				//numOfFrames = 2000
		for(int i = 0; i < numOfFrames; i++) {listOfFrames[i] = new frame();}
	}

	public boolean canPlaceInMemory(Process p) {
		int numOfEmptyFrames = 0;
		for(int i = 0; i < numOfFrames; i++) {
			if((numOfEmptyFrames) >= p.getProcessSize()) {return true;}
			if(listOfFrames[i].getFrameOwner() == 0) {numOfEmptyFrames++;}
			if(listOfFrames[i].getFrameOwner() == 1) {numOfEmptyFrames = 0;}
		}
		if((numOfEmptyFrames) >= p.getProcessSize()) {return true;}
		return false;
	}
	
	public void fitProcessinMemory(Process p) {
		int currentPage = 0;
		int remainingMem = p.getProcessSize();
		
		for(int i = 0; i < numOfFrames; i++) {
			if(listOfFrames[i].getFrameOwner() == 0) {
				listOfFrames[i].setFrameOwner(1);
				listOfFrames[i].setSegNumber(currentPage);
				listOfFrames[i].setAssignedProcess(p.getProcessId());
				listOfFrames[i].setSegSize(p.getSegmentSizes()[currentPage]);
				remainingMem -= p.getSegmentSizes()[currentPage];
				currentPage++;
			}
			if(remainingMem <= 0){break;}
		}
	}
	
	public void printFrameList() {
		int inFreeBlock = 0;
		int start = 0;
		int startp = 0;
		int i;
		
		System.out.printf("\tMemory map:\n");
	    for (i = 0; i < numOfFrames; i++) {
	        if(inFreeBlock != 1 && listOfFrames[i].getFrameOwner() != 1) { //Process -> Hole
	            inFreeBlock = 1;
	            start = i;
	        }else if(inFreeBlock == 1 && listOfFrames[i].getFrameOwner() == 1) { //Hole -> Process
	            inFreeBlock = 0;
	            startp = i;
	            System.out.printf("\t\t%d-%d: Hole(inside for loop)\n",
	                   start,
	                   (i - 1));
	        }
	        if (listOfFrames[i].getFrameOwner() == 1) {
	        	System.out.printf("\t\t%d-%d: Process %d, Segment %d\n",
	        			startp,
		                startp+(listOfFrames[i].getSegSize() - 1),
		                listOfFrames[i].getAssignedProcess(),
		                listOfFrames[i].getSegNumber());
	        	startp = startp+(listOfFrames[i].getSegSize());
	        }
	    }
	    if (inFreeBlock == 1 && startp != 2000) {
	        System.out.printf("\t\t%d-%d: Hole (outside for loop)\n",
	               startp,
	               (i - 1));
	    }
	}
	
	public boolean isFramelistEmpty() {
		for(int i = 0; i < numOfFrames; i++) {
			if(listOfFrames[i].getFrameOwner() == 1) {return false;}
		}
		return true;
	}
	
	public void freeMemory(int pid) {
		frame f;
		for(int i = 0; i < numOfFrames; i++) {
			f = listOfFrames[i];
			if(f.getAssignedProcess() == pid) {
				f.setAssignedProcess(0);
	            f.setFrameOwner(0);
			}
		}
	}
}
