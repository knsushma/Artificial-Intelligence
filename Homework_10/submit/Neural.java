import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Neural {
	
	public static void main(String[] args) {
				
		if (args.length < 10) {
			System.out.println("Please enter valid arguments and <flag> <weights> <optional arbitrary arguments>");
				return;
		}
		int flag = Integer.parseInt(args[0]);
		
		double[] w = new double[9];
		for (int i=0; i<9; i++) {
			w[i] = Double.parseDouble(args[i+1]);
		}
		
		if (flag == 100) {
			double x1 = Double.parseDouble(args[10]);
			double x2 = Double.parseDouble(args[11]);
			
			double input_a = 1*w[0] + x1*w[1] + x2*w[2];
			double input_b = 1*w[3] + x2*w[5] + x1*w[4];		
			double output_a = Math.max(input_a, 0);
			double output_b = Math.max(input_b, 0);
			
			double input_c = output_a*w[7] + output_b*w[8] + 1*w[6];
			double output_c = 1 / (1+Math.exp(-input_c));
			
			System.out.println(print(input_a) + " " + print(output_a) + " " + print(input_b) + " " + print(output_b) + " " + print(input_c) + " " + print(output_c));
		
		} else if (flag == 200) {
			double x1 = Double.parseDouble(args[10]);
			double x2 = Double.parseDouble(args[11]);
			double y = Double.parseDouble(args[12]);
			
			double output_c = neuralC(neuralA(x1, x2, w), neuralB(x1, x2, w), w);
			double neuralError = 1.0/2*Math.pow(output_c-y, 2);
			double partialDerivativeWRTOutput = output_c - y;
			double partialDerivativeWRTInput = partialDerivativeWRTOutput * output_c * (1 - output_c);
			
			System.out.println(print(neuralError) + " " + print(partialDerivativeWRTOutput) + " " + print(partialDerivativeWRTInput));
			
		} else if (flag == 300) {
			double x1 = Double.parseDouble(args[10]);
			double x2 = Double.parseDouble(args[11]);		
			double y = Double.parseDouble(args[12]);
			
			double partialDerivativeWRTInput = partialDerivitiveC(x1, x2, y, w);			
			double partialDerivativeWRTOutputNeuralA = w[7] * partialDerivativeWRTInput;
			double partialDerivativeWRTOutputNeuralB = w[8] * partialDerivativeWRTInput;
			
			double partialDerivativeWRTInputNeuralA = partialDerivitiveA(x1, x2, y, w, partialDerivativeWRTInput);
			double partialDerivativeWRTInputNeuralB = partialDerivitiveB(x1, x2, y, w, partialDerivativeWRTInput);
			
			System.out.println(print(partialDerivativeWRTOutputNeuralA) + " " + print(partialDerivativeWRTInputNeuralA) + " " + print(partialDerivativeWRTOutputNeuralB) + " " + print(partialDerivativeWRTInputNeuralB) );
		
		} else if (flag == 400) {
			double x1 = Double.parseDouble(args[10]);
			double x2 = Double.parseDouble(args[11]);			
			double y = Double.parseDouble(args[12]);
			double neural_a = neuralA(x1, x2, w);
			double neural_b = neuralB(x1, x2, w);
			
			double partialDerivativeWRTInput = partialDerivitiveCWithNeuralAB(neural_a, neural_b, y, w);
			double partialDerivativeWRTInputNeuralA = partialDerivitiveA(x1, x2, y, w, partialDerivativeWRTInput);
			double partialDerivativeWRTInputNeuralB = partialDerivitiveB(x1, x2, y, w, partialDerivativeWRTInput);
			
			double w1 = partialDerivativeWRTInputNeuralA;
			double w2 = x1 * partialDerivativeWRTInputNeuralA;
			double w3 = x2 * partialDerivativeWRTInputNeuralA;
			double w4 = partialDerivativeWRTInputNeuralB;
			double w5 = x1 * partialDerivativeWRTInputNeuralB;
			double w6 = x2 * partialDerivativeWRTInputNeuralB;
			double w7 = partialDerivativeWRTInput;
			double w8 = neural_a * partialDerivativeWRTInput;
			double w9 = neural_b * partialDerivativeWRTInput;
			
			System.out.println(print(w1) + " " + print(w2) + " " + print(w3) + " " + print(w4) + " " + print(w5) + " " + print(w6) + " " + print(w7) + " " + print(w8) + " " + print(w9) );
			
		} else if (flag == 500) {
			double x1 = Double.parseDouble(args[10]);
			double x2 = Double.parseDouble(args[11]);			
			double y = Double.parseDouble(args[12]);
			double eta = Double.parseDouble(args[13]);
			double neural_a = neuralA(x1, x2, w);
			double neural_b = neuralB(x1, x2, w);
			
			double partialDerivativeWRTInput = partialDerivitiveCWithNeuralAB(neural_a, neural_b, y, w);
			double partialDerivativeWRTInputNeuralA = partialDerivitiveA(x1, x2, y, w, partialDerivativeWRTInput);
			double partialDerivativeWRTInputNeuralB = partialDerivitiveB(x1, x2, y, w, partialDerivativeWRTInput);
			
					
			double neuralErrorBefore = 1.0/2*Math.pow(neuralC(neural_a, neural_b, w)-y, 2);
			System.out.println(print(w[0]) + " " + print(w[1]) + " " + print(w[2]) + " " + print(w[3]) + " " + print(w[4]) + " " + print(w[5]) + " " + print(w[6]) + " " + print(w[7]) + " " + print(w[8]) );
			System.out.println(print(neuralErrorBefore));
			
			double w1 = w[0] - eta * partialDerivativeWRTInputNeuralA;
			double w2 = w[1] - eta * x1 * partialDerivativeWRTInputNeuralA;
			double w3 = w[2] - eta * x2 * partialDerivativeWRTInputNeuralA;
			double w4 = w[3] - eta * partialDerivativeWRTInputNeuralB;
			double w5 = w[4] - eta * x1 * partialDerivativeWRTInputNeuralB;
			double w6 = w[5] - eta * x2 * partialDerivativeWRTInputNeuralB;
			double w7 = w[6] - eta * partialDerivativeWRTInput;
			double w8 = w[7] - eta * neural_a * partialDerivativeWRTInput;
			double w9 = w[8] - eta * neural_b * partialDerivativeWRTInput;
			
			double[] wNew = new double[]{w1, w2, w3, w4, w5, w6, w7, w8, w9};
			double neuralErrorAfter = 1.0/2*Math.pow(neuralC(neuralA(x1, x2, wNew), neuralB(x1, x2, wNew), wNew)-y, 2);
			System.out.println(print(w1) + " " + print(w2) + " " + print(w3) + " " + print(w4) + " " + print(w5) + " " + print(w6) + " " + print(w7) + " " + print(w8) + " " + print(w9) );
			System.out.println(print(neuralErrorAfter));
			
		} else if (flag == 600 ) {
			String trainingFilename = "./hw2_midterm_A_train.txt";
			String evaluationFilename = "./hw2_midterm_A_eval.txt";		
			ArrayList<String> trainingData = readNeuralData(trainingFilename);
			ArrayList<String> evaluationData = readNeuralData(evaluationFilename);		
			double eta = Double.parseDouble(args[10]);
			
			for (String tData : trainingData) {				
				String[] tValues = tData.split(" ");
				double x1 = Double.parseDouble(tValues[0]);
				double x2 = Double.parseDouble(tValues[1]);
				double y = Double.parseDouble(tValues[2]);
				double neural_a = neuralA(x1,x2,w);
				double neural_b = neuralB(x1,x2,w);
				
				double partialDerivativeWRTInput = partialDerivitiveCWithNeuralAB(neural_a, neural_b, y, w);
				double partialDerivativeWRTInputNeuralA = partialDerivitiveA(x1, x2, y, w, partialDerivativeWRTInput);
				double partialDerivativeWRTInputNeuralB = partialDerivitiveB(x1, x2, y, w, partialDerivativeWRTInput);
								
				w[0] = w[0] - eta * partialDerivativeWRTInputNeuralA;
				w[1] = w[1] - eta * (x1 * partialDerivativeWRTInputNeuralA);
				w[2] = w[2] - eta * (x2 * partialDerivativeWRTInputNeuralA);
				w[3] = w[3] - eta * partialDerivativeWRTInputNeuralB;
				w[4] = w[4] - eta * (x1 * partialDerivativeWRTInputNeuralB);
				w[5] = w[5] - eta * (x2 * partialDerivativeWRTInputNeuralB);
				w[6] = w[6] - eta * partialDerivativeWRTInput;
				w[7] = w[7] - eta * neural_a * partialDerivativeWRTInput;
				w[8] = w[8] - eta * neural_b * partialDerivativeWRTInput;
				
				double result = 0.0;
				for (String eData : evaluationData) {
					String[] eValues = eData.split(" ");
					double ex1 = Double.parseDouble(eValues[0]);
					double ex2 = Double.parseDouble(eValues[1]);
					double ey = Double.parseDouble(eValues[2]);
					double neuralError = 1.0/2*Math.pow(neuralC(neuralA(ex1, ex2, w), neuralB(ex1, ex2, w), w)-ey, 2);	
					result += neuralError;	
				}
				
				System.out.println(print(x1) + " " + print(x2) + " " + print(y));
				System.out.println(print(w[0]) + " " + print(w[1]) + " " + print(w[2]) + " " + print(w[3]) + " " + print(w[4]) + " " + print(w[5]) + " " + print(w[6]) + " " + print(w[7]) + " " + print(w[8]) );
				System.out.println(print(result));
				
			}	
			
		} else if (flag == 700) {
			String trainingFilename = "./hw2_midterm_A_train.txt";
			String evaluationFilename = "./hw2_midterm_A_eval.txt";			
			ArrayList<String> trainingData = readNeuralData(trainingFilename);
			ArrayList<String> evaluationData = readNeuralData(evaluationFilename);			
			double eta = Double.parseDouble(args[10]);
			double epoch = Double.parseDouble(args[11]);
			
			for (int i=0; i<epoch; i++) {
				double result = 0.0;
				for (String tData : trainingData) {
					
					String[] tValues = tData.split(" ");
					double x1 = Double.parseDouble(tValues[0]);
					double x2 = Double.parseDouble(tValues[1]);
					double y = Double.parseDouble(tValues[2]);
					double neural_a = neuralA(x1,x2,w);
					double neural_b = neuralB(x1,x2,w);
					
					double partialDerivativeWRTInput = partialDerivitiveCWithNeuralAB(neural_a, neural_b, y, w);
					double partialDerivativeWRTInputNeuralA = partialDerivitiveA(x1, x2, y, w, partialDerivativeWRTInput);
					double partialDerivativeWRTInputNeuralB = partialDerivitiveB(x1, x2, y, w, partialDerivativeWRTInput);
					
					w[0] = w[0] - eta * partialDerivativeWRTInputNeuralA;
					w[1] = w[1] - eta * (x1 * partialDerivativeWRTInputNeuralA);
					w[2] = w[2] - eta * (x2 * partialDerivativeWRTInputNeuralA);
					w[3] = w[3] - eta * partialDerivativeWRTInputNeuralB;
					w[4] = w[4] - eta * (x1 * partialDerivativeWRTInputNeuralB);
					w[5] = w[5] - eta * (x2 * partialDerivativeWRTInputNeuralB);
					w[6] = w[6] - eta * partialDerivativeWRTInput;
					w[7] = w[7] - eta * neural_a * partialDerivativeWRTInput;
					w[8] = w[8] - eta * neural_b * partialDerivativeWRTInput;
					
					result = 0.0;
					for (String eData : evaluationData) {
						String[] eValues = eData.split(" ");
						double ex1 = Double.parseDouble(eValues[0]);
						double ex2 = Double.parseDouble(eValues[1]);
						double ey = Double.parseDouble(eValues[2]);
						double neuralError = 1.0/2*Math.pow(neuralC(neuralA(ex1, ex2, w), neuralB(ex1, ex2, w), w)-ey, 2);	
						result += neuralError;	
					}	
				}
				System.out.println(print(w[0]) + " " + print(w[1]) + " " + print(w[2]) + " " + print(w[3]) + " " + print(w[4]) + " " + print(w[5]) + " " + print(w[6]) + " " + print(w[7]) + " " + print(w[8]) );
				System.out.println(print(result));
			}
		} else if (flag == 800) {
			String trainingFilename = "./hw2_midterm_A_train.txt";
			String evaluationFilename = "./hw2_midterm_A_eval.txt";
			String testFilename = "./hw2_midterm_A_test.txt";			
			ArrayList<String> trainingData = readNeuralData(trainingFilename);
			ArrayList<String> evaluationData = readNeuralData(evaluationFilename);
			ArrayList<String> testData = readNeuralData(testFilename);		
			double eta = Double.parseDouble(args[10]);
			double epoch = Double.parseDouble(args[11]);
			
			double preResult = 0.0;
			for (int i=0; i<epoch; i++) {
				double result = 0.0;
				for (String tData : trainingData) {
					
					String[] tValues = tData.split(" ");
					double x1 = Double.parseDouble(tValues[0]);
					double x2 = Double.parseDouble(tValues[1]);
					double y = Double.parseDouble(tValues[2]);
					double neural_a = neuralA(x1,x2,w);
					double neural_b = neuralB(x1,x2,w);
					
					double partialDerivativeWRTInput = partialDerivitiveCWithNeuralAB(neural_a, neural_b, y, w);
					double partialDerivativeWRTInputNeuralA = partialDerivitiveA(x1, x2, y, w, partialDerivativeWRTInput);
					double partialDerivativeWRTInputNeuralB = partialDerivitiveB(x1, x2, y, w, partialDerivativeWRTInput);
					
					w[0] = w[0] - eta * partialDerivativeWRTInputNeuralA;
					w[1] = w[1] - eta * (x1 * partialDerivativeWRTInputNeuralA);
					w[2] = w[2] - eta * (x2 * partialDerivativeWRTInputNeuralA);
					w[3] = w[3] - eta * partialDerivativeWRTInputNeuralB;
					w[4] = w[4] - eta * (x1 * partialDerivativeWRTInputNeuralB);
					w[5] = w[5] - eta * (x2 * partialDerivativeWRTInputNeuralB);
					w[6] = w[6] - eta * partialDerivativeWRTInput;
					w[7] = w[7] - eta * neural_a * partialDerivativeWRTInput;
					w[8] = w[8] - eta * neural_b * partialDerivativeWRTInput;
					
					result = 0.0;
					for (String eData : evaluationData) {
						String[] eValues = eData.split(" ");
						double ex1 = Double.parseDouble(eValues[0]);
						double ex2 = Double.parseDouble(eValues[1]);
						double ey = Double.parseDouble(eValues[2]);
						double neuralError = 1.0/2*Math.pow(neuralC(neuralA(ex1, ex2, w), neuralB(ex1, ex2, w), w)-ey, 2);	
						result += neuralError;	
					}
				}
				if ( result > preResult && i!=0 ) {
					System.out.println(i+1);
					System.out.println(print(w[0]) + " " + print(w[1]) + " " + print(w[2]) + " " + print(w[3]) + " " + print(w[4]) + " " + print(w[5]) + " " + print(w[6]) + " " + print(w[7]) + " " + print(w[8]) );
					System.out.println(print(result));
					break;
				}
				preResult = result;
			}
			int correctPrediction = 0;
			for (String test : testData) {
				String[] tValues = test.split(" ");
				double tx1 = Double.parseDouble(tValues[0]);
				double tx2 = Double.parseDouble(tValues[1]);
				double ty = Double.parseDouble(tValues[2]);
				
				double neuralOutput = neuralC(neuralA(tx1, tx2, w), neuralB(tx1, tx2, w), w);
				int neuralResult = (neuralOutput >= 0.5) ? 1 : 0;
				
				if (neuralResult == ty) {
					correctPrediction++;
				}
			}
			System.out.println(print((double)correctPrediction/testData.size()));
			
		}
		
	}
	
	
	private static ArrayList<String> readNeuralData(String filename) {
        ArrayList<String> data = new ArrayList<String>();      
        try{
            File f = new File(filename);
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
            	data.add(sc.nextLine());
            } 
            sc.close();
        }
        catch(FileNotFoundException ex){
            System.out.println("File Not Found.");
        }
        return data;
    }
	
	
	private static double partialDerivitiveA(double x1, double x2, double y, double[] w, double partial_derivative_c) {
		double partialDerivativeWRTOutputNeuralA = w[7] * partial_derivative_c;
		double input_a = 1*w[0] + x1*w[1] + x2*w[2];
		int multipleA = (input_a >= 0) ? 1 : 0;
		return partialDerivativeWRTOutputNeuralA * multipleA;
		
	}
	
	private static double partialDerivitiveB(double x1, double x2, double y, double[] w, double partial_derivative_c) {
		double partialDerivativeWRTOutputNeuralB = w[8] * partial_derivative_c;
		double input_b = 1*w[3] + x2*w[5] + x1*w[4];
		int multipleB = (input_b >= 0) ? 1 : 0;
		return  partialDerivativeWRTOutputNeuralB * multipleB;
	}
	
	private static double partialDerivitiveC(double x1, double x2, double y, double[] w) {
		double output_c = neuralC(neuralA(x1, x2, w), neuralB(x1, x2, w), w);
		double partialDerivativeWRTOutput = output_c - y;
		double partialDerivativeWRTInput = partialDerivativeWRTOutput * output_c * (1 - output_c);
		return partialDerivativeWRTInput;
	}
	
	private static double partialDerivitiveCWithNeuralAB(double neural_a, double neural_b, double y, double[] w) {
		double output_c = neuralC(neural_a, neural_b, w);
		double partialDerivativeWRTOutput = output_c - y;
		double partialDerivativeWRTInput = partialDerivativeWRTOutput * output_c * (1 - output_c);
		return partialDerivativeWRTInput;
	}
	
	private static double neuralA(double x1, double x2, double[] w) {
		double input_a = 1*w[0] + x1*w[1] + x2*w[2];
		double output_a = Math.max(input_a, 0);
		return output_a;
		
	}
	
	private static double neuralB(double x1, double x2, double[] w) {
		double input_b = 1*w[3] + x2*w[5] + x1*w[4];
		double output_b = Math.max(input_b, 0);
		return output_b;
	}
	
	private static double neuralC(double neural_a, double neural_b, double[] w) {
		double input_c = neural_a*w[7] + neural_b*w[8] + 1*w[6];
		double output_c = 1 / (1+Math.exp(-input_c));
		return output_c;
	}
	
	public static String print(double value) {
		return String.format("%.5f", value);
	}
}
