package eb2cpp.ast.expressions;

import eb2cpp.ast.types.ASTDataType;

public class ASTSetExtension extends ASTExpression {
	
	///////////////
	// VARIABLES //
	///////////////
	
	private int numberOfElements;
	private ASTDataType setType;
	private ASTExpression[] elements;
	
	/////////////
	// METHODS //
	/////////////

	public ASTSetExtension() {
		// TODO Auto-generated constructor stub
		expressionType = "SetExtension";
	}
	
	public ASTSetExtension(int n) {
		// TODO Auto-generated constructor stub
		expressionType = "SetExtension";
		numberOfElements = n;
		elements = new ASTExpression[numberOfElements];
	}
	
	public ASTDataType getSetType() {
		return setType;
	}
	
	public ASTExpression[] getElements() {
		return elements;
	}
	
	public void setElement(int index, ASTExpression e) {
		elements[index] = e;
	}
	
	public void setSetType(ASTDataType type) {
		setType = type;
	}

}
