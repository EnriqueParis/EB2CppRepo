package eb2cpp.ast;

import java.util.ArrayList;

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
	
	private EB2CppAST CppAST;

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
	
	
	public String generateExpressionDataType(ASTExpression expression) {
		String result = "";
		
		StringBuilder builtResult = new StringBuilder();
		
		String expressionType = expression.getType();
		
		switch(expressionType) {
		case "AssociativeExpression":
			ASTAssociativeExpression associativeExp = (ASTAssociativeExpression) expression;
			//int childIndex;
			
			switch(associativeExp.getAssociativeType()) {
			default:
				if (associativeExp.getChildExpressions().size() > 0 ) {
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
			case "True": // It is the boolean TRUE in EventB
				builtResult.append("bool");
				break;
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
			case "Tuple":
				
				builtResult.append("Tuple<");
				builtResult.append(leftExpType);
				builtResult.append(",");
				builtResult.append(rightExpType);
				builtResult.append(">");
				break;
			}
			break;
		case "FreeIdentifier":
			ASTFreeIdentifier freeIdentifier = (ASTFreeIdentifier) expression;
			builtResult.append( (CppAST.getFreeIdentifiersTypes().get(freeIdentifier.getIdentifier())).getTypeName() );
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
			
			int index = 0;
			while(index < setElements.length) {
				if (index != 0) {
					builtResult.append(",");
				}
				builtResult.append(generateExpression(setElements[index]));
				index += 1;
			}
			
			if (setExtension.getSetType().getTypeName() == "BinaryExpression") {
				ASTBinaryExpressionType setExtensionBinaryType = (ASTBinaryExpressionType) setExtension.getSetType();
				
				if (setExtensionBinaryType.getBinaryOperator() == "Pair") {
					isRelation = true;
					builtResult.append("Relation<");
					builtResult.append(generateDataType(setExtensionBinaryType.getLeftSideDataType()));
					builtResult.append(",");
					builtResult.append(generateDataType(setExtensionBinaryType.getRightSideDataType()));
					builtResult.append(">");
				}
			}
			
			if (!isRelation) {
				builtResult.append("Set<");
				builtResult.append(generateDataType(setExtension.getSetType()));
				builtResult.append(">");
			}
			
			builtResult.append("({");
			
			
			
				
			builtResult.append("})");
			break;
		case "UnaryExpression":
			ASTUnaryExpression unaryExpression = (ASTUnaryExpression) expression;
		
			String internalExpressionString = generateExpression(unaryExpression.getInternalExpression());
			
			builtResult.append(internalExpressionString);
			
			switch(unaryExpression.getUnaryType()) {
			case "Cardinality":
				builtResult.append(".Cardinality()");
				break;
			case "PowerSet":
				builtResult.append(".PowerSet()");
				break;
			case "PowerSet1":
				builtResult.append(".PowerSet1()");
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
			
			switch(associativeExp.getAssociativeType()) {
			case "Union":
				int childIndex = 0;
				for (ASTExpression child : associativeExp.getChildExpressions()) {
					if (childIndex != 0)
						builtResult.append(".CppUnion(");
					builtResult.append(generateExpression(child));
					if (childIndex != 0)
						builtResult.append(")");
					childIndex += 1;
				}
				break;
			case "Intersection":
				int childIntIndex = 0;
				for (ASTExpression child : associativeExp.getChildExpressions()) {
					if (childIntIndex != 0)
						builtResult.append(".CppIntersection(");
					builtResult.append(generateExpression(child));
					if (childIntIndex != 0)
						builtResult.append(")");
					childIntIndex += 1;
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
				builtResult.append(".CppDifference(");
				builtResult.append(rightExp);
				builtResult.append(")");
				break;
			case "Tuple":
				String leftDataType = generateDataType(binaryExpression.getLeftDataType());
				String rightDataType = generateDataType(binaryExpression.getRightDataType());
				
				builtResult.append("Tuple<");
				builtResult.append(leftDataType);
				builtResult.append(",");
				builtResult.append(rightDataType);
				builtResult.append(">(");
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
			
			//StringBuilder setExtensionTyping = new StringBuilder();
			
			// The CppVisitor figures out the data type of elements in the set extension
			// We need that to construct the Set object
			
			// First, we need to check if the Set Extension is a relation,
			// cause in that case, we have to use the Relation class instead
			boolean isRelation = false;
			if (setExtension.getSetType().getTypeName() == "BinaryExpression") {
				ASTBinaryExpressionType setExtensionBinaryType = (ASTBinaryExpressionType) setExtension.getSetType();
				
				if (setExtensionBinaryType.getBinaryOperator() == "Pair") {
					isRelation = true;
					builtResult.append("Relation<");
					builtResult.append(generateDataType(setExtensionBinaryType.getLeftSideDataType()));
					builtResult.append(",");
					builtResult.append(generateDataType(setExtensionBinaryType.getRightSideDataType()));
					builtResult.append(">");
				}
			}
			
			if (!isRelation) {
				builtResult.append("Set<");
				builtResult.append(generateDataType(setExtension.getSetType()));
				builtResult.append(">");
			}
			
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
				builtResult.append(".Cardinality()");
				break;
			case "PowerSet":
				builtResult.append(".PowerSet()");
				break;
			case "PowerSet1":
				builtResult.append(".PowerSet1()");
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
			builtResult.append(".Partition(Set<");
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
				builtResult.append(rightSideRPString);
				builtResult.append(" == ");
				builtResult.append(leftSideRPString);
				break;
			case "NotEqual":
				builtResult.append(rightSideRPString);
				builtResult.append(" != ");
				builtResult.append(leftSideRPString);
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
				builtResult.append(".Contains(");
				builtResult.append(leftSideRPString);
				builtResult.append(")");
				break;
			case "NotBelongs":
				builtResult.append(rightSideRPString);
				builtResult.append(".NotContains(");
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
