package eb2cpp.ast.context;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eventb.core.ISCAxiom;
import org.eventb.core.ISCCarrierSet;
import org.eventb.core.ISCConstant;
import org.eventb.core.ISCIdentifierElement;
import org.eventb.core.ast.FormulaFactory;

import eb2cpp.ast.EB2CppAST;
import eb2cpp.ast.EB2CppVisitor;
import eb2cpp.ast.expressions.ASTExpression;
import eb2cpp.ast.expressions.ASTFreeIdentifier;
import eb2cpp.ast.predicates.ASTMultiplePredicate;
import eb2cpp.ast.predicates.ASTPredicate;
import eb2cpp.ast.types.ASTDataType;
import eb2cpp.ast.types.ASTFreeIdentifierType;

public class ASTContext {
	///////////////
	// VARIABLES //
	///////////////
	private EB2CppAST CppAST;
	
	private String contextName;
	private ArrayList<ASTContext> extendedContext; //If this value is empty, it doesn't extend any other context
	
	private HashMap<String,ASTConstant> constants;
	private HashMap<String,ASTCarrierSet> carrierSets;
	private HashMap<String,ASTAxiomTheorem> axioms;
	
	private EB2CppVisitor Visitor;
	
	/////////////
	// METHODS //
	/////////////
	
	public ASTContext(String name, EB2CppAST ast) {
		contextName = name;
		extendedContext = new ArrayList<ASTContext>();
		constants = new HashMap<String,ASTConstant>();
		carrierSets = new HashMap<String,ASTCarrierSet>();
		axioms = new HashMap<String,ASTAxiomTheorem>();
		
		CppAST = ast;
		
		Visitor = new EB2CppVisitor();
		Visitor.setCppAST(CppAST);
	}
	
	
	public String getContextName() {
		return contextName;
	}
	
	public void setExtendedContext(ASTContext context) {
		extendedContext.add(context);
	}
	
	public void addElementToSet(String set, String element) {
		carrierSets.get(set).addSetElement(element);
	}
	
	public HashMap<String,ASTConstant> getConstants() {
		return constants;
	}
	
	public HashMap<String,ASTCarrierSet> getCarrierSets() {
		return carrierSets;
	}
	
	public HashMap<String,ASTAxiomTheorem> getAxioms() {
		return axioms;
	}
	
	public ArrayList<ASTContext> getExtendedContexts() {
		return extendedContext;
	}

	public void addConstant(ISCConstant constantRoot) throws CoreException {
		String constantName = constantRoot.getElementName();
		ASTConstant newConstant = new ASTConstant(constantName);
		
		System.out.println("Name of Constant: " + constantName);
		
		final FormulaFactory factory = FormulaFactory.getDefault();
		
		ASTDataType constantType = Visitor.getDataType(((ISCIdentifierElement) constantRoot).getType(factory).toString());
		newConstant.assignDataType(constantType);
		
		constants.put(constantName, newConstant);
		
		// Add the constant (a free identifier) to the AST list of freeIdentifiers
		// for future use in CppTranslation (i.e. set extension typing)
		CppAST.addFreeIdentifierType(constantName, constantType);
		
		// FOR CARRIER SET PARTITION
		// If the constant is an element of a SET, we need to modify the
		// set AST element, so that when its translated to Cpp, the constant
		// is in its elements.
		// The constant must be a FreeIdentifier belonging to a SET
		
		if (constantType.getTypeName().equals("FreeIdentifier")) {
			String setName = ((ASTFreeIdentifierType) constantType).getIdentifierName();
			
			if (carrierSets.containsKey(setName)) {
				// The partition is about a CARRIER SET
				//carrierSets.get(setName).setIsPartitioned(true);
				carrierSets.get(setName).addSetElement(constantName);
				newConstant.setNeedsToBeGenerated(false);
			}
		}
	}
	
	public void addCarrierSet(ISCCarrierSet carrierSet) throws CoreException {
		String setName = carrierSet.getElementName();
		ASTCarrierSet newCarrierSet = new ASTCarrierSet(setName);
		
		// Add the carrierset (a free identifier) to the AST list of freeIdentifiers
		// for future use in CppTranslation (i.e. set extension typing)
		CppAST.addFreeIdentifierType(setName, new ASTFreeIdentifierType(setName));
		
		carrierSets.put(setName, newCarrierSet);
	}
	
	public void addAxiomTheorem(ISCAxiom axiom) throws CoreException {
		String axiomName = axiom.getLabel();
		boolean isTheorem = axiom.isTheorem();
		
		System.out.println("Name of Axiom: " + axiomName);
		
		ASTAxiomTheorem newAxiom = new ASTAxiomTheorem(axiomName, isTheorem);
		
		ASTPredicate newAxiomPredicate = new ASTPredicate();
		
		newAxiomPredicate = Visitor.getPredicate(axiom.getPredicateString());
		
		// We will check if the axiom is a partition of a carrier set.
		// In that case, we have to set the carrierSet as being partitioned
		if (newAxiomPredicate.getType() == "MultiplePredicate") {
			ASTMultiplePredicate partitionPredicate = (ASTMultiplePredicate) newAxiomPredicate;
			ASTFreeIdentifier partedSet = (ASTFreeIdentifier) partitionPredicate.getPartitionedSet();
			String partitionedSetName = partedSet.getIdentifier();
			carrierSets.get(partitionedSetName).setIsPartitioned(true);
		}
		
		newAxiom.setPredicate(newAxiomPredicate);
		
		axioms.put(axiomName, newAxiom);
	}
	
}
