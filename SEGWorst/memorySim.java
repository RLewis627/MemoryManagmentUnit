import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class memorySim {
	private final static int MAX_TIME = 100000;
	static int frameSize = 0;
	static int fitAlgorithm = 0;
	static Process[] PArray;
	static processQueue p_queue;
	static processFrameList framelist;
	private static int policy;
	private static int memSize;
	private static String file;
	private static int numOfProcesses = 0;
	private static long prevAnnouncement = -1;

	public static void main(String[] args) {
		getUserInput();
		readProcessFile();
		p_queue = new processQueue(numOfProcesses);
		framelist = new processFrameList(memSize);
		
		mLoop();
	}
	
	private static void mLoop() {
		long time = 0;
		while(true) {
			 enqueueProcess(time);
			 terminateProcess(time);
			 allocateMemory(time);
			 time++;
			 if (time > MAX_TIME) {System.out.printf("Max time reached\n");break;}
		     if (p_queue.getSize() == 0 && framelist.isFramelistEmpty()) {
		    	 System.out.printf("All processes completed\n");
		    	 break;
		     }
		}
		printTurnaround();
	}

	private static void printTurnaround() {
		int total = 0;
	    for (int i = 0; i < numOfProcesses; i++) {
	    	total += PArray[i].getTimeFinished() - PArray[i].getArrival();
	    }
	    System.out.printf("Average Turnaround Time: %4d\n", (total / numOfProcesses));
	}

	private static void allocateMemory(long time) {
		int index; 
		int limit;
	    Process p;

	    limit = p_queue.getSize();
	    
	    for (int i = 0; i < limit; i++) {
	    	index = p_queue.iterateQueue(i);
	    	p = p_queue.getProcesses(index);

	        if (framelist.canPlaceInMemory(p)) {
	            System.out.printf("%sMM moves Process %d to memory\n",
	            		getAnnouncedTime(time),
	            		p.getProcessId());

	            framelist.fitProcessinMemory(p);

	            p.setIsActive(true);
	            p.setArrivalInMemory(time);
	            p_queue.dequeuProcessAtIndex(i);
	            p_queue.printQueue();
	            framelist.printFrameList();
	        }
	    }		
	}

	private static void terminateProcess(long time) {
		long timeInMemory;
	    Process p;

	    for (int i = 0; i < numOfProcesses; i++) {
	        p = PArray[i];
	        timeInMemory = time - p.getLifetime();
	        if (p.getIsActive() && (timeInMemory >= p.getArrivalInMemory())){
	            System.out.printf("%sProcess %d completes\n",
	            		getAnnouncedTime(time),
	                   p.getProcessId());
	            p.setIsActive(false);
	            p.setTimeFinished(time);
	            framelist.freeMemory(p.getProcessId());
	            framelist.printFrameList();
	        }
	    }
	}

	private static void enqueueProcess(long time) {
		Process p;
	    for (int i = 0; i < numOfProcesses; i++) {
	        p = PArray[i];

	        if (p.getArrival() == time) {
	            System.out.printf("%sProcess %d arrives\n",
	                   getAnnouncedTime(time),
	                   p.getProcessId());
	            
	            p_queue.enqueueProcess(p);
	            p_queue.printQueue();
	            framelist.printFrameList();
	        }
	    }
	}

	private static String getAnnouncedTime(long time) {
		String result;
		if (prevAnnouncement == time){result = "\t";}
		else{result = String.format("t = %d: ", time);}
		prevAnnouncement = time;
		return result;
	}

	private static void readProcessFile() {
		File fileObj = new File(file);
		Scanner readFile = null;
		int tmp[];
		Process P = null;
		
		try {
			readFile = new Scanner(fileObj);
		} catch (FileNotFoundException e) {
			System.out.println("File "+file+" cannot be found");
			System.exit(0);
		}
		
		numOfProcesses = readFile.nextInt();
		PArray = new Process[numOfProcesses];
		int k = 0;
		int NumOfSegments = 0;
		while(k < numOfProcesses) {
			int i = 0; int processSize = 0;
			int ProcessID = readFile.nextInt();
			int ArrivalTime = readFile.nextInt();
			int Lifetime = readFile.nextInt();
			NumOfSegments = readFile.nextInt();
			tmp = new int[NumOfSegments];
			while(i < NumOfSegments) {
				tmp[i] = readFile.nextInt();
				processSize += tmp[i];
				i++;
			}
			P = new Process(ProcessID, ArrivalTime, Lifetime, NumOfSegments, processSize, frameSize, tmp);
			PArray[k++] = P;
		}
		readFile.close();
	}

	private static void getUserInput() {
		Scanner sc = new Scanner(System.in);
		System.out.printf("Memory size: ");
		memSize = sc.nextInt();
		System.out.printf("Memory management policy (1- VSP, 2- PAG, 3- SEG): ");
		policy = sc.nextInt();
		if(policy == 2) {
			System.out.printf("Page/frame size: ");
			frameSize = sc.nextInt();
		}else {
			System.out.printf("Fit algorithm (1- first-fit, 2- best-fit, 3- worst-fit): ");
			fitAlgorithm = sc.nextInt();
		}
		System.out.printf("Name of workload file: ");
		file = sc.next();
		sc.close();
	}
}
