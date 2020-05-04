public class processQueue {
	private int capacity;
	private int size;
	private int front;
	private int rear;
	private Process[] processes;
	
	public int getSize() {return size;}
	public Process getProcesses(int index){return processes[index];}

	public processQueue(int length) {
		capacity = length;
		processes = new Process[length];
		size = 0;
		front = 0;
		rear = -1;
	}
	
	public void enqueueProcess(Process P) {
		if(size == capacity) {
			System.out.println("Cannot queue process to a filled queue!");
			System.exit(0);
		}
		size++;
		rear++;
		processes[rear] = P;
	}
	
	public void dequeueProcess() {
		if(!hasNext()){
			System.out.println("Cannot dequeue process from an empty queue!");
			System.exit(0);
		}
		size--;
		front++;
		if (front == capacity) {front = 0;}
	}
	
	public void dequeuProcessAtIndex(int index) {
		int prev = 0;
		for(int i = 0; i < size; i++) {
			if(i > index) {processes[prev] = processes[i];}
			prev = i;
		}
		size--;
		rear--;
	}
	
	private boolean hasNext() {
	    if(size == 0) {return false;}
	    else{return true;}
	}
	
	public Process peekProcessQueue() {
	    if (!hasNext()) {
	        System.out.println("Cannot peek on an empty queue!");
	        System.exit(0);
	    }
	    return processes[front];
	}
	
	public int iterateQueue(int index) {return front + index;}
	
	public Process peekProcessQueue(int index) {
	    if (!hasNext()) {
	        System.out.println("Cannot peek on an empty queue!");
	        System.exit(0);
	    }
	    return processes[index];
	}
	
	public void printQueue() {
		Process p;
		System.out.printf("\tInput queue: [ ");
		for(int i = 0; i < size; i++) {
			p = peekProcessQueue(iterateQueue(i));
			System.out.printf("%d ", p.getProcessId());
		}
		System.out.printf("]\n");
	}
}