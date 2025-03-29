#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/wait.h>

//Name: Raghad Murad Buzia ID: 1212214 Section: 1

#define MATRIX_SIZE 100
#define CHILDPROCESSES_NUMBER 10
#define THREADS_NUMBER 10


//Define three matrices (A, B, and Result)
int matrixA[MATRIX_SIZE][MATRIX_SIZE]={0};
int matrixB[MATRIX_SIZE][MATRIX_SIZE]={0};
int matrixResult1[MATRIX_SIZE][MATRIX_SIZE]={0};
int matrixResult2[MATRIX_SIZE][MATRIX_SIZE]={0};
int matrixResult3[MATRIX_SIZE][MATRIX_SIZE]={0};
int matrixResult4[MATRIX_SIZE][MATRIX_SIZE]={0};
pthread_mutex_t mutex;


void GenerateMatrices(int student_ID[], int student_IDXBirthYear[]);
void PrintMateix(int matrix[MATRIX_SIZE][MATRIX_SIZE]);
void NaiveApproach ();
void MultipleChildPocesses();
void MultipleJoinableThreads();
void* JoinableThreadMultiplyMatrices(void* arg);
void MultipleDetachedThreads();
void* DetachedThreadMultiplyMatrices(void* arg);


int main()
{

    //Define the repeating pattern ( (student id) and (student birth year  * student id) )
    int student_ID[] = {1, 2, 1, 2, 2, 1, 4};
    int student_IDXBirthYear[] = {2,4,2,8,0,6,4,6,4,2};

    //Generate the matrices A & B
    GenerateMatrices(student_ID,student_IDXBirthYear);
    //PrintMateix(matrixA);
    //printf("\n\n");
    //PrintMateix(matrixB);
    //printf("\n\n");

    /* Part One: The Naive Approach */
    NaiveApproach();

    /* Part Three: Multiple Joinable Threads Running in Parallel */
    MultipleJoinableThreads();

    /* Part four: Multiple Detached Threads Running in Parallel */
    MultipleDetachedThreads();

    /* Part Two: Multiple Child Pocesses Running in Parallel */
    MultipleChildPocesses();

    return 0;
}



//Functon to generate\create two matrices
void GenerateMatrices(int student_ID[], int student_IDXBirthYear[]) {

    // Fill the matrices with the repeating of student_ID and student_IDXBirthYear
    for (int i = 0; i < MATRIX_SIZE; ++i) {
        for (int j = 0; j < MATRIX_SIZE; ++j) {
            matrixA[i][j] = student_ID[j % 7];
            matrixB[i][j] = student_IDXBirthYear[j % 10];
        }
    }

}



//Function to prints the matrix elements
void PrintMateix(int matrix [MATRIX_SIZE][MATRIX_SIZE]) {

      for(int i=0; i < MATRIX_SIZE;i++){
        for(int j=0; j < MATRIX_SIZE;j++){
           printf("%d\t",matrix[i][j]);
        }
        printf("\n");
      }

}



//Function to perform part one
void NaiveApproach() {

    /* Part One: The Naive Approach */

    clock_t start_time = clock();

    //Perform the matrices multiply (Result = A*B)
    for (int i = 0; i < MATRIX_SIZE; i++) {
        for (int j = 0; j < MATRIX_SIZE; j++) {
            matrixResult1[i][j] = 0;
            for (int k = 0; k < MATRIX_SIZE; k++) {
                matrixResult1[i][j] += matrixA[i][k] * matrixB[k][j];
            }
        }
    }

    //Print the elements of the result matrix
    //printf("\nThe result matrix:\n");
    //PrintMateix(matrixResult1);

    clock_t end_time = clock();

    double execution_time = ((double)(end_time - start_time)) / CLOCKS_PER_SEC;
    double throughput=(1/execution_time);

    printf("#The time taken for The Naive Approach : %f secondss.\n", execution_time);
    printf("#Throughput : %f secondss.\n", throughput);

}



//Function to perform part two
void MultipleChildPocesses() {

    /* Part Two: Multiple Child Pocesses Running in Parallel */

    clock_t start_time = clock();

    int PipesDescriptors[CHILDPROCESSES_NUMBER][2];

    for (int i = 0; i < CHILDPROCESSES_NUMBER; ++i) {

        if (pipe(PipesDescriptors[i]) == -1) {
            printf("#Warning: An error ocurred with opening the pipe\n");
            return 1;
        }

        int Process_pid = fork();

        //if fork function return 0 ( pid = 0 ) then this peocess is a chiled process
        if (Process_pid == 0) {

            //Child process code

            //PipesDescriptors[i][0] - read end (Close read end in child process)
            close(PipesDescriptors[i][0]);
            //Perform the matrices multiply (Result = A*B)
            for (int i = 0; i < MATRIX_SIZE; i++) {
                for (int j = 0; j < MATRIX_SIZE; j++) {
                    matrixResult2[i][j] = 0;
                    for (int k = 0; k < MATRIX_SIZE; k++) {
                        matrixResult2[i][j] += matrixA[i][k] * matrixB[k][j];
                    }
                }
            }
            //PipesDescriptors[i][1] - write end
            if(write(PipesDescriptors[i][1], matrixResult2, sizeof(matrixResult2)) == -1){
                printf("#Warning: An error ocurred writing to the pipe.\n");
                return 2;
            }
            //Close write end in child process
            close(PipesDescriptors[i][1]);
            return 3;
        }

        //if fork function returns -1 ( pid = -1 ) then there is an error with fork
        else if (Process_pid == -1) {
            printf("#Warning: An error ocurred with fork.\n");
            return 4;
        }

        //if fork functin neither return 0 nor -1 then its a parent process
        else {
            //Close write end in parent process
            close(PipesDescriptors[i][1]);
        }
    }


    //Parent Process reads results from each child process
    for (int j = 0; j < CHILDPROCESSES_NUMBER; ++j) {
        if(read(PipesDescriptors[j][0], matrixResult2, sizeof(matrixResult2)) == -1){
            printf("#Warning: An error ocurred reading from the pipe.\n");
            return 5;
        }
        //Close read end in parent process
        close(PipesDescriptors[j][0]);
        wait(NULL);
    }

    //Print the elements of the result matrix
    //printf("\n\nThe result matrix:\n");
    //PrintMateix(matrixResult2);

    clock_t end_time = clock();
    double execution_time = ((double)(end_time - start_time)) / CLOCKS_PER_SEC;
    double throughput=(1/execution_time);

    printf("\n**********************************************************************************\n\n");
    printf("#The time taken for Multiple Child Processes (Processes Number is %d) : %f seconds.\n", CHILDPROCESSES_NUMBER, execution_time);
    printf("#Throughput : %f secondss.\n", throughput);

}



//Function to perform part three
void MultipleJoinableThreads() {

    /* Part Three: Multiple Joinable Threads Running in Parallel */

    clock_t start_time = clock();

    pthread_t Threads[THREADS_NUMBER];

    for (int i = 0; i < THREADS_NUMBER; i++) {

         if (pthread_create(&Threads[i], NULL, JoinableThreadMultiplyMatrices, (void*)matrixResult3) != 0) {
            printf("#Warning: An error ocurred with creating joinable thread\n");
            return 1;
        }
    }


    for (int i = 0; i < THREADS_NUMBER; i++) {
        if (pthread_join(Threads[i], NULL) != 0) {
            printf("#Warning: An error ocurred with joinning thread\n");
            return 2;
        }
    }

    //Print the elements of the result matrix
    //printf("\nThe result matrix:\n");
    //PrintMateix(matrixResult3);

    clock_t end_time = clock();
    double execution_time = ((double)(end_time - start_time)) / CLOCKS_PER_SEC;
    double throughput=(1/execution_time);

    printf("\n**********************************************************************************\n\n");
    printf("#The time taken for Multiple Joinable Threads (Threads Number is %d) : %f seconds.\n", THREADS_NUMBER, execution_time);
    printf("#Throughput : %f secondss.\n", throughput);

}



//Function to perform the matrices multiple in the threads parts
void* JoinableThreadMultiplyMatrices(void* args) {

    int* Thread = (int*) args;
    int START = Thread[0];
    int END = Thread[1];

    //Perform the matrices multiply (Result = A*B)
    for (int i = START; i < END; i++) {
        for (int j = 0; j < MATRIX_SIZE; j++) {
            matrixResult3[i][j] = 0;
            for (int k = 0; k < MATRIX_SIZE; k++) {
                matrixResult3[i][j] += matrixA[i][k] * matrixB[k][j];
            }
        }
    }

    pthread_exit(NULL);
}



//Function to perform part four
void MultipleDetachedThreads() {

    /* Part Three: Multiple Detached Threads Running in Parallel */

    pthread_t Threads[THREADS_NUMBER];
    int ThreadIDs[THREADS_NUMBER];

    pthread_mutex_init(&mutex, NULL);

     clock_t start_time = clock();

    for (int i = 0; i < THREADS_NUMBER; i++) {
        ThreadIDs[i] = i;
        if (pthread_create(&Threads[i], NULL, DetachedThreadMultiplyMatrices, (void*)&ThreadIDs[i]) != 0) {
            printf("#Warning: An error ocurred with creating Detached thread\n");
            return 1;
        }
        pthread_detach(Threads);
    }

    pthread_mutex_destroy(&mutex);

    //Some time for detached threads to complete
    sleep(0.5);

    //Print the elements of the result matrix
    //printf("\nThe result matrix:\n");
    //PrintMateix(matrixResult4);

    clock_t end_time = clock();
    double execution_time = ((double)(end_time - start_time)) / CLOCKS_PER_SEC;
    double throughput=(1/execution_time);

    printf("\n**********************************************************************************\n\n");
    printf("#The time taken for Multiple Detached Threads (Threads Number is %d) : %f seconds.\n", THREADS_NUMBER, execution_time);
    printf("#Throughput : %f secondss.\n", throughput);

}




//Function to perform the matrices multiple in the threads parts
void* DetachedThreadMultiplyMatrices(void* args) {

    int ThreadID = *((int*)args);

    int START = ThreadID * (MATRIX_SIZE / THREADS_NUMBER);
    int END = (ThreadID + 1) * (MATRIX_SIZE / THREADS_NUMBER);

    pthread_mutex_lock(&mutex);

    //Perform the matrices multiply (Result = A*B)
    for (int i = START; i < END; i++) {
        for (int j = 0; j < MATRIX_SIZE; j++) {
            matrixResult4[i][j] = 0;
            for (int k = 0; k < MATRIX_SIZE; k++) {
                matrixResult4[i][j] += matrixA[i][k] * matrixB[k][j];
            }
        }
    }

    pthread_mutex_unlock(&mutex);
    pthread_exit(NULL);
}
