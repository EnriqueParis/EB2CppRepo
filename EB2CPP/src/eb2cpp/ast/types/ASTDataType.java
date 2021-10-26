package eb2cpp.ast.types;

public class ASTDataType {
	///////////////
	// VARIABLES //
	///////////////

	protected String typeName;
	
/////////////
// METHODS //
/////////////
	
	public ASTDataType() {typeName = new String();}
	public ASTDataType(String name) {
		typeName = name;
	}
	
	public String getTypeName() {
		return typeName;
	}
	
	public void setAsError() {
		typeName = "ErrorDataType";
	}
}
