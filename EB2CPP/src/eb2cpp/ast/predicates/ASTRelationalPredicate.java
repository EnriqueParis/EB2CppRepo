package eb2cpp.ast.predicates;

import eb2cpp.ast.expressions.ASTExpression;

public class ASTRelationalPredicate extends ASTPredicate {
	///////////////
	// VARIABLES //
	///////////////
	private String typeOfRelation;
	private ASTExpression leftSideExpression;
	private ASTExpression rightSideExpression;
	
	/////////////
	// METHODS //
	/////////////
	public ASTRelationalPredicate() {
		predicateType = "RelationalPredicate";
		typeOfRelation = "JustInitialized";
	}
	
	public ASTRelationalPredicate(String newTypeOfRelation) {
		predicateType = "RelationalPredicate";
		typeOfRelation = newTypeOfRelation;
	}
	
	public String getRelationalType() {
		return typeOfRelation;
	}
	
	public ASTExpression getLeftSide() {
		return leftSideExpression;
	}
	
	public ASTExpression getRightSide() {
		return rightSideExpression;
	}
	
	public void setLeftSideExpression(ASTExpression leftSide) {
		leftSideExpression = leftSide;
	}
	
	public void setRightSideExpression(ASTExpression rightSide) {
		rightSideExpression = rightSide;
	}

}
