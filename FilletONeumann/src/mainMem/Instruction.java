package mainMem;

import java.util.*;

public class Instruction {

	private char[] instrBits;
	private char[] opCode;
	private String istrID;

	public Instruction(String istrID) {
		this.istrID = istrID;
		instrBits = new char[32];
		char value = '0';
		for (int i = 0; i < instrBits.length; i++) {
			instrBits[i] = value;
		}

		opCode = new char[4];
		for (int i = 0; i < opCode.length; i++) {
			opCode[i] = value;
		}

	}

	public char[] getInstrBits() {
		return instrBits;
	}

	public void setInstrBits(char[] instrBits) {
		this.instrBits = instrBits;
		setOpCode();
	}

	public char[] getOpCode() {
		return opCode;
	}

	private void setOpCode() {
		System.arraycopy(instrBits, 0, opCode, 0, 4);
	}

	public String instrString() {

		String parsedString = new String(instrBits); // Convert char array to String

		return parsedString;

	}

	public String opcodeString() {

		String parsedString = new String(opCode); // Convert char array to String

		return parsedString;

	}

	public String getIstrID() {
		return istrID;
	}

	

	public static void main(String[] args) {
//		jFormat instr1 = new jFormat("Instruction "+1);
//		char[] instrBits = new char[32];
//		Random random = new Random();
//
//        for (int i = 0; i < 32; i++) {
//            int randomBit = random.nextInt(2);
//            instrBits[i] = (char) ('0' + randomBit);
//        }
//        
//        instr1.setInstrBits(instrBits);
//        System.out.println(instr1.instrString());
//        System.out.println(instr1.opcodeString());
//        System.out.println(instr1.jString());
//        System.out.println(instr1.getIstrID());

	}

}
