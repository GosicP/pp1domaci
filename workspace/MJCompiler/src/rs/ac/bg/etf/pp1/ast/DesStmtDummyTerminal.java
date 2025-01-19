// generated with ast extension for cup
// version 0.8
// 19/0/2025 19:3:33


package rs.ac.bg.etf.pp1.ast;

public class DesStmtDummyTerminal implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public DesStmtDummyTerminal () {
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
        buffer.append("DesStmtDummyTerminal(\n");

        buffer.append(tab);
        buffer.append(") [DesStmtDummyTerminal]");
        return buffer.toString();
    }
}
