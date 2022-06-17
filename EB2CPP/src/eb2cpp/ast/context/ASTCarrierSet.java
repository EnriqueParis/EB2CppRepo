package eb2cpp.ast.context;

import java.util.ArrayList;

import eb2cpp.ast.expressions.ASTExpression;

public class ASTCarrierSet {
	///////////////
	// VARIABLES //
	///////////////
	private String carrierSetName;
	private ArrayList<String> setElements;
	private boolean isPartitioned;
	private ArrayList<String> finalSetElements;
	
	/////////////
	// METHODS //
	/////////////
	
	public ASTCarrierSet(String name) {
		carrierSetName = name;
		setElements = new ArrayList<String>();
		isPartitioned = false;
		finalSetElements = new ArrayList<String>();
	}
	
	public String getName() {
		return carrierSetName;
	}
	
	public boolean hasPartition() {
		return isPartitioned;
	}
	
	public void addSetElement(String e) {
		setElements.add(e);
	}
	
	public ArrayList<String> getSetElements() {
		return setElements;
	}
	
	public void setIsPartitioned(boolean b) {
		isPartitioned = b;
	}
	
	public ArrayList<String> getFinalSetElements () {
		return finalSetElements;
	}
	
	public void setFinalSetElements(ArrayList<String> v) {
		finalSetElements = v;
	}
}
