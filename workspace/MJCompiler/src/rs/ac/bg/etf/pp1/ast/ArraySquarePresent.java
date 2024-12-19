// generated with ast extension for cup
// version 0.8
// 18/11/2024 16:9:7


package rs.ac.bg.etf.pp1.ast;

public class ArraySquarePresent extends ArraySquare {

    private String I1;

    public ArraySquarePresent (String I1) {
        this.I1=I1;
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ArraySquarePresent(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ArraySquarePresent]");
        return buffer.toString();
    }
}
