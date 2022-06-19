package eb2cpp.ast.context;

import eb2cpp.ast.types.ASTDataType;

public class ASTConstant {
	///////////////
	// VARIABLES //
	///////////////
	
	private String constantName;
	private ASTDataType dataType;
	private String dataTypeText;
	private boolean needsToBeGenerated;
	
	/////////////
	// METHODS //
	/////////////
	
	public ASTConstant(String name) {
		constantName = name;
		needsToBeGenerated = true;
	}
	
	public ASTDataType getDataType() {
		return dataType;
	}
	
	public String getConstantName() {
		return constantName;
	}
	
	public boolean getNeedsToBeGenerated() {
		return needsToBeGenerated;
	}
	
	public void setNeedsToBeGenerated(boolean newValue) {
		needsToBeGenerated = newValue;
	}
	
	public void assignDataType(ASTDataType newDataType) {
		dataType = newDataType;
	}
	
	public String getDataTypeText() {
		return dataTypeText;
	}
	
	public void setDataTypeText(String s) {
		dataTypeText = s;
	}
}
