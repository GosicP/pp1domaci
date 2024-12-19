// generated with ast extension for cup
// version 0.8
// 18/11/2024 16:9:8


package rs.ac.bg.etf.pp1.ast;

public class MethodDeclVarList extends MethodDeclVar {

    private MethodDeclVar MethodDeclVar;
    private VarDeclList VarDeclList;

    public MethodDeclVarList (MethodDeclVar MethodDeclVar, VarDeclList VarDeclList) {
        this.MethodDeclVar=MethodDeclVar;
        if(MethodDeclVar!=null) MethodDeclVar.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
    }

    public MethodDeclVar getMethodDeclVar() {
        return MethodDeclVar;
    }

    public void setMethodDeclVar(MethodDeclVar MethodDeclVar) {
        this.MethodDeclVar=MethodDeclVar;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodDeclVar!=null) MethodDeclVar.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodDeclVar!=null) MethodDeclVar.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodDeclVar!=null) MethodDeclVar.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDeclVarList(\n");

        if(MethodDeclVar!=null)
            buffer.append(MethodDeclVar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDeclVarList]");
        return buffer.toString();
    }
}
