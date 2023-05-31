package mainMem;

public class MainMemory {

	public Instruction[] instr;
	public Data[] data;

	public MainMemory() {
		this.instr = new Instruction[1024];
		this.data = new Data[1024];

	}

	public Data getData(int address) {

		return data[address];

	}

	public void setData(int address, char[] newData) {
		Data other = new Data();
		other.setDataBits(newData);

		data[address] = other;

	}

	public Instruction getInstr(int address) {

		return instr[address];

	}

	public void setInstr(int address, char[] newInstr) {
		Instruction dummy = instr[address];

		Instruction other = new Instruction(dummy.getIstrID());
		other.setInstrBits(newInstr);
		instr[address] = other;

	}

	public void displayInstructions() {
		for (int i = 0; i < instr.length; i++) {
			Instruction instruction = instr[i];
			if (instruction != null) {
				System.out.println("Instruction " + (i+1) + ":");
				System.out.println("  Instruction ID: " + instruction.getIstrID());
				System.out.println("  Instruction Bits: " + instruction.instrString());
				System.out.println("  Opcode: " + instruction.opcodeString());
				System.out.println();
			}
			
			else {
				System.out.println("Instruction " + (i+1) + ":");
				System.out.println("00000000000000000000000000000000");
			}
		}
	}

	// Display method for data array
	public void displayData() {
		for (int i = 0; i < data.length; i++) {
			Data dataItem = data[i];
			if (dataItem != null) {
				System.out.println("Data " + i+1024 + ":");
				System.out.println("  Data Bits: " + new String(dataItem.getDataBits()));
				System.out.println();
			}
			else {
				System.out.println("Data " + (i+1024) + ":");
				System.out.println("00000000000000000000000000000000");
			}
		}
	}

}
