package OperatingSystemTaskTwo;

public class Process {
	
	int ProcessId;
	int ArrivalTime;
	int BurstTime;
	int ComesBackAfter;
	int Priority;
	
	public Process(int processId, int arrivalTime, int burstTime, int comesBackAfter, int priority) {
		super();
		this.ProcessId = processId;
		this.ArrivalTime = arrivalTime;
		this.BurstTime = burstTime;
		this.ComesBackAfter = comesBackAfter;
		this.Priority = priority;
	}

	public int getProcessId() {
		return ProcessId;
	}

	public void setProcessId(int processId) {
		ProcessId = processId;
	}

	public int getArrivalTime() {
		return ArrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		ArrivalTime = arrivalTime;
	}

	public int getBurstTime() {
		return BurstTime;
	}

	public void setBurstTime(int burstTime) {
		BurstTime = burstTime;
	}

	public int getComesBackAfter() {
		return ComesBackAfter;
	}

	public void setComesBackAfter(int comesBackAfter) {
		ComesBackAfter = comesBackAfter;
	}

	public int getPriority() {
		return Priority;
	}

	public void setPriority(int priority) {
		Priority = priority;
	}

	public void printInfo() {
	   if(BurstTime/10 == 0) {
		System.out.println("          " + ProcessId + "                " + ArrivalTime + "                  " + BurstTime
				+ "                   " + ComesBackAfter + "                  " + Priority ) ;
	   }
	   else{
			System.out.println("          " + ProcessId + "                " + ArrivalTime + "                 " + BurstTime
					+ "                   " + ComesBackAfter + "                  " + Priority ) ;
		   }
	} 
}
