package OperatingSystemTaskTwo;

public class GanttChart {

	int ProcessID;
	int StartExecutionTime;
	int EndExecutionTime;
	
	public GanttChart(int processID, int startExecutionTime, int endExecutionTime) {
		super();
		ProcessID = processID;
		StartExecutionTime = startExecutionTime;
		EndExecutionTime = endExecutionTime;
	}

	public int getProcessID() {
		return ProcessID;
	}

	public void setProcessID(int processID) {
		ProcessID = processID;
	}

	public int getStartExecutionTime() {
		return StartExecutionTime;
	}

	public void setStartExecutionTime(int startExecutionTime) {
		StartExecutionTime = startExecutionTime;
	}

	public int getEndExecutionTime() {
		return EndExecutionTime;
	}

	public void setEndExecutionTime(int endExecutionTime) {
		EndExecutionTime = endExecutionTime;
	}
		
	public void printInfo() {
		
			System.out.println("PID: " + ProcessID + " => Start Execution at: " + StartExecutionTime + " => CPU Release at: " + EndExecutionTime);
		
	}
}
