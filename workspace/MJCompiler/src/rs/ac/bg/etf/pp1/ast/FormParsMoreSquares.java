// generated with ast extension for cup
// version 0.8
// 19/0/2025 19:3:33


package rs.ac.bg.etf.pp1.ast;

public class FormParsMoreSquares extends FormParsMore {

    private FormParsMore FormParsMore;
    private FormParsVarOrArray FormParsVarOrArray;

    public FormParsMoreSquares (FormParsMore FormParsMore, FormParsVarOrArray FormParsVarOrArray) {
        this.FormParsMore=FormParsMore;
        if(FormParsMore!=null) FormParsMore.setParent(this);
        this.FormParsVarOrArray=FormParsVarOrArray;
        if(FormParsVarOrArray!=null) FormParsVarOrArray.setParent(this);
    }

    public FormParsMore getFormParsMore() {
        return FormParsMore;
    }

    public void setFormParsMore(FormParsMore FormParsMore) {
        this.FormParsMore=FormParsMore;
    }

    public FormParsVarOrArray getFormParsVarOrArray() {
        return FormParsVarOrArray;
    }

    public void setFormParsVarOrArray(FormParsVarOrArray FormParsVarOrArray) {
        this.FormParsVarOrArray=FormParsVarOrArray;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParsMore!=null) FormParsMore.accept(visitor);
        if(FormParsVarOrArray!=null) FormParsVarOrArray.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParsMore!=null) FormParsMore.traverseTopDown(visitor);
        if(FormParsVarOrArray!=null) FormParsVarOrArray.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParsMore!=null) FormParsMore.traverseBottomUp(visitor);
        if(FormParsVarOrArray!=null) FormParsVarOrArray.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsMoreSquares(\n");

        if(FormParsMore!=null)
            buffer.append(FormParsMore.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormParsVarOrArray!=null)
            buffer.append(FormParsVarOrArray.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsMoreSquares]");
        return buffer.toString();
    }
}
