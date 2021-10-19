package eb2cpp.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import org.eclipse.core.runtime.CoreException;
import org.eventb.core.ISCAxiom;
import org.eventb.core.ISCCarrierSet;
import org.eventb.core.ISCConstant;
import org.eventb.core.ISCEvent;
import org.eventb.core.ISCInvariant;
import org.eventb.core.ISCVariable;
import org.eventb.core.basis.SCContextRoot;
import org.eventb.core.basis.SCMachineRoot;
import org.rodinp.core.RodinDBException;

import eb2cpp.ast.EB2CppAST;
import eb2cpp.ast.context.ASTContext;
import eb2cpp.ast.machine.ASTMachine;

public class MainHandler {
	///////////////
	// VARIABLES //
	///////////////
	
	private EB2CppRodinHandler rodinHandler;
	private String ProjectName;
	private EB2CppAST CppAST;
	private String rodinFilesPath;
	private CodeGenerationHandler generationHandler;
	
	/////////////////
	// Constructor //
	/////////////////
	public MainHandler() throws RodinDBException {
		ProjectName = askProjectName();
		rodinHandler = new EB2CppRodinHandler(ProjectName);
		CppAST = new EB2CppAST();
		
	}
	
	/////////////
	// METHODS //
	/////////////
	
	public String askProjectName() {
		String outputString = "";
		String inputtedProjectName = JOptionPane.showInputDialog("Type name of project to be translated");
		if (inputtedProjectName != null) {outputString = inputtedProjectName;}
		return outputString;
	}
	
	public boolean projectExists() {
		return rodinHandler.projectExists();
	}
	
	public void turnContextsIntoNewAST() throws RodinDBException{
		System.out.println("TURNING CONTEXTS TO CPPAST...");
		SCContextRoot context2Translate;
		ArrayList<String> extendedContextsNames = new ArrayList<String>();
		HashMap<String,ArrayList<String>> contextExtensions = rodinHandler.getContextsExtensions();
		
		//Using the sorted translation order, obtain each name to store each into an AST
		for (String contextName2Translate : rodinHandler.getContextNamesOrdered()) {
			context2Translate = rodinHandler.getContextRoot(contextName2Translate);

			extendedContextsNames = contextExtensions.get(contextName2Translate);
			
			
			System.out.println("CREATING EMPTY CONTEXT INTO AST...");
			ASTContext contextBeingBuilt = CppAST.addContext(contextName2Translate, extendedContextsNames);
			
			System.out.println("TURNING CARRIER SETS INTO AST...");
			//Turn carrier sets to CPPAST.
			//PREGUNTAR A JUANFER ACERCA DEL ESTIMATED SIZE DE CARRIER SETS
			for (ISCCarrierSet carrierSet : context2Translate.getSCCarrierSets()) {
				try {
					CppAST.addCarrierSetToContext(carrierSet, contextBeingBuilt);
				}
				catch (CoreException  e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("TURNING CONSTANTS INTO AST...");
			//Turn constants to CPPAST
			for (ISCConstant constant : context2Translate.getSCConstants()) {
				try {
					CppAST.addConstantToContext(constant, contextBeingBuilt);
				}
				catch (CoreException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("TURNING AXIOMS INTO AST...");
			//Turn axioms to CPPAST.
			for (ISCAxiom axiomTheorem : context2Translate.getSCAxioms()) {
				try {
					CppAST.addAxiomTheoremToContext(axiomTheorem, contextBeingBuilt);
				}
				catch (CoreException  e) {
					e.printStackTrace();
				}
			}
			
			extendedContextsNames = new ArrayList<String>();
		}
		
		
	}
	
	public void turnMachinesIntoNewAST() throws RodinDBException {
		
		System.out.println("TURNING MACHINES INTO CPPAST...");
		SCMachineRoot machine2Translate;
		ArrayList<String> refinedMachinesNames = new ArrayList<String>();
		LinkedList<String> seenContexts = new LinkedList<String>();
		HashMap<String,ArrayList<String>> machineRefinements = rodinHandler.getMachineRefinements();
		HashMap<String,LinkedList<String>> machineSeenContexts = rodinHandler.getMachinesSeenContexts();
		
		//Using the sorted translation order, obtain each name to store each into an AST
		for (String machineName2Translate : rodinHandler.getMachinesOrdered()) {
			machine2Translate = rodinHandler.getMachineRoot(machineName2Translate);

			refinedMachinesNames = machineRefinements.get(machineName2Translate);
			
			if (machineSeenContexts.containsKey(machineName2Translate))
				seenContexts = machineSeenContexts.get(machineName2Translate);
			
			System.out.println("CREATING EMPTY MACHINE IN CPPAST...");
			ASTMachine machineBeingBuilt = CppAST.addMachine(machineName2Translate, refinedMachinesNames, seenContexts);
			
			System.out.println("TURNING VARIABLES INTO CPPAST...");
			//Turn variables to CPPAST
			for (ISCVariable variable : machine2Translate.getSCVariables()) {
				try {
					CppAST.addVariableToMachine(variable, machineBeingBuilt);
				}
				catch (CoreException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("TURNING INVARIANTS INTO CPPAST...");
			for (ISCInvariant invariant : machine2Translate.getSCInvariants()) {
				try {
					CppAST.addInvariantToMachine(invariant, machineBeingBuilt);
				}
				catch (CoreException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("TURNING EVENTS INTO CPPAST...");
			for (ISCEvent event : machine2Translate.getSCEvents()) {
				try {
					CppAST.addEventToMachine(event, machineBeingBuilt);
				}
				catch (CoreException e) {
					e.printStackTrace();
				}
			}
			
			refinedMachinesNames = new ArrayList<String>();
			seenContexts = new LinkedList<String>();
			
		}
		
	}
	
	//This method begins the chain of fetch from rodinDB,
	//turn elements into our own AST, and translate into the output files
	public void translateProject() throws RodinDBException {
		rodinHandler.fetchRodinProject();
		rodinHandler.fetchContexts();
		rodinHandler.fetchMachines();
		
		turnContextsIntoNewAST();
		turnMachinesIntoNewAST();
		
		rodinFilesPath = rodinHandler.getFilePath();
		
		generationHandler = new CodeGenerationHandler(CppAST,rodinFilesPath,ProjectName);
		
		generationHandler.startCodeGeneration();
	}
}
