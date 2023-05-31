package engine;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import mainMem.*;
import regs.*;

public class processor2 {

	public MainMemory mainMem;
	public regGP[] generalRegisters;
	public regPC PC;
	public regZero zereRegister;
	public Register resultALU;
	public Register forJump;
	public Instruction lastInstruction;
	public static List<Instruction> fetchStage;
	public static List<Instruction> decodeStage;
	public static List<Instruction> executeStage;
	public static List<Instruction> memoryStage;
	public static List<Instruction> writeBackStage;
	public static int clockCycles = 0;
	public static int clockCycles2 = 0;
	public static boolean jumpCheck = false;

	public processor2() {
		mainMem = new MainMemory();
		generalRegisters = new regGP[31];
		for (int i = 1; i <= 31; i++) {
			regGP R = new regGP("R" + i);
			generalRegisters[i - 1] = R;
		}

		PC = new regPC();
		resultALU = null;
		forJump = new Register("forJump");

		zereRegister = new regZero();
	}

	public void parseProgram() {

		String fileName = "program"; // Replace with your file name

		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line;
			int i = 0;
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split("\\s+"); // Split the line by whitespace
				if (tokens.length > 0) {
					String instruction = tokens[0];
					String binaryInstruction = "";

					if (instruction.equals("MOVI")) {
						String register = tokens[1];
						int value = Integer.parseInt(tokens[2]);
						binaryInstruction = translateMOVI(register, value);
						// System.out.println(binaryInstruction);
					} else if (instruction.equals("ADD")) {
						String destRegister = tokens[1];
						String srcRegister1 = tokens[2];
						String srcRegister2 = tokens[3];
						binaryInstruction = translateADD(destRegister, srcRegister1, srcRegister2);
						// System.out.println(binaryInstruction);
					} else if (instruction.equals("SUB")) {
						String destRegister = tokens[1];
						String srcRegister1 = tokens[2];
						String srcRegister2 = tokens[3];
						binaryInstruction = translateSUB(destRegister, srcRegister1, srcRegister2);
						// System.out.println(binaryInstruction);

					} else if (instruction.equals("MUL")) {
						String destRegister = tokens[1];
						String srcRegister1 = tokens[2];
						String srcRegister2 = tokens[3];
						binaryInstruction = translateMUL(destRegister, srcRegister1, srcRegister2);
						// System.out.println(binaryInstruction);

					} else if (instruction.equals("JEQ")) {

						String srcRegister1 = tokens[1];
						String srcRegister2 = tokens[2];
						int address = Integer.parseInt(tokens[3]);
						binaryInstruction = translateJEQ(srcRegister1, srcRegister2, address);
						// System.out.println(binaryInstruction);
					} else if (instruction.equals("AND")) {
						String destRegister = tokens[1];
						String srcRegister1 = tokens[2];
						String srcRegister2 = tokens[3];
						binaryInstruction = translateAND(destRegister, srcRegister1, srcRegister2);
						// System.out.println(binaryInstruction);

					} else if (instruction.equals("XORI")) {

						String destRegister = tokens[1];
						String srcRegister = tokens[2];
						int address = Integer.parseInt(tokens[3]);
						binaryInstruction = translateXORI(destRegister, srcRegister, address);
						// System.out.println(binaryInstruction);
					} else if (instruction.equals("JMP")) {

						int address = Integer.parseInt(tokens[1]);
						binaryInstruction = translateJMP(address);
						// System.out.println(binaryInstruction);
					} else if (instruction.equals("LSL")) {

						String destRegister = tokens[1];
						String srcRegister = tokens[2];
						int address = Integer.parseInt(tokens[3]);
						binaryInstruction = translateLSL(destRegister, srcRegister, address);
						// System.out.println(binaryInstruction);
					} else if (instruction.equals("LSR")) {

						String destRegister = tokens[1];
						String srcRegister = tokens[2];
						int address = Integer.parseInt(tokens[3]);
						binaryInstruction = translateLSR(destRegister, srcRegister, address);
						// System.out.println(binaryInstruction);
					}

					else if (instruction.equals("MOVM")) {

						String destRegister = tokens[1];
						String srcRegister = tokens[2];
						int address = Integer.parseInt(tokens[3]);
						binaryInstruction = translateMOVM(destRegister, srcRegister, address);
						// System.out.println(binaryInstruction);
					} else if (instruction.equals("MOVR")) {
						String destRegister = tokens[1];
						String srcRegister = tokens[2];
						int address = Integer.parseInt(tokens[3]);
						binaryInstruction = translateMOVR(destRegister, srcRegister, address);
						// System.out.println(binaryInstruction);
					}

					if (i > 1023) {
						i = 0;
					}
					char[] charArray = binaryInstruction.toCharArray();
					Instruction instr = new Instruction("Instruction " + (i + 1));
					instr.setInstrBits(charArray);
					mainMem.instr[i] = instr;
					i++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String translateADD(String destRegister, String srcRegister1, String srcRegister2) {
		// Convert the register numbers to 5-bit binary representation
		String binaryDestRegister = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(destRegister.charAt(1))))
				.replace(' ', '0');
		String binarySrcRegister1 = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(srcRegister1.charAt(1))))
				.replace(' ', '0');
		String binarySrcRegister2 = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(srcRegister2.charAt(1))))
				.replace(' ', '0');

		// Concatenate the binary representations to form the 32-bit instruction
		return "0000" + binaryDestRegister + binarySrcRegister1 + binarySrcRegister2 + "0000000000000";
	}

	public String translateSUB(String destRegister, String srcRegister1, String srcRegister2) {
		// Convert the register numbers to 5-bit binary representation
		String binaryDestRegister = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(destRegister.charAt(1))))
				.replace(' ', '0');
		String binarySrcRegister1 = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(srcRegister1.charAt(1))))
				.replace(' ', '0');
		String binarySrcRegister2 = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(srcRegister2.charAt(1))))
				.replace(' ', '0');

		// Concatenate the binary representations to form the 32-bit instruction
		return "0001" + binaryDestRegister + binarySrcRegister1 + binarySrcRegister2 + "0000000000000";
	}

	public String translateMUL(String destRegister, String srcRegister1, String srcRegister2) {
		// Convert the register numbers to 5-bit binary representation
		String binaryDestRegister = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(destRegister.charAt(1))))
				.replace(' ', '0');
		String binarySrcRegister1 = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(srcRegister1.charAt(1))))
				.replace(' ', '0');
		String binarySrcRegister2 = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(srcRegister2.charAt(1))))
				.replace(' ', '0');

		// Concatenate the binary representations to form the 32-bit instruction
		return "0010" + binaryDestRegister + binarySrcRegister1 + binarySrcRegister2 + "0000000000000";
	}

	public String translateMOVI(String register, int value) {
		// Convert the register number to 5-bit binary representation
		String binaryRegister = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(register.charAt(1)))).replace(' ', '0');

		// Convert the value to 16-bit binary representation
		String binaryValue = String.format("%18s", Integer.toBinaryString(value)).replace(' ', '0');

		// Concatenate the binary representations to form the 32-bit instruction

		if (binaryValue.length() == 32) {
			binaryValue = binaryValue.substring(14, 32);
		}
		// System.out.println(binaryValue.length());

		return "0011" + binaryRegister + "00000" + binaryValue;
	}

	public String translateJEQ(String srcRegister1, String srcRegister2, int address) {
		// Convert the register numbers to 5-bit binary representation
		String binarysrcRegister1 = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(srcRegister1.charAt(1))))
				.replace(' ', '0');
		String binarysrcRegister2 = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(srcRegister2.charAt(1))))
				.replace(' ', '0');

		// Convert the address to 16-bit binary representation
		String binaryAddress = String.format("%18s", Integer.toBinaryString(address)).replace(' ', '0');

		if (binaryAddress.length() == 32) {
			binaryAddress = binaryAddress.substring(14, 32);
		}

		// Combine the binary representations of the instruction components
		String binaryInstruction = "0100" + binarysrcRegister1 + binarysrcRegister2 + binaryAddress;

		return binaryInstruction;
	}

	public String translateAND(String destRegister, String srcRegister1, String srcRegister2) {
		// Convert the register numbers to 5-bit binary representation
		String binaryDestRegister = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(destRegister.charAt(1))))
				.replace(' ', '0');
		String binarySrcRegister1 = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(srcRegister1.charAt(1))))
				.replace(' ', '0');
		String binarySrcRegister2 = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(srcRegister2.charAt(1))))
				.replace(' ', '0');

		// Concatenate the binary representations to form the 32-bit instruction
		return "0101" + binaryDestRegister + binarySrcRegister1 + binarySrcRegister2 + "0000000000000";
	}

	public String translateXORI(String destRegister, String srcRegister, int address) {
		// Convert the register numbers to 5-bit binary representation
		String binaryDestRegister = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(destRegister.charAt(1))))
				.replace(' ', '0');
		String binarysrcRegister = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(srcRegister.charAt(1))))
				.replace(' ', '0');

		// Convert the address to 16-bit binary representation
		String binaryAddress = String.format("%18s", Integer.toBinaryString(address)).replace(' ', '0');

		if (binaryAddress.length() == 32) {
			binaryAddress = binaryAddress.substring(14, 32);
		}
		// Combine the binary representations of the instruction components
		String binaryInstruction = "0110" + binaryDestRegister + binarysrcRegister + binaryAddress;

		return binaryInstruction;
	}

	public String translateJMP(int address) {

		// Convert the address to 16-bit binary representation
		String binaryAddress = String.format("%28s", Integer.toBinaryString(address)).replace(' ', '0');

		// Combine the binary representations of the instruction components
		String binaryInstruction = "0111" + binaryAddress;

		return binaryInstruction;
	}

	public String translateLSL(String destRegister, String srcRegister, int address) {
		// Convert the register numbers to 5-bit binary representation
		String binaryDestRegister = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(destRegister.charAt(1))))
				.replace(' ', '0');
		String binarysrcRegister = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(srcRegister.charAt(1))))
				.replace(' ', '0');

		// Convert the address to 16-bit binary representation
		String binaryAddress = String.format("%13s", Integer.toBinaryString(address)).replace(' ', '0');

		// Combine the binary representations of the instruction components
		String binaryInstruction = "1000" + binaryDestRegister + binarysrcRegister + "00000" + binaryAddress;

		return binaryInstruction;
	}

	public String translateLSR(String destRegister, String srcRegister, int address) {
		// Convert the register numbers to 5-bit binary representation
		String binaryDestRegister = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(destRegister.charAt(1))))
				.replace(' ', '0');
		String binarysrcRegister = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(srcRegister.charAt(1))))
				.replace(' ', '0');

		// Convert the address to 16-bit binary representation
		String binaryAddress = String.format("%13s", Integer.toBinaryString(address)).replace(' ', '0');

		// Combine the binary representations of the instruction components
		String binaryInstruction = "1001" + binaryDestRegister + binarysrcRegister + "00000" + binaryAddress;

		return binaryInstruction;
	}

	public String translateMOVR(String destRegister, String srcRegister, int address) {
		// Convert the register numbers to 5-bit binary representation
		String binaryDestRegister = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(destRegister.charAt(1))))
				.replace(' ', '0');
		String binarySrcRegister = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(srcRegister.charAt(1))))
				.replace(' ', '0');

		// Convert the address to 16-bit binary representation
		String binaryAddress = String.format("%18s", Integer.toBinaryString(address)).replace(' ', '0');

		if (binaryAddress.length() == 32) {
			binaryAddress = binaryAddress.substring(14, 32);
		}
		// Combine the binary representations of the instruction components
		String binaryInstruction = "1010" + binaryDestRegister + binarySrcRegister + binaryAddress;

		return binaryInstruction;
	}

	public String translateMOVM(String destRegister, String srcRegister, int address) {
		// Convert the register numbers to 5-bit binary representation
		String binaryDestRegister = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(destRegister.charAt(1))))
				.replace(' ', '0');
		String binarySrcRegister = String
				.format("%5s", Integer.toBinaryString(Character.getNumericValue(srcRegister.charAt(1))))
				.replace(' ', '0');

		// Convert the address to 16-bit binary representation
		String binaryAddress = String.format("%18s", Integer.toBinaryString(address)).replace(' ', '0');

		if (binaryAddress.length() == 32) {
			binaryAddress = binaryAddress.substring(14, 32);
		}
		// Combine the binary representations of the instruction components
		String binaryInstruction = "1011" + binaryDestRegister + binarySrcRegister + binaryAddress;

		return binaryInstruction;

	}

	public void displayRegisters() {
		for (int i = 0; i < generalRegisters.length; i++) {
			Register reg = generalRegisters[i];
			if (reg != null) {
				System.out.println("Register " + (i + 1) + ":");
				System.out.println("  Register ID: " + reg.getName());
				System.out.println("  Register Bits: " + reg.bitsString());

				System.out.println();
			}
		}
	}

	public void initializePipelineStages() {
		fetchStage = new ArrayList<>();
		decodeStage = new ArrayList<>();
		executeStage = new ArrayList<>();
		memoryStage = new ArrayList<>();
		writeBackStage = new ArrayList<>();
	}

	public void fetch() {

		if (jumpCheck) {
			jumpCheck = false;
		}

		if (clockCycles2 % 2 == 1) {

			Instruction instruction = mainMem.instr[Register.convertBinaryToDecimal(PC.bitsString())];
			if (lastInstruction != null) {
				System.out.println("PC before fetching is: " + Register.convertBinaryToDecimal(PC.bitsString()));
				PC.incrementPC();
				System.out.println("PC after fetching is: " + Register.convertBinaryToDecimal(PC.bitsString()));
			}
			if (instruction != null) {

				System.out.println("PC before fetching is: " + Register.convertBinaryToDecimal(PC.bitsString()));

				fetchStage.add(instruction);
				PC.incrementPC();
				System.out.println("PC after fetching is: " + Register.convertBinaryToDecimal(PC.bitsString()));
				Instruction isLast = mainMem.instr[(Register.convertBinaryToDecimal(PC.bitsString()))];
				if (isLast == null)
					lastInstruction = instruction;
				System.out.println("Fetch: " + fetchStage.get(fetchStage.size() - 1).getIstrID());

			}
		}
		if (clockCycles2 % 2 == 0) {
			System.out.println("Fetch: ");
		}

	}

	public void decode() {

		if (jumpCheck) {
			fetchStage = new ArrayList<>();
		}

		if (clockCycles2 < 2) {
			System.out.println("Decode: ");

		}

		if (!fetchStage.isEmpty() && clockCycles2 >= 2) {

			if (clockCycles % 2 == 0) {
				Instruction instruction = fetchStage.get(0);
				String opCode = instruction.opcodeString();

				switch (opCode) {
				case "0000":
				case "0001":
				case "0010":
				case "0101":
				case "1000":
				case "1001":
					rFormat rInstr = new rFormat(instruction.getIstrID());
					rInstr.setInstrBits(instruction.getInstrBits());
					instruction = rInstr;
					System.out.println("R type instruction: ");
					System.out.println("R1: " + "R" + Register.convertBinaryToDecimal(rInstr.r1String()));
					System.out.println("R2: " + "R" + Register.convertBinaryToDecimal(rInstr.r2String()));
					System.out.println("R3: " + "R" + Register.convertBinaryToDecimal(rInstr.r3String()));

					break;
				case "0011":
				case "0100":
				case "0110":
				case "1010":
				case "1011":
					iFormat iInstr = new iFormat(instruction.getIstrID());
					iInstr.setInstrBits(instruction.getInstrBits());
					instruction = iInstr;
					System.out.println("I type instruction: ");
					System.out.println("R1: " + "R" + Register.convertBinaryToDecimal(iInstr.r1String()));
					System.out.println("R2: " + "R" + Register.convertBinaryToDecimal(iInstr.r2String()));
					System.out.println("Immediate: " + ALU.convertBinaryToDecimal(iInstr.iString()));
					break;
				case "0111":
					jFormat jInstr = new jFormat(instruction.getIstrID());
					jInstr.setInstrBits(instruction.getInstrBits());
					instruction = jInstr;
					System.out.println("J type instruction: ");
					System.out.println("Jump Adress: " + ALU.convertBinaryToDecimal(jInstr.jString()));

					break;
				default:
					break;
				}

				decodeStage.add(instruction);
				System.out.println("Decode: " + decodeStage.get(decodeStage.size() - 1).getIstrID());
			} else {
				Instruction instruction = fetchStage.remove(0);
				String opCode = instruction.opcodeString();

				switch (opCode) {
				case "0000":
				case "0001":
				case "0010":
				case "0101":
				case "1000":
				case "1001":
					rFormat rInstr = new rFormat(instruction.getIstrID());
					rInstr.setInstrBits(instruction.getInstrBits());
					instruction = rInstr;
					System.out.println("R type instruction: ");
					System.out.println("R1: " + "R" + Register.convertBinaryToDecimal(rInstr.r1String()));
					System.out.println("R2: " + "R" + Register.convertBinaryToDecimal(rInstr.r2String()));
					System.out.println("R3: " + "R" + Register.convertBinaryToDecimal(rInstr.r3String()));

					break;
				case "0011":
				case "0100":
				case "0110":
				case "1010":
				case "1011":
					iFormat iInstr = new iFormat(instruction.getIstrID());
					iInstr.setInstrBits(instruction.getInstrBits());
					instruction = iInstr;
					System.out.println("I type instruction: ");
					System.out.println("R1: " + "R" + Register.convertBinaryToDecimal(iInstr.r1String()));
					System.out.println("R2: " + "R" + Register.convertBinaryToDecimal(iInstr.r2String()));
					System.out.println("Immediate: " + ALU.convertBinaryToDecimal(iInstr.iString()));
					break;
				case "0111":
					jFormat jInstr = new jFormat(instruction.getIstrID());
					jInstr.setInstrBits(instruction.getInstrBits());
					instruction = jInstr;
					System.out.println("J type instruction: ");
					System.out.println("Jump Adress: " + ALU.convertBinaryToDecimal(jInstr.jString()));

					break;
				default:
					break;
				}

				System.out.println("Decode: " + decodeStage.get(decodeStage.size() - 1).getIstrID());
			}
		}

	}

	public void execute() {
		if (jumpCheck) {
			decodeStage = new ArrayList<>();

		}

		if (clockCycles2 < 4) {
			System.out.println("Execute: ");

		}
		if (!decodeStage.isEmpty() && clockCycles2 >= 4) {
			if (clockCycles % 2 == 1) {
				Instruction instruction = decodeStage.remove(0);

				if (instruction instanceof rFormat) {
					rFormat rInstr = new rFormat(instruction.getIstrID());
					rInstr.setInstrBits(instruction.getInstrBits());
					int r2 = -1;
					int r3 = -1;
					Register temp = null;
					int register1Num = Register.convertBinaryToDecimal(rInstr.r1String());
					int register2Num = Register.convertBinaryToDecimal(rInstr.r2String());
					int register3Num = Register.convertBinaryToDecimal(rInstr.r3String());

					for (int i = 0; i < generalRegisters.length; i++) {
						if (generalRegisters[i].getName().equals("R" + register1Num)) {
							temp = new Register(generalRegisters[i].getName());
							temp.setBits(generalRegisters[i].getBits());

						}
						if (generalRegisters[i].getName().equals("R" + register2Num))
							r2 = i;
						if (generalRegisters[i].getName().equals("R" + register3Num))
							r3 = i;
					}

					System.out.println("R type instruction is executing ");
					System.out.println("R1: " + "R" + Register.convertBinaryToDecimal(rInstr.r1String())
							+ " with value " + (temp == null ? ALU.convertBinaryToDecimal(zereRegister.bitsString())
									: ALU.convertBinaryToDecimal(temp.bitsString())));
					System.out.println("R2: " + "R" + Register.convertBinaryToDecimal(rInstr.r2String())
							+ " with value " + (r2 == -1 ? ALU.convertBinaryToDecimal(zereRegister.bitsString())
									: ALU.convertBinaryToDecimal(generalRegisters[r2].bitsString())));
					System.out.println("R3: " + "R" + Register.convertBinaryToDecimal(rInstr.r2String())
							+ " with value " + (r3 == -1 ? ALU.convertBinaryToDecimal(zereRegister.bitsString())
									: ALU.convertBinaryToDecimal(generalRegisters[r3].bitsString())));

					executeRInstr(rInstr, temp == null ? new Register("R0") : temp,
							r2 == -1 ? zereRegister : generalRegisters[r2],
							r3 == -1 ? zereRegister : generalRegisters[r3]);

				}

				else if (instruction instanceof iFormat) {
					iFormat iInstr = new iFormat(instruction.getIstrID());
					iInstr.setInstrBits(instruction.getInstrBits());
					int rI2 = -1;
					Register tempI = null;
					int register1Num = Register.convertBinaryToDecimal(iInstr.r1String());
					int register2Num = Register.convertBinaryToDecimal(iInstr.r2String());

					for (int i = 0; i < generalRegisters.length; i++) {
						if (generalRegisters[i].getName().equals("R" + register1Num)) {
							tempI = new Register(generalRegisters[i].getName());
							tempI.setBits(generalRegisters[i].getBits());

						}
						if (generalRegisters[i].getName().equals("R" + register2Num))
							rI2 = i;
					}

					System.out.println("I type instruction is executing ");
					System.out.println("R1: " + "R" + Register.convertBinaryToDecimal(iInstr.r1String())
							+ " with value " + (tempI == null ? ALU.convertBinaryToDecimal(zereRegister.bitsString())
									: ALU.convertBinaryToDecimal(tempI.bitsString())));
					System.out.println("R2: " + "R" + Register.convertBinaryToDecimal(iInstr.r2String())
							+ " with value " + (rI2 == -1 ? ALU.convertBinaryToDecimal(zereRegister.bitsString())
									: ALU.convertBinaryToDecimal(generalRegisters[rI2].bitsString())));
					System.out.println("Immediate value is " + ALU.convertBinaryToDecimal(iInstr.iString()));

					executeIInstr(iInstr, tempI == null ? new Register("R0") : tempI,
							rI2 == -1 ? zereRegister : generalRegisters[rI2]);

				} else {
					jFormat jInstr = new jFormat(instruction.getIstrID());
					jInstr.setInstrBits(instruction.getInstrBits());
					System.out.println("J instruction is executing ");
					System.out.println("Jump Adress is " + ALU.convertBinaryToDecimal(jInstr.jString()));
					executeJInstr(jInstr);
				}
				// executeStage.add(instruction);
				System.out.println("Execute: " + instruction.getIstrID());
			}

			else {
				Instruction instruction = decodeStage.get(0);

				if (instruction instanceof rFormat) {
					rFormat rInstr = new rFormat(instruction.getIstrID());
					rInstr.setInstrBits(instruction.getInstrBits());
					int r2 = -1;
					int r3 = -1;
					Register temp = null;
					int register1Num = Register.convertBinaryToDecimal(rInstr.r1String());
					int register2Num = Register.convertBinaryToDecimal(rInstr.r2String());
					int register3Num = Register.convertBinaryToDecimal(rInstr.r3String());

					for (int i = 0; i < generalRegisters.length; i++) {
						if (generalRegisters[i].getName().equals("R" + register1Num)) {
							temp = new Register(generalRegisters[i].getName());
							temp.setBits(generalRegisters[i].getBits());

						}
						if (generalRegisters[i].getName().equals("R" + register2Num))
							r2 = i;
						if (generalRegisters[i].getName().equals("R" + register3Num))
							r3 = i;
					}

					System.out.println("R type instruction is executing ");
					System.out.println("R1: " + "R" + Register.convertBinaryToDecimal(rInstr.r1String())
							+ " with value " + (temp == null ? ALU.convertBinaryToDecimal(zereRegister.bitsString())
									: ALU.convertBinaryToDecimal(temp.bitsString())));
					System.out.println("R2: " + "R" + Register.convertBinaryToDecimal(rInstr.r2String())
							+ " with value " + (r2 == -1 ? ALU.convertBinaryToDecimal(zereRegister.bitsString())
									: ALU.convertBinaryToDecimal(generalRegisters[r2].bitsString())));
					System.out.println("R3: " + "R" + Register.convertBinaryToDecimal(rInstr.r2String())
							+ " with value " + (r3 == -1 ? ALU.convertBinaryToDecimal(zereRegister.bitsString())
									: ALU.convertBinaryToDecimal(generalRegisters[r3].bitsString())));

				}

				else if (instruction instanceof iFormat) {
					iFormat iInstr = new iFormat(instruction.getIstrID());
					iInstr.setInstrBits(instruction.getInstrBits());
					int rI2 = -1;
					Register tempI = null;
					int register1Num = Register.convertBinaryToDecimal(iInstr.r1String());
					int register2Num = Register.convertBinaryToDecimal(iInstr.r2String());

					for (int i = 0; i < generalRegisters.length; i++) {
						if (generalRegisters[i].getName().equals("R" + register1Num)) {
							tempI = new Register(generalRegisters[i].getName());
							tempI.setBits(generalRegisters[i].getBits());

						}
						if (generalRegisters[i].getName().equals("R" + register2Num))
							rI2 = i;
					}

					System.out.println("I type instruction is executing ");
					System.out.println("R1: " + "R" + Register.convertBinaryToDecimal(iInstr.r1String())
							+ " with value " + (tempI == null ? ALU.convertBinaryToDecimal(zereRegister.bitsString())
									: ALU.convertBinaryToDecimal(tempI.bitsString())));
					System.out.println("R2: " + "R" + Register.convertBinaryToDecimal(iInstr.r2String())
							+ " with value " + (rI2 == -1 ? ALU.convertBinaryToDecimal(zereRegister.bitsString())
									: ALU.convertBinaryToDecimal(generalRegisters[rI2].bitsString())));
					System.out.println("Immediate value is " + ALU.convertBinaryToDecimal(iInstr.iString()));

				} else {
					jFormat jInstr = new jFormat(instruction.getIstrID());
					jInstr.setInstrBits(instruction.getInstrBits());
					System.out.println("J instruction is executing ");
					System.out.println("Jump Adress is " + ALU.convertBinaryToDecimal(jInstr.jString()));
				}

				executeStage.add(instruction);
				System.out.println("Execute: " + executeStage.get(executeStage.size() - 1).getIstrID());
			}

		}
	}

	public void executeRInstr(rFormat instr, Register R1, Register R2, Register R3) {
		String opCode = instr.opcodeString();

		int shamt = Register.convertBinaryToDecimal(instr.shamtString());

		switch (opCode) {
		case "0000":
			ALU.ADD(R1, R2, R3);
			resultALU = R1;

			break;
		case "0001":
			ALU.SUB(R1, R2, R3);
			resultALU = R1;

			break;
		case "0010":
			ALU.MUL(R1, R2, R3);
			resultALU = R1;

			break;
		case "0101":
			ALU.AND(R1, R2, R3);
			resultALU = R1;

			break;
		case "1000":
			ALU.LSL(R1, R2, shamt);
			resultALU = R1;

			break;
		case "1001":
			ALU.LSR(R1, R2, shamt);
			resultALU = R1;

			break;
		default:
			break;
		}

	}

	public void executeIInstr(iFormat instr, Register R1, Register R2) {
		String opCode = instr.opcodeString();

		char[] imm = instr.getIValue();
		// System.out.println(new String(imm));

		switch (opCode) {
		case "0011":
			ALU.MOVI(R1, imm);
			resultALU = R1;
			break;
		case "0100":
			forJump.setBits(PC.getBits());
			ALU.JEQ(forJump, R1, R2, imm);
			break;
		case "0110":
			ALU.XORI(R1, R2, imm);
			resultALU = R1;
			break;
		case "1010":
		case "1011":
			break;
		default:
			break;
		}

	}

	public void executeJInstr(jFormat instr) {

		char[] jAddress = instr.getjAddress();
		forJump.setBits(PC.getBits());
		ALU.JMP(forJump, jAddress);

	}

	public void memory() {
		if (jumpCheck) {
			executeStage = new ArrayList<>();
		}
		if (clockCycles2 < 6 || clockCycles2 % 2 == 1) {
			System.out.println("Memory: ");

		}

		if (!executeStage.isEmpty() && clockCycles2 >= 6 && clockCycles2 % 2 == 0) {
			Instruction instruction = executeStage.remove(0);
			iFormat instr = null;

			if (instruction instanceof iFormat) {
				instr = (iFormat) instruction;
				String opCode = instr.opcodeString();
				char[] imm = instr.getIValue();
				int r1 = -1;
				int r2 = -1;
				int register1Num = Register.convertBinaryToDecimal(instr.r1String());
				int register2Num = Register.convertBinaryToDecimal(instr.r2String());

				for (int i = 0; i < generalRegisters.length; i++) {
					if (generalRegisters[i].getName().equals("R" + register1Num)) {
						r1 = i;
					}
					if (generalRegisters[i].getName().equals("R" + register2Num))
						r2 = i;
				}

				switch (opCode) {
				case "1010":
					ALU.MOVR(mainMem, r1 == -1 ? zereRegister : generalRegisters[r1],
							r2 == -1 ? zereRegister : generalRegisters[r2], imm);
					break;
				case "1011":
					ALU.MOVM(mainMem, r1 == -1 ? zereRegister : generalRegisters[r1],
							r2 == -1 ? zereRegister : generalRegisters[r2], imm);
					break;
				default:
					break;
				}
			}
			memoryStage.add(instruction);
			System.out.println("Memory: " + instruction.getIstrID());
		}
	}

	public void writeBack() {

		if (clockCycles2 < 7 || clockCycles2 % 2 == 0) {
			System.out.println("Write Back: ");

		}

		if (!memoryStage.isEmpty() && clockCycles2 >= 7 && clockCycles2 % 2 == 1) {
			Instruction instruction = memoryStage.remove(0);
			String opCode = instruction.opcodeString();

			switch (opCode) {
			case "0000":
			case "0001":
			case "0010":
			case "0101":
			case "1000":
			case "1001":
			case "0011":
			case "0110":
				for (int i = 0; i < generalRegisters.length; i++) {
					if (generalRegisters[i].getName().equals(resultALU.getName())) {
						generalRegisters[i].setBits(resultALU.getBits());
						System.out.println(generalRegisters[i].getName() + " was updated in WB stage with value "
								+ ALU.convertBinaryToDecimal(generalRegisters[i].bitsString()));
					}

				}
				break;
			case "0100":
				jumpCheck = true;
				// System.out.println(Register.convertBinaryToDecimal(PC.bitsString()));
				// System.out.println(Register.convertBinaryToDecimal(forJump.bitsString()));
				// PC.setBits(forJump.getBits());
				if (lastInstruction == null) {
					PC.setBits(forJump.getBits());
					System.out.println("PC was updated for JEQ in WB stage with value "
							+ ALU.convertBinaryToDecimal(PC.bitsString()));

				} else if (!(lastInstruction.equals(instruction))) {
					PC.setBits(forJump.getBits());
					System.out.println("PC was updated for JEQ in WB stage with value "
							+ ALU.convertBinaryToDecimal(PC.bitsString()));

				}

				// System.out.println(Register.convertBinaryToDecimal(PC.bitsString()));
				// System.out.println(Register.convertBinaryToDecimal(forJump.bitsString()));

				clockCycles2 = 1;
				break;
			case "0111":
				jumpCheck = true;
				PC.setBits(forJump.getBits());
				System.out.println(
						"PC was updated for JMP in WB stage with value" + ALU.convertBinaryToDecimal(PC.bitsString()));

				clockCycles2 = 1;
				break;
			default:
				break;
			}

			writeBackStage.add(instruction);
			System.out.println("Write Back: " + instruction.getIstrID());
		}
	}

	public boolean pipelineIsEmpty() {
		return fetchStage.isEmpty() && decodeStage.isEmpty() && executeStage.isEmpty() && memoryStage.isEmpty();
	}

	public void startProgram() {

		initializePipelineStages(); // Initialize the pipeline stages

		clockCycles = 1;
		clockCycles2 = 1;

		// System.out.println(Register.convertBinaryToDecimal(forJump.toString()));

		while (mainMem.instr[Register.convertBinaryToDecimal(PC.bitsString())] != null || !(pipelineIsEmpty())) {

			// System.out.println(Register.convertBinaryToDecimal(PC.bitsString()));
			System.out.println("Cycle " + clockCycles);
			writeBack();
			memory();
			execute();
			decode();
			fetch();

			clockCycles2++;
			clockCycles++;

			System.out.println("----------");

		}

		displayRegisters();
		mainMem.displayInstructions();
		mainMem.displayData();		

	}

	public static void main(String[] args) {
		processor2 cpu = new processor2();
		cpu.parseProgram();
		cpu.startProgram();

	}

}
