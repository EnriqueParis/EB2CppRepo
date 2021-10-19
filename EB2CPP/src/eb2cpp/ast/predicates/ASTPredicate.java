package eb2cpp.ast.predicates;

public class ASTPredicate {
	///////////////
	// VARIABLES //
	///////////////
	protected String predicateType;

	/////////////
	// METHODS //
	/////////////
	public ASTPredicate() {
		// TODO Auto-generated constructor stub
		predicateType = "RootPredicate";
	}
	
	public ASTPredicate(String typeName) {
		predicateType = typeName;
	}
	
	public String getType() {
		return predicateType;
	}
	
	public void setAsError() {
		predicateType = "ErrorPredicate";
	}

}
