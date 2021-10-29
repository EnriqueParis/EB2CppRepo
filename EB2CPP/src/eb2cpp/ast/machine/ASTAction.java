package eb2cpp.ast.machine;

public class ASTAction {
	private String label;
	private ASTAssignment assignment;
	
	public ASTAction() {
		// TODO Auto-generated constructor stub
	}
	
	public ASTAction(String newLabel) {
		label = newLabel;
	}
	
	public String getLabel() {
		return label;
	}
	
	public ASTAssignment getAssignment() {
		return assignment;
	}
	
	public void setAssignment(ASTAssignment a) {
		assignment = a;
	}

}
