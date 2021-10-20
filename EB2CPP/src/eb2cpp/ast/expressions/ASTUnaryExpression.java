package eb2cpp.ast.expressions;

public class ASTUnaryExpression extends ASTExpression {
	
	private String typeOfUnary;
	private ASTExpression internalExpression;

	public ASTUnaryExpression() {
		// TODO Auto-generated constructor stub
		expressionType = "UnaryExpression";
	}
	
	public ASTUnaryExpression(String newTypeOfUnary) {
		expressionType = "UnaryExpression";
		typeOfUnary = newTypeOfUnary;
	}
	
	public void setInternalExpression(ASTExpression givenInternal) {
		internalExpression = givenInternal;
	}
	
	public String getUnaryType() {
		return typeOfUnary;
	}
	
	public ASTExpression getInternalExpression() {
		return internalExpression;
	}

}
