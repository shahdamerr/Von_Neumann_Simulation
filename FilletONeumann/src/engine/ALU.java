package engine;

import regs.*;
import mainMem.*;
import java.util.*;

public class ALU {

	public static void ADD(Register R1, Register R2, Register R3) {
		int r3 = convertBinaryToDecimal(R3.bitsString());
		int r2 = convertBinaryToDecimal(R2.bitsString());

		int r1 = r3 + r2;


		Register temp = new Register("Temp");
		temp.setBits(convertDecimalToBinary(r1));
		
		R1.setBits(temp.getBits());
		
	}
	
	public static void SUB(Register R1, Register R2, Register R3) {
		int r3 = convertBinaryToDecimal(R3.bitsString());
		int r2 = convertBinaryToDecimal(R2.bitsString());

		int r1 = r2 - r3;


		Register temp = new Register("Temp");
		temp.setBits(convertDecimalToBinary(r1));
		
		R1.setBits(temp.getBits());
		
	}

	public static void ADD1(Register R1, Register R2, Register R3) {
		int[] carry = new int[32];
		int sum;
		Register temp = new Register("Temp");

		for (int i = 31; i >= 0; i--) {
			int bit1 = R2.getBits()[i] - '0';
			int bit2 = R3.getBits()[i] - '0';
			sum = bit1 + bit2 + carry[i];
			temp.getBits()[i] = (char) ('0' + (sum % 2));
			if (i > 0) {
				carry[i - 1] = sum / 2;
			}
		}
		char xorCarry = (char) ('0' + (carry[0] ^ carry[1]));
		if (xorCarry == '1') {
			System.out.println("Warning: Overflow occurred during addition.");
		}

		R1.setBits(temp.getBits());

	}

	public static void SUB1(Register R1, Register R2, Register R3) {
		// Negate the bits of R2 by performing 2's complement
		regGP negatedR3 = new regGP("NegatedR3");
		int[] carry = new int[32];

		// Invert all the bits of R2
		for (int i = 0; i < 32; i++) {
			negatedR3.getBits()[i] = (char) ('0' + (R3.getBits()[i] == '0' ? 1 : 0));
		}

		regGP regOne = new regGP("1");
		regOne.getBits()[31] = '1';

		// Add 1 to the inverted R2 to obtain 2's complement
		ADD1(negatedR3, regOne, negatedR3);

		// Perform addition of R1 and negated R2
		ADD1(R1, R2, negatedR3);

		// Check for overflow
		char[] carryBits = new char[2];
		carryBits[0] = (char) ('0' + (carry[0] ^ carry[1]));
		carryBits[1] = (char) ('0' + (carry[1] ^ carry[2]));

		if (carryBits[0] == '1' && carryBits[1] == '1') {
			System.out.println("Warning: Overflow occurred during subtraction.");
		}
	}

	public static void MUL(Register R1, Register R2, Register R3) {

//	    boolean isPosR3 = false;
//	    boolean isPosR2 = false;
//	    int i = convertBinaryToDecimal(R3.bitsString());
//
//	    if (i >= 0)
//	        isPosR3 = true;
//	    else
//	        i = -i;
//
//	    if (convertBinaryToDecimal(R2.bitsString()) >= 0)
//	        isPosR2 = true;
//
//	    Register temp = new Register("Temp");
//
//	    while (i > 0) {
//	        ADD(temp, temp, R2);
//	        i--;
//	    }
//
//	    if ((isPosR2 && !isPosR3) || (!isPosR2 && isPosR3)) {
//	        int num = convertBinaryToDecimal(temp.bitsString());
//	        num = -num;
//	        char[] newBits = convertDecimalToBinary(num);
//	        temp.setBits(newBits);
//	    }

		int r3 = convertBinaryToDecimal(R3.bitsString());
		int r2 = convertBinaryToDecimal(R2.bitsString());

		int r1 = r3 * r2;
		Register temp = new Register("Temp");
		temp.setBits(convertDecimalToBinary(r1));

		R1.setBits(temp.getBits());

	}

	public static void MOVI(Register R1, char[] IMM) {
		char MSB = IMM[0];
		// System.out.println(new String(IMM));

		int j = 31;
		char[] newValue = new char[32];

		for (int i = IMM.length - 1; i >= 0; i--) {
			newValue[j] = IMM[i];
			j--;
		}

		for (int i = 0; i < newValue.length - IMM.length; i++) {
			newValue[i] = MSB;
		}

		// System.out.println(new String(newValue));

		R1.setBits(newValue);
	}

	public static void JEQ(Register PC, Register R1, Register R2, char[] IMM) {
//		System.out.println(ALU.convertBinaryToDecimal(R1.bitsString()));
//		System.out.println(ALU.convertBinaryToDecimal(R2.bitsString()));
//		System.out.println(Register.convertBinaryToDecimal(PC.bitsString()));
		regGP helper = new regGP("helper");

		if (ALU.convertBinaryToDecimal(R1.bitsString()) == ALU.convertBinaryToDecimal(R2.bitsString())) {

			MOVI(helper, IMM);
			ADD1(PC, PC, helper);

		}
		char[] newChar = { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1' };
		ALU.MOVI(helper, newChar);
		ALU.SUB1(PC, PC, helper);
		// System.out.println(Register.convertBinaryToDecimal(PC.bitsString()));

	}

	public static void AND(Register R1, Register R2, Register R3) {

		Register temp = new Register("Temp");

		for (int i = 0; i < R1.getBits().length; i++) {
			temp.getBits()[i] = (char) ('0' + (R2.getBits()[i] - '0' & R3.getBits()[i] - '0'));
		}

		R1.setBits(temp.getBits());
	}

	public static void XORI(Register R1, Register R2, char[] IMM) {

		Register other = new Register("other");
		MOVI(other, IMM);
		XOR(R1, R2, other);
	}

	public static void XOR(Register R1, Register R2, Register R3) {

		Register temp = new Register("Temp");

		for (int i = 0; i < R1.getBits().length; i++) {
			temp.getBits()[i] = (char) ('0' + (R2.getBits()[i] - '0' ^ R3.getBits()[i] - '0'));
		}

		R1.setBits(temp.getBits());

	}

	public static void JMP(Register PC, char[] address) {
		char[] pcBits = PC.getBits();
		// System.out.println(Register.convertBinaryToDecimal(PC.bitsString()));

		// Create a new array to hold the concatenated bits
		char[] concatenatedBits = new char[32];

		// Copy the original 4 bits of the PC to the concatenated array
		System.arraycopy(pcBits, 0, concatenatedBits, 0, 4);

		// Copy the bits of the address to the concatenated array
		System.arraycopy(address, 0, concatenatedBits, 4, address.length);

		PC.setBits(concatenatedBits);

		// System.out.println(Register.convertBinaryToDecimal(PC.bitsString()));
	}

	public static void LSL(Register R1, Register R2, int SHAMT) {
		int numBits = R1.getBits().length;
		Register temp = new Register("Temp");

		// Shift R2's bits left by SHAMT positions
		for (int i = 0; i < numBits - SHAMT; i++) {
			temp.getBits()[i] = R2.getBits()[i + SHAMT];
		}

		// Fill the remaining positions with '0'
		for (int i = numBits - SHAMT; i < numBits; i++) {
			temp.getBits()[i] = '0';
		}

		R1.setBits(temp.getBits());

	}

	public static void LSR(Register R1, Register R2, int SHAMT) {

		int numBits = R1.getBits().length;
		Register temp = new Register("Temp");

		// Shift R2's bits right by SHAMT positions
		for (int i = SHAMT; i < numBits; i++) {
			temp.getBits()[i] = R2.getBits()[i - SHAMT];
		}

		// Fill the first SHAMT positions with '0'
		for (int i = 0; i < SHAMT; i++) {
			temp.getBits()[i] = '0';
		}
		R1.setBits(temp.getBits());

	}

	public static void MOVR(MainMemory mainMem, Register R1, Register R2, char[] IMM) {
		regGP helper = new regGP("helper");
		MOVI(helper, IMM);
		ADD(helper, helper, R2);

		int memAddress = Register.convertBinaryToDecimal(helper.bitsString());

		if (memAddress > 1023 && memAddress <= 2047) {
			Data data = mainMem.getData(memAddress - 1024);
			R1.setBits(data.getDataBits());
			
			System.out.println(R1.getName() + " was updated in memory stage with value " + ALU.convertBinaryToDecimal(data.dataString()));
		} else if (memAddress < 1024 && memAddress >= 0) {
			Instruction instr = mainMem.getInstr(memAddress);
			R1.setBits(instr.getInstrBits());
			System.out.println(R1.getName() + " was updated in memory stage with value " + ALU.convertBinaryToDecimal(instr.instrString()));

		}
	}

	public static void MOVM(MainMemory mainMem, Register R1, Register R2, char[] IMM) {
		regGP helper = new regGP("helper");
		MOVI(helper, IMM);
		ADD(helper, helper, R2);

		int memAddress = Register.convertBinaryToDecimal(helper.bitsString());

		if (memAddress > 1023 && memAddress <= 2047) {
			mainMem.setData(memAddress - 1024, R1.getBits());
		} else if (memAddress < 1024 && memAddress >= 0) {
			mainMem.setInstr(memAddress, R1.getBits());

		}
		System.out.println("Main memory at address "+memAddress+ " was updated in memory stage with value " + ALU.convertBinaryToDecimal(R1.bitsString()));

	}

	// converts binary string to decimal, positive or negative

	public static int convertBinaryToDecimal(String binary) {
		int decimal = 0;
		int length = binary.length();
		boolean isNegative = false;

		if (binary.charAt(0) == '1') {
			// If the most significant bit is 1, it indicates a negative number in 2's
			// complement representation
			isNegative = true;

			// Invert all the bits
			StringBuilder invertedBinary = new StringBuilder();
			for (int i = 0; i < length; i++) {
				invertedBinary.append(binary.charAt(i) == '0' ? '1' : '0');
			}

			// Add 1 to the inverted number to get the 2's complement
			int invertedDecimal = convertBinaryToDecimal(invertedBinary.toString());
			decimal = -(invertedDecimal + 1);
		} else {
			// If the most significant bit is 0, the number is positive
			for (int i = 0; i < length; i++) {
				char bit = binary.charAt(length - i - 1);
				if (bit == '1') {
					decimal += Math.pow(2, i);
				}
			}
		}

		return decimal;
	}

	public static char[] convertDecimalToBinary(int decimal) {
		if (decimal == 0) {
			return new char[] { '0' }; // Special case for decimal 0
		}

		boolean isNegative = false;
		if (decimal < 0) {
			isNegative = true;
			decimal = -decimal; // Work with the absolute value of the decimal
		}

		char[] binary = new char[32];
		int index = 31; // Start from the rightmost bit

		while (decimal > 0) {
			binary[index--] = (char) ('0' + (decimal % 2)); // Assign the character '0' or '1'
			decimal /= 2; // Divide the decimal by 2
		}

		// Fill the remaining bits with '0'
		while (index >= 0) {
			binary[index--] = '0';
		}

		// If the number was negative, perform 2's complement
		if (isNegative) {
			for (int i = 0; i < binary.length; i++) {
				binary[i] = (binary[i] == '0') ? '1' : '0'; // Invert each bit
			}
			int carry = 1;
			for (int i = binary.length - 1; i >= 0; i--) {
				if (binary[i] == '1') {
					if (carry == 1) {
						binary[i] = '0';
					} else {
						break;
					}
				} else {
					if (carry == 1) {
						binary[i] = '1';
						carry = 0;
					}
				}
			}
		}

		return binary;
	}

	public static void main(String[] args) {
//		regGP r1 = new regGP("R1");
//		regGP r2 = new regGP("R2");
//		regGP r3 = new regGP("R3");
//
//		char[] regBits1 = { '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
//				'1', '1', '1', '1', '1', '1', '1', '1', '0', '0', '0', '0', '0', '0' };
//		char[] regBits2 = { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
//				'0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '1', '1' };
//
//		r2.setBits(regBits1);
//		r3.setBits(regBits2);
//
//		MUL(r1, r2, r3);
//
//		System.out.println(convertBinaryToDecimal(r2.bitsString()));
//		System.out.println(convertBinaryToDecimal(r3.bitsString()));
//		System.out.println(convertBinaryToDecimal(r1.bitsString()));

	}

}
