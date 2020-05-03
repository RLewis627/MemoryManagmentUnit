public class processFrameList {
	private static frame[] listOfFrames;
	private static int numOfFrames;

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
		int numOfEmptyFrames = 0;
		hole[] holeArray = new hole[100]; //array of 100 holes
		hole h;
		int i; int j = 0;
		
		for(i = 0; i < numOfFrames; i++) {
			if(listOfFrames[i].getFrameOwner() == 0) {
				numOfEmptyFrames++;
			}
			if(i != 0 && listOfFrames[i].getFrameOwner() == 1 && listOfFrames[i-1].getFrameOwner() == 0){
				h = new hole(numOfEmptyFrames, i - numOfEmptyFrames, i - 1); //size, start index, end index
				holeArray[j++] = h;
				numOfEmptyFrames = 0;
			}
			if(i == numOfFrames-1 && listOfFrames[i].getFrameOwner() == 0) {
				h = new hole(numOfEmptyFrames, i - (numOfEmptyFrames-1), i); //size, start index, end index
				holeArray[j++] = h;
				numOfEmptyFrames = 0;
			}
		}
		if(numOfEmptyFrames == numOfFrames) {
			int g = 0;
			while(remainingMem > 0) {
				listOfFrames[g].setFrameOwner(1);
				listOfFrames[g++].setAssignedProcess(p.getProcessId());
				remainingMem--;
			}
		}else {
			int minAt = 0;
			int startIndex = 0;
			for (int m = 0; m < j; m++) {
				if(holeArray[m].getSize() < holeArray[minAt].getSize()) {
					if(holeArray[m].getSize() >= p.getProcessSize()) {minAt = m;}
				}
			}
			startIndex = holeArray[minAt].getStart();

			while(remainingMem > 0 && startIndex < 2000) {
				listOfFrames[startIndex].setFrameOwner(1);
				listOfFrames[startIndex++].setAssignedProcess(p.getProcessId());
				remainingMem--;
			}
		}
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