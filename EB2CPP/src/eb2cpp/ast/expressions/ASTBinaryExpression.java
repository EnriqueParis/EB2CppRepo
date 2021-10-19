package eb2cpp.ast.expressions;

import eb2cpp.ast.types.ASTDataType;

public class ASTBinaryExpression extends ASTExpression {
	
	private String typeOfBinaryExpression;
	
	private ASTDataType leftDataType;
	private ASTDataType rightDataType;
	private ASTExpression leftSideExpression;
	private ASTExpression rightSideExpression;
	
	public ASTBinaryExpression() {
		// TODO Auto-generated constructor stub
		expressionType = "BinaryExpression";
	}
	
	public ASTBinaryExpression(String givenTypeOfExpression) {
		expressionType = "BinaryExpression";
		typeOfBinaryExpression = givenTypeOfExpression;
	}
	
	public String getBinaryExpressionType() {
		return typeOfBinaryExpression;
	}
	
	public ASTExpression getLeftSideExpression() {
		return leftSideExpression;
	}
	
	public ASTExpression getRightSideExpression() {
		return rightSideExpression;
	}
	
	public ASTDataType getLeftDataType() {
		return leftDataType;
	}
	
	public ASTDataType getRightDataType() {
		return rightDataType;
	}
	
	public void setLeftDataType(ASTDataType newType) {
		leftDataType = newType;
	}
	
	public void setRightDataType(ASTDataType newType) {
		rightDataType = newType;
	}
	
	public void setLeftSide(ASTExpression newLeftSide) {
		leftSideExpression = newLeftSide;
	}
	
	public void setRightSide(ASTExpression newRightSide) {
		rightSideExpression = newRightSide;
	}

}
