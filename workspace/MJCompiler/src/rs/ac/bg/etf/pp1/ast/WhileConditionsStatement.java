// generated with ast extension for cup
// version 0.8
// 7/0/2025 22:9:55


package rs.ac.bg.etf.pp1.ast;

public class WhileConditionsStatement extends WhileConditions {

    private Condition Condition;
    private DesignatorStatement DesignatorStatement;
    private DesStmtDummyTerminal DesStmtDummyTerminal;

    public WhileConditionsStatement (Condition Condition, DesignatorStatement DesignatorStatement, DesStmtDummyTerminal DesStmtDummyTerminal) {
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.DesignatorStatement=DesignatorStatement;
        if(DesignatorStatement!=null) DesignatorStatement.setParent(this);
        this.DesStmtDummyTerminal=DesStmtDummyTerminal;
        if(DesStmtDummyTerminal!=null) DesStmtDummyTerminal.setParent(this);
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public DesignatorStatement getDesignatorStatement() {
        return DesignatorStatement;
    }

    public void setDesignatorStatement(DesignatorStatement DesignatorStatement) {
        this.DesignatorStatement=DesignatorStatement;
    }

    public DesStmtDummyTerminal getDesStmtDummyTerminal() {
        return DesStmtDummyTerminal;
    }

    public void setDesStmtDummyTerminal(DesStmtDummyTerminal DesStmtDummyTerminal) {
        this.DesStmtDummyTerminal=DesStmtDummyTerminal;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Condition!=null) Condition.accept(visitor);
        if(DesignatorStatement!=null) DesignatorStatement.accept(visitor);
        if(DesStmtDummyTerminal!=null) DesStmtDummyTerminal.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(DesignatorStatement!=null) DesignatorStatement.traverseTopDown(visitor);
        if(DesStmtDummyTerminal!=null) DesStmtDummyTerminal.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(DesignatorStatement!=null) DesignatorStatement.traverseBottomUp(visitor);
        if(DesStmtDummyTerminal!=null) DesStmtDummyTerminal.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("WhileConditionsStatement(\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStatement!=null)
            buffer.append(DesignatorStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesStmtDummyTerminal!=null)
            buffer.append(DesStmtDummyTerminal.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [WhileConditionsStatement]");
        return buffer.toString();
    }
}
