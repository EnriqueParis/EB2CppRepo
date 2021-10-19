package eb2cpp.ast.predicates;

public class ASTBinaryPredicate extends ASTPredicate {
	
	private String typeOfBinaryPredicate;
	private ASTPredicate leftSide;
	private ASTPredicate rightSide;

	public ASTBinaryPredicate() {
		// TODO Auto-generated constructor stub
		predicateType = "BinaryPredicate";
	}
	
	public ASTBinaryPredicate(String type) {
		// TODO Auto-generated constructor stub
		predicateType = "BinaryPredicate";
		typeOfBinaryPredicate = type;
	}
	
	public String getBinaryType() {
		return typeOfBinaryPredicate;
	}
	
	public ASTPredicate getLeftSide() {
		return leftSide;
	}
	
	public ASTPredicate getRightSide() {
		return rightSide;
	}
	
	public void setLeftSide(ASTPredicate newPredicate) {
		leftSide = newPredicate;
	}
	
	public void setRightSide(ASTPredicate newPredicate) {
		rightSide = newPredicate;
	}
}
