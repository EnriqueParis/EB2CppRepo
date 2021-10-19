package eb2cpp.ast.expressions;

public class ASTUnaryExpression extends ASTExpression {
	
	private String typeOfUnary;
	private ASTExpression internalExpression;

	public ASTUnaryExpression() {
		// TODO Auto-generated constructor stub
	}
	
	public ASTUnaryExpression(String newTypeOfUnary) {
		typeOfUnary = newTypeOfUnary;
	}
	
	public void setInternalExpression(ASTExpression givenInternal) {
		internalExpression = givenInternal;
	}

}
