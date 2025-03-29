package OperatingSystemTaskTwo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Collections;
import java.util.Iterator;

public class CPUSchedulerSimulation {

	public static void main(String[] args) {
		System.out.println("///////////////////////////////////Welcome to My CPU Scheduler///////////////////////////////////\n\n");
		
		/*creates an ArrayList called Processes of type Process*/
		ArrayList<Process>Processes = new ArrayList<>();
		
		/*we adds seven different processes to the ArrayList processes*/
		Processes.add(new Process(1, 0, 10, 2, 3));
		Processes.add(new Process(2, 1, 8, 4, 2));
		Processes.add(new Process(3, 3, 14, 6, 3));
		Processes.add(new Process(4, 4, 7, 8, 1));
		Processes.add(new Process(5, 6, 5, 3, 0));
		Processes.add(new Process(6, 7, 4, 6, 1));
		Processes.add(new Process(7, 8, 6, 9, 2));

		/*Prints the processes information "Process Id, Arrival Time, Burst Time, Comes back after, Priority"*/
		System.out.println("      ------------------------------------------------------------------------------------");
		System.out.println("       Process       Arrival Time        Burst Time       Comes back after       Priority");
		System.out.println("      ------------------------------------------------------------------------------------");
		for(Process process : Processes) {
			process.printInfo();
		}
		System.out.println("      ------------------------------------------------------------------------------------");
		
		/*First Come First Served (FCFS) Scheduling Algorithm*/
		FirstComeFirstServed(Processes);
		
		/*Shortest Job First (SJF) Scheduling Algorithm*/
		ShortestJobFirst(Processes);
		
		ShortestRemainingTimeFirst(Processes);
		
		RoundRobin(Processes);
		
		PreemptivePriority(Processes);
		
		NonPreemptivePriority(Processes);
	}
	
	public static void FirstComeFirstServed(ArrayList<Process>Processes) {
		
		/*First Come First Served (FCFS) Scheduling Algorithm: the process that arrives first is the one thats gets executed first (non-preemetive)*/
		System.out.println("\n\n***********************************************");
		System.out.println(" Part One: First Come First Served Scheduling:");
		System.out.println("***********************************************");
		
		//Define a copy of the Processes ArrayList
        ArrayList<Process> CopyProcesses = new ArrayList<>(Processes);
        
        //Sort the copy processes based on arrival time (Just to confirm the order)
        Collections.sort(CopyProcesses, (p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
        
		//creates an ArrayList called ganttChart of type GanttChart to store the Gantt chart
		ArrayList<GanttChart>ganttChart = new ArrayList<>();
		
        //Define the ready queue to manage ready processes
      	Queue<Process> ReadyQueue = new LinkedList<>();
      		
      	//Define the Waiting queue
      	Queue<Process> WaitingQueue = new LinkedList<>();
      
        int ProcessesNumber = CopyProcesses.size();
        int[] CompletionTime = new int[ProcessesNumber];
        int[] TurnAroundTime = new int[ProcessesNumber];
        int[] WaitingTime = new int[ProcessesNumber];
        int[] TimeSenttoWaitingQueue = new int[ProcessesNumber];
        int[] timeinWaitingQueue = new int[ProcessesNumber];
        boolean[] executedProcess = new boolean[ProcessesNumber];
        
        int[] ArriveToReadyQueue = new int[ProcessesNumber];
        for(Process process : CopyProcesses) {
        	ArriveToReadyQueue[process.getProcessId()-1] = process.getArrivalTime(); 
        }
        
        int currentTime = 0;
        int numberofExecutionProcesses = 0;
        int SumWaitingTime = 0;
        int SumTurnaroundTime = 0;

        //Find Waiting Time, TurnAroundTime and Gantt Chart
        while((!CopyProcesses.isEmpty() || !ReadyQueue.isEmpty()) && currentTime < 200) {
        	
        	//Add arriving processes to the ready queue
            while (!CopyProcesses.isEmpty() && CopyProcesses.get(0).getArrivalTime() <= currentTime) {
                ReadyQueue.add(CopyProcesses.remove(0));
            }
            
            //If the ready queue is not empty, execute the process 
            if (!ReadyQueue.isEmpty()) {
            	
                Process currentProcess = ReadyQueue.poll();
                
                //Calculate completion time, turn around time, and waiting time
                CompletionTime[currentProcess.getProcessId()-1] = currentTime + currentProcess.getBurstTime();
                //Waiting time of the process is the time that the process spent in the ready queue:
                WaitingTime[currentProcess.getProcessId()-1] = currentTime - ArriveToReadyQueue[currentProcess.getProcessId()-1];
                //Turn Around time is the time from the moment a process reaches the ready queue until the CPU is released from it:
                TurnAroundTime[currentProcess.getProcessId() - 1] = WaitingTime[currentProcess.getProcessId() - 1] + timeinWaitingQueue[currentProcess.getProcessId()-1] + currentProcess.getBurstTime();
                
                //Calculate the sum of waiting time and the sum of turn around time
                if(CompletionTime[currentProcess.getProcessId()-1] <= 200) {
                    SumWaitingTime += WaitingTime[currentProcess.getProcessId()-1];
                    SumTurnaroundTime += TurnAroundTime[currentProcess.getProcessId()-1];
                    ganttChart.add(new GanttChart(currentProcess.getProcessId(), currentTime, CompletionTime[currentProcess.getProcessId()-1]));
                }
                if(CompletionTime[currentProcess.getProcessId()-1] > 200) {
                	CompletionTime[currentProcess.getProcessId()-1] = 200;
                	ganttChart.add(new GanttChart(currentProcess.getProcessId(), currentTime, CompletionTime[currentProcess.getProcessId()-1]));
                }
                
                //Move the process to the waiting queue
                WaitingQueue.add(currentProcess);
                
                TimeSenttoWaitingQueue[currentProcess.getProcessId()-1] = CompletionTime[currentProcess.getProcessId()-1]; 
                
                ArriveToReadyQueue[currentProcess.getProcessId()-1] = TimeSenttoWaitingQueue[currentProcess.getProcessId()-1] + currentProcess.getComesBackAfter();
                
                currentTime = CompletionTime[currentProcess.getProcessId()-1];
                
                if(!executedProcess[currentProcess.getProcessId()-1]) {
                    executedProcess[currentProcess.getProcessId()-1] = true;
                    numberofExecutionProcesses++;
                }
            } 
            
            //If there is no process in the ready queue,then move to the next arriving process
            else {
                currentTime = Processes.get(0).getArrivalTime();
            }
            
            //Check if processes in the waiting queue should come back to the ready queue
            /**/
            for (Iterator<Process> iterator = WaitingQueue.iterator(); iterator.hasNext(); ) {
                Process waitingProcess = iterator.next();
                if (currentTime - TimeSenttoWaitingQueue[waitingProcess.getProcessId()-1] >= waitingProcess.getComesBackAfter()) {
                    ReadyQueue.add(waitingProcess);
                    timeinWaitingQueue[waitingProcess.getProcessId()-1] = waitingProcess.getComesBackAfter() ;
                    iterator.remove();
                }
            }
        	
        }
        
        //We need to build the Gantt chart:
        System.out.println("\n#Gantt Chart:");
		System.out.println("\n  Process ID => Start Execution at => CPU Release at");
		System.out.println("-----------------------------------------------------------");
        for(GanttChart value : ganttChart) {
			value.printInfo();
		}
        
        //Calculate average waiting time and average turn around time
        double AverageWaitingTime = (double)SumWaitingTime/numberofExecutionProcesses;
        double AverageTurnaroundTime = (double)SumTurnaroundTime/numberofExecutionProcesses;  
        
        System.out.println("\n#Average Waiting Time = " + AverageWaitingTime);
        System.out.println("\n#Average Turnaround Time = " + AverageTurnaroundTime);
        
	}
	
	public static void ShortestJobFirst(ArrayList<Process>Processes) {
		
		/*Shortest Job First (SJF) Scheduling Algorithm: the process that arrives first with the smallest burst job gets executed first (non-preemetive)*/
		System.out.println("\n\n***********************************************");
		System.out.println("   Part Two: Shortest Job First Scheduling:");
		System.out.println("***********************************************");
        
        //Define a copy of the Processes ArrayList
        ArrayList<Process> CopyProcesses = new ArrayList<>(Processes);

		//Sort the processes based on arrival time (Just to confirm the order)
        Collections.sort(CopyProcesses, (p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
        
		/*creates an ArrayList called ganttChart of type GanttChart to store the Gantt chart*/
		ArrayList<GanttChart>ganttChart = new ArrayList<>();
		
		//Define the ready queue to manage ready processes
		PriorityQueue<Process> ReadyQueue = new PriorityQueue<>((p1, p2) -> Integer.compare(p1.getBurstTime(), p2.getBurstTime()));
		
		//Define the Waiting queue
      	Queue<Process> WaitingQueue = new LinkedList<>();
      	
      	int ProcessesNumber = CopyProcesses.size();
        int[] CompletionTime = new int[ProcessesNumber];
        int[] TurnAroundTime = new int[ProcessesNumber];
        int[] WaitingTime = new int[ProcessesNumber];
        int[] TimeSenttoWaitingQueue = new int[ProcessesNumber];
        int[] timeinWaitingQueue = new int[ProcessesNumber];
        boolean[] executedProcess = new boolean[ProcessesNumber];
        
        int[] ArriveToReadyQueue = new int[ProcessesNumber];
        for(Process process : CopyProcesses) {
        	ArriveToReadyQueue[process.getProcessId()-1] = process.getArrivalTime(); 
        }
        
        int currentTime = 0;
        int numberofExecutionProcesses = 0;
        int SumWaitingTime = 0;
        int SumTurnaroundTime = 0;

        //Find Waiting Time, TurnAroundTime and Gantt Chart
        while((!CopyProcesses.isEmpty() || !ReadyQueue.isEmpty()) && currentTime < 200) {
        	
        	//Add arriving processes to the ready queue
            while (!CopyProcesses.isEmpty() && CopyProcesses.get(0).getArrivalTime() <= currentTime) {
                ReadyQueue.add(CopyProcesses.remove(0));
            }
            
            //If the ready queue is not empty, execute the process 
            if (!ReadyQueue.isEmpty()) {
            	
                Process currentProcess = ReadyQueue.poll();
                
                //Calculate completion time, turn around time, and waiting time
                CompletionTime[currentProcess.getProcessId()-1] = currentTime + currentProcess.getBurstTime();
                //Waiting time of the process is the time that the process spent in the ready queue:
                WaitingTime[currentProcess.getProcessId()-1] = currentTime - ArriveToReadyQueue[currentProcess.getProcessId()-1];
                //Turn Around time is the time from the moment a process reaches the ready queue until the CPU is released from it:
                TurnAroundTime[currentProcess.getProcessId() - 1] = WaitingTime[currentProcess.getProcessId() - 1] + timeinWaitingQueue[currentProcess.getProcessId()-1] + currentProcess.getBurstTime();
                
                //Calculate the sum of waiting time and the sum of turn around time
                if(CompletionTime[currentProcess.getProcessId()-1] <= 200) {
                    SumWaitingTime += WaitingTime[currentProcess.getProcessId()-1];
                    SumTurnaroundTime += TurnAroundTime[currentProcess.getProcessId()-1];
                    ganttChart.add(new GanttChart(currentProcess.getProcessId(), currentTime, CompletionTime[currentProcess.getProcessId()-1]));
                }
                if(CompletionTime[currentProcess.getProcessId()-1] > 200) {
                	CompletionTime[currentProcess.getProcessId()-1] = 200;
                	ganttChart.add(new GanttChart(currentProcess.getProcessId(), currentTime, CompletionTime[currentProcess.getProcessId()-1]));
                }
                
                //Move the process to the waiting queue
                WaitingQueue.add(currentProcess);
                
                TimeSenttoWaitingQueue[currentProcess.getProcessId()-1] = CompletionTime[currentProcess.getProcessId()-1]; 
                
                ArriveToReadyQueue[currentProcess.getProcessId()-1] = TimeSenttoWaitingQueue[currentProcess.getProcessId()-1] + currentProcess.getComesBackAfter();
                
                currentTime = CompletionTime[currentProcess.getProcessId()-1];
                
                if(!executedProcess[currentProcess.getProcessId()-1]) {
                    executedProcess[currentProcess.getProcessId()-1] = true;
                    numberofExecutionProcesses++;
                }
            } 
            
            //If there is no process in the ready queue,then move to the next arriving process
            else {
                currentTime = Processes.get(0).getArrivalTime();
            }
            
            //Check if processes in the waiting queue should come back to the ready queue
            /**/
            for (Iterator<Process> iterator = WaitingQueue.iterator(); iterator.hasNext(); ) {
                Process waitingProcess = iterator.next();
                if (currentTime - TimeSenttoWaitingQueue[waitingProcess.getProcessId()-1] >= waitingProcess.getComesBackAfter()) {
                    ReadyQueue.add(waitingProcess);
                    timeinWaitingQueue[waitingProcess.getProcessId()-1] = waitingProcess.getComesBackAfter() ;
                    iterator.remove();
                }
            }
        	
        }
        
        //We need to build the Gantt chart:
        System.out.println("\n#Gantt Chart:");
		System.out.println("\n  Process ID => Start Execution at => CPU Release at");
		System.out.println("-----------------------------------------------------------");
        for(GanttChart value : ganttChart) {
			value.printInfo();
		}
        
        //Calculate average waiting time and average turn around time
        double AverageWaitingTime = (double)SumWaitingTime/numberofExecutionProcesses;
        double AverageTurnaroundTime = (double)SumTurnaroundTime/numberofExecutionProcesses;  
        
        System.out.println("\n#Average Waiting Time = " + AverageWaitingTime);
        System.out.println("\n#Average Turnaround Time = " + AverageTurnaroundTime);
        
	}
	
    public static void ShortestRemainingTimeFirst(ArrayList<Process>Processes) {
		
        /*Shortest Remaining Time First(SRTF) Scheduling Algorithm: the process that arrives first is the one thats gets executed first (preemetive)*/
		System.out.println("\n\n*******************************************************");
		System.out.println(" Part Three: Shortest Remaining Time First Scheduling:");
		System.out.println("*******************************************************");
        
        //Define a copy of the Processes ArrayList
        ArrayList<Process> CopyProcesses = new ArrayList<>(Processes);
		
		//Sort the processes based on arrival time (Just to confirm the order)
        Collections.sort(CopyProcesses, (p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
        
		/*creates an ArrayList called ganttChart of type GanttChart to store the Gantt chart*/
		ArrayList<GanttChart>ganttChart = new ArrayList<>();
		
		//Define the ready queue to manage ready processes
		PriorityQueue<Process> ReadyQueue = new PriorityQueue<>((p1, p2) -> Integer.compare(p1.getBurstTime(), p2.getBurstTime()));
		
		//Define the Waiting queue
      	Queue<Process> WaitingQueue = new LinkedList<>();

	    int ProcessesNumber = Processes.size();
        int[] TurnAroundTime = new int[ProcessesNumber];
        int[] WaitingTime = new int[ProcessesNumber];
        int[] TimeSenttoWaitingQueue = new int[ProcessesNumber];
        int[] NumberofUnitesExecution = new int[ProcessesNumber];
        int[] startTime = new int[ProcessesNumber];
        int[] timeinWaitingQueue = new int[ProcessesNumber];
        boolean[] executedProcess = new boolean[ProcessesNumber];
        
        int[] ArriveToReadyQueue = new int[ProcessesNumber];
        for(Process process : CopyProcesses) {
        	ArriveToReadyQueue[process.getProcessId()-1] = process.getArrivalTime(); 
        }
        
        int[] burstTime = new int[ProcessesNumber];
        for(Process process : Processes) {
        	burstTime[process.getProcessId()-1] = process.getBurstTime();
        }
        
        int currentTime = 0;
        int numberofExecutionProcesses = 0;
        int SumWaitingTime = 0;
        int SumTurnaroundTime = 0;

        Process currentProcess = null;

        //Find Waiting Time, TurnAroundTime, and Gantt Chart
        while ((!CopyProcesses.isEmpty() || !ReadyQueue.isEmpty()) && currentTime <= 200) {

            //Add arriving processes to the ready queue
            while (!CopyProcesses.isEmpty() && CopyProcesses.get(0).getArrivalTime() <= currentTime) {
                ReadyQueue.add(CopyProcesses.remove(0));
            }

            // Check if there are processes in the ready queue
            if (!ReadyQueue.isEmpty()) {
            	
            	if(currentProcess == null) {
            		currentProcess = ReadyQueue.poll();
            		startTime[currentProcess.getProcessId()-1] = currentTime;
            	}
            	
                //Iterate through all processes in the ready queue
                for (Process readyProcess : ReadyQueue) {
                    if (burstTime[currentProcess.getProcessId() - 1] > burstTime[readyProcess.getProcessId() - 1]) {
                        //There is a process with shorter burst time, preempt current process
                    	startTime[readyProcess.getProcessId()-1] = currentTime;
                        ReadyQueue.add(currentProcess);
                        ganttChart.add(new GanttChart(currentProcess.getProcessId(), startTime[currentProcess.getProcessId()-1], currentTime));
                        currentProcess = ReadyQueue.poll();
                        break;
                    }
                }
                 
                burstTime[currentProcess.getProcessId() - 1]--; 
                NumberofUnitesExecution[currentProcess.getProcessId() - 1]++;
                currentTime++;
                
                //Check if the current process has finished
                if (burstTime[currentProcess.getProcessId() - 1] == 0){
                	
                    WaitingTime[currentProcess.getProcessId() - 1] = currentTime - NumberofUnitesExecution[currentProcess.getProcessId() - 1] - ArriveToReadyQueue[currentProcess.getProcessId()-1];
                    TurnAroundTime[currentProcess.getProcessId() - 1] = WaitingTime[currentProcess.getProcessId() - 1] + timeinWaitingQueue[currentProcess.getProcessId()-1] + currentProcess.getBurstTime();

                	
                    if(currentTime <= 200) {
                        SumWaitingTime += WaitingTime[currentProcess.getProcessId() - 1];
                        SumTurnaroundTime += TurnAroundTime[currentProcess.getProcessId() - 1];
                        ganttChart.add(new GanttChart(currentProcess.getProcessId(), startTime[currentProcess.getProcessId()-1], currentTime));
                    }
                    else if(currentTime > 200) {
                    	ganttChart.add(new GanttChart(currentProcess.getProcessId(), startTime[currentProcess.getProcessId()-1], 200));
                    }
                    
                    burstTime[currentProcess.getProcessId()-1] = currentProcess.getBurstTime();
                    NumberofUnitesExecution[currentProcess.getProcessId() - 1] = 0;
               
                    //Move the process to the waiting queue
                    WaitingQueue.add(currentProcess);
                    
                    TimeSenttoWaitingQueue[currentProcess.getProcessId()-1] = currentTime;
                    
                    if(!executedProcess[currentProcess.getProcessId()-1]) {
                        executedProcess[currentProcess.getProcessId()-1] = true;
                        numberofExecutionProcesses++;
                    }
                    
                    currentProcess = null;
                }
               
            }
            
            // Check if processes in the waiting queue should come back to the ready queue
            for (Iterator<Process> iterator = WaitingQueue.iterator(); iterator.hasNext(); ) {
                Process waitingProcess = iterator.next();
                if (currentTime - TimeSenttoWaitingQueue[waitingProcess.getProcessId() - 1] >= waitingProcess.getComesBackAfter()) {
                    ArriveToReadyQueue[waitingProcess.getProcessId()-1] = currentTime;
                    timeinWaitingQueue[waitingProcess.getProcessId()-1] = waitingProcess.getComesBackAfter() ;
                    ReadyQueue.add(waitingProcess);
                    iterator.remove();
                }
            }

        }

	    // We need to build the Gantt chart:
	    System.out.println("\n#Gantt Chart:");
	    System.out.println("\n  Process ID => Start Execution at => CPU Release at");
	    System.out.println("-----------------------------------------------------------");
	    for (GanttChart value : ganttChart) {
	        value.printInfo();
	    }
  
	    //Calculate average waiting time and average turn around time
	    double AverageWaitingTime = (double)SumWaitingTime/numberofExecutionProcesses;
	    double AverageTurnaroundTime = (double)SumTurnaroundTime/numberofExecutionProcesses;
        
	    System.out.println("\n#Average Waiting Time = " + AverageWaitingTime);
	    System.out.println("\n#Average Turnaround Time = " + AverageTurnaroundTime);
	}
	
	public static void RoundRobin(ArrayList<Process>Processes) {
		
		/* Round Robin (RR) Scheduling Algorithm, with q = 5(preemetive)*/
		System.out.println("\n\n************************************");
		System.out.println(" Part Four: Round Roben Scheduling:");
		System.out.println("************************************");
        
        //Define a copy of the Processes ArrayList
        ArrayList<Process> CopyProcesses = new ArrayList<>(Processes);
		
		//Sort the processes based on arrival time (Just to confirm the order)
        Collections.sort(CopyProcesses, (p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
        
		/*creates an ArrayList called ganttChart of type GanttChart to store the Gantt chart*/
		ArrayList<GanttChart>ganttChart = new ArrayList<>();
		
		//Define the ready queue to manage ready processes
      	Queue<Process> ReadyQueue = new LinkedList<>();
		
		//Define the Waiting queue
      	Queue<Process> WaitingQueue = new LinkedList<>();

	    int ProcessesNumber = Processes.size();
        int[] TurnAroundTime = new int[ProcessesNumber];
        int[] WaitingTime = new int[ProcessesNumber];
        int[] TimeSenttoWaitingQueue = new int[ProcessesNumber];
        int[] NumberofUnitesExecution = new int[ProcessesNumber];
        int[] startTime = new int[ProcessesNumber];
        int[] executionTime = new int[ProcessesNumber];
        int[] timeinWaitingQueue = new int[ProcessesNumber];
        boolean[] executedProcess = new boolean[ProcessesNumber];
        
        int[] ArriveToReadyQueue = new int[ProcessesNumber];
        for(Process process : CopyProcesses) {
        	ArriveToReadyQueue[process.getProcessId()-1] = process.getArrivalTime(); 
        }
        
        int[] burstTime = new int[ProcessesNumber];
        for(Process process : Processes) {
        	burstTime[process.getProcessId()-1] = process.getBurstTime();
        }
        
        int currentTime = 0;
        int numberofExecutionProcesses = 0;
        int SumWaitingTime = 0;
        int SumTurnaroundTime = 0;
        int q = 5;

        Process currentProcess = null;
        
        // Find Waiting Time, TurnAroundTime, and Gantt Chart
        while ((!CopyProcesses.isEmpty() || !ReadyQueue.isEmpty()) && currentTime <= 200) {

            //Add arriving processes to the ready queue
            while (!CopyProcesses.isEmpty() && CopyProcesses.get(0).getArrivalTime() <= currentTime) {
                ReadyQueue.add(CopyProcesses.remove(0));
            }

            // Check if there are processes in the ready queue
            if (!ReadyQueue.isEmpty()) {
            	
            	if(currentProcess == null) {
            		currentProcess = ReadyQueue.poll();
            		startTime[currentProcess.getProcessId()-1] = currentTime;
            	}
            	
            	executionTime[currentProcess.getProcessId()-1]++;
        		currentTime++;
            	
        		if(currentProcess != null && (burstTime[currentProcess.getProcessId()-1] <= q && executionTime[currentProcess.getProcessId()-1] == burstTime[currentProcess.getProcessId()-1])) {
            		burstTime[currentProcess.getProcessId() - 1] -= executionTime[currentProcess.getProcessId()-1];
            		NumberofUnitesExecution[currentProcess.getProcessId() - 1] += executionTime[currentProcess.getProcessId()-1];
            		ganttChart.add(new GanttChart(currentProcess.getProcessId(), startTime[currentProcess.getProcessId()-1], currentTime));
            		executionTime[currentProcess.getProcessId()-1] = 0;
            		
            		//Check if the current process has finished
                    if (burstTime[currentProcess.getProcessId() - 1] == 0){
                    	
                        WaitingTime[currentProcess.getProcessId() - 1] = currentTime - NumberofUnitesExecution[currentProcess.getProcessId() - 1] - ArriveToReadyQueue[currentProcess.getProcessId()-1];
                        TurnAroundTime[currentProcess.getProcessId() - 1] = WaitingTime[currentProcess.getProcessId() - 1] + timeinWaitingQueue[currentProcess.getProcessId()-1] + currentProcess.getBurstTime();

                    	
                        if(currentTime <= 200) {
                            SumWaitingTime += WaitingTime[currentProcess.getProcessId() - 1];
                            SumTurnaroundTime += TurnAroundTime[currentProcess.getProcessId() - 1];
                            //ganttChart.add(new GanttChart(currentProcess.getProcessId(), startTime[currentProcess.getProcessId()-1], currentTime));
                        }
                        /*  else if(currentTime > 200) {
                        	ganttChart.add(new GanttChart(currentProcess.getProcessId(), startTime[currentProcess.getProcessId()-1], 200));
                        }*/
                        
                        burstTime[currentProcess.getProcessId()-1] = currentProcess.getBurstTime();
                        NumberofUnitesExecution[currentProcess.getProcessId() - 1] = 0;
                   
                        //Move the process to the waiting queue
                        WaitingQueue.add(currentProcess);
                        
                        TimeSenttoWaitingQueue[currentProcess.getProcessId()-1] = currentTime;
                        
                        if(!executedProcess[currentProcess.getProcessId()-1]) {
                            executedProcess[currentProcess.getProcessId()-1] = true;
                            numberofExecutionProcesses++;
                        }
                        
                        currentProcess = null;
                    }
                    
                    else {
                		ReadyQueue.add(currentProcess);
                		currentProcess = null;
                    }
            	}
        		
            	if(currentProcess != null && (burstTime[currentProcess.getProcessId()-1] > q && executionTime[currentProcess.getProcessId()-1] == q)) {
            		burstTime[currentProcess.getProcessId() - 1] -= executionTime[currentProcess.getProcessId()-1];
            		NumberofUnitesExecution[currentProcess.getProcessId() - 1] += executionTime[currentProcess.getProcessId()-1];
            		ganttChart.add(new GanttChart(currentProcess.getProcessId(), startTime[currentProcess.getProcessId()-1], currentTime)); 
            		executionTime[currentProcess.getProcessId()-1] = 0;
            		
            		//Check if the current process has finished
                    if (burstTime[currentProcess.getProcessId() - 1] == 0){
                    	
                        WaitingTime[currentProcess.getProcessId() - 1] = currentTime - NumberofUnitesExecution[currentProcess.getProcessId() - 1] - ArriveToReadyQueue[currentProcess.getProcessId()-1];
                        TurnAroundTime[currentProcess.getProcessId() - 1] = WaitingTime[currentProcess.getProcessId() - 1] + timeinWaitingQueue[currentProcess.getProcessId()-1] + currentProcess.getBurstTime();

                    	
                        if(currentTime <= 200) {
                            SumWaitingTime += WaitingTime[currentProcess.getProcessId() - 1];
                            SumTurnaroundTime += TurnAroundTime[currentProcess.getProcessId() - 1];
                            //ganttChart.add(new GanttChart(currentProcess.getProcessId(), startTime[currentProcess.getProcessId()-1], currentTime));
                        }
                        /*  else if(currentTime > 200) {
                        	ganttChart.add(new GanttChart(currentProcess.getProcessId(), startTime[currentProcess.getProcessId()-1], 200));
                        }*/
                        
                        burstTime[currentProcess.getProcessId()-1] = currentProcess.getBurstTime();
                        NumberofUnitesExecution[currentProcess.getProcessId() - 1] = 0;
                   
                        //Move the process to the waiting queue
                        WaitingQueue.add(currentProcess);
                        
                        TimeSenttoWaitingQueue[currentProcess.getProcessId()-1] = currentTime;
                        
                        if(!executedProcess[currentProcess.getProcessId()-1]) {
                            executedProcess[currentProcess.getProcessId()-1] = true;
                            numberofExecutionProcesses++;
                        }
                        
                        currentProcess = null;
                    }
                    
                    else {
                		ReadyQueue.add(currentProcess);
                		currentProcess = null;
                    }
            	}
            }
            
            // Check if processes in the waiting queue should come back to the ready queue
            for (Iterator<Process> iterator = WaitingQueue.iterator(); iterator.hasNext(); ) {
                Process waitingProcess = iterator.next();
                if (currentTime - TimeSenttoWaitingQueue[waitingProcess.getProcessId() - 1] >= waitingProcess.getComesBackAfter()) {
                    ArriveToReadyQueue[waitingProcess.getProcessId()-1] = currentTime;
                    timeinWaitingQueue[waitingProcess.getProcessId()-1] = waitingProcess.getComesBackAfter() ;
                    ReadyQueue.add(waitingProcess);
                    iterator.remove();
                }
            }
        }
        
        //We need to build the Gantt chart:
	    System.out.println("\n#Gantt Chart:");
	    System.out.println("\n  Process ID => Start Execution at => CPU Release at");
	    System.out.println("-----------------------------------------------------------");
	    for (GanttChart value : ganttChart) {
	        value.printInfo();
	    }
  
	    //Calculate average waiting time and average turn around time
	    double AverageWaitingTime = (double)SumWaitingTime/numberofExecutionProcesses;
	    double AverageTurnaroundTime = (double)SumTurnaroundTime/numberofExecutionProcesses;
        
	    System.out.println("\n#Average Waiting Time = " + AverageWaitingTime);
	    System.out.println("\n#Average Turnaround Time = " + AverageTurnaroundTime);
	}
	
    public static void  PreemptivePriority(ArrayList<Process>Processes) {
		
		/*Priority Scheduling Scheduling Algorithm (preemptive)*/
		System.out.println("\n\n***********************************************************");
		System.out.println("   Part Five: Preemptive Priority Scheduling Scheduling:");
		System.out.println("***********************************************************");
        
        //Define a copy of the Processes ArrayList
        ArrayList<Process> CopyProcesses = new ArrayList<>(Processes);

		//Sort the processes based on arrival time (Just to confirm the order)
        Collections.sort(CopyProcesses, (p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
        
		/*creates an ArrayList called ganttChart of type GanttChart to store the Gantt chart*/
		ArrayList<GanttChart>ganttChart = new ArrayList<>();
		
		//Define the ready queue to manage ready processes
		PriorityQueue<Process> ReadyQueue = new PriorityQueue<>((p1, p2) -> Integer.compare(p1.getArrivalTime() ,p2.getArrivalTime()));
				
		//Define the Waiting queue
      	Queue<Process> WaitingQueue = new LinkedList<>();
      	
      	int ProcessesNumber = Processes.size();
        int[] TurnAroundTime = new int[ProcessesNumber];
        int[] WaitingTime = new int[ProcessesNumber];
        int[] TimeSenttoWaitingQueue = new int[ProcessesNumber];
        int[] NumberofUnitesExecution = new int[ProcessesNumber];
        int[] startTime = new int[ProcessesNumber];
        int[] timeinWaitingQueue = new int[ProcessesNumber];
        boolean[] executedProcess = new boolean[ProcessesNumber];
        
        //Array to keep track of the last time priority was decremented for each process
        int[] lastPriorityDecrement = new int[ProcessesNumber];
        for(Process process : CopyProcesses) {
        	lastPriorityDecrement[process.getProcessId()-1] = process.getArrivalTime(); 
        }
        
        int[] ArriveToReadyQueue = new int[ProcessesNumber];
        for(Process process : CopyProcesses) {
        	ArriveToReadyQueue[process.getProcessId()-1] = process.getArrivalTime(); 
        }
        
        int[] priority = new int[ProcessesNumber];
        for(Process process : Processes) {
        	priority[process.getProcessId()-1] = process.getPriority();
        }
        
        int[] burstTime = new int[ProcessesNumber];
        for(Process process : Processes) {
        	burstTime[process.getProcessId()-1] = process.getBurstTime();
        }
        
        int currentTime = 0;
        int numberofExecutionProcesses = 0;
        int SumWaitingTime = 0;
        int SumTurnaroundTime = 0;
       
        Process currentProcess = null;

        //Find Waiting Time, TurnAroundTime, and Gantt Chart
        while ((!CopyProcesses.isEmpty() || !ReadyQueue.isEmpty()) && currentTime <= 200) {
        	
        	
            while (!CopyProcesses.isEmpty() && CopyProcesses.get(0).getArrivalTime() <= currentTime) {
                ReadyQueue.add(CopyProcesses.remove(0));
            }

            if (!ReadyQueue.isEmpty()) {
                
            	//If there is no process execution now, we should execute the process with less priority value (high priority)
            	if(currentProcess == null) {
            		currentProcess = ReadyQueue.poll();
            		startTime[currentProcess.getProcessId()-1] = currentTime;
            	}
            	
            	for (Process readyProcess : ReadyQueue) {
                    // If the process spent 5 times in the ready queue, decrement priority by 1
                    if ((currentTime - ArriveToReadyQueue[readyProcess.getProcessId() - 1]) != 0 && (currentTime - lastPriorityDecrement[readyProcess.getProcessId() - 1]) % 5 == 0) {
                        lastPriorityDecrement[readyProcess.getProcessId() - 1] = currentTime;
                        if (priority[readyProcess.getProcessId() - 1] > 0) {
                            priority[readyProcess.getProcessId() - 1]--;
                        }
                    }
                }
            	
            	for (Process readyProcess : ReadyQueue) { 
                    if (priority[currentProcess.getProcessId() - 1] > priority[readyProcess.getProcessId() - 1] || (priority[currentProcess.getProcessId() - 1] == priority[readyProcess.getProcessId() - 1] && ArriveToReadyQueue[currentProcess.getProcessId() - 1] > ArriveToReadyQueue[readyProcess.getProcessId() - 1])) {
                    	startTime[readyProcess.getProcessId()-1] = currentTime;
                        ReadyQueue.add(currentProcess);
                        ganttChart.add(new GanttChart(currentProcess.getProcessId(), startTime[currentProcess.getProcessId()-1], currentTime));
                        currentProcess = readyProcess;
                        break;
                    }
                }
                
                burstTime[currentProcess.getProcessId() - 1]--;
                NumberofUnitesExecution[currentProcess.getProcessId() - 1]++;
                currentTime++;

                //Check if the process finished it's execution
                if (burstTime[currentProcess.getProcessId() - 1] == 0) {
                    WaitingTime[currentProcess.getProcessId() - 1] = currentTime - NumberofUnitesExecution[currentProcess.getProcessId() - 1] - ArriveToReadyQueue[currentProcess.getProcessId() - 1];
                    TurnAroundTime[currentProcess.getProcessId() - 1] = WaitingTime[currentProcess.getProcessId() - 1] + timeinWaitingQueue[currentProcess.getProcessId() - 1] + currentProcess.getBurstTime();

                    if (currentTime <= 200) {
                        SumWaitingTime += WaitingTime[currentProcess.getProcessId() - 1];
                        SumTurnaroundTime += TurnAroundTime[currentProcess.getProcessId() - 1];
                        ganttChart.add(new GanttChart(currentProcess.getProcessId(), startTime[currentProcess.getProcessId() - 1], currentTime));
                    } else if (currentTime > 200) {
                        ganttChart.add(new GanttChart(currentProcess.getProcessId(), startTime[currentProcess.getProcessId() - 1], 200));
                    }

                    burstTime[currentProcess.getProcessId() - 1] = currentProcess.getBurstTime();
                    NumberofUnitesExecution[currentProcess.getProcessId() - 1] = 0;

                    WaitingQueue.add(currentProcess);

                    priority[currentProcess.getProcessId() - 1] = currentProcess.getPriority();

                    timeinWaitingQueue[currentProcess.getProcessId() - 1] = currentTime - ArriveToReadyQueue[currentProcess.getProcessId() - 1];

                    if (!executedProcess[currentProcess.getProcessId() - 1]) {
                        executedProcess[currentProcess.getProcessId() - 1] = true;
                        numberofExecutionProcesses++;
                    }

                    currentProcess = null;
                }
            }

            for (Iterator<Process> iterator = WaitingQueue.iterator(); iterator.hasNext(); ) {
                Process waitingProcess = iterator.next();
                if (currentTime - TimeSenttoWaitingQueue[waitingProcess.getProcessId() - 1] >= waitingProcess.getComesBackAfter()) {
                    ArriveToReadyQueue[waitingProcess.getProcessId() - 1] = currentTime;
                    timeinWaitingQueue[waitingProcess.getProcessId() - 1] = waitingProcess.getComesBackAfter();
                    ReadyQueue.add(waitingProcess);
                    iterator.remove();
                }
            }

        }
        
        //We need to build the Gantt chart:
        System.out.println("\n#Gantt Chart:");
		System.out.println("\n  Process ID => Start Execution at => CPU Release at");
		System.out.println("-----------------------------------------------------------");
        for(GanttChart value : ganttChart) {
			value.printInfo();
		}
        
        //Calculate average waiting time and average turn around time
        double AverageWaitingTime = (double)SumWaitingTime/numberofExecutionProcesses;
        double AverageTurnaroundTime = (double)SumTurnaroundTime/numberofExecutionProcesses;  
        
        System.out.println("\n#Average Waiting Time = " + AverageWaitingTime);
        System.out.println("\n#Average Turnaround Time = " + AverageTurnaroundTime);
        
	}

    public static void  NonPreemptivePriority(ArrayList<Process>Processes) {
		
		/*Priority Scheduling Algorithm (non-preemetive)*/
		System.out.println("\n\n*************************************************************");
		System.out.println("   Part Six: Non-preemptive Priority Scheduling:");
		System.out.println("*************************************************************");
        
        //Define a copy of the Processes ArrayList
        ArrayList<Process> CopyProcesses = new ArrayList<>(Processes);

		//Sort the processes based on arrival time (Just to confirm the order)
        Collections.sort(CopyProcesses, (p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
        
		/*creates an ArrayList called ganttChart of type GanttChart to store the Gantt chart*/
		ArrayList<GanttChart>ganttChart = new ArrayList<>();
		
		//Define the ready queue to manage ready processes
		PriorityQueue<Process> ReadyQueue = new PriorityQueue<>((p1, p2) -> Integer.compare(p1.getArrivalTime() ,p2.getArrivalTime()));
				
		//Define the Waiting queue
      	Queue<Process> WaitingQueue = new LinkedList<>();
      	
      	int ProcessesNumber = Processes.size();
        int[] TurnAroundTime = new int[ProcessesNumber];
        int[] WaitingTime = new int[ProcessesNumber];
        int[] TimeSenttoWaitingQueue = new int[ProcessesNumber];
        int[] NumberofUnitesExecution = new int[ProcessesNumber];
        int[] startTime = new int[ProcessesNumber];
        int[] timeinWaitingQueue = new int[ProcessesNumber];
        boolean[] executedProcess = new boolean[ProcessesNumber];
        
        int[] ArriveToReadyQueue = new int[ProcessesNumber];
        for(Process process : CopyProcesses) {
        	ArriveToReadyQueue[process.getProcessId()-1] = process.getArrivalTime(); 
        }
        
        int[] priority = new int[ProcessesNumber];
        for(Process process : Processes) {
        	priority[process.getProcessId()-1] = process.getPriority();
        }
        
        int[] burstTime = new int[ProcessesNumber];
        for(Process process : Processes) {
        	burstTime[process.getProcessId()-1] = process.getBurstTime();
        }
        
        int currentTime = 0;
        int numberofExecutionProcesses = 0;
        int SumWaitingTime = 0;
        int SumTurnaroundTime = 0;
       
        Process currentProcess = null;

        //Find Waiting Time, TurnAroundTime, and Gantt Chart
        while ((!CopyProcesses.isEmpty() || !ReadyQueue.isEmpty()) && currentTime <= 200) {
        	
        	
            while (!CopyProcesses.isEmpty() && CopyProcesses.get(0).getArrivalTime() <= currentTime) {
                ReadyQueue.add(CopyProcesses.remove(0));
            }

            if (!ReadyQueue.isEmpty()) {
                
            	//If there is no process execution now, we should execute the process with less priority value (high priority)
            	if (currentProcess == null) {
                    currentProcess = ReadyQueue.poll();
                    startTime[currentProcess.getProcessId() - 1] = currentTime;

                    ArrayList<Process> processesToPreempt = new ArrayList<>();
                    for (Process readyProcess : ReadyQueue) {

                        if (priority[currentProcess.getProcessId() - 1] > priority[readyProcess.getProcessId() - 1]) {
                            processesToPreempt.add(currentProcess);
                            currentProcess = readyProcess;
                            startTime[currentProcess.getProcessId() - 1] = currentTime;
                        }

                        //if there is two process with the same priority, we run the process which arrive first to the ready queue
                        if (priority[currentProcess.getProcessId() - 1] == priority[readyProcess.getProcessId() - 1] && ArriveToReadyQueue[currentProcess.getProcessId() - 1] > ArriveToReadyQueue[readyProcess.getProcessId() - 1]) {
                            processesToPreempt.add(currentProcess);
                            currentProcess = readyProcess;
                            startTime[currentProcess.getProcessId() - 1] = currentTime;
                        }
                    }

                    for (Process preemptedProcess : processesToPreempt) {
                        ReadyQueue.add(preemptedProcess);
                    }
                }
                
                //if the process spent 5 times in the ready queue, decremented priority by 1
                for (Process readyProcess : ReadyQueue) {

                	if ((currentTime - ArriveToReadyQueue[readyProcess.getProcessId() - 1])!= 0 && (currentTime - ArriveToReadyQueue[readyProcess.getProcessId() - 1]) % 5 == 0 && priority[readyProcess.getProcessId() - 1] > 0) {
                        priority[readyProcess.getProcessId() - 1]--;
                	}
                }

                burstTime[currentProcess.getProcessId() - 1]--;
                NumberofUnitesExecution[currentProcess.getProcessId() - 1]++;
                currentTime++;

                //Check if the process finished it's execution
                if (burstTime[currentProcess.getProcessId() - 1] == 0) {
                    WaitingTime[currentProcess.getProcessId() - 1] = currentTime - NumberofUnitesExecution[currentProcess.getProcessId() - 1] - ArriveToReadyQueue[currentProcess.getProcessId() - 1];
                    TurnAroundTime[currentProcess.getProcessId() - 1] = WaitingTime[currentProcess.getProcessId() - 1] + timeinWaitingQueue[currentProcess.getProcessId() - 1] + currentProcess.getBurstTime();

                    if (currentTime <= 200) {
                        SumWaitingTime += WaitingTime[currentProcess.getProcessId() - 1];
                        SumTurnaroundTime += TurnAroundTime[currentProcess.getProcessId() - 1];
                        ganttChart.add(new GanttChart(currentProcess.getProcessId(), startTime[currentProcess.getProcessId() - 1], currentTime));
                    } else if (currentTime > 200) {
                        ganttChart.add(new GanttChart(currentProcess.getProcessId(), startTime[currentProcess.getProcessId() - 1], 200));
                    }

                    burstTime[currentProcess.getProcessId() - 1] = currentProcess.getBurstTime();
                    NumberofUnitesExecution[currentProcess.getProcessId() - 1] = 0;

                    WaitingQueue.add(currentProcess);

                    priority[currentProcess.getProcessId() - 1] = currentProcess.getPriority();

                    timeinWaitingQueue[currentProcess.getProcessId() - 1] = currentTime - ArriveToReadyQueue[currentProcess.getProcessId() - 1];

                    if (!executedProcess[currentProcess.getProcessId() - 1]) {
                        executedProcess[currentProcess.getProcessId() - 1] = true;
                        numberofExecutionProcesses++;
                    }

                    currentProcess = null;
                }
            }

            for (Iterator<Process> iterator = WaitingQueue.iterator(); iterator.hasNext(); ) {
                Process waitingProcess = iterator.next();
                if (currentTime - TimeSenttoWaitingQueue[waitingProcess.getProcessId() - 1] >= waitingProcess.getComesBackAfter()) {
                    ArriveToReadyQueue[waitingProcess.getProcessId() - 1] = currentTime;
                    timeinWaitingQueue[waitingProcess.getProcessId() - 1] = waitingProcess.getComesBackAfter();
                    ReadyQueue.add(waitingProcess);
                    iterator.remove();
                }
            }

        }
        
        //We need to build the Gantt chart:
        System.out.println("\n#Gantt Chart:");
		System.out.println("\n  Process ID => Start Execution at => CPU Release at");
		System.out.println("-----------------------------------------------------------");
        for(GanttChart value : ganttChart) {
			value.printInfo();
		}
        
        //Calculate average waiting time and average turn around time
        double AverageWaitingTime = (double)SumWaitingTime/numberofExecutionProcesses;
        double AverageTurnaroundTime = (double)SumTurnaroundTime/numberofExecutionProcesses;  
        
        System.out.println("\n#Average Waiting Time = " + AverageWaitingTime);
        System.out.println("\n#Average Turnaround Time = " + AverageTurnaroundTime);
        
	}

}