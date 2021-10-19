package eb2cpp.ast.types;

public class ASTUnaryExpressionType extends ASTDataType {
	///////////////
	// VARIABLES //
	///////////////
	private String unaryOperator;
	private ASTDataType internalType;
	
	/////////////
	// METHODS //
	/////////////
	public ASTUnaryExpressionType(String operator) {
		typeName = "UnaryExpression";
		unaryOperator = operator;
	}
	
	public void setInternalType(ASTDataType newInternalType) {
		internalType = newInternalType;
	}
	
	public String getUnaryOperator() {
		return unaryOperator;
	}
	
	public ASTDataType getInternalType() {
		return internalType;
	}

}
