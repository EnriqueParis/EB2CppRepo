package eb2cpp.ast.predicates;

import java.util.ArrayList;

public class ASTAssociativePredicate extends ASTPredicate {
	
	private String typeOfAssociative;
	private ArrayList<ASTPredicate> childPredicates; //Predicates in AND or OR expression

	public ASTAssociativePredicate() {
		// TODO Auto-generated constructor stub
		predicateType = "AssociativePredicate";
		childPredicates = new ArrayList<ASTPredicate>();
	}
	
	public ASTAssociativePredicate(String type) {
		// TODO Auto-generated constructor stub
		predicateType = "AssociativePredicate";
		typeOfAssociative = type;
		childPredicates = new ArrayList<ASTPredicate>();
	}
	
	public String getAssociativeType() {
		return typeOfAssociative;
	}
	
	public ArrayList<ASTPredicate> getChildPredicates(){
		return childPredicates;
	}
	
	public void addPredicate(ASTPredicate child) {
		childPredicates.add(child);
	}

}
