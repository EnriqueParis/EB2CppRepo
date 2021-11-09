package eb2cpp.ast;

import java.util.ArrayList;
import java.util.HashMap;

import eb2cpp.ast.expressions.ASTAssociativeExpression;
import eb2cpp.ast.expressions.ASTAtomicExpression;
import eb2cpp.ast.expressions.ASTBinaryExpression;
import eb2cpp.ast.expressions.ASTExpression;
import eb2cpp.ast.expressions.ASTFreeIdentifier;
import eb2cpp.ast.expressions.ASTIntegerLiteral;
import eb2cpp.ast.expressions.ASTSetExtension;
import eb2cpp.ast.expressions.ASTUnaryExpression;
import eb2cpp.ast.machine.ASTAssignment;
import eb2cpp.ast.predicates.ASTAssociativePredicate;
import eb2cpp.ast.predicates.ASTBinaryPredicate;
import eb2cpp.ast.predicates.ASTMultiplePredicate;
import eb2cpp.ast.predicates.ASTPredicate;
import eb2cpp.ast.predicates.ASTRelationalPredicate;
import eb2cpp.ast.predicates.ASTSimplePredicate;
import eb2cpp.ast.predicates.ASTUnaryPredicate;
import eb2cpp.ast.types.ASTBinaryExpressionType;
import eb2cpp.ast.types.ASTDataType;
import eb2cpp.ast.types.ASTFreeIdentifierType;
import eb2cpp.ast.types.ASTUnaryExpressionType;

public class CppAST2CppBuilder {
	
	///////////////
	// VARIABLES //
	///////////////
	// Reference to the CppAST being generated
	private EB2CppAST CppAST;
	
	
	/////////////
	// METHODS //
	/////////////

	public CppAST2CppBuilder(EB2CppAST newCppAST) {
		// TODO Auto-generated constructor stub
		CppAST = newCppAST;
	}
	
	public String generateDataType(ASTDataType data) {
		String result = "";
		
		StringBuilder builtResult = new StringBuilder();
		
		String typeName = data.getTypeName();
		
		switch(typeName) {
		case "BinaryExpression": //It is a Pair, or a Relation
			ASTBinaryExpressionType binaryExpression = (ASTBinaryExpressionType) data;
			
			ASTDataType leftSide = binaryExpression.getLeftSideDataType();
			String leftString = generateDataType(leftSide);
			
			ASTDataType rightSide = binaryExpression.getRightSideDataType();
			String rightString = generateDataType(rightSide);
			
			switch(binaryExpression.getBinaryOperator()) {
			case "Pair":
				builtResult.append("Tuple<");
				break;
			case "Relation":
				builtResult.append("Relation<");
				break;
			}

			builtResult.append(leftString);
			builtResult.append(",");
			builtResult.append(rightString);
			builtResult.append(">");
			
			result = builtResult.toString();
			
			break;
		case "Bool": //It is a boolean, true or false
			result = "bool";
			break;
		case "FreeIdentifier": //It is an element of a carrier set
			ASTFreeIdentifierType freeIdentifier = (ASTFreeIdentifierType) data;
			builtResult.append(freeIdentifier.getIdentifierName());
			builtResult.append("_CS");
			result = builtResult.toString();
			break;
		case "Integer": //It is a number
			result = "int";
			break;
		case "UnaryExpression": //It is a set.
			ASTUnaryExpressionType unaryType = (ASTUnaryExpressionType) data;
			
			//Finding out the inner type of the set.
			ASTDataType internalType = unaryType.getInternalType();
			String internalString = generateDataType(internalType);
			
			builtResult.append("Set<");
			builtResult.append(internalString);
			builtResult.append(">");
			
			result = builtResult.toString();
			
			break;
		default:
			result = "ERROR_DATATYPENOTFOUND";
		}
		
		return result;
	}
	
	
	public String extractTypeFromRelationText(String relation, String leftOrRight) {
		String result = "";
		
		StringBuilder builtResult = new StringBuilder();
		
		boolean isGrabbingText;
		
		// This function can either get the left or right part of a relation
		// The leftOrRight parameters sets which gets
		if (leftOrRight == "Left") {isGrabbingText= true;}
		else {isGrabbingText= false;}
		
		// The typing of a relation is 'Relation<X,Y>'
		// We want to know about text that comes after 'Relation<'
		// So we start the loop at int i = 9
		int i = 9;
		
		// We want to travel the string until we bump into the middle comma that
		// splits the typing of the Relation. But if either the domain or range is its own
		// complicated data type with a comma, like say a set, we need to know when to ignore
		// said comma. We'll do that by keeping track of any opened brackets 
		// that haven't been closed yet
		int bracketsOpened = 0;
		
		boolean finishedCreatingResult = false;
		
		char currentLetter;
		
		while (i < relation.length() && !finishedCreatingResult) {
			
			currentLetter = relation.charAt(i);
			
			if (currentLetter == '<') //Inside a type, a bracket for like a Set<int> was opened
				bracketsOpened++;
			
			if (currentLetter == '>' && bracketsOpened > 0) //The closing bracket of an inner type is found
				bracketsOpened--;
			
			if ((currentLetter == ',' || currentLetter == '>') && isGrabbingText) { //Finish conditions for left and right
				if (bracketsOpened == 0) { // The finish for the function was found
					finishedCreatingResult = true;
				}
				else { // Theres a '<' thats pending to be closed
					builtResult.append(currentLetter);
				}
			}
			
			// Add the current letter to the built string if we are in grabbing text mode
			// and the finish conditions haven't been reached
			if (isGrabbingText && !finishedCreatingResult) {
				builtResult.append(currentLetter);
			}
			
			// When we want to find the right type, and we are traveling through the left type
			// to get to the point the right type begins. This is THAT point.
			// From here, we begin grabbing the text to extract the right type from the relation
			if ( (currentLetter == ',') && !isGrabbingText && bracketsOpened == 0 ) {
				isGrabbingText = true;
			}
			
			i++;
		}
		
		result = builtResult.toString();
		return result;
	}
	
	
	public String generateExpressionDataType(ASTExpression expression) {
		// This function is called by a Set Extension, a Relation, 
		// or a Tuple when wanting to determine the data type of elements
		// inside these expressions
		String result = "";
		
		HashMap<String,ASTDataType> freeIdTypes = CppAST.getFreeIdentifiersTypes();
		
		StringBuilder builtResult = new StringBuilder();
		
		String expressionType = expression.getType();
		
		switch(expressionType) {
		case "AssociativeExpression":
			ASTAssociativeExpression associativeExp = (ASTAssociativeExpression) expression;
			
			int childSize = associativeExp.getChildExpressions().size();
			
			switch(associativeExp.getAssociativeType()) {
			case "BackwardComposition":
				// The return type of a relational composition is complicated
				// Its a relation whose domain is the domain of the first in the chain of compositions
				// and whose range is the range of the last in the chain
				if (childSize > 0 ) {
					String typingForLeft = generateExpressionDataType(associativeExp.getChildExpressions().get(childSize-1));
					String typingForRight = generateExpressionDataType(associativeExp.getChildExpressions().get(0));
					
					//These WILL return relations. We have to extract the domains and ranges.
					typingForLeft = extractTypeFromRelationText(typingForLeft,"Left");
					typingForRight = extractTypeFromRelationText(typingForRight,"Right");
					
					builtResult.append("Relation<");
					builtResult.append(typingForLeft);
					builtResult.append(",");
					builtResult.append(typingForRight);
					builtResult.append(">");
					
				}
				break;
			case "ForwardComposition":
				// The return type of a relational composition is complicated
				// Its a relation whose domain is the domain of the first in the chain of compositions
				// and whose range is the range of the last in the chain
				if (childSize > 0 ) {
					String typingForLeft = generateExpressionDataType(associativeExp.getChildExpressions().get(0));
					String typingForRight = generateExpressionDataType(associativeExp.getChildExpressions().get(childSize-1));
					
					//These WILL return relations. We have to extract the domains and ranges.
					typingForLeft = extractTypeFromRelationText(typingForLeft,"Left");
					typingForRight = extractTypeFromRelationText(typingForRight,"Right");
					
					builtResult.append("Relation<");
					builtResult.append(typingForLeft);
					builtResult.append(",");
					builtResult.append(typingForRight);
					builtResult.append(">");
					
				}
				break;
			default:
				if (childSize > 0 ) {
					builtResult.append( generateExpressionDataType(associativeExp.getChildExpressions().get(0)) );
				}
			}
			break;
		case "AtomicExpression":
			ASTAtomicExpression atomicExpression = (ASTAtomicExpression) expression;
			
			switch(atomicExpression.getAtomicExpressionType()) {
			case "Bool": //It is the pre-defined carrier set BOOL, that equals {TRUE,FALSE}
				builtResult.append("Set<bool>");
				break;
			case "EmptySet": // It is an empty set, i.e.: {}
				builtResult.append("EMPTY_SET()");
				break;
			case "False": // It is the boolean value FALSE in EventB
				builtResult.append("bool");
				break;
			case "Integer":
				builtResult.append("INT_SET");
				break;
			case "Natural":
				builtResult.append("NAT_SET");
				break;
			case "Natural1":
				builtResult.append("NAT1_SET");
				break;
			case "True": // It is the boolean TRUE in EventB
				builtResult.append("bool");
				break;
			default:
				
			}
			
			break;
		case "BinaryExpression":
			ASTBinaryExpression binaryExpression = (ASTBinaryExpression) expression;
			
			String leftExpType = generateExpressionDataType(binaryExpression.getLeftSideExpression());
			String rightExpType = generateExpressionDataType(binaryExpression.getRightSideExpression());
			
			switch(binaryExpression.getBinaryExpressionType()) {
			case "Difference":
				builtResult.append(leftExpType);
				break;
			case "DirectProduct":
				builtResult.append("Relation<");
				builtResult.append(extractTypeFromRelationText(leftExpType,"Left"));
				builtResult.append(",Tuple<");
				builtResult.append(extractTypeFromRelationText(leftExpType,"Right"));
				builtResult.append(",");
				builtResult.append(extractTypeFromRelationText(rightExpType,"Right"));
				builtResult.append(">>");
				break;
			case "DomainRestriction":
				builtResult.append(rightExpType);
				break;
			case "DomainSubtraction":
				builtResult.append(rightExpType);
				break;
			case "FunctionImage":
				builtResult.append(extractTypeFromRelationText(leftExpType,"Right"));
				break;
			case "ParallelProduct":
				String type1 = extractTypeFromRelationText(leftExpType,"Left");
				String type2 = extractTypeFromRelationText(rightExpType,"Left");
				String type3 = extractTypeFromRelationText(leftExpType,"Right");
				String type4 = extractTypeFromRelationText(rightExpType,"Right");
				
				builtResult.append("Relation<");
				builtResult.append("Tuple<");
				builtResult.append(type1);
				builtResult.append(",");
				builtResult.append(type2);
				builtResult.append(">,Tuple<");
				builtResult.append(type3);
				builtResult.append(",");
				builtResult.append(type4);
				builtResult.append(">>");
				break;
			case "RangeRestriction":
				builtResult.append(leftExpType);
				break;
			case "RangeSubtraction":
				builtResult.append(leftExpType);
				break;
			case "RelationalImage":
				builtResult.append("Set<");
				builtResult.append(extractTypeFromRelationText(leftExpType,"Right"));
				builtResult.append(">");
				break;
			// The following is the handling of the relation types. All of them are handled
			// in the same way, so thats why we set up the code like this to take advantage
			// of the way a switch statement works. No matter the type of relation.
			// They will land in the code "case Relation"
			case "TotalRelation":
			case "SurjectiveRelation":
			case "TotalSurjectiveRelation":
			case "PartialFunction":
			case "TotalFunction":
			case "PartialInjection":
			case "TotalInjection":
			case "PartialSurjection":
			case "TotalSurjection":
			case "TotalBijection":
			case "Relation":
				builtResult.append("RelationType<");
				builtResult.append(leftExpType);
				builtResult.append(",");
				builtResult.append(rightExpType);
				builtResult.append(">");
				break;
			case "Tuple":
				builtResult.append("Tuple<");
				builtResult.append(leftExpType);
				builtResult.append(",");
				builtResult.append(rightExpType);
				builtResult.append(">");
				break;
			default:
				
			}
			break;
		case "FreeIdentifier":
			ASTFreeIdentifier freeIdentifier = (ASTFreeIdentifier) expression;
			builtResult.append( generateDataType(freeIdTypes.get(freeIdentifier.getIdentifier())) );
			break;
		case "IntegerLiteral":
			builtResult.append("int");
			break;

		case "SetExtension": // It is a fixed set like {1,2,3}
			ASTSetExtension setExtension = (ASTSetExtension) expression;
			
			//Elements of the set
			ASTExpression[] setElements = setExtension.getElements();
					
			// First, we need to check if the Set Extension is a relation,
			// cause in that case, we have to use the Relation class instead
			boolean isRelation = false;
			
			if (setElements.length > 0) {
				// If the inner element of the set extension is a explicit pair like
				// {(a|->b)}. In that case its a binary exp that is a Tuple
				if (setElements[0].getType() == "BinaryExpression") {
					ASTBinaryExpression innerBinaryExp = (ASTBinaryExpression) setElements[0];
					if (innerBinaryExp.getBinaryExpressionType() == "Tuple") {
						isRelation = true;
						builtResult.append("Relation<");
						builtResult.append(generateExpressionDataType(innerBinaryExp.getLeftSideExpression()));
						builtResult.append(",");
						builtResult.append(generateExpressionDataType(innerBinaryExp.getRightSideExpression()));
						builtResult.append(">");
					}
				}
				else if (setElements[0].getType() == "FreeIdentifier") {
					ASTFreeIdentifier innerFreeId = (ASTFreeIdentifier) setElements[0];
					if (freeIdTypes.get(innerFreeId.getIdentifier()).getTypeName() == "BinaryExpression" ) {
						ASTBinaryExpressionType innerFreeIdType = (ASTBinaryExpressionType) freeIdTypes.get(innerFreeId.getIdentifier());
						if (innerFreeIdType.getBinaryOperator() == "Pair") {
							isRelation = true;
							builtResult.append("Relation<");
							builtResult.append(generateDataType( innerFreeIdType.getLeftSideDataType() ));
							builtResult.append(",");
							builtResult.append(generateDataType( innerFreeIdType.getRightSideDataType() ));
							builtResult.append(">");
						}
					}
				}
				
				if (!isRelation) {
					builtResult.append("Set<");
					builtResult.append(generateExpressionDataType(setElements[0]));
					builtResult.append(">");
				}
			}

			break;
		case "UnaryExpression":
			ASTUnaryExpression unaryExpression = (ASTUnaryExpression) expression;
		
			String internalExpressionString = generateExpressionDataType(unaryExpression.getInternalExpression());
			
			switch(unaryExpression.getUnaryType()) {
			case "Cardinality":
				builtResult.append("int");
				break;
			case "Inverse":
				builtResult.append("Relation<");
				builtResult.append(extractTypeFromRelationText(internalExpressionString,"Right"));
				builtResult.append(",");
				builtResult.append(extractTypeFromRelationText(internalExpressionString,"Left"));
				builtResult.append(">");
				break;
			case "PowerSet":
				builtResult.append("Set<");
				builtResult.append(internalExpressionString);
				builtResult.append(">");
				break;
			case "PowerSet1":
				builtResult.append("Set<");
				builtResult.append(internalExpressionString);
				builtResult.append(">");
				break;
			}
			break;
			
		default:
			
		}
		
		result = builtResult.toString();
		return result;
	}
	
	
	public String generateExpression(ASTExpression expression) {
		String result = "";
		
		StringBuilder builtResult = new StringBuilder();
		
		// A expression will always be surrounded by parenthesis
		builtResult.append("(");
		
		String expressionType = expression.getType();
		
		switch(expressionType) {
		case "AssociativeExpression":
			ASTAssociativeExpression associativeExp = (ASTAssociativeExpression) expression;
			
			int childIndex = 0;
			
			switch(associativeExp.getAssociativeType()) {
			case "BackwardComposition":
				for (ASTExpression child : associativeExp.getChildExpressions()) {
					if (childIndex != 0)
						builtResult.append(".backwardComposition(");
					builtResult.append(generateExpression(child));
					if (childIndex != 0)
						builtResult.append(")");
					childIndex += 1;
				}
				break;
			case "ForwardComposition":
				for (ASTExpression child : associativeExp.getChildExpressions()) {
					if (childIndex != 0)
						builtResult.append(".forwardComposition(");
					builtResult.append(generateExpression(child));
					if (childIndex != 0)
						builtResult.append(")");
					childIndex += 1;
				}
				break;
			case "RelationalOverride":
				for (ASTExpression child : associativeExp.getChildExpressions()) {
					if (childIndex != 0)
						builtResult.append(".relationalOverride(");
					builtResult.append(generateExpression(child));
					if (childIndex != 0)
						builtResult.append(")");
					childIndex += 1;
				}
				break;
			case "Union":
				for (ASTExpression child : associativeExp.getChildExpressions()) {
					if (childIndex != 0)
						builtResult.append(".cppUnion(");
					builtResult.append(generateExpression(child));
					if (childIndex != 0)
						builtResult.append(")");
					childIndex += 1;
				}
				break;
			case "Intersection":
				for (ASTExpression child : associativeExp.getChildExpressions()) {
					if (childIndex != 0)
						builtResult.append(".cppIntersection(");
					builtResult.append(generateExpression(child));
					if (childIndex != 0)
						builtResult.append(")");
					childIndex += 1;
				}
				break;
			
			}
			break;
		case "AtomicExpression":
			ASTAtomicExpression atomicExpression = (ASTAtomicExpression) expression;
			
			switch(atomicExpression.getAtomicExpressionType()) {
			case "Bool": //It is the pre-defined carrier set BOOL, that equals {TRUE,FALSE}
				builtResult.append("BOOL_SET()");
				break;
			case "EmptySet": // It is an empty set, i.e.: {}
				builtResult.append("EMPTY_SET()");
				break;
			case "False": // It is the boolean value FALSE in EventB
				builtResult.append("false");
				break;
			case "Integer":
				builtResult.append("INT_SET()");
				break;
			case "Natural":
				builtResult.append("NAT_SET()");
				break;
			case "Natural1":
				builtResult.append("NAT1_SET()");
				break;
			case "True": // It is the boolean TRUE in EventB
				builtResult.append("true");
				break;
			}
			
			break;
		case "BinaryExpression":
			ASTBinaryExpression binaryExpression = (ASTBinaryExpression) expression;
			
			String leftExp = generateExpression(binaryExpression.getLeftSideExpression());
			String rightExp = generateExpression(binaryExpression.getRightSideExpression());
			
			switch(binaryExpression.getBinaryExpressionType()) {
			case "Difference":
				builtResult.append(leftExp);
				builtResult.append(".cppDifference(");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "DirectProduct":
				builtResult.append(leftExp);
				builtResult.append(".directProduct(");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "DomainRestriction":
				builtResult.append(rightExp);
				builtResult.append(".domainRestriction(");
				builtResult.append(leftExp);
				builtResult.append(")");
				break;
			case "DomainSubtraction":
				builtResult.append(rightExp);
				builtResult.append(".domainSubtraction(");
				builtResult.append(leftExp);
				builtResult.append(")");
				break;
			case "FunctionImage":
				builtResult.append(leftExp);
				builtResult.append(".functionImage(");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "ParallelProduct":
				builtResult.append(leftExp);
				builtResult.append(".parallelProduct(");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "RangeRestriction":
				builtResult.append(leftExp);
				builtResult.append(".domainRestriction(");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "RangeSubtraction":
				builtResult.append(leftExp);
				builtResult.append(".domainSubtraction(");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "Relation":
				// Generate the "RelationType<x,y>" data typing
				builtResult.append(generateExpressionDataType(expression));
				builtResult.append("(\"Basic\", ");
				builtResult.append(leftExp);
				builtResult.append(", ");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "TotalRelation":
				// Generate the "RelationType<x,y>" data typing
				builtResult.append(generateExpressionDataType(expression));
				builtResult.append("(\"Total\", ");
				builtResult.append(leftExp);
				builtResult.append(", ");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "SurjectiveRelation":
				// Generate the "RelationType<x,y>" data typing
				builtResult.append(generateExpressionDataType(expression));
				builtResult.append("(\"Surjective\", ");
				builtResult.append(leftExp);
				builtResult.append(", ");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "TotalSurjectiveRelation":
				// Generate the "RelationType<x,y>" data typing
				builtResult.append(generateExpressionDataType(expression));
				builtResult.append("(\"TotalSurjective\", ");
				builtResult.append(leftExp);
				builtResult.append(", ");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "PartialFunction":
				// Generate the "RelationType<x,y>" data typing
				builtResult.append(generateExpressionDataType(expression));
				builtResult.append("(\"PartialFunction\", ");
				builtResult.append(leftExp);
				builtResult.append(", ");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "TotalFunction":
				// Generate the "RelationType<x,y>" data typing
				builtResult.append(generateExpressionDataType(expression));
				builtResult.append("(\"TotalFunction\", ");
				builtResult.append(leftExp);
				builtResult.append(", ");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "PartialInjection":
				// Generate the "RelationType<x,y>" data typing
				builtResult.append(generateExpressionDataType(expression));
				builtResult.append("(\"PartialInjection\", ");
				builtResult.append(leftExp);
				builtResult.append(", ");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "TotalInjection":
				// Generate the "RelationType<x,y>" data typing
				builtResult.append(generateExpressionDataType(expression));
				builtResult.append("(\"TotalInjection\", ");
				builtResult.append(leftExp);
				builtResult.append(", ");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "PartialSurjection":
				// Generate the "RelationType<x,y>" data typing
				builtResult.append(generateExpressionDataType(expression));
				builtResult.append("(\"PartialSurjection\", ");
				builtResult.append(leftExp);
				builtResult.append(", ");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "TotalSurjection":
				// Generate the "RelationType<x,y>" data typing
				builtResult.append(generateExpressionDataType(expression));
				builtResult.append("(\"TotalSurjection\", ");
				builtResult.append(leftExp);
				builtResult.append(", ");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "TotalBijection":
				// Generate the "RelationType<x,y>" data typing
				builtResult.append(generateExpressionDataType(expression));
				builtResult.append("(\"TotalBijection\", ");
				builtResult.append(leftExp);
				builtResult.append(", ");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "RelationalImage":
				builtResult.append(leftExp);
				builtResult.append(".relationalImage(");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "Tuple":
				// Generate the "Tuple<x,y>" data typing
				builtResult.append(generateExpressionDataType(expression));

				builtResult.append("(");
				builtResult.append(leftExp);
				builtResult.append(",");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			}
			break;
		case "FreeIdentifier":
			ASTFreeIdentifier freeIdentifier = (ASTFreeIdentifier) expression;
			builtResult.append(freeIdentifier.getIdentifier());
			break;
		case "IntegerLiteral":
			ASTIntegerLiteral integerLiteral = (ASTIntegerLiteral) expression;
			
			builtResult.append(integerLiteral.getValue());
			break;

		case "SetExtension": // It is a fixed set like {1,2,3}
			ASTSetExtension setExtension = (ASTSetExtension) expression;
			
			// StringBuilder setExtensionTyping = new StringBuilder();
			
			// The CppVisitor figures out the data type of elements in the set extension
			// We need that to construct the Set object
			
			// First, we need to check if the Set Extension is a relation,
			// cause in that case, we have to use the Relation class instead
			
			builtResult.append(generateExpressionDataType(expression));
			
			builtResult.append("({");
			
			//Elements of the set
			ASTExpression[] setElements = setExtension.getElements();
			int index = 0;
			
			while(index < setElements.length) {
				if (index != 0) {
					builtResult.append(",");
				}
				builtResult.append(generateExpression(setElements[index]));
				index += 1;
			}
				
			builtResult.append("})");
			break;
		case "UnaryExpression":
			ASTUnaryExpression unaryExpression = (ASTUnaryExpression) expression;
		
			String internalExpressionString = generateExpression(unaryExpression.getInternalExpression());
			
			builtResult.append(internalExpressionString);
			
			switch(unaryExpression.getUnaryType()) {
			case "Cardinality":
				builtResult.append(".cardinality()");
				break;
			case "Inverse":
				builtResult.append(".inverse()");
			break;
			case "PowerSet":
				builtResult.append(".powerSet()");
				break;
			case "PowerSet1":
				builtResult.append(".powerSet1()");
				break;
			}
			break;
			
		default:
			
		}
		
		builtResult.append(")");
		result = builtResult.toString();
		return result;
	}
	
	
	public String generateAssignment(ASTAssignment assignment) {
		String result = "";
		
		StringBuilder builtResult = new StringBuilder();
		
		switch(assignment.getType()) {
		case "BecomesEqualTo":
			builtResult.append( generateExpression(assignment.getChangingIdentifier()) );
			builtResult.append(" = ");
			builtResult.append( generateExpression(assignment.getNewValue()) );
			
			break;
		default:
			builtResult.append("ERROR_DEFAUTLSWITCH_IN_GENERATE_ASSIGNMENT");
		}
		
		result = builtResult.toString();
		
		return result;
	}
	
	
	public String generatePredicate(ASTPredicate predicate) {
		String result = "";
		
		StringBuilder builtResult = new StringBuilder();
		
		// A predicate will always be surrounded by parenthesis
		builtResult.append("(");
		
		String predicateType = predicate.getType();
		
		switch(predicateType) {
		case "AssociativePredicate": //It is a logical AND / OR
			ASTAssociativePredicate associativePredicate = (ASTAssociativePredicate) predicate;
			
			ArrayList<ASTPredicate> childPredicates = associativePredicate.getChildPredicates();
			int numberOfChildPredicates = childPredicates.size();
			String associativeOperator;
			
			switch(associativePredicate.getAssociativeType()) {
			case "And":
				associativeOperator = "&&";
				break;
			case "Or":
				associativeOperator = "||";
				break;
			default:
				associativeOperator = "ERROR_FINDING_ASSOCIATIVEPREDICATE_TYPE";
			}
			
			int i = 0;
			while (i < numberOfChildPredicates) {
				if (i != 0) { //If its not the first element in the line of AND's/OR's, add said operator
					builtResult.append(" ");
					builtResult.append(associativeOperator);
					builtResult.append(" ");
				}
				
				builtResult.append(generatePredicate(childPredicates.get(i)));
				i += 1;
			}
			break;
			
		case "BinaryPredicate": //Its a logical implication, equivalence
			ASTBinaryPredicate binaryPredicate = (ASTBinaryPredicate) predicate;
			
			String leftSideString = generatePredicate(binaryPredicate.getLeftSide());
			String rightSideString = generatePredicate(binaryPredicate.getRightSide());
			
			switch(binaryPredicate.getBinaryType()) {
			case "LogicalImplication": // a => b   not(a) or (b)
				builtResult.append("!");
				builtResult.append(leftSideString);
				
				builtResult.append(" || ");
				
				builtResult.append(rightSideString);
				
				break;
			case "LogicalEquivalence": // a <==> b   a == b
				builtResult.append(leftSideString);
				builtResult.append(" == ");
				builtResult.append(rightSideString);
				break;
			default:
				builtResult.append("ERROR_FINDING_BINARYPREDICATE_TYPE");
			}
			
			break;
		case "MultiplePredicate":
			ASTMultiplePredicate multiplePredicate = (ASTMultiplePredicate) predicate;
			
			ArrayList<ASTExpression> multiplePredicateChildren = multiplePredicate.getChildren();
			int multipleChildrenSize = multiplePredicateChildren.size();
			
			int m = 0;
			
			builtResult.append(generateExpression(multiplePredicate.getPartitionedSet()));
			builtResult.append(".partition(Set<");
			builtResult.append(generateDataType(multiplePredicate.getSetDataType()));
			builtResult.append(">({");
			
			while (m < multipleChildrenSize) {
				if (m != 0)
					builtResult.append(", ");
				builtResult.append(generateExpression(multiplePredicateChildren.get(m)));
				m += 1;
			}
			
			builtResult.append("}))");
			break;
		case "RelationalPredicate":
			//Its a binary conditional that compares two expressions
			//E.g. equal to, less than, belongs to, greater equal to, subset etc.
			
			ASTRelationalPredicate relationalPredicate = (ASTRelationalPredicate) predicate;
			
			String leftSideRPString = generateExpression(relationalPredicate.getLeftSide());
			String rightSideRPString = generateExpression(relationalPredicate.getRightSide());
			
			switch(relationalPredicate.getRelationalType()) {
			case "Equal":
				builtResult.append(leftSideRPString);
				builtResult.append(" == ");
				builtResult.append(rightSideRPString);
				break;
			case "NotEqual":
				builtResult.append(leftSideRPString);
				builtResult.append(" != ");
				builtResult.append(rightSideRPString);
				break;
			case "Subset":
				builtResult.append(rightSideRPString);
				builtResult.append(".hasSubset(");
				builtResult.append(leftSideRPString);
				builtResult.append(")");
				break;
			case "NotSubset":
				builtResult.append(rightSideRPString);
				builtResult.append(".hasNotSubset(");
				builtResult.append(leftSideRPString);
				builtResult.append(")");
				break;
			case "SubsetOrEqual":
				builtResult.append(rightSideRPString);
				builtResult.append(".hasSubsetOrEqual(");
				builtResult.append(leftSideRPString);
				builtResult.append(")");
				break;
			case "NotSubsetOrEqual":
				builtResult.append(rightSideRPString);
				builtResult.append(".hasNotSubsetOrEqual(");
				builtResult.append(leftSideRPString);
				builtResult.append(")");
				break;
			case "Belongs":
				builtResult.append(rightSideRPString);
				builtResult.append(".contains(");
				builtResult.append(leftSideRPString);
				builtResult.append(")");
				break;
			case "NotBelongs":
				builtResult.append(rightSideRPString);
				builtResult.append(".notContains(");
				builtResult.append(leftSideRPString);
				builtResult.append(")");
				break;
			default:
				
			}
			
			break;
		case "SimplePredicate":
			ASTSimplePredicate simplePredicate = (ASTSimplePredicate) predicate;
			
			String internalExpressionString = generateExpression(simplePredicate.getInternalExpression());
			
			switch(simplePredicate.getSimplePredicateType()) {
			case "Finite":
				builtResult.append(internalExpressionString);
				builtResult.append(".getIsFinite()");
			default:
			}
			break;
		case "UnaryPredicate": // Its a NOT logical operator
			ASTUnaryPredicate unaryPredicate = (ASTUnaryPredicate) predicate;
			
			ASTPredicate internalPredicate = unaryPredicate.getInternalPredicate();
			
			switch(unaryPredicate.getUnaryType()) {
			case "Not":
				builtResult.append("!");
				builtResult.append(generatePredicate(internalPredicate));
				break;
			default:
				builtResult.append("ERROR_FINDING_UNARYPREDICATE_TYPE");
			}
			break;
			
		case "False": // This is for the predicate false 
			builtResult.append("false");
			break;
		case "True": // This is for the predicate true 
			builtResult.append("true");
			break;
		default:
			builtResult.append("ERROR_PREDICATETYPENOTFOUND");
		}
		
		builtResult.append(")");
		result = builtResult.toString();
		return result;
	}

}
