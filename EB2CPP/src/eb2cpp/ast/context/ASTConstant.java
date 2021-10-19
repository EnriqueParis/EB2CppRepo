package eb2cpp.ast.context;

import eb2cpp.ast.types.ASTDataType;

public class ASTConstant {
	///////////////
	// VARIABLES //
	///////////////
	
	private String constantName;
	private ASTDataType dataType;
	
	/////////////
	// METHODS //
	/////////////
	
	public ASTConstant(String name) {
		constantName = name; 
	}
	
	public ASTDataType getDataType() {
		return dataType;
	}
	
	public String getConstantName() {
		return constantName;
	}
	
	public void assignDataType(ASTDataType newDataType) {
		dataType = newDataType;
	}
	
}
