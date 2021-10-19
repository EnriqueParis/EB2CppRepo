package eb2cpp.ast.predicates;

import eb2cpp.ast.expressions.ASTExpression;

public class ASTUnaryPredicate extends ASTPredicate {

	private String typeOfUnaryPredicate;
	private ASTPredicate internalPredicate;
	
	public ASTUnaryPredicate() {
		// TODO Auto-generated constructor stub
		predicateType = "UnaryPredicate";
	}
	
	public ASTUnaryPredicate(String type) {
		// TODO Auto-generated constructor stub
		predicateType = "UnaryPredicate";
		typeOfUnaryPredicate = type;
	}
	
	public String getUnaryType() {
		return typeOfUnaryPredicate;
	}
	
	public ASTPredicate getInternalPredicate() {
		return internalPredicate;
	}
	
	public void setInternalPredicate(ASTPredicate predicate) {
		internalPredicate = predicate;
	}

}
