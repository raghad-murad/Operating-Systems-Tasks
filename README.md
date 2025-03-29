# Operating Systems Projects and Homeworks

This repository contains projects and homework assignments related to operating systems concepts. Each task and homework is organized into separate folders for clarity.

---

## 📂 Structure of the Repository

The repository is structured as follows:

- **`HW`**: Contains homework assignments.
- **`Task 1`**: First project related to process management.
- **`Task 2`**: Second project involving CPU scheduling simulation.
- **`Task 3`**: Third project focusing on deadlock detection.

---

## 📚 Tasks and Homeworks

Below is a detailed overview of each task and homework:

--

### Task 1: Process Management

#### Description:
This task focuses on implementing a program that demonstrates process management using `fork()` system calls and inter-process communication (IPC) via pipes.

#### Files:
- **`Task.docx`**: Detailed task description and requirements.
- **`Task1_1212214.pdf`**: PDF version of the task description.
- **`Task1_Code_1212214.c`**: C source code for the implementation.
- **`Task1_ResultData_1212214.xlsx`**: Excel file containing results and analysis.

#### How to Run:
1. Compile the C code:
   ```bash
   gcc Task1_Code_1212214.c -o task1
   ```
2. Execute the program:
   ```bash
   ./task1
   ```

#### Output:
- The program will display process execution details, IPC communication, and any additional metrics specified in the task.

#### Notes:
- Ensure you have a C compiler installed (e.g., GCC).
- Review the `Task.docx` file for detailed instructions and expected output.

--

### Task 2: CPU Scheduling Simulation

#### Description:
This task involves simulating various CPU scheduling algorithms (FCFS, SJF, SRTF, RR, etc.) and generating Gantt charts for each algorithm.

#### Files:
- **`ENCS3390_project2.pdf`**: Detailed task description and requirements.
- **`CPUSchedulerSimulation.java`**: Java source code for the simulation.
- **`GanttChart.java`**: Java class for generating Gantt charts.
- **`Process.java`**: Java class representing processes.

#### How to Run:
1. Compile the Java code:
   ```bash
   javac CPUSchedulerSimulation.java
   ```
2. Execute the program:
   ```bash
   java CPUSchedulerSimulation
   ```

#### Output:
- The program will simulate different scheduling algorithms and generate Gantt charts and performance metrics (e.g., average waiting time, turnaround time).

#### Notes:
- Ensure you have Java installed on your machine.
- Review the `ENCS3390_project2.pdf` file for detailed instructions and expected output.

--

### Task 3: Deadlock Detection

#### Description:
This task involves detecting deadlocks in a resource allocation system using input files for allocation, request, and available resources.

#### Files:
- **`ENCS3390_project3.docx`**: Detailed task description and requirements.
- **`SystemDeadlockDetection.java`**: Java source code for deadlock detection.
- **CSV Files**:
  - `Allocation.csv`, `Allocation2.csv`, etc.: Allocation matrices.
  - `Available.csv`, `Available2.csv`, etc.: Available resources.
  - `Request.csv`, `Request2.csv`, etc.: Request matrices.

#### How to Run:
1. Compile the Java code:
   ```bash
   javac SystemDeadlockDetection.java
   ```
2. Execute the program with input files:
   ```bash
   java SystemDeadlockDetection Allocation.csv Available.csv Request.csv
   ```

#### Output:
- The program will detect whether a deadlock exists and list the processes involved if a deadlock is found.

#### Notes:
- Ensure you have Java installed on your machine.
- Input files should be provided in the correct format as specified in the task description.

--

### HW 1: Basic Concepts

#### Description:
This homework covers basic concepts in operating systems, including comparisons between Android and iOS, AOT vs. JIT compilation, native vs. cross-platform applications, and an explanation of Google Chrome's multi-process architecture.

#### Files:
- **`HW1_1212214.docx`**: Word document containing the solution.
- **`HW1_1212214.pdf`**: PDF version of the solution.

#### How to View:
- Open the `.docx` or `.pdf` file to view the solution.

#### Notes:
- This homework is primarily theoretical and does not require running any code.

--

### HW 2: CPU Scheduling Analysis

#### Description:
This homework involves analyzing and comparing different CPU scheduling algorithms (FCFS, SJF, SRTF, RR, etc.) using a set of processes with given arrival times, burst times, and priorities.

#### Files:
- **`HW2_1212214.docx`**: Word document containing the solution.
- **`HW2_1212214.pdf`**: PDF version of the solution.

#### How to View:
- Open the `.docx` or `.pdf` file to view the solution.

#### Notes:
- This homework includes calculations and explanations for each scheduling algorithm.

---

## 🛠️ Prerequisites

To run the programs in this repository, ensure you have the following installed:

- **C Compiler**: Required for Task 1 (`gcc` recommended).
- **Java Development Kit (JDK)**: Required for Tasks 2 and 3.

## 🚀 How to Use This Repository

1. Clone the repository:
   ```bash
   git clone https://github.com/raghad-murad/Operating-Systems-Tasks.git
   ```
2. Navigate to the desired folder:
   ```bash
   cd Operating-Systems-Tasks/<folder-name>
   ```
3. Follow the instructions in the respective section to compile and run the code or view the solutions.

---

## 🤝 Contributions

If you'd like to contribute to this repository, feel free to:
1. Fork the repository.
2. Create a new branch for your changes.
3. Submit a pull request with a detailed explanation of your changes.

---

## 📧 Contact

If you have any questions or suggestions, feel free to reach out!

- **Email:** raghadmbuzia@gmail.com
- **LinkedIn:** [in/raghad-murad](http://linkedin.com/in/raghad-murad-02690433a)

---

### Thank you for checking out this project! 🚀
