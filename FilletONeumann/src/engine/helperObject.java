package engine;

import mainMem.*;
import regs.*;

public class helperObject {
	
	public Instruction instr;
	public Register R1;
	public Register R2;
	
	public helperObject(Instruction instr, Register R1) {
		this.instr=instr;
		this.R1=R1;
		this.R2=null;


	}
	
	public helperObject(Instruction instr, Register R1, Register R2) {
		this.instr=instr;
		this.R1=R1;
		this.R2=R2;


	}

}
