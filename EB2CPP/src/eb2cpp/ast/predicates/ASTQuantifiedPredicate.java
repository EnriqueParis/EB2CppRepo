package eb2cpp.ast.predicates;

import eb2cpp.ast.types.ASTDataType;

public class ASTQuantifiedPredicate extends ASTPredicate {
	
	private String quantifierType;
	private ASTDataType[] boundIdentifiersTypes;
	private ASTPredicate internalPredicate;

	public ASTQuantifiedPredicate() {
		// TODO Auto-generated constructor stub
		predicateType = "QuantifiedPredicate";
	}
	
	public ASTQuantifiedPredicate(String newQuantType, int numOfIdentifiers) {
		predicateType = "QuantifiedPredicate";
		
		quantifierType = newQuantType;
		boundIdentifiersTypes = new ASTDataType[numOfIdentifiers];
	}
	
	public String getQuantifierType() {
		return quantifierType;
	}
	
	public ASTDataType[] getBoundIdentifiersTypes() {
		return boundIdentifiersTypes;
	}
	
	public void setBoundIdentifierType(int index, ASTDataType dataType) {
		boundIdentifiersTypes[index] = dataType;
	}
	
	public ASTPredicate getInternalPredicate() {
		return internalPredicate;
	}
	
	public void setInternalPredicate(ASTPredicate newPredicate) {
		internalPredicate = newPredicate;
	}

}
