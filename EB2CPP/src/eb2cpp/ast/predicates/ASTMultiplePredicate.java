package eb2cpp.ast.predicates;

import java.util.ArrayList;

import eb2cpp.ast.expressions.ASTExpression;

public class ASTMultiplePredicate extends ASTPredicate {
	
	private ASTExpression set;
	private ArrayList<ASTExpression> children; 

	public ASTMultiplePredicate() {
		// TODO Auto-generated constructor stub
		predicateType = "MultiplePredicate";
		children = new ArrayList<ASTExpression>();
	}
	
	public void setSetToCheck(ASTExpression newSet) {
		set = newSet;
	}
	public void addChild(ASTExpression expression) {
		children.add(expression);
	}

}
