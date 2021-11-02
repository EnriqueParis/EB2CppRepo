package eb2cpp.ast.machine;

import java.util.LinkedList;

import eb2cpp.ast.expressions.ASTExpression;
import eb2cpp.ast.predicates.ASTPredicate;

public class ASTEvent {
	///////////////
	// VARIABLES //
	///////////////
	
	private String name;
	private LinkedList<ASTVariable> parameters;
	private LinkedList<ASTGuard> guards;
	private LinkedList<ASTAction> actions;
	
	public ASTEvent() {
		// TODO Auto-generated constructor stub
		parameters = new LinkedList<ASTVariable>();
		guards = new LinkedList<ASTGuard>();
		actions = new LinkedList<ASTAction>();
	}
	
	public ASTEvent(String newName) {
		// TODO Auto-generated constructor stub
		name = newName;
		parameters = new LinkedList<ASTVariable>();
		guards = new LinkedList<ASTGuard>();
		actions = new LinkedList<ASTAction>();
	}
	
	public String getName() {
		return name;
	}
	
	public LinkedList<ASTVariable> getParameters(){
		return parameters;
	}
	
	public LinkedList<ASTGuard> getGuards(){
		return guards;
	}
	
	public LinkedList<ASTAction> getActions(){
		return actions;
	}
	
	public void addParameter(ASTVariable newParameter) {
		parameters.add(newParameter);
	}
	
	public void addGuard(ASTGuard newGuard) {
		guards.add(newGuard);
	}
	
	public void addAction(ASTAction newAction) {
		actions.add(newAction);
	}

}
