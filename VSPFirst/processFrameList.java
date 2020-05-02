public class processFrameList {
	private frame[] listOfFrames;
	private int numOfFrames;

	public processFrameList(int numOfPages) {
		//VSP numOfPages = memSize	pageSize = 1
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
		int remainingMem = p.getProcessSize();
		//indecies[0] = starting index in memory
		//indecies[1] = ending index in memory
		int[] indecies = new int[2];
		int i;
		
		for(i = 0; i < numOfFrames; i++) {
			if(listOfFrames[i].getFrameOwner() == 0) {
				indecies[0] = i;
				listOfFrames[i].setFrameOwner(1);
				listOfFrames[i].setAssignedProcess(p.getProcessId());
				remainingMem--;
			}
			if(remainingMem <= 0){indecies[1] = indecies[0] + (p.getProcessSize() - 1);break;}
		}
		listOfFrames[i].setIndexInMem(indecies);
	}
	
	public void printFrameList() {
		int inFreeBlock = 0;
		int start = 0;
		int startp = 0;
		int i;
		
		System.out.printf("\tMemory map:\n");
	    for (i = 0; i < numOfFrames; i++) {
	        if (inFreeBlock != 1 && listOfFrames[i].getFrameOwner() != 1) { //Process -> Hole
	            inFreeBlock = 1;
	            start = i;
	            if (i != 0) {
			       	System.out.printf("\t\t%d-%d: Process %d\n",
			       			startp,
				            (i-1),
				            listOfFrames[i-1].getAssignedProcess());
			    }
	        }else if (inFreeBlock == 1 && listOfFrames[i].getFrameOwner() == 1) { //Hole -> Process
	            inFreeBlock = 0;
	            startp = i;
	            System.out.printf("\t\t%d-%d: Hole\n",
	                   start,
	                   (i - 1));
	        }else if(i != 0){ //Process -> Process
	        	if(listOfFrames[i-1].getAssignedProcess() != listOfFrames[i].getAssignedProcess()) {
	        		System.out.printf("\t\t%d-%d: Process %d\n",
			       			startp,
				            (i-1),
				            listOfFrames[i-1].getAssignedProcess());
		        	startp = i;
	        	}
	        }
	    }
	    if (inFreeBlock == 1) {
	        System.out.printf("\t\t%d-%d: Hole\n",
	               start,
	               (i - 1));
	    }else {
	    	System.out.printf("\t\t%d-%d: Process %d\n",
	       			startp,
		            (i-1),
		            listOfFrames[i-1].getAssignedProcess());
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
