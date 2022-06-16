package eb2cpp.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.core.runtime.CoreException;
import org.eventb.core.ISCAxiom;
import org.eventb.core.ISCCarrierSet;
import org.eventb.core.ISCConstant;
import org.eventb.core.ISCEvent;
import org.eventb.core.ISCInvariant;
import org.eventb.core.ISCVariable;

import eb2cpp.ast.context.ASTContext;
import eb2cpp.ast.machine.ASTMachine;
import eb2cpp.ast.types.ASTDataType;

public class EB2CppAST {
	///////////////
	// VARIABLES //
	///////////////
	
	private ArrayList<ASTContext> contexts;
	private ArrayList<ASTMachine> machines;
	
	// Map of all FreeIdentifiers and their DataTypes
	private HashMap<String, ASTDataType> freeIdentifiersTypes;
	
	private ASTContext contextBeingBuilt;
	private ASTMachine machineBeingBuilt;
	
	/////////////
	// METHODS //
	/////////////
	public EB2CppAST() {
		contexts = new ArrayList<ASTContext>();
		machines = new ArrayList<ASTMachine>();
		freeIdentifiersTypes = new HashMap<String, ASTDataType>();
	}
	
	public ArrayList<ASTContext> getContexts() {
		return contexts;
	}
	
	public ArrayList<ASTMachine> getMachines() {
		return machines;
	}
	
	public HashMap<String, ASTDataType> getFreeIdentifiersTypes() {
		return freeIdentifiersTypes;
	}
	
	public void addFreeIdentifierType(String freeIdentifierName, ASTDataType dataType) {
		freeIdentifiersTypes.put(freeIdentifierName, dataType);
	}
	
	public ASTContext addContext(String name, ArrayList<String> extensionsNames) {
		ASTContext newContext = new ASTContext(name,this);
		
		
		ASTContext newContextExtension;
		
		//See if it extends another context
		if (extensionsNames.size() != 0) {
			for (String extensionName : extensionsNames) {
				
				//Because of the translation order step in RodinHandler
				//if this context is being processed, it's because the context it
				//extends should already be in CppAST contexts.
				boolean foundASTContext = false;
				int index = 0;
				ASTContext contextInCppAST;
				while (!foundASTContext & index < contexts.size()) {
					contextInCppAST = contexts.get(index);
					if ((contextInCppAST.getContextName()).equals(extensionName)) {
						newContextExtension = contextInCppAST;
						
						newContext.setExtendedContext(newContextExtension);
						
						foundASTContext = true;
					}
					
					index += 1;
				}
			}
		}
		
		contexts.add(newContext);
		return newContext;
	}
	
	public ASTMachine addMachine(String name, ArrayList<String> refinementsNames, LinkedList<String> seenContexts) {
		ASTMachine newMachine = new ASTMachine(name, this);
		
		
		if (refinementsNames.size() != 0) {
			for (String refinementName : refinementsNames) {
				//Because of the translation order step in RodinHandler
				//if this machine is being processed, it's because the machine it
				//refines should already be in AST machines.
				boolean foundASTMachine = false;
				int index = 0;
				ASTMachine machineInCppAST;
				while (!foundASTMachine & index < machines.size()) {
					machineInCppAST = machines.get(index);
					if ((machineInCppAST.getName()).equals(refinementName)) {
						newMachine.setRefinedMachine(machineInCppAST);
						
						foundASTMachine = true;
					}
					
					index += 1;
				}
			}
		}
		
		//See each context that this machine sees, and find it
		for (String seenContext : seenContexts) {
			for (ASTContext contextInCppAST : contexts) {
				if ((contextInCppAST.getContextName()).equals(seenContext))
					newMachine.setSeenContext(seenContext, contextInCppAST);
			}
		}
		
		machines.add(newMachine);
		return newMachine;
	}
	
	public void addConstantToContext(ISCConstant rawConstant, ASTContext context) throws CoreException {
		context.addConstant(rawConstant);
	}
	
	public void addCarrierSetToContext(ISCCarrierSet rawCarrierSet, ASTContext context) throws CoreException {
		context.addCarrierSet(rawCarrierSet);
	}
	
	public void addAxiomTheoremToContext(ISCAxiom rawAxiom, ASTContext context) throws CoreException {
		context.addAxiomTheorem(rawAxiom);
	}
	
	public void addVariableToMachine(ISCVariable rawVariable, ASTMachine machine) throws CoreException {
		machine.addVariable(rawVariable);
	}
	
	public void addInvariantToMachine(ISCInvariant rawInvariant, ASTMachine machine) throws CoreException {
		machine.addInvariant(rawInvariant);
	}
	
	public void addEventToMachine(ISCEvent rawEvent, ASTMachine machine) throws CoreException {
		machine.addEvent(rawEvent);
	}

}
