public class processFrameList {
	private frame[] listOfFrames;
	private int numOfFrames;
	private int frameSize;

	public processFrameList(int numOfPages) {
		listOfFrames = new frame[numOfPages];
		numOfFrames = numOfPages;
		for(int i = 0; i < numOfFrames; i++) {listOfFrames[i] = new frame();}
	}
	
	public processFrameList(int numOfPages, int pageSize) {
		listOfFrames = new frame[numOfPages];
		frameSize = pageSize;
		numOfFrames = numOfPages;
		for(int i = 0; i < numOfFrames; i++) {listOfFrames[i] = new frame();}
	}

	public boolean canPlaceInMemory(Process p, int policy) {
		if(policy == 2) {
			int numOfEmptyFrames = 0;
			for(int i = 0; i < numOfFrames; i++) {
				if(listOfFrames[i].getFrameOwner() == 0) {numOfEmptyFrames++;}
			}
			return ((numOfEmptyFrames * frameSize) >= p.getProcessSize());
		}else {
			int numOfEmptyFrames = 0;
			for(int i = 0; i < numOfFrames; i++) {
				if((numOfEmptyFrames) >= p.getProcessSize()) {return true;}
				if(listOfFrames[i].getFrameOwner() == 0) {numOfEmptyFrames++;}
				if(listOfFrames[i].getFrameOwner() == 1) {numOfEmptyFrames = 0;}
			}
			if((numOfEmptyFrames) >= p.getProcessSize()) {return true;}
			return false;
		}
	}
	
	public void fitProcessinMemory(Process p, int policy, int fitAlgorithm) {
		int numOfEmptyFrames = 0;
		hole[] holeArray = new hole[100]; //array of 100 holes
		hole h;
		int remainingMem = p.getProcessSize();
		int j = 0;
		if(fitAlgorithm == 2 || fitAlgorithm == 3) {	
			for(int i = 0; i < numOfFrames; i++) {
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
		}
		if(policy == 1 & fitAlgorithm == 1) {	//VSPFirst
			for(int i = 0; i < numOfFrames; i++) {
				if(listOfFrames[i].getFrameOwner() == 0) {
					listOfFrames[i].setFrameOwner(1);
					listOfFrames[i].setAssignedProcess(p.getProcessId());
					remainingMem--;
				}
				if(remainingMem <= 0){break;}
			}
		}
		if(policy == 1 && fitAlgorithm == 2) { //VSPBest
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
		if(policy == 1 && fitAlgorithm == 3) { //VSPWorst
			if(numOfEmptyFrames == numOfFrames) {
				int g = 0;
				while(remainingMem > 0) {
					listOfFrames[g].setFrameOwner(1);
					listOfFrames[g++].setAssignedProcess(p.getProcessId());
					remainingMem--;
				}
			}else {
				int maxAt = 0;
				int startIndex = 0;
				for (int m = 0; m < j; m++) {
					maxAt = holeArray[m].getSize() > holeArray[maxAt].getSize() ? m : maxAt;
				}
				startIndex = holeArray[maxAt].getStart();
					
				while(remainingMem > 0) {
					listOfFrames[startIndex].setFrameOwner(1);
					listOfFrames[startIndex++].setAssignedProcess(p.getProcessId());
					remainingMem--;
				}
			}
		}
		if(policy == 2) {	//PAG
			int currentPage = 1;
			
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
		if(policy == 3 && fitAlgorithm == 1) {	//SEGFirst
			int currentPage = 0;
			int i; int d = 0;
			for(i = 0; i < numOfFrames; i++) {
				if(listOfFrames[i].getFrameOwner() == 0) {
					for(int y = i; y < i+p.getProcessSize(); y++) {
						if(currentPage < p.getNumSegments()) {
							if(d == p.getSegmentSizes()[currentPage]){currentPage++;d=0;}
						}
						listOfFrames[y].setFrameOwner(1);
						listOfFrames[y].setSegNumber(currentPage);
						listOfFrames[y].setAssignedProcess(p.getProcessId());
						remainingMem--; d++;
						if(remainingMem <= 0){break;}
					}
				}
				if(remainingMem <= 0){break;}
			}
		}
		if(policy == 3 && fitAlgorithm == 2) {	//SEGBest
			int currentPage = 0;
			if(numOfEmptyFrames == numOfFrames) {
				int g = 0;
				while(remainingMem > 0) {
					listOfFrames[g].setFrameOwner(1);
					listOfFrames[g].setSegNumber(currentPage);
					listOfFrames[g].setAssignedProcess(p.getProcessId());
					listOfFrames[g++].setSegSize(p.getSegmentSizes()[currentPage]);
					remainingMem -= p.getSegmentSizes()[currentPage];
					currentPage++;
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
				int endIndex = holeArray[minAt].getEnd();
				int d = 0;

				for(int y = startIndex; y <= endIndex; y++) {
					if(currentPage < p.getNumSegments()) {
						if(d == p.getSegmentSizes()[currentPage]){currentPage++;d=0;}
					}
					listOfFrames[y].setFrameOwner(1);
					listOfFrames[y].setSegNumber(currentPage);
					listOfFrames[y].setAssignedProcess(p.getProcessId());
					remainingMem--; d++;
					if(remainingMem <= 0){break;}
				}
			}
		}
		if(policy == 3 && fitAlgorithm == 3) { //SEGWorst
			int currentPage = 0;
			if(numOfEmptyFrames == numOfFrames) {
				int g = 0;
				while(remainingMem > 0) {
					listOfFrames[g].setFrameOwner(1);
					listOfFrames[g].setSegNumber(currentPage);
					listOfFrames[g].setAssignedProcess(p.getProcessId());
					listOfFrames[g++].setSegSize(p.getSegmentSizes()[currentPage]);
					remainingMem -= p.getSegmentSizes()[currentPage];
					currentPage++;
				}
			}else {
				int maxAt = 0;
				int startIndex = 0;
				for (int m = 0; m < j; m++) {
					maxAt = holeArray[m].getSize() > holeArray[maxAt].getSize() ? m : maxAt;
				}
				startIndex = holeArray[maxAt].getStart();
				int endIndex = holeArray[maxAt].getEnd();
				int d = 0;

				for(int y = startIndex; y <= endIndex; y++) {
					if(currentPage < p.getNumSegments()) {
						if(d == p.getSegmentSizes()[currentPage]){currentPage++;d=0;}
					}
					listOfFrames[y].setFrameOwner(1);
					listOfFrames[y].setSegNumber(currentPage);
					listOfFrames[y].setAssignedProcess(p.getProcessId());
					remainingMem--; d++;
					if(remainingMem <= 0){break;}
				}
			}
		}
	}
	
	public void printFrameList(int policy, int fitAlgorithm) {
		int inFreeBlock = 0;
		int start = 0;
		int startp = 0;
		int i;
		if(policy == 1) {	//VSPFirst
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
		if(policy == 2) { //PAG
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
		if(policy == 3) {	//SEG
			System.out.printf("\tMemory map:\n");
		    for (i = 0; i < numOfFrames; i++) {
		        if (inFreeBlock != 1 && listOfFrames[i].getFrameOwner() != 1) {
		            inFreeBlock = 1;
		            start = i;
		            if(i != 0) {
		            	System.out.printf("\t\t%d-%d: Process %d, Segment %d\n",
			        			startp,
				                i-1,
				                listOfFrames[startp].getAssignedProcess(),
				                listOfFrames[startp].getSegNumber());
		            }
		        }else if (inFreeBlock == 1 && listOfFrames[i].getFrameOwner() == 1) {
		            inFreeBlock = 0;
		            startp = i;
		            System.out.printf("\t\t%d-%d: Hole\n",
		                   start,
		                   i - 1);
		        }
		        else if( i != 0 && listOfFrames[i].getAssignedProcess() != listOfFrames[i-1].getAssignedProcess()){
		        	if(listOfFrames[i].getAssignedProcess() != 0 && listOfFrames[i-1].getAssignedProcess() != 0) {
		        		System.out.printf("\t\t%d-%d: Process %d, Segment %d\n",
			        			startp,
				                i-1,
				                listOfFrames[startp].getAssignedProcess(),
				                listOfFrames[startp].getSegNumber());
			        	startp = i;
		        	}
		        }else if(i != 0 && listOfFrames[i].getSegNumber() != listOfFrames[i-1].getSegNumber()) {
		        	if(listOfFrames[i].getAssignedProcess() != 0 && listOfFrames[i-1].getAssignedProcess() != 0) {
		        		if(listOfFrames[i].getAssignedProcess() == listOfFrames[i-1].getAssignedProcess()) {
		        			System.out.printf("\t\t%d-%d: Process %d, Segment %d\n",
				        			startp,
					                i-1,
					                listOfFrames[startp].getAssignedProcess(),
					                listOfFrames[startp].getSegNumber());
			        		startp = i;
		        		}
		        	}
		        }
		    }
		    if (inFreeBlock == 1) {
		        System.out.printf("\t\t%d-%d: Hole\n",
		               start,
		               i - 1);
		    }else {
		    	System.out.printf("\t\t%d-%d: Process %d, Segment %d\n",
	        			startp,
		                i-1,
		                listOfFrames[startp].getAssignedProcess(),
		                listOfFrames[startp].getSegNumber());
		    }
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
	            f.setSegNumber(0);
			}
		}
	}
}