package eb2cpp.ast.machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.core.runtime.CoreException;
import org.eventb.core.ISCAction;
import org.eventb.core.ISCEvent;
import org.eventb.core.ISCGuard;
import org.eventb.core.ISCIdentifierElement;
import org.eventb.core.ISCInvariant;
import org.eventb.core.ISCParameter;
import org.eventb.core.ISCVariable;
import org.eventb.core.ast.FormulaFactory;

import eb2cpp.ast.EB2CppVisitor;
import eb2cpp.ast.context.ASTAxiomTheorem;
import eb2cpp.ast.context.ASTConstant;
import eb2cpp.ast.context.ASTContext;
import eb2cpp.ast.predicates.ASTPredicate;
import eb2cpp.ast.types.ASTDataType;


public class ASTMachine {
	///////////////
	// VARIABLES //
	///////////////
	private String name;
	private HashMap<String,ASTContext> seenContexts;
	private ArrayList<ASTMachine> refinedMachine;
	private HashMap<String, ASTVariable> variables;
	private HashMap<String, ASTInvariant> invariants;
	
	private EB2CppVisitor Visitor;
	
	/////////////
	// METHODS //
	/////////////
	public ASTMachine() {
		// TODO Auto-generated constructor stub
	}
	
	public ASTMachine(String newName) {
		name = newName;
		seenContexts = new HashMap<String,ASTContext>();
		refinedMachine = new ArrayList<ASTMachine>();
		variables = new HashMap<String, ASTVariable>();
		invariants = new HashMap<String, ASTInvariant>();
		Visitor = new EB2CppVisitor();
	}
	
	public String getName() {
		return name;
	}
	
	public HashMap<String, ASTVariable> getVariables() {
		return variables;
	}
	
	public HashMap<String, ASTInvariant> getInvariants() {
		return invariants;
	}
	
	public void setSeenContext(String contextName, ASTContext context) {
		seenContexts.put(contextName, context);
	}
	
	public void setRefinedMachine(ASTMachine newRefinedMachine) {
		refinedMachine.add(newRefinedMachine);
	}
	
	public void addVariable(ISCVariable variableRoot) throws CoreException {
		String variableName = variableRoot.getElementName();
		ASTVariable newVariable = new ASTVariable(variableName);
		
		System.out.println("Name of Variable: " + variableName);
		
		final FormulaFactory factory = FormulaFactory.getDefault();
		
		ASTDataType variableType = Visitor.getDataType(((ISCIdentifierElement) variableRoot).getType(factory).toString());
		newVariable.setDataType(variableType);
		
		variables.put(variableName, newVariable);
	}
	
	public void addInvariant(ISCInvariant invariantRoot) throws CoreException {
		String invariantLabel = invariantRoot.getLabel();
		boolean isTheorem = invariantRoot.isTheorem();
		
		System.out.println("Name of Invariant: " + invariantLabel);
		
		ASTInvariant newInvariant = new ASTInvariant(invariantLabel, isTheorem);
		
		ASTPredicate newInvariantPredicate = Visitor.getPredicate(invariantRoot.getPredicateString());
		
		newInvariant.setPredicate(newInvariantPredicate);
		
		invariants.put(invariantLabel, newInvariant);
	}
	
	public void addEvent(ISCEvent eventRoot) throws CoreException {
		String eventLabel = eventRoot.getLabel();
		
		System.out.println("Name of Event: " + eventLabel);
		
		ASTEvent newEvent = new ASTEvent(eventLabel);
		
		System.out.println("- Processing event parameters...");
		
		//Any's / Parameters
		ASTVariable parameterVariable;
		ASTDataType parameterDataType;
		final FormulaFactory factory = FormulaFactory.getDefault();
		
		for (ISCParameter parameter : eventRoot.getSCParameters()) {
			parameterVariable = new ASTVariable(parameter.getElementName());
			parameterDataType = Visitor.getDataType(((ISCIdentifierElement) parameter).getType(factory).toString());
			parameterVariable.setDataType(parameterDataType);
			
			newEvent.addParameter(parameterVariable);
		}
		
		System.out.println("- Processing event guards...");
		
		//Guards / Conditions for event
		ASTPredicate guardPredicate;
		for (ISCGuard guard : eventRoot.getSCGuards()) {
			guardPredicate = Visitor.getPredicate(guard.getPredicateString());
			
			newEvent.addGuard(guardPredicate);
		}
		
		System.out.println("- Processing event actions...");
		
		//Actions
		ASTAction action;
		for (ISCAction actionRoot : eventRoot.getSCActions()) {
			String actionLabel = actionRoot.getLabel();
			action = new ASTAction(actionLabel);
			
			ASTAssignment assignment = Visitor.getAssignment(actionRoot.getAssignmentString());
			
			action.setAssignment(assignment);
		}
	}

}
