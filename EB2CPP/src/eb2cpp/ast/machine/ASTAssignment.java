package eb2cpp.ast.machine;

import eb2cpp.ast.expressions.ASTExpression;
import eb2cpp.ast.expressions.ASTFreeIdentifier;

public class ASTAssignment {
	
	private String type;
	private ASTFreeIdentifier changingIdentifier;
	private ASTExpression newValue;

	public ASTAssignment() {
		// TODO Auto-generated constructor stub
	}
	
	public ASTAssignment(String newType) {
		// TODO Auto-generated constructor stub
		type = newType;
	}
	
	public String getType() {
		return type;
	}
	
	public ASTFreeIdentifier getChangingIdentifier() {
		return changingIdentifier;
	}
	
	public ASTExpression getNewValue() {
		return newValue;
	}
	
	public void setChangingIdentifier(ASTFreeIdentifier i) {
		changingIdentifier = i;
	}
	
	public void setNewValue(ASTExpression e) {
		newValue = e;
	}

}
