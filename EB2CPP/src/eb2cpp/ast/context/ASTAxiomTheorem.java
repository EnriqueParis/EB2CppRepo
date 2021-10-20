package eb2cpp.ast.context;

import eb2cpp.ast.predicates.ASTPredicate;

public class ASTAxiomTheorem {
	///////////////
	// VARIABLES //
	///////////////
	private String axiomLabel;
	private boolean isTheorem;
	private ASTPredicate predicate;

	/////////////
	// METHODS //
	/////////////
	public ASTAxiomTheorem(String label, boolean theorem) {
		axiomLabel = label;
		isTheorem = theorem;
		predicate = new ASTPredicate();
	}
	
	public void setPredicate(ASTPredicate newPredicate) {
		predicate = newPredicate;
	}
	
	public String getLabel() {
		return axiomLabel;
	}
	
	public ASTPredicate getPredicate() {
		return predicate;
	}

}
