/* Emre Erdoğan - 21COMP1007
   Oğuzhan Önder - 22COMP1007
 */
 
package opsys_project;

//imports here
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MatrixMultiplier_SingleThread {
    // Any static members defined here
    private static int MATRIX_SIZE;

    private static int[][] matrixA;
    private static int[][] matrixB;
    private static int[][] resultMatrix;

	  private static long duration;    

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        
        // Get the file path
        System.out.print("Enter the matrix input file path: ");
        String filePath = scanner.nextLine();
        
        // Get the matrix size
        System.out.print("Enter the matrix size (e.g., 1000 for 1000x1000): ");
        MATRIX_SIZE = scanner.nextInt();

        // Start timer for multiplication
        long startTime = (int) System.currentTimeMillis();

		matrixA = new int[MATRIX_SIZE][MATRIX_SIZE];
		matrixB = new int[MATRIX_SIZE][MATRIX_SIZE];

		try {
			loadMatrixA(filePath);
			loadMatrixB(filePath);
		} catch (Exception e) {
			System.out.println("IOException caught!");
		}

        // Perform matrix multiplication
        resultMatrix = new int[MATRIX_SIZE][MATRIX_SIZE];
          for (int y = 0; y < MATRIX_SIZE; y++) {
            for (int x = 0; x < MATRIX_SIZE; x++) {
                for (int k = 0; k < MATRIX_SIZE; k++) {
                    resultMatrix[y][x] += matrixA[y][k] * matrixB[k][x];
                }
            }
        }

        // End timer and calculate duration
        duration = (int) System.currentTimeMillis() - startTime;

        // Report duration
        System.out.println("Matrix multiplication completed (single-threaded).");
        System.out.println("Time taken (single-threaded): " + duration + " ms");
    }

    public static void loadMatrixA(String filePath) throws IOException {
		matrixA = new int[MATRIX_SIZE][MATRIX_SIZE];
	    BufferedReader reader = new BufferedReader(new FileReader(filePath));
	    String line;
	    boolean foundMatrixA = false;
	    int row = 0;

		line = reader.readLine();
	    while (line != null) {
	        if (!foundMatrixA) {
	            if (line.equals("Matrix A 1000x1000:")) {
	                foundMatrixA = true;
	            }
	        } else {
	            if (row < MATRIX_SIZE && !line.isEmpty()) {
	                String[] values = line.split("\\s+");
	                for (int col = 0; col < MATRIX_SIZE && col < values.length; col++) {
	                    matrixA[row][col] = Integer.parseInt(values[col]);
	                }
	                row++;
	            }
	            if (row >= MATRIX_SIZE) break;
	        }
			line = reader.readLine();
	    }
	    reader.close();
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
}