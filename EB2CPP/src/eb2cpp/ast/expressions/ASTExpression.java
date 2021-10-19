package eb2cpp.ast.expressions;

public class ASTExpression {
	
	protected String expressionType;
	
	public ASTExpression() {
		expressionType = "DefaultExpressionType";
	}
	
	public String getType() {
		return expressionType;
	}
	
	public void setAsError() {
		expressionType = "ErrorExpression";
	}

}
