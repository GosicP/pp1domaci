package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;


public class SemAnalyzer extends VisitorAdaptor {
	int printCallCount = 0;
	int varDeclCount = 0;
	Obj currentProgram = null;
	boolean returnFound = false;
	boolean errorDetected = false;
	int nVars;
	private Struct currentType;
	
	Logger log = Logger.getLogger(getClass());
	private int constant;
	private Struct constantType;
	private Struct boolType = Tab.find("bool").getType();
	private Obj mainMethod;
	private Obj currentMethod;
	
	private Stack<List<Struct>> actualParamsStack = new Stack<>();
	

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public boolean passed() {
		return !errorDetected;
	}
	
	public void checkFunctionParameters(Obj obj, SyntaxNode node) {
		Collection<Obj> formalParameters = obj.getLocalSymbols();
		int numFormPars = obj.getLevel();
		
		List<Struct> actualParsList = actualParamsStack.pop();
		int numActualPars = actualParsList.size();
		
		if(numFormPars != numActualPars) {
			report_error("Broj formalnih argumenata nije isti broju pravih argumenata", node);
			return;
		}
		
		List<Obj> formalParametersList = new ArrayList<>(formalParameters);
		
		for (int i = 0; i < numFormPars; i++) {
		    Obj formalParam = formalParametersList.get(i);
		    Struct actualParam = actualParsList.get(i);
		    
		    if(!formalParam.getType().assignableTo(actualParam)) {
		    	report_error("Tip formalnog i stvarnog parametra se ne poklapa ", node);
		    }
		}
	}
	
	@Override
	public void visit(ProgramName programName) {
		currentProgram = Tab.insert(Obj.Prog, programName.getI1(), Tab.noType);
		Tab.openScope();
	}
	
	@Override
	public void visit(Program program) {
		Tab.chainLocalSymbols(currentProgram);
		Tab.closeScope(); 
		currentProgram = null;
		
		if(mainMethod == null || mainMethod.getLevel() > 0) {
			report_error("Program nema adekvatnu main metodu", program);
		}
	}
	
	@Override
	public void visit(ConDecl conDecl) {
		Obj conObj = Tab.find(conDecl.getI1());
		if(conObj != Tab.noObj) {
			report_error("Dvostruka definicija konstante: " + conDecl.getI1(), conDecl);
		}else {
			if(constantType.assignableTo(currentType)) {
				conObj = Tab.insert(Obj.Con, conDecl.getI1(), currentType);
				conObj.setAdr(constant);
			}else {
				report_error("Neadekvatna dodela konstanti: " + conDecl.getI1(), conDecl);
			}
		}
	}
	
	@Override
	public void visit(ConstantN constantN) {
		constant = constantN.getN1();
		constantType = Tab.intType;
	}
	
	@Override
	public void visit(ConstantC constantC) {
		constant = constantC.getC1();
		constantType = Tab.charType;
	}
	
	@Override
	public void visit(ConstantB constantB) {
		constant = constantB.getB1();
		constantType = boolType;
	}
	
	/* VAR DECLARATIONS*/
	
	@Override
	public void visit(ArraySquareIdent arraySquareIdent) {
		Obj varObj;
		if(currentMethod==null) {
			varObj = Tab.find(arraySquareIdent.getI1());
		}else {
			varObj = Tab.currentScope.findSymbol(arraySquareIdent.getI1());
		}
		
		if(varObj == null || varObj == Tab.noObj) {
			varObj = Tab.insert(Obj.Var, arraySquareIdent.getI1(), currentType);
		}else {
			report_error("Dvostruka definicija promenljive: " + arraySquareIdent.getI1(), arraySquareIdent);
		}
	}
	
	@Override
	public void visit(ArraySquarePresent arraySquarePresent) {
		Obj varObj;
		if(currentMethod==null) {
			varObj = Tab.find(arraySquarePresent.getI1());
		}else {
			varObj = Tab.currentScope.findSymbol(arraySquarePresent.getI1());
		}
		
		if(varObj == null || varObj == Tab.noObj) {
			varObj = Tab.insert(Obj.Var, arraySquarePresent.getI1(), new Struct(Struct.Array, currentType));
		}else {
			report_error("Dvostruka definicija promenljive: " + arraySquarePresent.getI1(), arraySquarePresent);
		}
	}
	
	/* METHOD DECLARATIONS */
	
	@Override
	public void visit(MethodTypeAndNameVoid methodTypeAndNameVoid) {
		currentMethod = Tab.insert(Obj.Meth, methodTypeAndNameVoid.getI1(), Tab.noType);
		Tab.openScope();
		if(methodTypeAndNameVoid.getI1().equalsIgnoreCase("main")) { //main mora da nema parametre
			//to nisi proverio!!!!!!
			mainMethod = currentMethod;
		}
	}
	
	@Override
	public void visit(MethodTypeAndNameNonVoid methodTypeAndNameNonVoid) {
		currentMethod = Tab.insert(Obj.Meth, methodTypeAndNameNonVoid.getI2(), currentType);
		Tab.openScope();
		//mora da ide provera da li metoda ima return
	}
	
	@Override
	public void visit(MethodDecl methodDecl) { //zavrsavamo metodu, zatvaramo scope
		//NE RADI TI PROVERA DA LI METODA POSTOJI
//		if(currentMethod == Tab.noObj) {
//			currentMethod = null;
//			returnFound = false;
//			report_error("Metoda je vec prethodno deklarisana " + currentMethod.getName(), methodDecl);
//		}
		
		if(returnFound == false && currentMethod.getType() != Tab.noType) {
			report_error("Metoda nema return " + currentMethod.getName(), methodDecl);
		}
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		currentMethod = null;
		returnFound = false;
	}
	
	/* FORMPAR DECLARATIONS */
	
	@Override
	public void visit(FormParsVarOrArrayVar formParsVarOrArrayVar) {
		Obj varObj = null;
		if(currentMethod==null) {
			report_error("Semanticka greska. [formParsVarOrArrayVar]", formParsVarOrArrayVar);
		}else {
			varObj = Tab.currentScope.findSymbol(formParsVarOrArrayVar.getI2());
		}
		
		if(varObj == null || varObj == Tab.noObj) {
			varObj = Tab.insert(Obj.Var, formParsVarOrArrayVar.getI2(), currentType);
			varObj.setFpPos(1);
			currentMethod.setLevel(currentMethod.getLevel() + 1);
		}else {
			report_error("Dvostruka definicija form parametra: " + formParsVarOrArrayVar.getI2(), formParsVarOrArrayVar);
		}
	}
	
	@Override
	public void visit(FormParsVarOrArraySquares formParsVarOrArraySquares) {
		Obj varObj = null;
		if(currentMethod==null) {
			report_error("Semanticka greska. [formParsVarOrArraySquares]", formParsVarOrArraySquares);
		}else {
			varObj = Tab.currentScope.findSymbol(formParsVarOrArraySquares.getI2());
		}
		
		if(varObj == null || varObj == Tab.noObj) {
			varObj = Tab.insert(Obj.Var, formParsVarOrArraySquares.getI2(), currentType);
			varObj.setFpPos(1);
			currentMethod.setLevel(currentMethod.getLevel() + 1);
		}else {
			report_error("Dvostruka definicija form parametra: " + formParsVarOrArraySquares.getI2(), formParsVarOrArraySquares);
		}
	}
	
	@Override
	public void visit(TypeIdent type) { 
		Obj typeObj = Tab.find(type.getI1());
		if(typeObj == Tab.noObj) {
			report_error("Nepostojeci tip podatka: " + type.getI1(), type);
			currentType = Tab.noType;
		}else if(typeObj.getKind() != Obj.Type) {
			report_error("Neadekvatan tip podatka: " + type.getI1(), type);
			currentType = Tab.noType;
		}else {
			currentType = typeObj.getType();
		}
	}
	
	
	/* KONTEKSNI USLOVI */
	//OVE PROVERE SE MENI DESAVAJU U EXPR I U TERM
	//Factor
	@Override
	public void visit(FactorChar factorSubChar) {
		factorSubChar.struct = Tab.charType;
	}
	
	@Override
	public void visit(FactorNum factorSubNum) {
		factorSubNum.struct = Tab.intType;
	}
	
	@Override
	public void visit(FactorBool factorSubBool) {
		factorSubBool.struct = boolType;
	}
	
	@Override
	public void visit(FactorDesignator factorDesignator) {
		factorDesignator.struct = factorDesignator.getDesignator().obj.getType();
		
		//ovde fali neka provera
	}
	
	@Override
	public void visit(FactorNewArray factorNewArray) {
		if(!factorNewArray.getExpr().struct.equals(Tab.intType)) {
			report_error("Velicina niza nije INT tipa", factorNewArray);
			factorNewArray.struct = Tab.noType;
		}else {
			factorNewArray.struct = new Struct(Struct.Array, currentType);
		}
	}
	
	
	@Override
	public void visit(FactorExpr factorExpr) {
		//System.out.println(factorExpr.struct.getKind());
		factorExpr.struct = factorExpr.getExpr().struct;
	}
	
	//Designator
	
	@Override
	public void visit(DesignatorIdent designatorIdent) {
		Obj varObj = Tab.find(designatorIdent.getIdentName());
		if(varObj == Tab.noObj) {
			report_error("Pristup nedefinisanoj promenljivi: [designatorIdent]" + designatorIdent.getIdentName(), designatorIdent);
			designatorIdent.obj = Tab.noObj;
		}else if(varObj.getKind() != Obj.Var && varObj.getKind() != Obj.Con  && varObj.getKind() != Obj.Meth){
			report_error("Neadakvatna promenljiva: " + designatorIdent.getIdentName(), designatorIdent);
			designatorIdent.obj = Tab.noObj;
		}
		else {
			designatorIdent.obj = varObj;
		}
	}
	
	//ovde fali ona njegova implementacija za IDENT koji je cvor za sebe,
	//ali mislim da cu to uraditi u sledecem snimku
	
	//ZA SADA VIDIM da mi DOT IDENT smena uopste ne treba, ako je tako
	//uradicu implementaciju kao u njegovom snimku, ali probacu i bez toga
	//nisam siguran da menjam gramatiku toliko, iako kapiram da mi i ne treba toliko
	
	
	//VIDI STA SVE SUTRA MOZES DA ISTESTIRAS
	@Override
	public void visit(DesignatorListArray designatorListArray) {
		Obj arrayElem = designatorListArray.getDesignator().obj;
		//System.out.print("Tip ovog niza je: " + arrayElem.getKind());
		if(arrayElem.getType().getKind() != Struct.Array) {
			report_error("Promenljiva mora biti nizovskog tipa ", designatorListArray);
		}
		
		if(designatorListArray.getExpr().struct != Tab.intType) {
			report_error("Izraz koji predstavlja indeks niza mora biti celobrojnog tipa!", designatorListArray);
			designatorListArray.obj = Tab.noObj;
		}
		
		designatorListArray.obj = new Obj(Obj.Elem, arrayElem.getName(), arrayElem.getType().getElemType());
	}
	
	/* EXPR */
	
	@Override
	public void visit(TermFactor termFactor) {
		termFactor.struct = termFactor.getFactor().struct;
	}
	
	@Override
	public void visit(TermMul termMul) {
		Struct left = termMul.getTerm().struct;
		Struct right = termMul.getFactor().struct;
		
		if(left.equals(Tab.intType) && right.equals(Tab.intType)) {
			termMul.struct = Tab.intType;
		}else {
			report_error("Mnoze se 2 ne int vrednosti", termMul);
			termMul.struct = Tab.noType;
		}
	}
	
	/* Term */
	
	@Override
	public void visit(ExprSingleTerm exprSingleTerm) {
		exprSingleTerm.struct = exprSingleTerm.getTerm().struct;
	}
	
	@Override
	public void visit(ExprSingleTermMinus exprSingleTermMinus) {
		//System.out.println(exprSingleTermMinus.struct.getKind());
		if(!exprSingleTermMinus.getTerm().struct.equals(Tab.intType)) {
			report_error("Negirana vrednost mora biti int tipa", exprSingleTermMinus);
			exprSingleTermMinus.struct = Tab.noType;
		}else {
			exprSingleTermMinus.struct = exprSingleTermMinus.getTerm().struct;
		}
	}
	
	@Override
	public void visit(ExprMultipleTerm exprMultipleTerm) {
		Struct left = exprMultipleTerm.getExpr().struct;
		Struct right = exprMultipleTerm.getTerm().struct;
		
		if(left.equals(Tab.intType) && right.equals(Tab.intType)) {
			exprMultipleTerm.struct = Tab.intType;
		}else {
			report_error("Sabiraju se 2 ne int vrednosti", exprMultipleTerm);
			exprMultipleTerm.struct = Tab.noType;
		}
	}
	
	@Override
	public void visit(DesignatorStatementAssign designatorStatementAssign) {
		int kind = designatorStatementAssign.getDesignator().obj.getKind();
		if(kind != Obj.Var && kind != Obj.Elem) {
			report_error("Dodela u neadekvatnu promenljivu " + designatorStatementAssign.getDesignator().obj.getName(), designatorStatementAssign);
		}else if(!designatorStatementAssign.getExpr().struct.assignableTo(designatorStatementAssign.getDesignator().obj.getType())) {
			report_error("Neadekvatna dodela vrednosti u promenljivu: " + designatorStatementAssign.getDesignator().obj.getName(), designatorStatementAssign);
		}
	}
	
	@Override
	public void visit(DesignatorStatementIncrement designatorStatementIncrement) {
		int kind = designatorStatementIncrement.getDesignator().obj.getKind();
		if( kind != Obj.Var && kind != Obj.Elem) {
			report_error("Inkrement neadekvatne promenljive " + designatorStatementIncrement.getDesignator().obj.getName(), designatorStatementIncrement);
		}else if(!designatorStatementIncrement.getDesignator().obj.getType().equals(Tab.intType)) {
			report_error("Inkrement ne int promenljive " + designatorStatementIncrement.getDesignator().obj.getName(), designatorStatementIncrement);
		}
	}
	
	@Override
	public void visit(DesignatorStatementDecrement designatorStatementDecrement) {
		int kind = designatorStatementDecrement.getDesignator().obj.getKind();
		if( kind != Obj.Var && kind != Obj.Elem) {
			report_error("Dekrement neadekvatne promenljive " + designatorStatementDecrement.getDesignator().obj.getName(), designatorStatementDecrement);
		}else if(!designatorStatementDecrement.getDesignator().obj.getType().equals(Tab.intType)) {
			report_error("Dekrement ne int promenljive " + designatorStatementDecrement.getDesignator().obj.getName(), designatorStatementDecrement);
		}
	}
	
	@Override
	public void visit(FunctionName functionName) {
		actualParamsStack.push(new ArrayList<Struct>());
	}
	
	@Override
	public void visit(DesignatorStatementFunctionCall designatorStatementFunctionCall) {
		Obj designatorObj = designatorStatementFunctionCall.getFunctionName().getDesignator().obj;
		
		if(designatorObj.getKind() != Obj.Meth) {
			report_error("Simbol " + designatorObj.getName() + " ne predstavlja metodu!", designatorStatementFunctionCall);
			return;
		}
		
		//DOVRSI CHECK PARAMETERS
		checkFunctionParameters(designatorObj, designatorStatementFunctionCall);
	}
	

	@Override
	public void visit(FactorDesignatorMethod factorDesignatorMethod) {
		Obj designatorObj = factorDesignatorMethod.getFunctionName().getDesignator().obj;
		
		factorDesignatorMethod.struct = designatorObj.getType();
		
		if(designatorObj.getKind() != Obj.Meth) {
			report_error("Simbol " + designatorObj.getName() + " nije metoda", factorDesignatorMethod);
			return;
		}
		//DOPUNI OVDE ISTIM ONIM USLOVIMA KAO ZA FUNCTION CALL
		//DOVRSI CHECK PARAMETERS
		
		checkFunctionParameters(designatorObj, factorDesignatorMethod);
	}
	

	@Override
	public void visit(StatementRead statementRead) {
		int kind = statementRead.getDesignator().obj.getKind();
		if( kind != Obj.Var && kind != Obj.Elem) {
			report_error("Read neadekvatne promenljive " + statementRead.getDesignator().obj.getName(), statementRead);
		}else if(!statementRead.getDesignator().obj.getType().equals(Tab.intType)
				&& !statementRead.getDesignator().obj.getType().equals(Tab.charType) 
				&& !statementRead.getDesignator().obj.getType().equals(boolType)) {
			report_error("Read pogresnog tipa promenljive " + statementRead.getDesignator().obj.getName(), statementRead);
		}
	}
	
	/*RETURN*/
	
	//PROVERI OVE DVE METODE
	@Override
	public void visit(StatementReturn statementReturn) {
		if(currentMethod == null) {
			report_error("Return se ne nalazi u metodi ", statementReturn);
		}
		if(currentMethod.getType() != Tab.noType) {
			report_error("Return bez parametara nije u void metodi ", statementReturn);
		}
	}
	
	@Override
	public void visit(StatementReturnExpr statementReturnExpr) {
		if(currentMethod == null) {
			report_error("Return se ne nalazi u metodi ", statementReturnExpr);
		}
	
		Struct methodType = currentMethod.getType();
		returnFound = true;
		if(methodType == Tab.noType) {
			report_error("Return sa promenljivom se nalazi u void metodi " + currentMethod.getName(), statementReturnExpr);
		}else if(!statementReturnExpr.getExpr().struct.compatibleWith(methodType)) {
			report_error("Tip returna se ne poklapa sa tipom metode " + currentMethod.getName(), statementReturnExpr);
		}
	}
	
	@Override
	public void visit(ActParsListExpr actParsListExpr) {
		//report_info("argument funkcije " + " tipa " + actParsListExpr.getExpr().struct.getKind() + " je ocitan ", actParsListExpr);
		actualParamsStack.peek().add(actParsListExpr.getExpr().struct);
	}
	
	@Override
	public void visit(ActParsListExprRecursion actParsListExprRecursion) {
		Expr expr = actParsListExprRecursion.getExpr();
		//report_info("argument funkcije " + " tipa " + actParsListExprRecursion.getExpr().struct.getKind() + " je ocitan ", actParsListExprRecursion);
		actualParamsStack.peek().add(actParsListExprRecursion.getExpr().struct);
	}
	
	
	/* IF */
	@Override
	public void visit(CondFactSingleExpr condFactSingleExpr) {
		if(condFactSingleExpr.getExpr().struct != boolType) {
			report_error("Uslov mora biti true ili false" , condFactSingleExpr);
			condFactSingleExpr.struct = Tab.noType;
		}
		condFactSingleExpr.struct = boolType;
	}
	
	@Override
	public void visit(CondFactDoubleExpr condFactDoubleExpr) {
		Struct left = condFactDoubleExpr.getExpr().struct;
		Struct right = condFactDoubleExpr.getExpr1().struct;
		
		if(!left.compatibleWith(right)) {
			report_error("Ne mozes porediti 2 nejednaka tipa ", condFactDoubleExpr);
			condFactDoubleExpr.struct = Tab.noType;
			return; //Sta ce ovde biti ako nema return?
		}
		if(left.getKind() == Struct.Array || right.getKind() == Struct.Array) {
			if(!(condFactDoubleExpr.getRelop() instanceof RelopEquality || condFactDoubleExpr.getRelop() instanceof RelopInequality)) {
				report_error("Ne mozes porediti 2 niza operatorima koji nisu == ili !=", condFactDoubleExpr);
				condFactDoubleExpr.struct = Tab.noType;
			}
		}
		
		condFactDoubleExpr.struct = boolType;
	}
	
	@Override
	public void visit(CondTermFact condTermFact) {
		if(condTermFact.getCondFact().struct != boolType) {
			report_error("Uslov mora biti true ili false" , condTermFact);
			condTermFact.struct = Tab.noType;
			return; //Sta se ovde desava ako nema return
		}
		condTermFact.struct = boolType;
	}
	
	@Override
	public void visit(CondTermRecursion condTermRecursion) {
		if(condTermRecursion.getCondFact().struct != boolType || condTermRecursion.getCondTerm().struct != boolType) {
			report_error("Prilikom koriscenja AND oba tipa moraju biti tipa bool" , condTermRecursion);
			condTermRecursion.struct = Tab.noType;
		}
		condTermRecursion.struct = boolType;
	}
	
	@Override
	public void visit(ConditionCondTerm conditionCondTerm) {
		if(conditionCondTerm.getCondTerm().struct != boolType) {
			report_error("Uslov mora biti true ili false" , conditionCondTerm);
			conditionCondTerm.struct = Tab.noType;
			return; //Sta se ovde desava ako nema return
		}
		conditionCondTerm.struct = boolType;
	}
	
	@Override
	public void visit(ConditionRecursion ConditionRecursion) {
		if(ConditionRecursion.getCondition().struct != boolType || ConditionRecursion.getCondTerm().struct != boolType) {
			report_error("Prilikom koriscenja AND oba tipa moraju biti tipa bool" , ConditionRecursion);
			ConditionRecursion.struct = Tab.noType;
		}
		ConditionRecursion.struct = boolType;
	}
	
	@Override
	public void visit(IfCondition ifCondition) {
		if(ifCondition.getCondition().struct != boolType) {
			report_error("Prilikom koriscenja AND oba tipa moraju biti tipa bool" , ifCondition);
		};
	}
	
	/* WHILE */
	@Override
	public void visit(WhileConditionsNoStatement whileConditionsNoStatement) {
		if( whileConditionsNoStatement.getCondition().struct != boolType ) {
			report_error("Condition u while mora biti boolean ", whileConditionsNoStatement);
		}
	}
	
	//FALI TI EXPRESSION DESIGNATOR POSTO GA JOS UVEK NE KAPIRAM STA TREBA DA URADI
	
	//MORAS DA VIDIS KAKO DA OBEZBEDIS DA NE MOZES DA POZOVES 2 ISTE METODE
}
