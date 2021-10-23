package eb2cpp.ast.predicates;

import eb2cpp.ast.expressions.ASTExpression;

public class ASTSimplePredicate extends ASTPredicate {
	
	private String typeOfSimplePredicate;
	private ASTExpression internalExpression;

	public ASTSimplePredicate() {
		// TODO Auto-generated constructor stub
		predicateType = "SimplePredicate";
	}
	
	public ASTSimplePredicate(String newType) {
		// TODO Auto-generated constructor stub
		predicateType = "SimplePredicate";
		typeOfSimplePredicate = newType;
	}
	
	public String getSimplePredicateType() {
		return typeOfSimplePredicate;
	}
	
	public ASTExpression getInternalExpression() {
		return internalExpression;
	}
	
	public void setInternalExpression(ASTExpression expression) {
		internalExpression = expression;
	}

}
