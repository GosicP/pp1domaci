// generated with ast extension for cup
// version 0.8
// 19/0/2025 19:3:33


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementFunctionCall extends DesignatorStatement {

    private FunctionName FunctionName;
    private ActPars ActPars;

    public DesignatorStatementFunctionCall (FunctionName FunctionName, ActPars ActPars) {
        this.FunctionName=FunctionName;
        if(FunctionName!=null) FunctionName.setParent(this);
        this.ActPars=ActPars;
        if(ActPars!=null) ActPars.setParent(this);
    }

    public FunctionName getFunctionName() {
        return FunctionName;
    }

    public void setFunctionName(FunctionName FunctionName) {
        this.FunctionName=FunctionName;
    }

    public ActPars getActPars() {
        return ActPars;
    }

    public void setActPars(ActPars ActPars) {
        this.ActPars=ActPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FunctionName!=null) FunctionName.accept(visitor);
        if(ActPars!=null) ActPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FunctionName!=null) FunctionName.traverseTopDown(visitor);
        if(ActPars!=null) ActPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FunctionName!=null) FunctionName.traverseBottomUp(visitor);
        if(ActPars!=null) ActPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatementFunctionCall(\n");

        if(FunctionName!=null)
            buffer.append(FunctionName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActPars!=null)
            buffer.append(ActPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatementFunctionCall]");
        return buffer.toString();
    }
}
