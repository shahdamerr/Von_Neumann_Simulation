package mainMem;

public class rFormat extends Instruction {
	
	private char[] R1;
	private char[] R2;
	private char[] R3;
	private char[] shamt;

	public rFormat(String instrID) {
		super(instrID);
		char value = '0';

		R1 = new char[5];
		R2 = new char[5];
		R3 = new char[5];

		for (int i = 0; i < R1.length; i++) {
			R1[i] = value;
			R2[i] = value;
			R3[i] = value;
		}

		shamt = new char[13];

		for (int i = 0; i < shamt.length; i++) {
			shamt[i] = value;

		}

	}

	public void setInstrBits(char[] instrBits) {
		super.setInstrBits(instrBits);
		setR1();
		setR2();
		setR3();
		setShamt();

	}

	public char[] getR1() {
		return R1;
	}

	private void setR1() {
		System.arraycopy(getInstrBits(), 4, R1, 0, 5);
	}

	public char[] getR2() {
		return R2;
	}

	private void setR2() {
		System.arraycopy(getInstrBits(), 9, R2, 0, 5);
	}

	public char[] getR3() {
		return R3;
	}

	private void setR3() {
		System.arraycopy(getInstrBits(), 14, R3, 0, 5);
	}

	public char[] getShamt() {
		return shamt;
	}

	private void setShamt() {
		System.arraycopy(getInstrBits(), 19, shamt, 0, 13);
	}

	public String r1String() {

		String parsedString = new String(R1);

		return parsedString;

	}

	public String r2String() {

		String parsedString = new String(R2);

		return parsedString;

	}

	public String r3String() {

		String parsedString = new String(R3);

		return parsedString;

	}

	public String shamtString() {

		String parsedString = new String(shamt); // Convert char array to String

		return parsedString;

	}
}
