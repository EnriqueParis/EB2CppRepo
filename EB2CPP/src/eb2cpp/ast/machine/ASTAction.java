package eb2cpp.ast.machine;

import eb2cpp.ast.expressions.ASTExpression;
import eb2cpp.ast.expressions.ASTFreeIdentifier;

public class ASTAction {
	private String label;
	private ASTAssignment assignment;
	
	public ASTAction() {
		// TODO Auto-generated constructor stub
	}
	
	public ASTAction(String newLabel) {
		label = newLabel;
	}
	
	public void setAssignment(ASTAssignment a) {
		assignment = a;
	}

}
