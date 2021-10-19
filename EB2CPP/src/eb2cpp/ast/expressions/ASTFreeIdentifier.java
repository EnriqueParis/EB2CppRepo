package eb2cpp.ast.expressions;

public class ASTFreeIdentifier extends ASTExpression {
	
	private String identifier;

	public ASTFreeIdentifier() {
		// TODO Auto-generated constructor stub
		expressionType = "FreeIdentifier";
	}
	
	public ASTFreeIdentifier(String newIdentifier) {
		// TODO Auto-generated constructor stub
		expressionType = "FreeIdentifier";
		identifier = newIdentifier;
	}
	
	public String getIdentifier() {
		return identifier;
	}

}
