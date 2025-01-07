// generated with ast extension for cup
// version 0.8
// 7/0/2025 22:9:55


package rs.ac.bg.etf.pp1.ast;

public class FormParsPresent extends FormPars {

    private FormParsVarOrArray FormParsVarOrArray;
    private FormParsMore FormParsMore;

    public FormParsPresent (FormParsVarOrArray FormParsVarOrArray, FormParsMore FormParsMore) {
        this.FormParsVarOrArray=FormParsVarOrArray;
        if(FormParsVarOrArray!=null) FormParsVarOrArray.setParent(this);
        this.FormParsMore=FormParsMore;
        if(FormParsMore!=null) FormParsMore.setParent(this);
    }

    public FormParsVarOrArray getFormParsVarOrArray() {
        return FormParsVarOrArray;
    }

    public void setFormParsVarOrArray(FormParsVarOrArray FormParsVarOrArray) {
        this.FormParsVarOrArray=FormParsVarOrArray;
    }

    public FormParsMore getFormParsMore() {
        return FormParsMore;
    }

    public void setFormParsMore(FormParsMore FormParsMore) {
        this.FormParsMore=FormParsMore;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParsVarOrArray!=null) FormParsVarOrArray.accept(visitor);
        if(FormParsMore!=null) FormParsMore.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParsVarOrArray!=null) FormParsVarOrArray.traverseTopDown(visitor);
        if(FormParsMore!=null) FormParsMore.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParsVarOrArray!=null) FormParsVarOrArray.traverseBottomUp(visitor);
        if(FormParsMore!=null) FormParsMore.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsPresent(\n");

        if(FormParsVarOrArray!=null)
            buffer.append(FormParsVarOrArray.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormParsMore!=null)
            buffer.append(FormParsMore.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsPresent]");
        return buffer.toString();
    }
}
