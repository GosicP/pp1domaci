// generated with ast extension for cup
// version 0.8
// 23/11/2024 21:44:27


package rs.ac.bg.etf.pp1.ast;

public class StatementWhile extends Statement {

    private DoWhileStartDummy DoWhileStartDummy;
    private Statement Statement;
    private WhileConditions WhileConditions;

    public StatementWhile (DoWhileStartDummy DoWhileStartDummy, Statement Statement, WhileConditions WhileConditions) {
        this.DoWhileStartDummy=DoWhileStartDummy;
        if(DoWhileStartDummy!=null) DoWhileStartDummy.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.WhileConditions=WhileConditions;
        if(WhileConditions!=null) WhileConditions.setParent(this);
    }

    public DoWhileStartDummy getDoWhileStartDummy() {
        return DoWhileStartDummy;
    }

    public void setDoWhileStartDummy(DoWhileStartDummy DoWhileStartDummy) {
        this.DoWhileStartDummy=DoWhileStartDummy;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public WhileConditions getWhileConditions() {
        return WhileConditions;
    }

    public void setWhileConditions(WhileConditions WhileConditions) {
        this.WhileConditions=WhileConditions;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DoWhileStartDummy!=null) DoWhileStartDummy.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(WhileConditions!=null) WhileConditions.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DoWhileStartDummy!=null) DoWhileStartDummy.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(WhileConditions!=null) WhileConditions.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DoWhileStartDummy!=null) DoWhileStartDummy.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(WhileConditions!=null) WhileConditions.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementWhile(\n");

        if(DoWhileStartDummy!=null)
            buffer.append(DoWhileStartDummy.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(WhileConditions!=null)
            buffer.append(WhileConditions.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementWhile]");
        return buffer.toString();
    }
}
