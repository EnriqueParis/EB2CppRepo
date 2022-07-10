package eb2cpp.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import eb2cpp.ast.CppAST2CppBuilder;
import eb2cpp.ast.EB2CppAST;
import eb2cpp.ast.context.ASTAxiomTheorem;
import eb2cpp.ast.context.ASTCarrierSet;
import eb2cpp.ast.context.ASTConstant;
import eb2cpp.ast.context.ASTContext;
import eb2cpp.ast.machine.ASTAction;
import eb2cpp.ast.machine.ASTEvent;
import eb2cpp.ast.machine.ASTGuard;
import eb2cpp.ast.machine.ASTInvariant;
import eb2cpp.ast.machine.ASTMachine;
import eb2cpp.ast.machine.ASTVariable;
import eb2cpp.ast.predicates.ASTPredicate;

public class CodeGenerationHandler {
	///////////////
	// VARIABLES //
	///////////////
	
	private EB2CppAST CppAST;
	private CppAST2CppBuilder AST2Cpp;
	private String projectName;
	private String finalFilePath;
	private FileWriter writer;
	
	private int indentTier;
	
	/////////////
	// METHODS //
	/////////////
	
	public CodeGenerationHandler() {
		// TODO Auto-generated constructor stub
	}
	
	public CodeGenerationHandler(EB2CppAST ast, String path, String newProjectName) {
		// TODO Auto-generated constructor stub
		CppAST = ast;
		finalFilePath = path;
		projectName = newProjectName;
		indentTier = 0;
		AST2Cpp = new CppAST2CppBuilder(ast);
	}
	
	public void writeLine(int numIndents, String line) {
		String indent = "";
		for (int i = 0; i < numIndents; i++) {
			indent += "    ";
		}
		
		try {
			writer.write(indent + line + "\n");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void blankLine() {
		try {
			writer.write("\n");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void blankLines(int numBlankLines) {
		try {
			for (int i = 0; i < numBlankLines; i++)
				writer.write("\n");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void generateDependencies(ASTContext context) {
		writeLine(0,"// DEPENDENCIES");
		writeLine(0,"#include \"EB2CppTools.h\"");
		
		StringBuilder builtLine = new StringBuilder();
		
		//Include the extended contexts
		for (ASTContext extCtx : context.getExtendedContexts()) {
			builtLine.append("#include \"");
			builtLine.append(extCtx.getContextName());
			builtLine.append(".h\"");
			writeLine(0,builtLine.toString());
		}
		blankLine();
		writeLine(0,"using namespace std;");
	}
	
	public void generateDependencies(ASTMachine machine) {
		writeLine(0,"// DEPENDENCIES");
		writeLine(0,"#include <assert.h>");
		writeLine(0,"#include \"EB2CppTools.h\"");
		
		StringBuilder builtLine = new StringBuilder();
		
		//Include the extended contexts
		for (ASTContext seenCtx : machine.getSeenContexts().values()) {
			builtLine.append("#include \"");
			builtLine.append(seenCtx.getContextName());
			builtLine.append(".h\"");
			writeLine(0,builtLine.toString());
		}
		
		blankLine();
		writeLine(0,"using namespace std;");
	}
	
	public void generateCarrierSetsEnumerations(ASTContext context) {
		writeLine(0,"////// CARRIER SETS");
		
		HashMap<String,ASTCarrierSet> sets = context.getCarrierSets();
		
		for (ASTCarrierSet set : sets.values()) {
			String setName = set.getName();
			ArrayList<String> finalSetElements = new ArrayList<String>();
			writeLine(0,"//// CARRIER SET: " + setName);
			writeLine(0,"enum " + setName + "_CS {");
			
			ArrayList<String> elements = set.getSetElements();
			int setSize = elements.size();
			boolean setIsPartitioned = set.hasPartition();
			Integer index = 0;
			
			if (!setIsPartitioned) {
				writeLine(1,"// Here the user can add elements to the carrier set as they wish, as shown in the line below");
				blankLine();
				// Adding the constants that are part of the carrier set
				for (String e : elements) {
					writeLine(1, e + ",");
					finalSetElements.add(e);
				}
				
				writeLine(1,setName + index.toString() + ",");
				finalSetElements.add(setName + index.toString());
				index += 1;
				writeLine(1,setName + index.toString() + ",");
				finalSetElements.add(setName + index.toString());
				index += 1;
				writeLine(1,setName + index.toString() );
				finalSetElements.add(setName + index.toString());
				blankLine();
				writeLine(1,"// Remember to use a comma after every element unless it's the final element");
				blankLine();
			}
			else {
				writeLine(1,"// Constants that belong to this carrier set:");
				
				while (index < setSize) {
					if (index != setSize-1) {
						writeLine(1,elements.get(index) + ",");
					}
					else
						writeLine(1,elements.get(index));
					finalSetElements.add(elements.get(index));
					index += 1;
				}
				
				writeLine(1,"// End of constants");
				
			}
			
			writeLine(0,"};");
			blankLines(2);
			
			set.setFinalSetElements(finalSetElements);
			
		}
	}
	
	public void generateCarrierSets(ASTContext context) {
		writeLine(2,"//// CARRIER SETS");
		
		HashMap<String,ASTCarrierSet> sets = context.getCarrierSets();
		
		for (ASTCarrierSet set : sets.values()) {
			String setName = set.getName();
			ArrayList<String> finalSetElements = set.getFinalSetElements();
		
			//// Each carrier set also needs a set object that contains every possible element
			// of the carrier set, to be used in belongs, intersections, unions, etc.
			writeLine(2,"// SET THAT CONTAINS ALL ELEMENTS OF " + setName);
			StringBuilder setLine = new StringBuilder();
			setLine.append("Set<");
			setLine.append(setName);
			setLine.append("_CS");
			setLine.append("> ");
			setLine.append(setName);
			setLine.append(" = ");
			setLine.append("Set<");
			setLine.append(setName);
			setLine.append("_CS>({");
			
			int index = 0;
			while(index < finalSetElements.size()) {
				if (index != 0)
					setLine.append(", ");
				setLine.append(finalSetElements.get(index));
				index += 1;
			}
			
			setLine.append("});");
			
			writeLine(2,setLine.toString());
			blankLine();
		}
	}
	
	
	public void generateConstants(ASTContext context) {
		writeLine(2,"//// CONSTANTS");
		blankLine();
		
		HashMap<String,ASTConstant> constants = context.getConstants();
		
		for (ASTConstant constant : constants.values()) {
			if (constant.getNeedsToBeGenerated()) {
				String constantName = constant.getConstantName();
				String constantType = AST2Cpp.generateDataType(constant.getDataType());
				constant.setDataTypeText(constantType);
				
				writeLine(2,"// CONSTANT: " + constantName);
				
				StringBuilder line = new StringBuilder();
				
				line.append(constantType);
				line.append(" ");
				line.append(constantName);
				line.append(";");
				
				writeLine(2,line.toString());
				blankLines(2);
			}
		}
	}
	
	public void generateGetConstants(ASTContext context, boolean isHeaderFile) {
		writeLine(indentTier,"//// GET FUNCTIONS");
		
		HashMap<String,ASTConstant> constants = context.getConstants();
		StringBuilder functionLine;
		
		for (ASTConstant constant : constants.values()) {
			if (constant.getNeedsToBeGenerated()) {
				String constantName = constant.getConstantName();
				String constantType = constant.getDataTypeText();
				
				writeLine(indentTier,"// GET CONSTANT: " + constantName);
				
				//Declaration of axiom function
				functionLine = new StringBuilder();
				functionLine.append(constantType);
				functionLine.append(" ");
				if (!isHeaderFile) {
					functionLine.append(context.getContextName());
					functionLine.append("::");
				}
				functionLine.append("get_");
				functionLine.append(constantName);
				functionLine.append("()");
				
				if (isHeaderFile)
					functionLine.append(";");
				else
					functionLine.append(" {");
				
				writeLine(indentTier,functionLine.toString());
				
				if (!isHeaderFile) {
					//Constant get return line
					functionLine = new StringBuilder();
					functionLine.append("return ");
					functionLine.append(constantName);
					functionLine.append(";");
					
					writeLine(indentTier+1, functionLine.toString());
					
					writeLine(indentTier,"}");
				}
				
				blankLine();
			}
		}
		
		blankLine();
	}
	
	public void generateAxioms(ASTContext context, boolean isHeaderFile) {
		writeLine(indentTier,"//// AXIOMS");
		
		HashMap<String,ASTAxiomTheorem> axioms = context.getAxioms();
		StringBuilder functionLine;
		
		for (ASTAxiomTheorem axiom : axioms.values()) {
			String axiomLabel = axiom.getLabel();
			
			writeLine(indentTier,"// AXIOM: " + axiomLabel);
			
			//Declaration of axiom function
			functionLine = new StringBuilder();
			functionLine.append("bool ");
			if (!isHeaderFile) {
				functionLine.append(context.getContextName());
				functionLine.append("::");
			}
			functionLine.append("checkAxiom_");
			functionLine.append(axiomLabel);
			functionLine.append("()");
			
			if (isHeaderFile)
				functionLine.append(";");
			else
				functionLine.append(" {");
			
			writeLine(indentTier,functionLine.toString());
			
			if (!isHeaderFile) {
				//Axiom return line
				String axiomLine = AST2Cpp.generatePredicate(axiom.getPredicate());
				
				functionLine = new StringBuilder();
				functionLine.append("return ");
				functionLine.append(axiomLine);
				functionLine.append(";");
				
				writeLine(indentTier+1, functionLine.toString());
				
				writeLine(indentTier,"}");
			}
		}
		
		blankLine();	
		
		//CREATE CHECK_ALL_AXIOMS FUNCTION
		writeLine(indentTier,"// BOOL FUNCTION TO CHECK ALL AXIOMS");
		functionLine = new StringBuilder();
		functionLine.append("bool ");
		if (!isHeaderFile) {
			functionLine.append(context.getContextName());
			functionLine.append("::");
		}
		functionLine.append("checkAllAxioms_");
		functionLine.append(context.getContextName());
		functionLine.append("()");
		
		if (isHeaderFile)
			functionLine.append(";");
		else
			functionLine.append(" {");
		
		writeLine(indentTier,functionLine.toString());
		
		if (!isHeaderFile) {
			// Store each axiom check in a boolean
			for (ASTAxiomTheorem axiom : axioms.values()) {
				functionLine = new StringBuilder();
				String axiomLabel = axiom.getLabel();
				
				functionLine.append("bool ");
				functionLine.append(axiomLabel);
				functionLine.append(" = checkAxiom_");
				functionLine.append(axiomLabel);
				functionLine.append("();");
				writeLine(indentTier+1, functionLine.toString());
			}
			blankLine();
			
			functionLine = new StringBuilder();
			functionLine.append("return ");
			boolean hasLoopedOnce = false;
			for (ASTAxiomTheorem axiom : axioms.values()) {
				if (hasLoopedOnce)
					functionLine.append(" && ");
				functionLine.append(axiom.getLabel());
				hasLoopedOnce = true;
			}
			functionLine.append(";");
			writeLine(indentTier+1,functionLine.toString());
			
			writeLine(indentTier,"}");
		}
	}
	
	public void generateVariables(ASTMachine machine) {
		writeLine(2,"//// VARIABLES");
		blankLine();
		
		HashMap<String,ASTVariable> variables = machine.getVariables();
		
		for (ASTVariable variable : variables.values()) {
			String variableName = variable.getName();
			String variableType = AST2Cpp.generateDataType(variable.getDataType());
			variable.setDataTypeText(variableType);
			
			writeLine(2,"// VARIABLE: " + variableName);
			
			StringBuilder line = new StringBuilder();
			
			line.append(variableType);
			line.append(" ");
			line.append(variableName);
			line.append(";");
			
			writeLine(2,line.toString());
			blankLines(2);
		}
	}
	
	public void generateGetVariables(ASTMachine machine, boolean isHeaderFile) {
		writeLine(indentTier,"//// GET FUNCTIONS");
		
		HashMap<String,ASTVariable> variables = machine.getVariables();
		StringBuilder functionLine;
		
		for (ASTVariable variable : variables.values()) {
			String variableName = variable.getName();
			String variableType = variable.getDataTypeText();
			
			writeLine(indentTier,"// GET VARIABLE: " + variableName);
			
			//Declaration of variable get function
			functionLine = new StringBuilder();
			functionLine.append(variableType);
			functionLine.append(" ");
			if (!isHeaderFile) {
				functionLine.append(machine.getName());
				functionLine.append("::");
			}
			functionLine.append("get_");
			functionLine.append(variableName);
			functionLine.append("()");
			
			if (isHeaderFile)
				functionLine.append(";");
			else
				functionLine.append(" {");
			
			writeLine(indentTier,functionLine.toString());
			
			if (!isHeaderFile) {
				//Constant get return line
				functionLine = new StringBuilder();
				functionLine.append("return ");
				functionLine.append(variableName);
				functionLine.append(";");
				
				writeLine(indentTier+1, functionLine.toString());
				
				writeLine(indentTier,"}");
			}
			
			blankLine();
		}
		
		blankLine();
	}
	
	public void generateInvariants(ASTMachine machine, boolean isHeaderFile) {
		writeLine(indentTier,"//// INVARIANTS");
		
		HashMap<String, ASTInvariant> invariants = machine.getInvariants();
		StringBuilder functionLine;
		
		for (ASTInvariant invariant : invariants.values()) {
			String invariantLabel = invariant.getLabel();
			
			writeLine(indentTier,"// INVARIANT: " + invariantLabel);
			
			//Declaration of invariant function
			functionLine = new StringBuilder();
			functionLine.append("bool ");
			if (!isHeaderFile) {
				functionLine.append(machine.getName());
				functionLine.append("::");
			}
			functionLine.append("checkInvariant_");
			functionLine.append(invariantLabel);
			functionLine.append("()");
			
			if (isHeaderFile)
				functionLine.append(";");
			else
				functionLine.append(" {");
			
			writeLine(indentTier,functionLine.toString());
			
			if (!isHeaderFile) {
				// Invariant return line
				String invariantLine = AST2Cpp.generatePredicate(invariant.getPredicate());
				
				functionLine = new StringBuilder();
				functionLine.append("return ");
				functionLine.append(invariantLine);
				functionLine.append(";");
				
				writeLine(indentTier+1,functionLine.toString());
				
				writeLine(indentTier,"}");
			}
			
			blankLine();
		}
		
		blankLine();
		
		//CREATE CHECK_ALL_INVARIANTS FUNCTION
		writeLine(indentTier,"// BOOL FUNCTION TO CHECK ALL INVARIANTS");
		functionLine = new StringBuilder();
		functionLine.append("bool ");
		if (!isHeaderFile) {
			functionLine.append(machine.getName());
			functionLine.append("::");
		}
		functionLine.append("checkAllInvariants_");
		functionLine.append(machine.getName());
		functionLine.append("()");
		
		if (isHeaderFile)
			functionLine.append(";");
		else
			functionLine.append(" {");
		
		writeLine(indentTier,functionLine.toString());
		
		if (!isHeaderFile) {

			for (ASTInvariant invariant : invariants.values()) {
				functionLine = new StringBuilder();
				String invariantLabel = invariant.getLabel();
	
				functionLine.append("bool ");
				functionLine.append(invariantLabel);
				functionLine.append(" = checkInvariant_");
				functionLine.append(invariantLabel);
				functionLine.append("();");
				writeLine(indentTier+1,functionLine.toString());
			}
	
			blankLine();
	
			functionLine = new StringBuilder();
			functionLine.append("return ");
			boolean hasLoopedOnce = false;
			for (ASTInvariant invariant : invariants.values()) {
				if (hasLoopedOnce)
					functionLine.append(" && ");
				functionLine.append(invariant.getLabel());
				hasLoopedOnce = true;
			}
			functionLine.append(";");
			writeLine(indentTier+1,functionLine.toString());
	
			writeLine(indentTier,"}");
		}
	}
	
	public void generateEvent(ASTMachine machine, ASTEvent event, boolean isHeaderFile) {
		System.out.println("GENERATING EVENT: " + event.getName());
		writeLine(indentTier,"//// EVENT: " + event.getName());
		blankLine();
		
		// Obtaining the string of event parameters as Cpp function parameters
		StringBuilder parametersLineBuilder = new StringBuilder();
		StringBuilder parametersNoTypeBuilder = new StringBuilder();
		boolean hasLoopedOnce = false;
		for (ASTVariable parameter : event.getParameters()) {
			if (hasLoopedOnce) {
				parametersLineBuilder.append(", ");
				parametersNoTypeBuilder.append(", ");
			}
			parametersLineBuilder.append(AST2Cpp.generateDataType(parameter.getDataType()));
			parametersLineBuilder.append(" ");
			parametersLineBuilder.append(parameter.getName());
			parametersNoTypeBuilder.append(parameter.getName());
			hasLoopedOnce = true;
		}
		
		String parametersLine = parametersLineBuilder.toString();
		String parametersNoTypeLine = parametersNoTypeBuilder.toString();
		
		////// EVENT GUARDS
		writeLine(indentTier,"// Event Guards Function");
		
		//Declaration of event guard function
		StringBuilder functionLine = new StringBuilder();
		functionLine.append("bool ");
		if (!isHeaderFile) {
			functionLine.append(machine.getName());
			functionLine.append("::");
		}
		functionLine.append(event.getName());
		functionLine.append("_checkGuards(");
		
		//Inserting the any parameters of the event as CppFunction parameters
		functionLine.append(parametersLine);

		functionLine.append(")");
		
		if (isHeaderFile)
			functionLine.append(";");
		else
			functionLine.append(" {");
		
		writeLine(indentTier,functionLine.toString());
		
		if (!isHeaderFile) {
		
			// StringBuilder used for each line of code of guard
			functionLine = new StringBuilder();
			
			// For each guard, declare a boolean that will then be conjoined in the return line
			for (ASTGuard guard : event.getGuards()) {
				functionLine.append("bool ");
				functionLine.append(guard.getGuardLabel());
				functionLine.append(" = ");
				functionLine.append(AST2Cpp.generatePredicate(guard.getGuardPredicate()));
				functionLine.append(";");
				
				writeLine(indentTier+1,functionLine.toString());
				
				functionLine = new StringBuilder();
			}
			blankLine();
			
			// Generate the return line that conjoins each guard
			functionLine.append("return ");
			
			hasLoopedOnce = false;
			for (ASTGuard guard : event.getGuards()) {
				if (hasLoopedOnce) 
					functionLine.append(" && ");
				
				functionLine.append(guard.getGuardLabel());
				hasLoopedOnce = true;
			}
			
			// This IF is triggered if the event doesn't have guards.
			// In that case, the function just returns true.
			if (!hasLoopedOnce)
				functionLine.append("true");
			
			functionLine.append(";");
			
			writeLine(indentTier+1,functionLine.toString());
	
			writeLine(indentTier,"}");
		
		}
		
		blankLine();
		
		////// EVENT ACTIONS
		//Declaration of event actions function
		writeLine(indentTier,"// Event Actions Function");
		functionLine = new StringBuilder();
		functionLine.append("void ");
		if (!isHeaderFile) {
			functionLine.append(machine.getName());
			functionLine.append("::");
		}
		functionLine.append(event.getName());
		functionLine.append("_actions(");
		
		//Inserting the any parameters of the event as CppFunction parameters
		functionLine.append(parametersLine);

		functionLine.append(")");
		
		if (isHeaderFile)
			functionLine.append(";");
		else
			functionLine.append(" {");
		
		writeLine(indentTier,functionLine.toString());
		
		if (!isHeaderFile) {
			for (ASTAction action : event.getActions()) {
				functionLine = new StringBuilder();
				writeLine(indentTier+1,"// Action: " + action.getLabel());
				functionLine.append(AST2Cpp.generateAssignment(action.getAssignment()));
				functionLine.append(";");
				writeLine(indentTier+1,functionLine.toString());
				blankLine();
			}
			
			writeLine(indentTier,"}");
		}
		
		blankLine();
		
		////// FULL EVENT
		//Declaration of full event function
		writeLine(indentTier,"// Event Function. CALL THIS EVENT TO CHECK GUARDS AND IF TRUE DO ACTIONS, AND CHECK FOR INVARIANTS");
		functionLine = new StringBuilder();
		functionLine.append("void ");
		if (!isHeaderFile) {
			functionLine.append(machine.getName());
			functionLine.append("::");
		}
		functionLine.append(event.getName());
		functionLine.append("(");
		functionLine.append(parametersLine);
		functionLine.append(")");
		
		if (isHeaderFile)
			functionLine.append(";");
		else
			functionLine.append(" {");
		
		writeLine(indentTier,functionLine.toString());
		
		if (!isHeaderFile) {
			functionLine = new StringBuilder();
			functionLine.append("if (");
			functionLine.append(event.getName());
			functionLine.append("_checkGuards(");
			functionLine.append(parametersNoTypeLine);
			functionLine.append(")) { ");
			writeLine(indentTier+1,functionLine.toString());
			
			functionLine = new StringBuilder();
			functionLine.append(event.getName());
			functionLine.append("_actions(");
			functionLine.append(parametersNoTypeLine);
			functionLine.append(");");
			writeLine(indentTier+2,functionLine.toString());
			
			functionLine = new StringBuilder();
			functionLine.append("assert( checkAllInvariants_");
			functionLine.append(machine.getName());
			functionLine.append("() );");
			writeLine(indentTier+2,functionLine.toString());
			
			writeLine(indentTier+1,"}");
			
			functionLine = new StringBuilder();
			functionLine.append("else { ");
			functionLine.append("cout << \"Can't execute ");
			functionLine.append(event.getName());
			functionLine.append(" event. Guards are false. \" << endl;");
			functionLine.append(" }");
			
			writeLine(indentTier+1,functionLine.toString());
			
			writeLine(indentTier,"}");
		}
		
		blankLine();
	}
	
	
	public void generateEvents(ASTMachine machine, boolean isHeaderFile) {
		System.out.println("GENERATING EVENTS");
		writeLine(indentTier,"////// EVENTS");
		blankLine();
		
		HashMap<String, ASTEvent> events = machine.getEvents();
		
		for (ASTEvent event : events.values()) {
			generateEvent(machine, event, isHeaderFile);
		}
		
	}
	
	public void generateContextConstructor(ASTContext context, boolean isHeaderFile) {
		writeLine(indentTier,"////// CONTEXT CONSTRUCTOR METHOD");
		
		StringBuilder functionLine = new StringBuilder();
		
		if (!isHeaderFile) {
			functionLine.append(context.getContextName());
			functionLine.append("::");
		}
		
		functionLine.append(context.getContextName());
		functionLine.append("()");
		
		if (isHeaderFile)
			functionLine.append(";");
		else
			functionLine.append(" {");
		
		writeLine(indentTier,functionLine.toString());
		
		if (!isHeaderFile) {
			HashMap<String,ASTConstant> constants = context.getConstants();
			if (constants.size() != 0)
					writeLine(indentTier+1,"// MODIFY THE INITIAL VALUE OF THE CONSTANT(S) IN THE LINE(S) BELOW");
			
			for (ASTConstant constant : constants.values()) {
				if (constant.getNeedsToBeGenerated()) {
					//Axiom return line
					String constantLine = AST2Cpp.generateStartingValue(constant.getDataType());
					
					functionLine = new StringBuilder();
					functionLine.append(constant.getConstantName());
					functionLine.append(" = ");
					functionLine.append(constantLine);
					functionLine.append(";");
					
					writeLine(indentTier+1, functionLine.toString());
				}
			}
			// Check all axioms to see if the initialized constant values work
			blankLine();
			
			functionLine = new StringBuilder();
			functionLine.append("if (!checkAllAxioms_");
			functionLine.append(context.getContextName());
			functionLine.append("()) {throw \"The axioms of the context ");
			functionLine.append(context.getContextName());
			functionLine.append(" can't be fulfilled. Check constant initialization values in ");
			functionLine.append(context.getContextName());
			functionLine.append(".cpp\";");
			functionLine.append("}");
			
			writeLine(indentTier+1, functionLine.toString());
			
			writeLine(indentTier,"}");
		}
	}
	
	public void generateMachineConstructor(ASTMachine machine) {
		writeLine(2,"////// MACHINE CONSTRUCTOR METHOD");
		blankLine();
		
		StringBuilder functionLine = new StringBuilder();
		
		functionLine.append(machine.getName());
		functionLine.append("() {");
		
		writeLine(2,functionLine.toString());
		
		writeLine(3,"INITIALISATION_actions();");
		
		writeLine(2,"}");
	}
	
	public void generateContextHeader(ASTContext context) {
		try {
			StringBuilder builtLine = new StringBuilder();
			builtLine.append(finalFilePath);
			builtLine.append(context.getContextName());
			builtLine.append(".h");
			
			writer = new FileWriter(builtLine.toString());
			
			builtLine = new StringBuilder();
			builtLine.append(context.getContextName());
			builtLine.append("_H");
			String headerDirective = builtLine.toString();
			
			builtLine = new StringBuilder();
			builtLine.append("#ifndef ");
			builtLine.append(headerDirective);
			writeLine(0,builtLine.toString());
			
			builtLine = new StringBuilder();
			builtLine.append("#define ");
			builtLine.append(headerDirective);
			writeLine(0,builtLine.toString());
			
			blankLine();
			
			generateDependencies(context);
			
			blankLine();
			
			generateCarrierSetsEnumerations(context);
			
			// Generation of the class declaration
			// If the context extends other contexts, this is where
			// its reflected, by inheriting from those contexts
			
			builtLine = new StringBuilder();
			builtLine.append("class ");
			builtLine.append(context.getContextName());
			
			// Add inheritance from extended contexts
			boolean hasLoopedOnce = false;
			for (ASTContext extCtx : context.getExtendedContexts()) {
				if (hasLoopedOnce)
					builtLine.append(", ");
				else
					builtLine.append(": ");
				
				builtLine.append("public ");
				builtLine.append(extCtx.getContextName());
				hasLoopedOnce = true;
			}
			builtLine.append(" {");
			
			writeLine(0,builtLine.toString());
			
			writeLine(1,"protected:");

			blankLine();
			
			generateCarrierSets(context);
			
			generateConstants(context);
			
			blankLine();
			
			writeLine(1,"public:");
			
			indentTier = 2;
			
			generateContextConstructor(context,true);
			
			blankLine();
			
			generateGetConstants(context, true);
			
			blankLine();
			
			generateAxioms(context, true);
			
			indentTier = 0;
			
			writeLine(0,"};");
			
			blankLine();
			
			writeLine(0,"#endif");
			
			writer.close();
		}
		catch (IOException e) {
			System.out.println("Can't create file");
			e.printStackTrace();
		}
	}
	
	public void generateContext(ASTContext context) {
		try {
			StringBuilder builtLine = new StringBuilder();
			builtLine.append(finalFilePath);
			builtLine.append(context.getContextName());
			builtLine.append(".cpp");
			
			writer = new FileWriter(builtLine.toString());
			
			builtLine = new StringBuilder();
			builtLine.append("#include \"");
			builtLine.append(context.getContextName());
			builtLine.append(".h\"");
			writeLine(0,builtLine.toString());
			
			blankLine();
			
			writeLine(0,"using namespace std;");
			
			blankLine();
			
			indentTier = 0;
			
			generateContextConstructor(context, false);
			
			blankLine();
			
			generateGetConstants(context, false);
			
			blankLine();
			
			generateAxioms(context, false);
			
			writer.close();
		}
		catch (IOException e) {
			System.out.println("Can't create file");
			e.printStackTrace();
		}
	}
	
	public void generateMachineHeader(ASTMachine machine) {
		try {
			StringBuilder builtLine = new StringBuilder();
			builtLine.append(finalFilePath);
			builtLine.append(machine.getName());
			builtLine.append(".h");
			
			writer = new FileWriter(builtLine.toString());
			
			builtLine = new StringBuilder();
			builtLine.append(machine.getName());
			builtLine.append("_H");
			String headerDirective = builtLine.toString();
			
			builtLine = new StringBuilder();
			builtLine.append("#ifndef ");
			builtLine.append(headerDirective);
			writeLine(0,builtLine.toString());
			
			builtLine = new StringBuilder();
			builtLine.append("#define ");
			builtLine.append(headerDirective);
			writeLine(0,builtLine.toString());
			
			blankLine();
			
			generateDependencies(machine);
			
			blankLine();
			
			// Generation of the class declaration
			// If the machine sees contexts, this is where
			// its reflected, by inheriting from those contexts
			builtLine = new StringBuilder();
			builtLine.append("class ");
			builtLine.append(machine.getName());
			
			// Add inheritance from seen contexts
			boolean hasLoopedOnce = false;
			for (ASTContext extCtx : machine.getSeenContexts().values()) {
				if (hasLoopedOnce)
					builtLine.append(", ");
				else
					builtLine.append(": ");
				
				builtLine.append("public ");
				builtLine.append(extCtx.getContextName());
				hasLoopedOnce = true;
			}
			builtLine.append(" {");
			writeLine(0,builtLine.toString());
			
			
			writeLine(1,"protected:");
			
			generateVariables(machine);
			
			blankLine();
			
			writeLine(1,"public:");
			
			generateMachineConstructor(machine);
			
			blankLine();
			
			indentTier = 2;
			
			generateGetVariables(machine, true);
			
			blankLine();
			
			generateInvariants(machine, true);
			
			indentTier = 0;
			
			blankLine();
			
			indentTier = 2;
			
			generateEvents(machine, true);
			
			writeLine(0,"};");
			
			blankLine();
			
			writeLine(0,"#endif");
			
			writer.close();
		}
		catch (IOException e) {
			System.out.println("Can't create file");
			e.printStackTrace();
		}
	}
	
	
	public void generateMachine(ASTMachine machine) {
		try {
			StringBuilder builtLine = new StringBuilder();
			builtLine.append(finalFilePath);
			builtLine.append(machine.getName());
			builtLine.append(".cpp");
			
			writer = new FileWriter(builtLine.toString());
			
			builtLine = new StringBuilder();
			builtLine.append("#include \"");
			builtLine.append(machine.getName());
			builtLine.append(".h\"");
			writeLine(0,builtLine.toString());
			
			blankLine();
			
			writeLine(0,"using namespace std;");
			
			blankLine();
			
			indentTier = 0;
			
			generateGetVariables(machine, false);
			
			blankLine();
			
			generateInvariants(machine, false);
			
			blankLine();
			
			generateEvents(machine, false);
			
			writer.close();
		}
		catch (IOException e) {
			System.out.println("Can't create file");
			e.printStackTrace();
		}
	}
	
	public void startCodeGeneration() {
		System.out.println("Creating directory for generated files...");
		
		// Exiting the directory of the original Event-B files
		finalFilePath += File.separator + ".." + File.separator;
		
		boolean directoryCreatedSuccessfully = new File(finalFilePath + projectName + "_EB2CppTranslation").mkdir();
		if (!directoryCreatedSuccessfully) {
			System.out.println("ERROR: COULDN'T CREATE DIRECTORY");
		}
		else {
			finalFilePath += projectName + "_EB2CppTranslation" + File.separator;
			System.out.println(finalFilePath);
			
			EB2CppToolsGenerator toolsGenerator = new EB2CppToolsGenerator(finalFilePath);
			toolsGenerator.generateCppTools();
			
			for (ASTContext context : CppAST.getContexts()) {
				generateContextHeader(context);
				generateContext(context);
			}
			
			for (ASTMachine machine : CppAST.getMachines()) {
				generateMachineHeader(machine);
				generateMachine(machine);
			}
			
		}
	}

}
