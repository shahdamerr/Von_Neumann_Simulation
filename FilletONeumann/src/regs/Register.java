package regs;


public class Register {

	private char[] bits;
	private String name;

	public Register(String name) {
		bits = new char[32];
		char value = '0'; 
		for (int i = 0; i < bits.length; i++) {
			bits[i] = value;
		}
		
		this.name = name;
	}
	
	public Register() {
		bits = new char[32];
		char value = '0'; 
		for (int i = 0; i < bits.length; i++) {
			bits[i] = value;
		}
	}

	public char[] getBits() {
		return bits;
	}

	public void setBits(char[] bits) {
		
		char MSB = bits[0];
		//	System.out.println(new String(IMM));

			int j = 31;
			char[] newValue = new char[32];
			
			for (int i = bits.length - 1; i >= 0; i--) {
				newValue[j] = bits[i];
				j--;
			}

			for (int i = 0; i < newValue.length - bits.length; i++) {
				newValue[i] = MSB;
			}
		this.bits = newValue;
	}

	public String bitsString() {

		String parsedString = new String(bits); // Convert char array to String


		return parsedString;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	//converting to decimal from binary only in positives
	
	public static int convertBinaryToDecimal(String binary) {
        int decimal = 0;
        int length = binary.length();

        for (int i = 0; i < length; i++) {
            char bit = binary.charAt(length - i - 1);
            if (bit == '1') {
                decimal += Math.pow(2, i);
            }
        }

        return decimal;
    }

}
