package eb2cpp.ast.types;

public class ASTFreeIdentifierType extends ASTDataType {
	///////////////
	// VARIABLES //
	///////////////
	private String identifierName;
	
	/////////////
	// METHODS //
	/////////////
	
	public ASTFreeIdentifierType(String newIdentifierName) {
		typeName = "FreeIdentifier";
		identifierName = newIdentifierName;
	}
	
	public String getIdentifierName() {
		return identifierName;
	}
}
