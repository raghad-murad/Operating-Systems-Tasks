package OS_ThirdTask;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class  SystemDeadlockDetection {

    public static void main(String[] args) {

        // Read file names from the user
        String allocationFile = getInputFileName("Allocation");
        String requestFile = getInputFileName("Request");
        String availableFile = getInputFileName("Available");

        // Path of all files
        String vectorPath = "C:\\Users\\Lenovo\\Desktop\\New\\Task3\\Task33";

        // Construct full path for the CSV files
        String AllocationPath = allocationFile;
        String RequestPath = requestFile;
        String AvailablePath = availableFile;

        // Read and verify Allocation matrix
        int[][] allocationMatrix = readMatrix(AllocationPath);
        if (allocationMatrix == null) {
            System.out.println("Error reading Allocation matrix.");
            return;
        }
        int allocationRows = allocationMatrix.length;
        int allocationCols = allocationMatrix[0].length;

        // Read and verify Request matrix
        int[][] requestMatrix = readMatrix(RequestPath);
        if (requestMatrix == null) {
            System.out.println("Error reading Request matrix.");
            return;
        }
        int requestRows = requestMatrix.length;
        int requestCols = requestMatrix[0].length;

        // Read and verify Available vector
        int[] availableVector = readVector(AvailablePath);
        if (availableVector == null) {
            System.out.println("Error reading Available vector.");
            return;
        }
        int availableCols = availableVector.length;

        if (allocationCols != requestCols || allocationCols != availableCols) { // Check if dimensions are consistent
            System.out.println("Error: Dimensions of input files are not consistent.");
            return;
        }

        int rows = allocationMatrix.length;
        int cols = allocationMatrix[0].length;
        
        // Print the dimensions
        System.out.println("Allocation matrix dimensions: " + allocationRows + "x" + allocationCols);
        System.out.println("Request matrix dimensions: " + requestRows + "x" + requestCols);
        System.out.println("Available vector dimensions: " + availableCols);
        
       detection(allocationMatrix, requestMatrix, availableVector, rows, cols);

    }

    private static String getInputFileName(String fileType) {
        System.out.println("Enter the name of the " + fileType + " file:");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static int[][] readMatrix(String filename) {

        BufferedReader reader = null;
        String line = "";
        List<String> lines = new ArrayList<>();
        int rows = 0;
        int cols = 0;

        try {
            reader = new BufferedReader(new FileReader(filename));

            // Determine the number of rows and columns in the matrix
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                rows++;
                lines.add(line); // Split by comma
                cols = Math.max(cols, line.split(",").length); // Calculate the maximum number of columns
            }

            // Reset the file reader
           // reader.close();
             // reader = new BufferedReader(new FileReader(filename)); // Reset reader

            

            

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
     // Create the matrix
        int[][] matrix = new int[rows-1][cols-1];

        // Read the matrix values
        // int currentRow = 0;
        for(int i = 1 ;i < rows ; i++) {
            //if (line.trim().isEmpty()) continue; // Skip empty lines
           // String[] values = line.split(",");
            String [] values  = lines.get(i).split(",");
            for (int j = 1; j < cols; j++) { // Start from index 1 to skip the first element
                matrix[i-1][j - 1] = Integer.parseInt(values[j]);
            }
           // currentRow++;
        }
        
        return matrix;
    }

    private static int[] readVector(String filename) {
    	BufferedReader reader = null;
        String line = "";
        List<String> lines = new ArrayList<>();
        int cols = 0;
        int[] vector = {0}; // Exclude the first element (header)

        try {
            reader = new BufferedReader(new FileReader(filename));

            // Determine the number of rows and columns in the matrix
            while ((line = reader.readLine()) != null) {
            	line = reader.readLine();
                if (line.trim().isEmpty()) continue; // Skip empty lines
                cols = Math.max(cols, line.split(",").length); // Calculate the maximum number of columns
                String [] values = line.split(",");
            	vector = new int [values.length];
                for (int j = 0; j < values.length; j++) {
                	vector[j] = Integer.parseInt(values[j].trim());
                }
                
                
            }

            // Reset the file reader
           // reader.close();
             // reader = new BufferedReader(new FileReader(filename)); // Reset reader

            

            

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return vector;
        
    }
    
private static void detection (int[][] allocation, int[][] request, int[] available, int rows, int cols) {
    	
    	//Array list to store the process that satisfied (No deadlock)
        ArrayList<Integer> noDeadlockProcess = new ArrayList<>();
        
    	 //Initializing work and finish arrays
    	boolean[] finish = new boolean[rows];
        int[] work = new int[cols];
        
        boolean deadlockChecker = true;
        
        for (int i = 0; i < cols; i++) {
            work[i] = available[i];
        }

        //checking if there is a process that can be satisfied the conditions
        for (int i = 0; i < rows; i++) {
            if (!finish[i]) {
                boolean satisfied = true;
                for (int j = 0; j < cols; j++) {
                    if (request[i][j] > work[j]) {
                    	satisfied = false;
                    	deadlockChecker = true;
                        break;
                    }
                }
                if (satisfied) {
                	
                    finish[i] = true;
                    deadlockChecker = false;
                    for (int j = 0; j < cols; j++) {
                        work[j] += allocation[i][j];
                    }
                    noDeadlockProcess.add(i); 
                    
                    
                    i = -1; //return to the loop to check if there is a new processes 
                }
            }
        }
        
      //listing the processes that are in deadlocked state
        if(deadlockChecker) {
        	System.out.println("\nThe System is deadlocked <Unsafe state> \n");
        	System.out.println("\nProcesses that are in  deadlock state:");
        	System.out.print("          ");
        	for(int i = 1; i <= rows; i++) { 
        		if(!finish[i-1]) {
        			System.out.printf(" P%d ,", i);
        		}
        	}
        			}
        else {
        	System.out.println("\nThe System is not in deadlocked <Safe state> ");
        	
        	//part 4 -> to show a series of process executions that are possible without deadlock
        	System.out.println("\nThe sequence for the system which is in the safe state:\n\n");
        	System.out.print("          ");
        	for(int process : noDeadlockProcess) {
        			System.out.printf(" P%d , ", process+1);
        	}
        	}
        System.out.println("\n");
    }

}