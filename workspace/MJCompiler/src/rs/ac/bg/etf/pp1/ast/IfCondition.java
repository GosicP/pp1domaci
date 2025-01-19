// generated with ast extension for cup
// version 0.8
// 19/0/2025 19:3:33


package rs.ac.bg.etf.pp1.ast;

public class IfCondition implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private IfStartDummy IfStartDummy;
    private Condition Condition;

    public IfCondition (IfStartDummy IfStartDummy, Condition Condition) {
        this.IfStartDummy=IfStartDummy;
        if(IfStartDummy!=null) IfStartDummy.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
    }

    public IfStartDummy getIfStartDummy() {
        return IfStartDummy;
    }

    public void setIfStartDummy(IfStartDummy IfStartDummy) {
        this.IfStartDummy=IfStartDummy;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IfStartDummy!=null) IfStartDummy.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfStartDummy!=null) IfStartDummy.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfStartDummy!=null) IfStartDummy.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfCondition(\n");

        if(IfStartDummy!=null)
            buffer.append(IfStartDummy.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IfCondition]");
        return buffer.toString();
    }
}
