package rb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CCMasker 
{
	private static String NEWLINE = "\n";
	private static char MASK_SYMBOL = 'X';
	private static int SIXTEEN_DIGIT_CC = 16;
	private static int FIFTEEN_DIGIT_CC = 15;
	private static int FOURTEEN_DIGIT_CC = 14; 
	
	private char[] inputArray;
	private int[][] digitArray; /* digit & its location */
	
	public CCMasker() {
	}
	
	public static void main(String[] args) 
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = null;

		CCMasker ccMasker = new CCMasker();
		
		try {
			while ((line = br.readLine()) != null) {
				
				String result = ccMasker.mask(line);
				System.out.print(result + NEWLINE);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String mask(String line) {
		
		setInput(line);
		extractDigitsAndItsLocation();
		checkForCCAndMaskIt();
		
		return getMaskedOutput();
	}

	private void checkForCCAndMaskIt() {
		
		checkFor14DigitCCAndMaskIt();
		checkFor15DigitCCAndMaskIt();
		checkFor16DigitCCAndMaskIt();
		
	}
	
	private void checkFor14DigitCCAndMaskIt() {
		if (noOfDigits() < FOURTEEN_DIGIT_CC) {
			return;
		}
		
		mask(FOURTEEN_DIGIT_CC);
	}

	private void checkFor15DigitCCAndMaskIt() {
		if (noOfDigits() < FIFTEEN_DIGIT_CC) {
			return;
		}
		
		mask(FIFTEEN_DIGIT_CC);
	}
	
	private void checkFor16DigitCCAndMaskIt() {
		if (noOfDigits() < SIXTEEN_DIGIT_CC) {
			return;
		}
		
		mask(SIXTEEN_DIGIT_CC);
	}

	private void mask(int chunk) {
		int start = 0;
		int end = start + chunk - 1;
		
		do {
			boolean validCC = isValidCC(digitArray, start, end);
			if (validCC) {
				maskDigits(inputArray, digitArray, start, end);
			}
			
			start += 1;
			end += 1;
		} while (end < noOfDigits());
	}

	/** uses luhn's algorithm */
	private boolean isValidCC(int[][] vals, int start, int end) {
		boolean isValid = false;

		int sumOfDigits = 0;
		int position = 0;

		for (int idx = end; idx >= start; idx--) {
			int number = vals[idx][0];

			if (position++ % 2 == 0) {
				sumOfDigits = sumOfDigits + number;
			} else {
				number = number * 2;

				while (number > 0) {
					int eachDigit = number % 10;
					sumOfDigits += eachDigit;
					number = number / 10;
				}
			}
		}

		isValid = (sumOfDigits % 10 == 0);
		return isValid;
	}

	private void maskDigits(char[] inputArray, int[][] digitArray, int start, int end) {
		for (int i = start; i <= end; i++) {
			int location = digitArray[i][1];
			inputArray[location] = MASK_SYMBOL;
		}
	}
	
	private void extractDigitsAndItsLocation() {
		int[][] digitArray = new int[inputArray.length][2];
		initArray(digitArray);

		int pos = 0;
		for (int idx = 0; idx < inputArray.length; idx++) {
			char ch = inputArray[idx];
			if (Character.isDigit(ch)) {
				digitArray[pos][0] = Character.getNumericValue(ch); // value
				digitArray[pos][1] = idx; // location
				pos++;
			}
		}

		if (pos < digitArray.length) {
			int[][] shrinkedDigitArray = new int[pos][2];
			int j = 0;
			for (int idx = 0; idx < digitArray.length; idx++) {
				if (digitArray[idx][0] != -1) {
					shrinkedDigitArray[j][0] = digitArray[idx][0]; // value
					shrinkedDigitArray[j][1] = digitArray[idx][1]; // location
					j++;
				}
			}

			digitArray = shrinkedDigitArray;
		}

		this.digitArray = digitArray;
	}

	private void initArray(int[][] vals) {
		for (int i = 0; i < vals.length; i++) {
			vals[i][0] = -1;
		}
	}
	
	private int noOfDigits() {
		return digitArray.length;
	}
	
	
	private String getMaskedOutput() {
		String output = String.valueOf(inputArray);
		return output;
	}

	private void setInput(String line) {
		this.inputArray = line.toCharArray();
	}

}
