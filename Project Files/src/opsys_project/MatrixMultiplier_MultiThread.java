/* Emre Erdoğan - 21COMP1007
   Oğuzhan Önder - 22COMP1007
 */
 
package opsys_project;

//imports here
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MatrixMultiplier_MultiThread{
    private static int MATRIX_SIZE;
    private static int NUM_THREADS;
    
    private static int[][] matrixB;
    
    private static int[][] resultMatrix;

	private static int gapBetweenRows;
	private static int currentRowforThreads;
	private static long duration;

	// Any static members defined here
	
    public static void main(String[] args) throws Exception{
        Scanner scanner = new Scanner(System.in);

        // Get the file path
        System.out.print("Enter the matrix input file path: ");
        String filePath = scanner.nextLine();
        
        // Get the matrix size
        System.out.print("Enter the matrix size (e.g., 1000 for 1000x1000): ");
        MATRIX_SIZE = scanner.nextInt();
        
        // Get the number of threads
        System.out.print("Enter the number of threads: ");
        NUM_THREADS = scanner.nextInt();

		resultMatrix = new int[MATRIX_SIZE][MATRIX_SIZE];
		gapBetweenRows = MATRIX_SIZE / NUM_THREADS;
		currentRowforThreads = 0;

        // Implement multi-threaded matrix multiplication logic here
		try {
			loadMatrixB(filePath);
		} catch (Exception e) {
			System.out.println("IOException catched!");
		}
		
		long startTime = (int) System.currentTimeMillis();

		//Make a for loop here to create threads		
        //Thread here
		//CurrentRowforThreads = currentRowforThreads + gapBetweenRows - 1 (check notebook);

		Thread[] threadList = new Thread[NUM_THREADS];
		for (int i = 0; i < NUM_THREADS; i++) {
			int temp = Math.min((currentRowforThreads + gapBetweenRows - 1),MATRIX_SIZE);
			Thread currentThread = new Thread(new Worker(filePath,matrixB,resultMatrix,currentRowforThreads,temp));
			threadList[i] = currentThread;
			currentRowforThreads = Math.min(temp + 1, MATRIX_SIZE);
			currentThread.start();
		}

		for (int i = 0; i < NUM_THREADS; i++) {
    		threadList[i].join();
		}

		duration = (int) System.currentTimeMillis() - startTime;
		scanner.close();

        // Report duration
        System.out.println("Matrix multiplication completed.");
        System.out.println("Time taken (multithreaded): " + duration + " ms");
    }

	
	public static void loadMatrixB(String filePath) throws IOException {
	    matrixB = new int[MATRIX_SIZE][MATRIX_SIZE];
	    BufferedReader reader = new BufferedReader(new FileReader(filePath));
	    String line;
	    boolean foundMatrixB = false;
	    int row = 0;

		line = reader.readLine();
	    while (line != null) {
	        if (!foundMatrixB) {
	            if (line.equals("Matrix B 1000x1000:")) {
	                foundMatrixB = true;
	            }
	        } else {
	            if (row < MATRIX_SIZE && !line.isEmpty()) {
	                String[] values = line.split("\\s+");
	                for (int col = 0; col < MATRIX_SIZE && col < values.length; col++) {
	                    matrixB[row][col] = Integer.parseInt(values[col]);
	                }
	                row++;
	            }
	            if (row >= MATRIX_SIZE) break;
	        }
			line = reader.readLine();
	    }
	    reader.close();
	}

	//Inner class is required
	static class Worker implements Runnable {
		// Fill with necessary logic for each thread
		int[][] matrixA;
		int[][] matrixB;
		int[][] resultMatrix;
		String filePath;
		int start;
		int end;

		public Worker(String filePath, int[][] matrixB, int[][] resultMatrix, int start, int end) {
			this.filePath = filePath;
			this.matrixB = matrixB;
			this.start = start;
			this.end = end;
			this.resultMatrix = resultMatrix;
		}

		@Override
		public void run() {
			try{
			matrixA = readMatrixARows(filePath,start,end);
			}
			catch (Exception e) {
				System.out.println("IO Exception thrown");
			}
			for(int y = 0; y < matrixA.length; y++) { //Okay, so this is for the Y dimention of Matrix A
				for(int x = 0; x < MATRIX_SIZE; x++) { //And this is the X dimention of Matrix A
					for(int z = 0; z < MATRIX_SIZE; z++) { //And this is for the Y dimention of Matrix B
						resultMatrix[start + y][x] += (matrixA[y][z] * matrixB[z][x]); //Matrix multiplication
					} //Phew, this was somehow more confusing to implement than I thought, and finally: the threads.
				}
			}	
		}



		public int[][] readMatrixARows(String filePath, int start, int end) throws IOException {
		    int numRows = end - start + 1;
		    int[][] MatrixA = new int[numRows][MATRIX_SIZE];
		    BufferedReader reader = new BufferedReader(new FileReader(filePath));
		    String line;
		    boolean foundMatrixA = false;
		    int row = 0;
		    int MatrixARows = 0;

			line = reader.readLine();
		    while (line != null) {
		        if (!foundMatrixA) {
		            if (line.equals("Matrix A 1000x1000:")) {
		                foundMatrixA = true;
		            }
		        } else {
		            if (row >= start && row <= end) {
		                String[] values = line.split("\\s+");
		                for (int col = 0; col < MATRIX_SIZE && col < values.length; col++) {
		                    MatrixA[MatrixARows][col] = Integer.parseInt(values[col]);
		                }
		                MatrixARows++;
		            }
		            row++;
		            if (row > end) break;
		        }
				line = reader.readLine();
		    }
		    reader.close();
		    return MatrixA;
		}
	}
}
