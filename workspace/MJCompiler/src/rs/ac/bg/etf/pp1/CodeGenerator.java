package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {
	
	private int mainPC;
	private boolean isWhileWithStatement = false;
	private Map<String, Integer> mapa = new HashMap<>();
	private int currArrLength;
	private Struct setType = Tab.find("set").getType();
	Obj printMeth;
	Obj unionAddAllMeth;
	Obj mapInternalMethod;
	int patchMapAddr = 0;
	
	private void initBuiltInMethods() {
		Obj ordMeth = Tab.find("ord");
		Obj chrMeth = Tab.find("chr");
		Obj lenMeth = Tab.find("len");
		Obj addMeth = Tab.find("add");
		printMeth = Tab.find("printSetInternalMeth");
		Obj addAllMeth = Tab.find("addAll");
		unionAddAllMeth = Tab.find("unionAddAllInternalMethod");
		mapInternalMethod = Tab.find("mapInternalMethod");
		
		ordMeth.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		chrMeth.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		lenMeth.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.arraylength);
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		
//		void setF(int arr[], int a) int i, arrlength, curr_free;{
//			//fali ti da proveris da li je curr_free slucajno veci od arraysize
//			curr_free = arr[arrlength-1];
//			i = 0;
//			do{
//				if(arr[i] == a) return;
//				i++;
//			}while(i<curr_free);
//			arr[curr_free] = a; //dodaj rojac prvog slobodnog mesta da bude prvi/poslednji elem u nizu
//			arr[arrlength-1]++;
//		}
		
		addMeth.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(2);
		Code.put(5);
		//var 0 je set(arr), var 1 je broj koji se dodaje, var 2 je i(cnt), var 3 je arr length
		Code.put(Code.load_n); // potrebno za arraylength
		Code.put(Code.arraylength); // skidam arrayLength
		Code.put(Code.store_3); //storuj u trecu varijablu (arrayLength)
		//U TRECOJ VARIJABLI JE ARR LENGTH
		//kod za print:
//		Code.put(Code.load_3);
//		Code.loadConst(0);
//		Code.put(Code.print);
		
		//curr_free = arr[arrlength-1];
		Code.put(Code.load_n);
		Code.put(Code.load_3);
		Code.put(Code.const_1);
		Code.put(Code.sub);
		Code.put(Code.aload);
		Code.put(Code.store);
		Code.put(4);
		
		//i = 0
		Code.loadConst(0);
		Code.put(Code.store_2);
		
//		do {
//			if(set[i] == br) return;
//			i++
//		}while(i<curr_free);
		//set[i] == br
		Code.put(Code.load_n);
		Code.put(Code.load_2);
		Code.put(Code.aload);
		Code.put(Code.load_1);
		//Code.put(Code.jcc + Code.inverse[Code.eq]); Code.put2(6); 
		//if
		Code.putFalseJump(Code.eq, Code.pc + 6);
		Code.putJump(Code.pc + 6);
		Code.putJump(Code.pc + 5);
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		//i++
		Code.put(Code.load_2);          
		Code.put(Code.const_1);         
		Code.put(Code.add);             
		Code.put(Code.store_2);   
		
		//while(i<curr_free)
		Code.put(Code.load_2);          
		Code.put(Code.load);
		Code.put(4);
		Code.putFalseJump(Code.lt, Code.pc + 6);
		Code.putJump(Code.pc + 6);
		Code.putJump(Code.pc + 6);
		Code.putJump(Code.pc - 31);
		
		//arr[arrlength] = a;
		Code.put(Code.load_n);    
		Code.put(Code.load);
		Code.put(4); 
		Code.put(Code.load_1);   
		Code.put(Code.astore);   
		
		Code.put(Code.load_n);
		Code.put(Code.load_3);
		Code.put(Code.const_1);
		Code.put(Code.sub);
		Code.put(Code.dup2);
		Code.put(Code.aload);
		Code.put(Code.const_1);
		Code.put(Code.add);
		Code.put(Code.astore);

		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		
//		void mojprint(int set1[]) int i, arraylength;{
//			i = 0;
//			do{
//				print(set1[i]);
//				i++;
//			}while(i<set1[arraylength]); // ovo je set1[arraylength-1]
//		}
		
		printMeth.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(3);
		Code.put(Code.load_n); // potrebno za arraylength
		Code.put(Code.arraylength); // skidam arrayLength
		//ovde nalazimo arraylength - 1 jer nam je to potrebno da pristupimo onom nasem brojacu
		Code.put(Code.const_1);
		Code.put(Code.sub);
		Code.put(Code.store_2); //storuj u drugu varijablu (arrayLength)
		//i = 0
		Code.put(Code.const_n);
		Code.put(Code.store_1);
		//print(set1[i]);
		Code.put(Code.load_n);
		Code.put(Code.load_1);
		Code.put(Code.aload);
		Code.put(Code.const_n);
		Code.put(Code.print);
		//i++;
		Code.put(Code.load_1);
		Code.put(Code.const_1);
		Code.put(Code.add);
		Code.put(Code.store_1);
		//while(i<set1[arraylength]);
		Code.put(Code.load_1);
		Code.put(Code.load_n);
		Code.put(Code.load_2);
		Code.put(Code.aload);
		//while jump
		Code.putFalseJump(Code.lt, Code.pc + 6);
		Code.putJump(Code.pc + 6);
		Code.putJump(Code.pc + 6);
		Code.putJump(Code.pc - 22);
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		
//		void addAllMoj(set set1, int arr[]) int i, arrlength;{
//			i = 0;
//			do{
//				add(set1, arr[i]);
//				i++;
//			}while(i<arrlength);
//		}
		
		addAllMeth.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(2);
		Code.put(4);
		//var 0 je set(arr), var 1 je niz koji se dodaje, var 2 je i(cnt), var 3 je arr length
		Code.put(Code.load_1); // potrebno za arraylength
		Code.put(Code.arraylength); // skidam arrayLength
		Code.put(Code.store_3); //storuj u trecu varijablu (arrayLength)
		
		//i=0
		Code.put(Code.const_n);
		Code.put(Code.store_2);
		
		//add(set1, arr[i]);
		Code.put(Code.load_n);
		Code.put(Code.load_1);
		Code.put(Code.load_2);
		Code.put(Code.aload);
		int adr = addMeth.getAdr();
		int offset = adr - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		//i++
		Code.put(Code.load_2);
		Code.put(Code.const_1);
		Code.put(Code.add);
		//while(i<arrlength);
		Code.put(Code.store_2);
		Code.put(Code.load_2);
		Code.put(Code.load_3);
		Code.putFalseJump(Code.lt, Code.pc + 6);
		Code.putJump(Code.pc + 6);
		Code.putJump(Code.pc + 6);
		Code.putJump(Code.pc - 22);

		Code.put(Code.exit);
		Code.put(Code.return_);
		
//		void addAllUnion(set set3, int set1[], int set2[]) int i, arrlength;{
//			i = 0;
//			do{
//				add(set3, set1[i]);
//				i++;
//			}while(i<set1[arrlength - 1]);
//			
//			i = 0;
//			do{
//				add(set3, set2[i]);
//				i++;
//			}while(i<set2[arrlength - 1]);
//		}

		
		unionAddAllMeth.setAdr(Code.pc);		
		Code.put(Code.enter);
		Code.put(3);
		Code.put(5);
		//var 0 je set(arr), var 1 je niz koji se dodaje, var 2 je i(cnt), var 3 je arr length
		Code.put(Code.load_1); // potrebno za arraylength
		Code.put(Code.arraylength); // skidam arrayLength
		//ovde nalazimo arraylength - 1 jer nam je to potrebno da pristupimo onom nasem brojacu
		Code.put(Code.const_1);
		Code.put(Code.sub);
		Code.put(Code.store); //storuj u trecu varijablu (arrayLength)
		Code.put(4);

		//i=0
		Code.put(Code.const_n);
		Code.put(Code.store_3);
		
		//add(set3, arr[i]);
		Code.put(Code.load_n);
		Code.put(Code.load_1);
		Code.put(Code.load_3);
		Code.put(Code.aload);
		adr = addMeth.getAdr();
		offset = adr - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		//i++
		Code.put(Code.load_3);
		Code.put(Code.const_1);
		Code.put(Code.add);
		//while(i<set1[arrlength - 1]);
		Code.put(Code.store_3);
		Code.put(Code.load_3);
		Code.put(Code.load_1);
		Code.put(Code.load);
		Code.put(4);
		Code.put(Code.aload);
		Code.putFalseJump(Code.lt, Code.pc + 6);
		Code.putJump(Code.pc + 6);
		Code.putJump(Code.pc + 6);
		Code.putJump(Code.pc - 25);
		
		//ubacujem u arraylength velicinu set2
		Code.put(Code.load_2); // potrebno za arraylength
		Code.put(Code.arraylength); // skidam arrayLength
		//ovde nalazimo arraylength - 1 jer nam je to potrebno da pristupimo onom nasem brojacu
		Code.put(Code.const_1);
		Code.put(Code.sub);
		Code.put(Code.store); //storuj u trecu varijablu (arrayLength)
		Code.put(4);

		
		//i=0
		Code.put(Code.const_n);
		Code.put(Code.store_3);
		
		//add(set3, arr[i]);
		Code.put(Code.load_n);
		Code.put(Code.load_2);
		Code.put(Code.load_3);
		Code.put(Code.aload);
		adr = addMeth.getAdr();
		offset = adr - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		//i++
		Code.put(Code.load_3);
		Code.put(Code.const_1);
		Code.put(Code.add);
		//while(i<set1[arrlength - 1]);
		Code.put(Code.store_3);
		Code.put(Code.load_3);
		Code.put(Code.load_2);
		Code.put(Code.load);
		Code.put(4);
		Code.put(Code.aload);
		Code.putFalseJump(Code.lt, Code.pc + 6);
		Code.putJump(Code.pc + 6);
		Code.putJump(Code.pc + 6);
		Code.putJump(Code.pc - 25);
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
//		int mojMap(int niz[]) int i, arraylength, sum, returnValue; {
//			i=0;
//			returnValue = 0;
//			do{
//				sum = f1(niz[i]);
//				returnValue = returnValue + sum;
//				i++;
//			}while(i<arraylength);
//			
//			return returnValue;
//		}
		
		mapInternalMethod.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(5);
		
		Code.put(Code.load_n); // potrebno za arraylength
		Code.put(Code.arraylength); // skidam arrayLength
		Code.put(Code.store_2); //storuj u drugu varijablu (arrayLength)
		
		//i = 0
		Code.put(Code.const_n);
		Code.put(Code.store_1);
		
		//returnValue = 0
		Code.put(Code.const_n);
		Code.put(Code.store);
		Code.put(4);
		
		//f1(niz[i]); Samo ovde moram da uradim fixup
		Code.put(Code.load_n);
		Code.put(Code.load_1);
		Code.put(Code.aload);
		
		Code.put(Code.call);
		Code.put2(0);
		patchMapAddr = Code.pc - 2;
		
		
		//returnValue = returnValue + sum;
		Code.put(Code.store_3);
		Code.put(Code.load);
		Code.put(4);
		Code.put(Code.load_3);
		Code.put(Code.add);
		Code.put(Code.store);
		Code.put(4);
		
		//i++
		Code.put(Code.load_1);
		Code.put(Code.const_1);
		Code.put(Code.add);
		Code.put(Code.store_1);
		
		//(i<arrayLength)
		Code.put(Code.load_1);
		Code.put(Code.load_2);
		
		Code.putFalseJump(Code.lt, Code.pc + 6);
		Code.putJump(Code.pc + 6);
		Code.putJump(Code.pc + 6);
		Code.putJump(Code.pc - 28);
		
		Code.put(Code.load);
		Code.put(4);
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	CodeGenerator() {
		initBuiltInMethods();
	}
	
	public int getmainPc() {
		return this.mainPC;
	}

	/* METHOD DECLARATIONS */
	@Override
	public void visit(MethodTypeAndNameVoid methodTypeAndNameVoid) {
		methodTypeAndNameVoid.obj.setAdr(Code.pc);
		if(methodTypeAndNameVoid.getI1().equalsIgnoreCase("main")) {
			mainPC = Code.pc;
		}
		
		Code.put(Code.enter);
		Code.put(methodTypeAndNameVoid.obj.getLevel()); //b1
		Code.put(methodTypeAndNameVoid.obj.getLocalSymbols().size()); //b2
	}
	
	@Override
	public void visit(MethodTypeAndNameNonVoid methodTypeAndNameNonVoid) {
		methodTypeAndNameNonVoid.obj.setAdr(Code.pc);
		if(methodTypeAndNameNonVoid.getI2().equalsIgnoreCase("main")) {
			mainPC = Code.pc;
		}
		
		Code.put(Code.enter);
		Code.put(methodTypeAndNameNonVoid.obj.getLevel()); //b1
		Code.put(methodTypeAndNameNonVoid.obj.getLocalSymbols().size()); //b2
	}
	
	@Override
	public void visit(MethodDecl methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	/* PRINT DECL */
	
	@Override
	public void visit(StatementPrintExpr statementPrintExpr) {
		if(statementPrintExpr.getExpr().struct != setType) {
			Code.loadConst(0);
			if(statementPrintExpr.getExpr().struct.equals(Tab.charType)) {
				Code.put(Code.bprint);
			}else {
				Code.put(Code.print);
			}
		}else {
			int adr = printMeth.getAdr();
			int offset = adr - Code.pc;
			Code.put(Code.call);
			Code.put2(offset);
		}
	}
	
	@Override
	public void visit(StatementPrintSet statementPrintSet) {
		Code.loadConst(statementPrintSet.getN2());
		if(statementPrintSet.getExpr().struct.equals(Tab.charType)) {
			Code.put(Code.bprint);
		}else {
			Code.put(Code.print);
		}
	}
	
	/* EXPR */
	
	@Override
	public void visit(ExprMultipleTerm exprMultipleTerm) {
		if(exprMultipleTerm.getAddop() instanceof AddopAdd) {
			Code.put(Code.add);
		}else if(exprMultipleTerm.getAddop() instanceof AddopSubtract) {
			Code.put(Code.sub);
		}
	}
	
	@Override
	public void visit(TermMul termMul) {
		if(termMul.getMulop() instanceof MulopMultiply) {
			Code.put(Code.mul);
		}else if(termMul.getMulop() instanceof MulopDiv) {
			Code.put(Code.div);
		}else if(termMul.getMulop() instanceof MulopMod) {
			Code.put(Code.rem);
		}
	}
	
	//ovo radi na drugi nacin, ali mi nije jasno zasto ne moze ovako? Gen koda 9 impl 3 19:00
	@Override
	public void visit(ExprSingleTermMinus exprSingleTermMinus) {
		Code.put(Code.neg);
	}
	
	@Override
	public void visit(FactorNewArray factorNewArray) {
		if(factorNewArray.struct != setType) {
			Code.put(Code.newarray);
			Type tip = factorNewArray.getType();
			if(tip.struct.equals(Tab.charType)) {
				Code.put(0);
			}else {
				Code.put(1);
			}
		}else {
			Code.loadConst(1);
			Code.put(Code.add);
			Code.put(Code.newarray);
			Code.put(1);
		}
		
	}
	
	@Override
	public void visit(FactorNum factorNum) {
		Code.loadConst(factorNum.getN1());
	}
	
	@Override
	public void visit(FactorChar factorChar) {
		Code.loadConst(factorChar.getC1());
	}
	
	@Override
	public void visit(FactorBool factorBool) {
		Code.loadConst(factorBool.getB1());
	}
	

	@Override
	public void visit(FactorDesignator factorDesignator) {
		Code.load(factorDesignator.getDesignator().obj);
	}
	
	@Override
	public void visit(FactorDesignatorMethod factorDesignatorMethod) {
		int adr = factorDesignatorMethod.getFunctionName().getDesignator().obj.getAdr();
		int offset = adr - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}
	
	/* DESIGNATORS */

	//ovo je objasnjeno gen koda 9 impl 3 na 8. minuti
	@Override
	public void visit(DesignatorArrayName designatorArrayName) {
		Code.load(designatorArrayName.getDesignator().obj);
	}
	
	@Override
	public void visit(DesignatorStatementAssign designatorStatementAssign) {
		Code.store(designatorStatementAssign.getDesignator().obj);
	}

	@Override
	public void visit(DesignatorStatementIncrement designatorStatementIncrement) {
		System.out.println("Usao sam u Inkrement");
		System.out.println(Code.pc);
		Designator des = designatorStatementIncrement.getDesignator();
		if(des.obj.getKind() == Obj.Elem) { // obj 10. snimak impl 11:00
			//kada vracamo vrednost u store, moramo da imamo i adresu niza, koja se gubi u narednim instrukcijama
			//sa steka
			Code.put(Code.dup2);
		}
		Code.load(des.obj);
		Code.loadConst(1); // sta bi ovde bilo da je put 1? - put je za instr, load je za vrednosti na stek
		Code.put(Code.add);
		Code.store(des.obj);
	}
	
	@Override
	public void visit(DesignatorStatementDecrement designatorStatementDecrement) {
		Designator des = designatorStatementDecrement.getDesignator();
		if(des.obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(des.obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(des.obj);
		
	}
	
	@Override
	public void visit(DesignatorStatementFunctionCall designatorStatementFunctionCall) {
		int adr = designatorStatementFunctionCall.getFunctionName().getDesignator().obj.getAdr();
		int offset = adr - Code.pc ;
		Code.put(Code.call);
		Code.put2(offset);
		
		if(designatorStatementFunctionCall.getFunctionName().getDesignator().obj.getType() != Tab.noType) {
			Code.put(Code.pop);
		}
	}
	
	/* STATEMENTS */
	@Override
	public void visit(StatementReturn statementReturn) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	@Override
	public void visit(StatementReturnExpr statementReturnExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	@Override
	public void visit(StatementRead statementRead) {
		if(statementRead.getDesignator().obj.getType().equals(Tab.charType)) {
			Code.put(Code.bread);
		}else {
			Code.put(Code.read);
		}
		Code.store(statementRead.getDesignator().obj);
	}
	
	@Override
	public void visit(DesignatorStatementAssignop designatorStatementAssignop) {
		Obj setDestination = designatorStatementAssignop.getDesignator().obj;
		Obj set1stOperand = designatorStatementAssignop.getDesignator1().obj;
		Obj set2ndOperand = designatorStatementAssignop.getDesignator2().obj;
		
		
		Code.load(setDestination);
		Code.load(set1stOperand);
		Code.load(set2ndOperand);
		
		int adr = unionAddAllMeth.getAdr(); //promeni ovo na drugu adresu
		int offset = adr - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}
	
	/* CONDITIONS */
	private int returnRelOp(Relop relop) {
		if(relop instanceof RelopEquality) {
			return Code.eq;
		}else if(relop instanceof RelopInequality) {
			return Code.ne;
		}else if(relop instanceof RelopGreater) {
			return Code.gt;
		}else if(relop instanceof RelopGreaterEqual) {
			return Code.ge;
		}else if(relop instanceof RelopLess) {
			return Code.lt;
		}else if(relop instanceof RelopLessEqual) {
			return Code.le;
		}else {
			return 0;
		}
			
	}

	//NAUCI OVO I ISPROGRAMIRAJ SAM
	
	//promeni imena promenljivih
	private Stack<Integer> skipCondFact = new Stack<>();
	private Stack<Integer> skipCondition = new Stack<>();
	private Stack<Integer> skipThen = new Stack<>();
	private Stack<Integer> skipElse = new Stack<>();
	
	@Override
	public void visit(CondFactSingleExpr condFactSingleExpr) {
		System.out.println("Usao sam u condFactSingleExpr");
		System.out.println(Code.pc);
		Code.loadConst(0); //false vrednost
		//netacna
		Code.putFalseJump(Code.ne, 0); //inverz od COde.ne je eq, skacem ukoliko su ekvivalentni (sa false vrednoscu)
		skipCondFact.push(Code.pc - 2); //ako je vrednost ekv sa nulom,ja skacem
		//tacna
	}
	
	@Override
	public void visit(CondFactDoubleExpr condFactDoubleExpr) {
		System.out.println("Usao sam u condFactDoubleExpr");
		System.out.println(Code.pc);
		//netacna
		Code.putFalseJump(returnRelOp(condFactDoubleExpr.getRelop()), 0); //inverz od COde.ne je eq, skacem ukoliko su      
		//ekvivalentni
		skipCondFact.push(Code.pc - 2); //ako je vrednost ekv sa nulom,ja skacem
		//tacna		
	}
	
	@Override
	public void visit(CondTermRecursion condTermRecursion) {
		
	}
	
	@Override
	public void visit(CondTerm condTerm) {
		System.out.println("Usao sam u condTerm");
		System.out.println(Code.pc);
		System.out.println("Moja trenutna vrednost isWhileWithStatement je ");
		System.out.println(isWhileWithStatement);
		if(isWhileWithStatement == false) { //zbog ovoga ne radi && u ifu, moze da se stavi neki dummy na ifu tako da on
			//postavi vrednost ovoga na false, i onda vrati na true?
			//tacne
			Code.putJump(0); //tacne bacamo na THEN, skaceom na then
			skipCondition.push(Code.pc - 2); //pamtimo tacne
			//ovde vracam netacne
			while(!skipCondFact.empty()) {
				Code.fixup(skipCondFact.pop());
			}
		}
		//netacne nastavljaju na ostatak orova
	}
	
	@Override
	public void visit(IfCondition ifCondition) {
		//netacni
		Code.putJump(0); //netacni idu na else
		skipThen.push(Code.pc - 2);
		//THEN
		while(!skipCondition.empty()) {
			Code.fixup(skipCondition.pop());
		}
		//tacne
	}
	
	@Override
	public void visit(IfStartDummy ifStartDummy) {
		System.out.println("USAO SAM U IFSTARTDUMMY");
		
		//OVO MI NE TREBA JER ja postavljam onaj true ili false tek kada dodjem do conditiona, i zato on izgenerise kod za if bez problema
		//ne znam kako radi bez ovog isWhileWithStatement na false
		//isWhileWithStatement = false;
	}
	
	@Override
	public void visit(ElseConditionEpsilon elseConditionEpsilon) {
		//tacne
		Code.fixup(skipThen.pop());
		//tacne + netacne
	}
	
	@Override
	public void visit(Else else_) {
		//tacne
		Code.putJump(0); //tacne bacamo na kraj else
		skipElse.push(Code.pc - 2); //adresa svih niti koje preskacu else
		Code.fixup(skipThen.pop());
		//netacne
	}
	
	@Override
	public void visit(ElseConditionExists elseConditionExists) {
		//netacne
		Code.fixup(skipElse.pop()); //ovde vracamo tacne koje su preskocili else
		//netacne + tacne
	}
	
	
	private Stack<Integer> doBegin = new Stack<>();
	
	@Override
	public void visit(DoWhileStartDummy doWhileStartDummy) {
		System.out.println("Usao sam u doWhileStartDummy");
		System.out.println(Code.pc);
		doBegin.push(Code.pc);
		breakJumps.push(new ArrayList<Integer>());
		continueJumps.push(new ArrayList<Integer>());
	}
	
	//ovde treba da napravis i onaj drugi while
	
	@Override
	public void visit(WhileConditionsNoStatement whileConditionsNoStatement) {
		//netacni
		Code.putJump(0); //netacni idu na else
		skipThen.push(Code.pc - 2);
		//THEN
		while(!skipCondition.empty()) {
			Code.fixup(skipCondition.pop());
		}
		//tacne
	}
	
	@Override
	public void visit(WhileConditionsStatement whileConditionsStatement) {
		//netacni
		System.out.println("Usao sam u whileConditionsStatement");
		System.out.println(Code.pc);
		Code.putJump(0); //netacni idu na else
		skipThen.push(Code.pc - 2);
		//THEN
		while(!skipCondition.empty()) {
			Code.fixup(skipCondition.pop());
		}
		//tacne
	}
	
	@Override
	public void visit(StatementWhile statementWhile) {
		System.out.println("Usao sam u statementWhile");
		System.out.println(Code.pc);
		Code.putJump(doBegin.pop());
		Code.fixup(skipThen.pop());
		isWhileWithStatement = false;
		
		while(!breakJumps.peek().isEmpty()) {
			Code.fixup(breakJumps.peek().remove(0));
		}
		breakJumps.pop();
	}
	
	@Override
	public void visit(DesStmtDummyTerminal desStmtDummyTerminal) {
		System.out.println("Usao sam u desStmtDummyTerminal");
		System.out.println(Code.pc);
		Code.putJump(0); //tacne bacamo na THEN, skaceom na then
		skipCondition.push(Code.pc - 2); //pamtimo tacne
		while(!skipCondFact.empty()) {
			Code.fixup(skipCondFact.pop());
		}
	}
	
	@Override
	public void visit(StartConditionDummy startConditionDummy) {
		System.out.println("Usao sam u startConditionDummy");
		System.out.println(Code.pc);
		isWhileWithStatement = true; //ovo sve sto radim sa isWhileWithStatement i desStmtDummyTerminal je
		//neki workaround da bi mi while(i<3; i++) radio
		
		while(!continueJumps.peek().isEmpty()) {
			Code.fixup(continueJumps.peek().remove(0));
		}
		continueJumps.pop();
	}
	
	//BREAK I CONTINUE
	
	private Stack<List<Integer>> breakJumps = new Stack<>();
	private Stack<List<Integer>> continueJumps = new Stack<>();

	@Override
	public void visit(StatementBreak statementBreak) {
		Code.putJump(0);
		breakJumps.peek().add(Code.pc - 2);
	}	
	
	@Override
	public void visit(StatementContinue statementContinue) {
		Code.putJump(0);
		continueJumps.peek().add(Code.pc - 2);
	}	
	
	
	@Override
	public void visit(ExprDesignator exprDesignator) {
		Obj mapFunction = exprDesignator.getFunctionName().getDesignator().obj;
		Obj designatorArray = exprDesignator.getDesignator().obj;
		
		int adrToPatchMap = mapFunction.getAdr();
		//KAKO DA FIXUPUJEM NA ADRESU KOJU JE U MAPFUNCTION
		//Code.fixup(patchMapAddr);
		System.out.println("patchMapAddr je " + patchMapAddr);
		System.out.println("adrToPatchMap je" + adrToPatchMap);
		Code.put2(patchMapAddr, (adrToPatchMap-patchMapAddr + 1));
		
		Code.load(designatorArray);
		
		int adr = mapInternalMethod.getAdr(); //promeni ovo na drugu adresu
		int offset = adr - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}
}
