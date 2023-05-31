package regs;


public class regPC extends Register {

	public regPC() {
		super();
		setName("PC");
	}

	public void incrementPC(){

		char[] currentBits = getBits();

		String parsedString = this.toString();
		int decimalRep = convertBinaryToDecimal(parsedString);


		if (decimalRep < 1023) {
			int leastSignificantBitIndex = currentBits.length - 1;

			boolean carry = true;
			for (int i = leastSignificantBitIndex; i >= 0 && carry; i--) {
				char currentValue = currentBits[i];
				if (currentValue == '0') {
					currentBits[i] = '1';
					carry = false;
				} else {
					currentBits[i] = '0';
				}
			}
		} 
		
		else {
			char value = '0';
			for (int i = 0; i < currentBits.length; i++) {
				currentBits[i] = value;
			}
		}

		setBits(currentBits);
	}

	public static void main(String[] args) {
	
	}

}
