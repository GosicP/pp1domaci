// generated with ast extension for cup
// version 0.8
// 7/0/2025 22:9:55


package rs.ac.bg.etf.pp1.ast;

public class ConVarDeclListCon extends ConVarDeclList {

    private ConVarDeclList ConVarDeclList;
    private ConDecList ConDecList;

    public ConVarDeclListCon (ConVarDeclList ConVarDeclList, ConDecList ConDecList) {
        this.ConVarDeclList=ConVarDeclList;
        if(ConVarDeclList!=null) ConVarDeclList.setParent(this);
        this.ConDecList=ConDecList;
        if(ConDecList!=null) ConDecList.setParent(this);
    }

    public ConVarDeclList getConVarDeclList() {
        return ConVarDeclList;
    }

    public void setConVarDeclList(ConVarDeclList ConVarDeclList) {
        this.ConVarDeclList=ConVarDeclList;
    }

    public ConDecList getConDecList() {
        return ConDecList;
    }

    public void setConDecList(ConDecList ConDecList) {
        this.ConDecList=ConDecList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConVarDeclList!=null) ConVarDeclList.accept(visitor);
        if(ConDecList!=null) ConDecList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConVarDeclList!=null) ConVarDeclList.traverseTopDown(visitor);
        if(ConDecList!=null) ConDecList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConVarDeclList!=null) ConVarDeclList.traverseBottomUp(visitor);
        if(ConDecList!=null) ConDecList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConVarDeclListCon(\n");

        if(ConVarDeclList!=null)
            buffer.append(ConVarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConDecList!=null)
            buffer.append(ConDecList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConVarDeclListCon]");
        return buffer.toString();
    }
}
