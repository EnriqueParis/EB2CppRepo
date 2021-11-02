package eb2cpp.ast.machine;

import eb2cpp.ast.predicates.ASTPredicate;

public class ASTGuard extends ASTPredicate {
	///////////////
	// VARIABLES //
	///////////////
	private String guardLabel;
	private ASTPredicate guardPredicate;
	
	/////////////
	// METHODS //
	/////////////
	
	public ASTGuard() {
		// TODO Auto-generated constructor stub
	}
	
	public ASTGuard(String label, ASTPredicate newP) {
		guardLabel = label;
		guardPredicate = newP;
	}
	
	public String getGuardLabel() {
		return guardLabel;
	}
	
	public ASTPredicate getGuardPredicate() {
		return guardPredicate;
	}

}
