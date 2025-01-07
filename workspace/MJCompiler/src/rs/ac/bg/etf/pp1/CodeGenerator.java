package rs.ac.bg.etf.pp1;

import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {
	
	private int mainPC;
	private boolean isWhileWithStatement = false;
	
	CodeGenerator() {
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
		Code.loadConst(0);
		if(statementPrintExpr.getExpr().struct.equals(Tab.charType)) {
			Code.put(Code.bprint);
		}else {
			Code.put(Code.print);
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
		Code.put(Code.newarray);
		Type tip = factorNewArray.getType();
		if(tip.struct.equals(Tab.charType)) {
			Code.put(0);
		}else {
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
	}

	
}
