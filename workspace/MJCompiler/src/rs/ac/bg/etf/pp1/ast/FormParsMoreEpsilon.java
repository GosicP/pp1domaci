// generated with ast extension for cup
// version 0.8
// 7/0/2025 22:9:55


package rs.ac.bg.etf.pp1.ast;

public class FormParsMoreEpsilon extends FormParsMore {

    public FormParsMoreEpsilon () {
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
        buffer.append("FormParsMoreEpsilon(\n");

        buffer.append(tab);
        buffer.append(") [FormParsMoreEpsilon]");
        return buffer.toString();
    }
}
