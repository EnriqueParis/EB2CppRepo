package eb2cpp.ast.machine;

import eb2cpp.ast.predicates.ASTPredicate;

public class ASTInvariant {
	///////////////
	// VARIABLES //
	///////////////
	private String label;
	private ASTPredicate predicate;
	private boolean isTheorem;
	
	public ASTInvariant() {
		// TODO Auto-generated constructor stub
	}
	
	public ASTInvariant(String newLabel, boolean newIsTheorem) {
		label = newLabel;
		isTheorem = newIsTheorem;
	}
	
	public void setPredicate (ASTPredicate newPredicate) {
		predicate = newPredicate;
	}

}
