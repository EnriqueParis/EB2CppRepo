package eb2cpp.ast.expressions;

import java.util.ArrayList;

public class ASTAssociativeExpression extends ASTExpression {
	
	private String typeOfAssociativeExpression;
	private ArrayList<ASTExpression> childExpression;

	public ASTAssociativeExpression() {
		// TODO Auto-generated constructor stub
		expressionType = "AssociativeExpression";
		childExpression = new ArrayList<ASTExpression>();
	}
	
	public ASTAssociativeExpression(String newType) {
		// TODO Auto-generated constructor stub
		expressionType = "AssociativeExpression";
		typeOfAssociativeExpression = newType;
		childExpression = new ArrayList<ASTExpression>();
	}
	
	public void addChildExpression(ASTExpression newChild) {
		childExpression.add(newChild);
	}
	
	public String getAssociativeType() {
		return typeOfAssociativeExpression;
	}
	
	public ArrayList<ASTExpression> getChildExpressions() {
		return childExpression;
	}

}
