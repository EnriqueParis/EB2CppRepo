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
import eb2cpp.ast.machine.ASTMachine;
import eb2cpp.ast.machine.ASTVariable;

public class CodeGenerationHandler {
	///////////////
	// VARIABLES //
	///////////////
	
	private EB2CppAST CppAST;
	private CppAST2CppBuilder AST2Cpp;
	private String projectName;
	private String finalFilePath;
	private FileWriter writer;
	
	private File fileBeingWritten;
	
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
		AST2Cpp = new CppAST2CppBuilder();
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
	
	public void generateDependencies() {
		writeLine(0,"// DEPENDENCIES");
		writeLine(0,"#include \"EB2CppTools.h\"");
		blankLine();
		writeLine(0,"using namespace std;");
	}
	
	public void generateCarrierSets(ASTContext context) {
		writeLine(0,"////// CARRIER SETS");
		
		HashMap<String,ASTCarrierSet> sets = context.getCarrierSets();
		
		for (ASTCarrierSet set : sets.values()) {
			String setName = set.getName();
			ArrayList<String> finalSetElements = new ArrayList<String>();
			writeLine(0,"//// CARRIER SET: " + setName);
			writeLine(0,"enum " + setName + " {");
			
			ArrayList<String> elements = set.getSetElements();
			int setSize = elements.size();
			boolean setIsPartitioned = set.hasPartition();
			Integer index = 0;
			
			if (!setIsPartitioned) {
				writeLine(1,"// Here the user can add elements to the carrier set as they wish, as shown in the line below");
				blankLine();
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
			blankLine();
			
			//// Each carrier set also needs a set object that contains every possible element
			// of the carrier set, to be used in belongs, intersections, unions, etc.
			writeLine(0,"// SET THAT CONTAINS ALL ELEMENTS OF " + setName);
			StringBuilder setLine = new StringBuilder();
			setLine.append("Set<");
			setLine.append(setName);
			setLine.append("> ");
			setLine.append(setName);
			setLine.append("_SET({");
			
			index = 0;
			while(index < finalSetElements.size()) {
				if (index != 0)
					setLine.append(", ");
				setLine.append(finalSetElements.get(index));
				index += 1;
			}
			
			setLine.append("});");
			
			writeLine(0,setLine.toString());
			blankLines(2);
		}
	}
	
	
	public void generateConstants(ASTContext context) {
		writeLine(2,"//// CONSTANTS");
		blankLine();
		
		HashMap<String,ASTConstant> constants = context.getConstants();
		
		for (ASTConstant constant : constants.values()) {
			String constantName = constant.getConstantName();
			String constantType = AST2Cpp.generateDataType(constant.getDataType());
			
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
	
	public void generateAxioms(ASTContext context) {
		writeLine(2,"//// AXIOMS");
		
		HashMap<String,ASTAxiomTheorem> axioms = context.getAxioms();
		
		for (ASTAxiomTheorem axiom : axioms.values()) {
			String axiomLabel = axiom.getLabel();
			
			writeLine(2,"// AXIOM: " + axiomLabel);
			
			//Declaration of axiom function
			StringBuilder functionLine = new StringBuilder();
			functionLine.append("bool checkAxiom_");
			functionLine.append(axiomLabel);
			functionLine.append("() {");
			
			writeLine(2,functionLine.toString());
			
			String axiomLine = AST2Cpp.generatePredicate(axiom.getPredicate());
			
			functionLine = new StringBuilder();
			functionLine.append("return ");
			functionLine.append(axiomLine);
			functionLine.append(";");
			
			writeLine(3,functionLine.toString());
			
			writeLine(2,"}");
		}
	}
	
	public void generateVariables(ASTMachine machine) {
		writeLine(2,"//// VARIABLES");
		blankLine();
		
		HashMap<String,ASTVariable> variables = machine.getVariables();
		
		for (ASTVariable variable : variables.values()) {
			String variableName = variable.getName();
			String variableType = AST2Cpp.generateDataType(variable.getDataType());
			
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
	
	
	public void generateContext(ASTContext context) {
		try {
			writer = new FileWriter(finalFilePath + context.getContextName() + ".cpp");
			
			generateDependencies();
			
			blankLine();
			
			generateCarrierSets(context);
			
			writeLine(0,"class " + context.getContextName() + " {");
			writeLine(1,"protected:");

			blankLine();
			
			generateConstants(context);
			
			blankLine();
			
			writeLine(1,"public:");
			
			generateAxioms(context);
			
			writeLine(0,"};");
			
			writer.close();
		}
		catch (IOException e) {
			System.out.println("Can't create file");
			e.printStackTrace();
		}
	}
	
	public void generateMachine(ASTMachine machine) {
		try {
			writer = new FileWriter(finalFilePath + machine.getName() + ".cpp");
			StringBuilder builtLine = new StringBuilder();
			
			generateDependencies();
			
			blankLine();
			
			builtLine.append("class ");
			builtLine.append(machine.getName());
			builtLine.append(" {");
			writeLine(0,builtLine.toString());
			
			writeLine(1,"protected:");
			
			blankLine();
			
			generateVariables(machine);
			
			writeLine(0,"};");
		}
		catch (IOException e) {
			
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
			
			for (ASTContext context : CppAST.getContexts()) {
				generateContext(context);
			}
			
			for (ASTMachine machine : CppAST.getMachines()) {
				generateMachine(machine);
			}
			
		}
	}

}
