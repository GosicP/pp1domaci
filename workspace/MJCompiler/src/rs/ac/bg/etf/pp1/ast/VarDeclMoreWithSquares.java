// generated with ast extension for cup
// version 0.8
// 18/11/2024 16:9:7


package rs.ac.bg.etf.pp1.ast;

public class VarDeclMoreWithSquares extends VarDeclMore {

    private VarDeclMore VarDeclMore;
    private ArraySquare ArraySquare;

    public VarDeclMoreWithSquares (VarDeclMore VarDeclMore, ArraySquare ArraySquare) {
        this.VarDeclMore=VarDeclMore;
        if(VarDeclMore!=null) VarDeclMore.setParent(this);
        this.ArraySquare=ArraySquare;
        if(ArraySquare!=null) ArraySquare.setParent(this);
    }

    public VarDeclMore getVarDeclMore() {
        return VarDeclMore;
    }

    public void setVarDeclMore(VarDeclMore VarDeclMore) {
        this.VarDeclMore=VarDeclMore;
    }

    public ArraySquare getArraySquare() {
        return ArraySquare;
    }

    public void setArraySquare(ArraySquare ArraySquare) {
        this.ArraySquare=ArraySquare;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclMore!=null) VarDeclMore.accept(visitor);
        if(ArraySquare!=null) ArraySquare.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclMore!=null) VarDeclMore.traverseTopDown(visitor);
        if(ArraySquare!=null) ArraySquare.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclMore!=null) VarDeclMore.traverseBottomUp(visitor);
        if(ArraySquare!=null) ArraySquare.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclMoreWithSquares(\n");

        if(VarDeclMore!=null)
            buffer.append(VarDeclMore.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ArraySquare!=null)
            buffer.append(ArraySquare.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclMoreWithSquares]");
        return buffer.toString();
    }
}
