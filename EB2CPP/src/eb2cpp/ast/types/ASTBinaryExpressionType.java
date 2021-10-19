package eb2cpp.ast.types;

public class ASTBinaryExpressionType extends ASTDataType {
	///////////////
	// VARIABLES //
	///////////////
	private ASTDataType leftSideDataType;
	private ASTDataType rightSideDataType;
	private String binaryOperator;
	
	/////////////
	// METHODS //
	/////////////
	public ASTBinaryExpressionType(String operation) {
		typeName = "BinaryExpression";
		binaryOperator = operation;
	}
	
	public void setLeftSide(ASTDataType leftData) {
		leftSideDataType = leftData;
	}
	
	public void setRightSide(ASTDataType rightData) {
		rightSideDataType = rightData;
	}
	
	public String getBinaryOperator() {
		return binaryOperator;
	}
	
	public ASTDataType getLeftSideDataType() {
		return leftSideDataType;
	}
	
	public ASTDataType getRightSideDataType() {
		return rightSideDataType;
	}

}
