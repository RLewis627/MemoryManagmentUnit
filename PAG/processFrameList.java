public class processFrameList {
	private frame[] listOfFrames;
	private int numOfFrames;
	private int frameSize;

	public processFrameList(int numOfPages, int pageSize) {
		listOfFrames = new frame[numOfPages];
		frameSize = pageSize;
		numOfFrames = numOfPages;
		for(int i = 0; i < numOfFrames; i++) {listOfFrames[i] = new frame();}
	}

	public boolean canPlaceInMemory(Process p) {
		int numOfEmptyFrames = 0;
		for(int i = 0; i < numOfFrames; i++) {
			if(listOfFrames[i].getFrameOwner() == 0) {numOfEmptyFrames++;}
		}
		return ((numOfEmptyFrames * frameSize) >= p.getProcessSize());
	}
	
	public void fitProcessinMemory(Process p) {
		int currentPage = 1;
		int remainingMem = p.getProcessSize();
		
		for(int i = 0; i < numOfFrames; i++) {
			if(listOfFrames[i].getFrameOwner() == 0) {
				listOfFrames[i].setFrameOwner(1);
				listOfFrames[i].setPageNumber(currentPage);
				listOfFrames[i].setAssignedProcess(p.getProcessId());
				currentPage++;
				remainingMem -= frameSize;
			}
			if(remainingMem <= 0){break;}
		}
	}
	
	public void printFrameList() {
		int inFreeBlock = 0;
		int start = 0;
		int i;
		
		System.out.printf("\tMemory map:\n");
	    for (i = 0; i < numOfFrames; i++) {
	        if (inFreeBlock != 1 && listOfFrames[i].getFrameOwner() != 1) {
	            inFreeBlock = 1;
	            start = i;
	        }else if (inFreeBlock == 1 && listOfFrames[i].getFrameOwner() == 1) {
	            inFreeBlock = 0;
	            System.out.printf("\t\t%d-%d: Free frame(s)\n",
	                   start * frameSize,
	                   (i * frameSize) - 1);
	        }
	        if (listOfFrames[i].getFrameOwner() == 1) {
	        	System.out.printf("\t\t%d-%d: Process %d, Page %d\n",
	        			i * frameSize,
		                ((i + 1) * frameSize) - 1,
		                listOfFrames[i].getAssignedProcess(),
		                listOfFrames[i].getPageNumber());
	        }
	    }
	    if (inFreeBlock == 1) {
	        System.out.printf("\t\t%d-%d: Free frame(s)\n",
	               start * frameSize,
	               (i * frameSize) - 1);
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
	            f.setPageNumber(0);
	            f.setFrameOwner(0);
			}
		}
	}
}
