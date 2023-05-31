package mainMem;

public class Data {
	
	private char[] dataBits;
	
	
	public Data() {
		dataBits = new char[32];
		char value = '0'; 
		for (int i = 0; i < dataBits.length; i++) {
			dataBits[i] = value;
		}
		
		
	}

	public char[] getDataBits() {
		return dataBits;
	}
	
	public String dataString() {

		String parsedString = new String(dataBits);

		return parsedString;

	}

	public void setDataBits(char[] dataBits) {
		this.dataBits = dataBits;
	}
	
}
