package eb2cpp.ast.expressions;

public class ASTAtomicExpression extends ASTExpression {

	private String atomicExpressionType;
	
	public ASTAtomicExpression() {
		// TODO Auto-generated constructor stub
	}
	
	public ASTAtomicExpression (String givenAtomicExpressionType) {
		expressionType = "AtomicExpression";
		atomicExpressionType = givenAtomicExpressionType;
	}
	
	public String getAtomicExpressionType() {
		return atomicExpressionType;
	}

}
