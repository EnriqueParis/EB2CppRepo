package eb2cpp.ast.predicates;

import java.util.ArrayList;

import eb2cpp.ast.expressions.ASTExpression;
import eb2cpp.ast.types.ASTDataType;

public class ASTMultiplePredicate extends ASTPredicate {
	
	private ASTExpression set;
	private ArrayList<ASTExpression> children;
	private ASTDataType setDataType;

	public ASTMultiplePredicate() {
		// TODO Auto-generated constructor stub
		predicateType = "MultiplePredicate";
		children = new ArrayList<ASTExpression>();
		setDataType = new ASTDataType();
	}
	
	public ASTExpression getPartitionedSet() {
		return set;
	}
	
	public ArrayList<ASTExpression> getChildren(){
		return children;
	}
	
	public ASTDataType getSetDataType() {
		return setDataType;
	}
	
	public void setSetDataType(ASTDataType newType) {
		setDataType = newType;
	}
	
	public void setSetToCheck(ASTExpression newSet) {
		set = newSet;
	}
	public void addChild(ASTExpression expression) {
		children.add(expression);
	}

}
