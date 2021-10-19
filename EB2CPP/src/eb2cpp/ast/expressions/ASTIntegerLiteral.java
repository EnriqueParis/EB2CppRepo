package eb2cpp.ast.expressions;

public class ASTIntegerLiteral extends ASTExpression {
	
	private String integerValue;

	public ASTIntegerLiteral() {
		// TODO Auto-generated constructor stub
		expressionType = "IntegerLiteral";
	}
	
	public ASTIntegerLiteral(String value) {
		// TODO Auto-generated constructor stub
		expressionType = "IntegerLiteral";
		integerValue = value;
	}
	
	public String getValue() {
		return integerValue;
	}

}
