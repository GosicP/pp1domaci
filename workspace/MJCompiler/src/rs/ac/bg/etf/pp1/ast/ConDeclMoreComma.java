// generated with ast extension for cup
// version 0.8
// 3/0/2025 17:43:41


package rs.ac.bg.etf.pp1.ast;

public class ConDeclMoreComma extends ConDeclMore {

    private ConDeclMore ConDeclMore;
    private ConDecl ConDecl;

    public ConDeclMoreComma (ConDeclMore ConDeclMore, ConDecl ConDecl) {
        this.ConDeclMore=ConDeclMore;
        if(ConDeclMore!=null) ConDeclMore.setParent(this);
        this.ConDecl=ConDecl;
        if(ConDecl!=null) ConDecl.setParent(this);
    }

    public ConDeclMore getConDeclMore() {
        return ConDeclMore;
    }

    public void setConDeclMore(ConDeclMore ConDeclMore) {
        this.ConDeclMore=ConDeclMore;
    }

    public ConDecl getConDecl() {
        return ConDecl;
    }

    public void setConDecl(ConDecl ConDecl) {
        this.ConDecl=ConDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConDeclMore!=null) ConDeclMore.accept(visitor);
        if(ConDecl!=null) ConDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConDeclMore!=null) ConDeclMore.traverseTopDown(visitor);
        if(ConDecl!=null) ConDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConDeclMore!=null) ConDeclMore.traverseBottomUp(visitor);
        if(ConDecl!=null) ConDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConDeclMoreComma(\n");

        if(ConDeclMore!=null)
            buffer.append(ConDeclMore.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConDecl!=null)
            buffer.append(ConDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConDeclMoreComma]");
        return buffer.toString();
    }
}
