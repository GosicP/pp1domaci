// generated with ast extension for cup
// version 0.8
// 19/0/2025 19:3:33


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementAssignop extends DesignatorStatement {

    private Designator Designator;
    private AssignOp AssignOp;
    private Designator Designator1;
    private Setop Setop;
    private Designator Designator2;

    public DesignatorStatementAssignop (Designator Designator, AssignOp AssignOp, Designator Designator1, Setop Setop, Designator Designator2) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.AssignOp=AssignOp;
        if(AssignOp!=null) AssignOp.setParent(this);
        this.Designator1=Designator1;
        if(Designator1!=null) Designator1.setParent(this);
        this.Setop=Setop;
        if(Setop!=null) Setop.setParent(this);
        this.Designator2=Designator2;
        if(Designator2!=null) Designator2.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public AssignOp getAssignOp() {
        return AssignOp;
    }

    public void setAssignOp(AssignOp AssignOp) {
        this.AssignOp=AssignOp;
    }

    public Designator getDesignator1() {
        return Designator1;
    }

    public void setDesignator1(Designator Designator1) {
        this.Designator1=Designator1;
    }

    public Setop getSetop() {
        return Setop;
    }

    public void setSetop(Setop Setop) {
        this.Setop=Setop;
    }

    public Designator getDesignator2() {
        return Designator2;
    }

    public void setDesignator2(Designator Designator2) {
        this.Designator2=Designator2;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(AssignOp!=null) AssignOp.accept(visitor);
        if(Designator1!=null) Designator1.accept(visitor);
        if(Setop!=null) Setop.accept(visitor);
        if(Designator2!=null) Designator2.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(AssignOp!=null) AssignOp.traverseTopDown(visitor);
        if(Designator1!=null) Designator1.traverseTopDown(visitor);
        if(Setop!=null) Setop.traverseTopDown(visitor);
        if(Designator2!=null) Designator2.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(AssignOp!=null) AssignOp.traverseBottomUp(visitor);
        if(Designator1!=null) Designator1.traverseBottomUp(visitor);
        if(Setop!=null) Setop.traverseBottomUp(visitor);
        if(Designator2!=null) Designator2.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatementAssignop(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AssignOp!=null)
            buffer.append(AssignOp.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Designator1!=null)
            buffer.append(Designator1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Setop!=null)
            buffer.append(Setop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Designator2!=null)
            buffer.append(Designator2.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatementAssignop]");
        return buffer.toString();
    }
}
