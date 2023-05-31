package mainMem;

public class jFormat extends Instruction {

	private char[] jAddress;

	public jFormat(String instrID) {
		super(instrID);
		char value = '0';

		jAddress = new char[28];

		for (int i = 0; i < jAddress.length; i++) {
			jAddress[i] = value;

		}
	}

	public void setInstrBits(char[] instrBits) {
		super.setInstrBits(instrBits);
		setjAddress();

	}

	public char[] getjAddress() {
		return jAddress;
	}

	private void setjAddress() {
		System.arraycopy(getInstrBits(), 4, jAddress, 0, 28);

	}

	public String jString() {

		String parsedString = new String(jAddress);

		return parsedString;

	}

}
