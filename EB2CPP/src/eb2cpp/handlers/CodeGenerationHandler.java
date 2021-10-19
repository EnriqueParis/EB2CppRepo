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
		writeLine(0,"#include <iostream>");
		writeLine(0,"#include <iterator>");
		writeLine(0,"#include <set>");
		blankLine();
		writeLine(0,"using namespace std;");
	}
	
	public void generateCarrierSets(ASTContext context) {
		writeLine(2,"////// CARRIER SETS");
		
		HashMap<String,ASTCarrierSet> sets = context.getCarrierSets();
		
		for (ASTCarrierSet set : sets.values()) {
			String setName = set.getName();
			ArrayList<String> finalSetElements = new ArrayList<String>();
			writeLine(2,"//// CARRIER SET: " + setName);
			writeLine(2,"enum " + setName + " {");
			
			ArrayList<String> elements = set.getSetElements();
			int setSize = elements.size();
			boolean setIsPartitioned = set.hasPartition();
			Integer index = 0;
			
			if (!setIsPartitioned) {
				writeLine(3,"// Here the user can add elements to the carrier set as they wish, as shown in the line below");
				blankLine();
				writeLine(3,setName + index.toString() + ",");
				finalSetElements.add(setName + index.toString());
				index += 1;
				writeLine(3,setName + index.toString() + ",");
				finalSetElements.add(setName + index.toString());
				index += 1;
				writeLine(3,setName + index.toString() );
				finalSetElements.add(setName + index.toString());
				blankLine();
				writeLine(3,"// Remember to use a comma after every element unless it's the final element");
				blankLine();
			}
			else {
				writeLine(3,"// Constants that belong to this carrier set:");
				
				while (index < setSize) {
					if (index != setSize-1) {
						writeLine(3,elements.get(index) + ",");
					}
					else
						writeLine(3,elements.get(index));
					finalSetElements.add(elements.get(index));
					index += 1;
				}
				
				writeLine(3,"// End of constants");
				
			}
			
			writeLine(2,"};");
			blankLine();
			
			//// Each carrier set also needs a set object that contains every possible element
			// of the carrier set, to be used in belongs, intersections, unions, etc.
			writeLine(2,"// SET THAT CONTAINS ALL ELEMENTS OF " + setName);
			StringBuilder setLine = new StringBuilder();
			setLine.append("set<");
			setLine.append(setName);
			setLine.append("> ");
			setLine.append(setName);
			setLine.append("_SET = {");
			
			index = 0;
			while(index < finalSetElements.size()) {
				setLine.append(finalSetElements.get(index));
				if (index != (finalSetElements.size()-1))
					setLine.append(", ");
				index += 1;
			}
			
			setLine.append("};");
			
			writeLine(2,setLine.toString());
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
			
			
			
			writeLine(2,"}");
		}
	}
	
	
	public void generateContext(ASTContext context) {
		try {
			writer = new FileWriter(finalFilePath + context.getContextName() + ".cpp");
			
			generateDependencies();
			
			blankLine();
			
			writeLine(0,"class " + context.getContextName() + " {");
			writeLine(1,"protected:");
			
			generateCarrierSets(context);

			blankLine();
			
			generateConstants(context);
			
			blankLine();
			
			writeLine(1,"public:");
			
			
			
			writeLine(0,"};");
			
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
			
			
			
			for (ASTContext context : CppAST.getContexts()) {
				generateContext(context);
			}
			
		}
	}

}
