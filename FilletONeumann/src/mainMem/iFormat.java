package mainMem;

public class iFormat extends Instruction {

	private char[] R1;
	private char[] R2;
	private char[] iValue;

	public iFormat(String instrID) {
		super(instrID);

		char value = '0';

		R1 = new char[5];
		R2 = new char[5];

		for (int i = 0; i < R1.length; i++) {
			R1[i] = value;
			R2[i] = value;

		}

		iValue = new char[18];

		for (int i = 0; i < iValue.length; i++) {
			iValue[i] = value;

		}
	}

	public void setInstrBits(char[] instrBits) {
		super.setInstrBits(instrBits);
		setR1();
		setR2();
		setIValue();

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

	public char[] getIValue() {
		return iValue;
	}

	private void setIValue() {
		System.arraycopy(getInstrBits(), 14, iValue, 0, 18);

	}

	public String r1String() {

		String parsedString = new String(R1);

		return parsedString;

	}

	public String r2String() {

		String parsedString = new String(R2);

		return parsedString;

	}

	public String iString() {

		String parsedString = new String(iValue);

		return parsedString;

	}

}
