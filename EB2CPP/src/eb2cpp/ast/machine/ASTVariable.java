package eb2cpp.ast.machine;

import eb2cpp.ast.types.ASTDataType;

public class ASTVariable {
	///////////////
	// VARIABLES //
	///////////////
	private String name;
	private ASTDataType type;

	/////////////
	// METHODS //
	/////////////
	public ASTVariable() {
		// TODO Auto-generated constructor stub
	}
	
	public ASTVariable (String newName) {
		name = newName;
	}
	
	public void setDataType(ASTDataType newType) {
		type = newType;
	}

}
