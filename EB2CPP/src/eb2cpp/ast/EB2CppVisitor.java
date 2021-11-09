package eb2cpp.ast;

import java.util.HashMap;
import java.util.LinkedList;

import org.eventb.core.ast.Assignment;
import org.eventb.core.ast.AssociativeExpression;
import org.eventb.core.ast.AssociativePredicate;
import org.eventb.core.ast.AtomicExpression;
import org.eventb.core.ast.BecomesEqualTo;
import org.eventb.core.ast.BecomesMemberOf;
import org.eventb.core.ast.BecomesSuchThat;
import org.eventb.core.ast.BinaryExpression;
import org.eventb.core.ast.BinaryPredicate;
import org.eventb.core.ast.BoolExpression;
import org.eventb.core.ast.BoundIdentDecl;
import org.eventb.core.ast.BoundIdentifier;
import org.eventb.core.ast.Expression;
import org.eventb.core.ast.ExtendedExpression;
import org.eventb.core.ast.ExtendedPredicate;
import org.eventb.core.ast.Formula;
import org.eventb.core.ast.FormulaFactory;
import org.eventb.core.ast.FreeIdentifier;
import org.eventb.core.ast.IParseResult;
import org.eventb.core.ast.ISimpleVisitor2;
import org.eventb.core.ast.IntegerLiteral;
import org.eventb.core.ast.LiteralPredicate;
import org.eventb.core.ast.MultiplePredicate;
import org.eventb.core.ast.Predicate;
import org.eventb.core.ast.PredicateVariable;
import org.eventb.core.ast.QuantifiedExpression;
import org.eventb.core.ast.QuantifiedPredicate;
import org.eventb.core.ast.RelationalPredicate;
import org.eventb.core.ast.SetExtension;
import org.eventb.core.ast.SimplePredicate;
import org.eventb.core.ast.UnaryExpression;
import org.eventb.core.ast.UnaryPredicate;

import eb2cpp.ast.context.ASTContext;
import eb2cpp.ast.expressions.ASTAssociativeExpression;
import eb2cpp.ast.expressions.ASTAtomicExpression;
import eb2cpp.ast.expressions.ASTBinaryExpression;
import eb2cpp.ast.expressions.ASTExpression;
import eb2cpp.ast.expressions.ASTFreeIdentifier;
import eb2cpp.ast.expressions.ASTIntegerLiteral;
import eb2cpp.ast.expressions.ASTSetExtension;
import eb2cpp.ast.expressions.ASTUnaryExpression;
import eb2cpp.ast.machine.ASTAssignment;
import eb2cpp.ast.machine.ASTMachine;
import eb2cpp.ast.predicates.ASTAssociativePredicate;
import eb2cpp.ast.predicates.ASTBinaryPredicate;
import eb2cpp.ast.predicates.ASTMultiplePredicate;
import eb2cpp.ast.predicates.ASTPredicate;
import eb2cpp.ast.predicates.ASTQuantifiedPredicate;
import eb2cpp.ast.predicates.ASTRelationalPredicate;
import eb2cpp.ast.predicates.ASTSimplePredicate;
import eb2cpp.ast.predicates.ASTUnaryPredicate;
import eb2cpp.ast.types.ASTBinaryExpressionType;
import eb2cpp.ast.types.ASTBoolType;
import eb2cpp.ast.types.ASTDataType;
import eb2cpp.ast.types.ASTFreeIdentifierType;
import eb2cpp.ast.types.ASTIntegerType;
import eb2cpp.ast.types.ASTUnaryExpressionType;



public class EB2CppVisitor implements ISimpleVisitor2 {
	
	///////////////
	// VARIABLES //
	///////////////
	//This same visitor class is used to navigate the rodinAST.
	//It needs to act differently depending on if its looking for
	//a data type, or a predicate
	private boolean isGettingDataType;
	private boolean isGettingPredicateOrExpression;
	
	// For use in the Cpp code generation, when encountering
	// certain elements (set extensions, binary expressions...) 
	// we need to know the type of the elements they contain
	private boolean isGettingComplementaryType;
	
	// The storage of whatever data type, expression, predicate or assignment being found
	private ASTDataType dataTypeBeingFound;
	private ASTPredicate predicateBeingFound;
	private ASTExpression predicateExpressionBeingFound;
	private ASTAssignment assignmentBeingFound;
	
	
	// A reference to the CppAST that the component using the visitor belongs to
	private EB2CppAST CppAST;
	
	
	/////////////
	// METHODS //
	/////////////
	
	public void setCppAST(EB2CppAST ast) {
		CppAST = ast;
	}
	
	public ASTDataType getDataType(String rawDataType) {
		dataTypeBeingFound = new ASTDataType();
		isGettingDataType = true;
		
		final FormulaFactory formulaFactory = FormulaFactory.getDefault();
		final IParseResult parseResult = formulaFactory.parseExpression(rawDataType, null);
		final Expression expression = parseResult.getParsedExpression();
		
		System.out.print("const parsed exp: ");System.out.println(expression.toString());
		System.out.print("const syntax tree exp: ");System.out.println(expression.getSyntaxTree());
		
		expression.accept(this);
		isGettingDataType = false;
		
		return dataTypeBeingFound;
	}
	
	public ASTPredicate getPredicate(String predicateString) {
		predicateBeingFound = new ASTPredicate();
		isGettingPredicateOrExpression = true;
		
		FormulaFactory formulaFactory = FormulaFactory.getDefault();
        IParseResult parseResult = formulaFactory.parsePredicate(predicateString, null);
        Predicate parsedPredicate = parseResult.getParsedPredicate();
        
        System.out.print("predicate parsed pred: ");System.out.println(parsedPredicate.toString());
		System.out.print("predicate syntax tree exp: ");System.out.println(parsedPredicate.getSyntaxTree());
		
		parsedPredicate.accept(this);
		
		isGettingPredicateOrExpression = false;
		
		return predicateBeingFound;
	}
	
	public ASTAssignment getAssignment(String assignmentString) {
		assignmentBeingFound = new ASTAssignment();
		isGettingPredicateOrExpression = true;
		
		FormulaFactory formulaFactory = FormulaFactory.getDefault();
        IParseResult parseResult = formulaFactory.parseAssignment(assignmentString, null);
        Assignment parsedAssignment = parseResult.getParsedAssignment();
        
        System.out.print("assignment parsed pred: ");System.out.println(parsedAssignment.toString());
		System.out.print("assignment syntax tree exp: ");System.out.println(parsedAssignment.getSyntaxTree());
		
		parsedAssignment.accept(this);
		
		isGettingPredicateOrExpression = false;
		
		return assignmentBeingFound;
	}
	
	public ASTDataType getComplementaryDataType(Expression exp) {
		ASTDataType result;
		
		isGettingDataType = true;
		isGettingPredicateOrExpression = false;
		isGettingComplementaryType = true;
		
		if (exp.getType() == null) { //Its a free identifier??
			System.out.println("Visited get ComplementaryDataType is null");
			exp.accept(this);
			result = dataTypeBeingFound;
			}
		else {
			System.out.println("Visited get ComplementaryDataType wasnt null (non-free identifier");
			result = getDataType(exp.getType().toString());
		}
		isGettingDataType = false;
		isGettingPredicateOrExpression = true;
		isGettingComplementaryType = false;
		
		System.out.println("Finished getComplementaryDataType");
		
		return result;
	}

	@Override
	public void visitBecomesEqualTo(BecomesEqualTo assignment) {
		// TODO Auto-generated method stub
		System.out.println("Visited BecomesEqualTo ");
		
		ASTAssignment equalToAssignment = new ASTAssignment("BecomesEqualTo");
		
		assignment.getAssignedIdentifiers()[0].accept(this);
		equalToAssignment.setChangingIdentifier((ASTFreeIdentifier) predicateExpressionBeingFound);
		
		assignment.getExpressions()[0].accept(this);
		equalToAssignment.setNewValue(predicateExpressionBeingFound);
		
		assignmentBeingFound = equalToAssignment;
	}

	@Override
	public void visitBecomesMemberOf(BecomesMemberOf assignment) {
		// TODO Auto-generated method stub
		System.out.println("Visited BecomesMemberOf ");
		
		ASTAssignment equalToAssignment = new ASTAssignment("BecomesMemberOf");
		
		assignment.getAssignedIdentifiers()[0].accept(this);
		equalToAssignment.setChangingIdentifier((ASTFreeIdentifier) predicateExpressionBeingFound);
		
		assignment.getSet().accept(this);
		equalToAssignment.setNewValue(predicateExpressionBeingFound);
		
		assignmentBeingFound = equalToAssignment;
	}

	@Override
	public void visitBecomesSuchThat(BecomesSuchThat assignment) {
		// TODO Auto-generated method stub
		System.out.println("Visited BecomesSuchThat ");
		
		ASTAssignment equalToAssignment = new ASTAssignment("BecomesEqualTo");
		
		assignment.getAssignedIdentifiers()[0].accept(this);
		equalToAssignment.setChangingIdentifier((ASTFreeIdentifier) predicateExpressionBeingFound);
		
		assignment.getCondition().accept(this);
		equalToAssignment.setNewValue(predicateExpressionBeingFound);
		
		assignmentBeingFound = equalToAssignment;
	}

	@Override
	public void visitBoundIdentDecl(BoundIdentDecl boundIdentDecl) {
		// TODO Auto-generated method stub
		System.out.println("Visited BoundIdentDecl ");
	}

	@Override
	public void visitAssociativeExpression(AssociativeExpression expression) {
		// TODO Auto-generated method stub
		System.out.println("Visited AssociativeExpression ");
		
		int expressionTag = expression.getTag();
		boolean hasDefaultError = false;
		
		System.out.println("Expression tag: " + expressionTag);
		
		ASTAssociativeExpression newAssociativeExpression = new ASTAssociativeExpression();
		
		switch(expressionTag) {
		case Formula.BUNION: //301
			if (isGettingPredicateOrExpression)
				newAssociativeExpression = new ASTAssociativeExpression("Union");
			break;
		case Formula.BINTER: //302
			if (isGettingPredicateOrExpression)
				newAssociativeExpression = new ASTAssociativeExpression("Intersection");
			break;
		case Formula.BCOMP: //303
			if (isGettingPredicateOrExpression)
				newAssociativeExpression = new ASTAssociativeExpression("BackwardComposition");
			break;
		case Formula.FCOMP: //304
			if (isGettingPredicateOrExpression)
				newAssociativeExpression = new ASTAssociativeExpression("ForwardComposition");
			break;
		case Formula.OVR: //305
			if (isGettingPredicateOrExpression)
				newAssociativeExpression = new ASTAssociativeExpression("RelationalOverride");
			break;
		case Formula.PLUS: //306
			if (isGettingPredicateOrExpression)
				newAssociativeExpression = new ASTAssociativeExpression("Addition");
			break;
		case Formula.MUL: //307
			if (isGettingPredicateOrExpression)
				newAssociativeExpression = new ASTAssociativeExpression("Multiplication");
			break;
		default:
			hasDefaultError = true;
			if (isGettingPredicateOrExpression)
				predicateExpressionBeingFound.setAsError();
		}
		
		if (!hasDefaultError) {
			if (isGettingPredicateOrExpression) {
				for (Expression child : expression.getChildren()) {
					child.accept(this);
					newAssociativeExpression.addChildExpression(predicateExpressionBeingFound);
				}
			}
			
			predicateExpressionBeingFound = newAssociativeExpression;
		}
	}

	@Override
	public void visitAtomicExpression(AtomicExpression expression) {
		// TODO Auto-generated method stub
		System.out.println("Visited AtomicExpression ");
		System.out.println(expression.getTag());
		
		int expressionTag = expression.getTag();
		
		//By printing the tag of the expression, you can learn what dataType each
		//tag represents. The tag is that 3-digit number (401, 404)
		
		switch(expressionTag) {
		case Formula.INTEGER: //401
			if (isGettingDataType) dataTypeBeingFound = new ASTIntegerType();
			else if (isGettingPredicateOrExpression) 
				predicateExpressionBeingFound = new ASTAtomicExpression("Integer");
			break;
		case Formula.BOOL: //404
			if (isGettingDataType) dataTypeBeingFound = new ASTBoolType();
			else if (isGettingPredicateOrExpression) 
				predicateExpressionBeingFound = new ASTAtomicExpression("Bool");
			break;
		case Formula.TRUE: //405
			if (isGettingPredicateOrExpression) 
				predicateExpressionBeingFound = new ASTAtomicExpression("True");
			break;
		case Formula.FALSE: //406
			if (isGettingPredicateOrExpression) 
				predicateExpressionBeingFound = new ASTAtomicExpression("False");
			break;
		case Formula.NATURAL: //402
			if (isGettingPredicateOrExpression) 
				predicateExpressionBeingFound = new ASTAtomicExpression("Natural");
			break;
		case Formula.NATURAL1: //403
			if (isGettingPredicateOrExpression) 
				predicateExpressionBeingFound = new ASTAtomicExpression("Natural1");
			break;
		case Formula.EMPTYSET: //407
			if (isGettingPredicateOrExpression) 
				predicateExpressionBeingFound = new ASTAtomicExpression("EmptySet");
			break;
		case Formula.KID_GEN: //412
			if (isGettingPredicateOrExpression) 
				predicateExpressionBeingFound = new ASTAtomicExpression("ID");
			break;
		default:
			if (isGettingDataType) dataTypeBeingFound.setAsError();
		}
	}

	@Override
	public void visitBinaryExpression(BinaryExpression expression) {
		// TODO Auto-generated method stub
		System.out.println("Visited BinaryExpression ");
		System.out.println(expression.getTag());
		
		int expressionTag = expression.getTag();
		
		ASTBinaryExpressionType binaryExpressionType = new ASTBinaryExpressionType();
		ASTBinaryExpression binaryExpression = new ASTBinaryExpression();
		boolean hasDefaultError = false;
		
		switch(expressionTag) {
		case Formula.MAPSTO: //201
			if (isGettingDataType) {
				binaryExpressionType = new ASTBinaryExpressionType("Pair");
			}
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("Tuple"); //Was set as tuple in other work
			break;
		case Formula.REL: //202
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("Relation");
			break;
		case Formula.TREL: //203
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("TotalRelation");
			break;
		case Formula.SREL: //204
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("SurjectiveRelation");
			break;
		case Formula.STREL: //205
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("TotalSurjectiveRelation");
			break;
		case Formula.PFUN: //206
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("PartialFunction");
			break;
		case Formula.TFUN: //207
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("TotalFunction");
			break;
		case Formula.PINJ: //208
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("PartialInjection");
			break;
		case Formula.TINJ: //209
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("TotalInjection");
			break;
		case Formula.PSUR: //210
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("PartialSurjection");
			break;
		case Formula.TSUR: //211
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("TotalSurjection");
			break;
		case Formula.TBIJ: //212
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("TotalBijection");
			break;
		case Formula.SETMINUS: //213
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("Difference");
			break;
		case Formula.CPROD: //214 Cartesian Product
			if (isGettingDataType) {
				binaryExpressionType = new ASTBinaryExpressionType("Pair");
			}
			
			if (isGettingPredicateOrExpression) {
				binaryExpression = new ASTBinaryExpression("CartesianProduct");
			}
			break;
		case Formula.DPROD: //215
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("DirectProduct");
			break;
		case Formula.PPROD: //216
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("ParallelProduct");
			break;
		case Formula.DOMRES: //217
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("DomainRestriction");
			break;
		case Formula.DOMSUB: //218
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("DomainSubtraction");
			break;
		case Formula.RANRES: //219
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("RangeRestriction");
			break;
		case Formula.RANSUB: //220
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("RangeSubtraction");
			break;
		case Formula.UPTO: //221
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("UpTo");
			break;
		case Formula.MINUS: //222
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("Minus");
			break;
		case Formula.DIV: //223
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("Division");
			break;
		case Formula.MOD: //224
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("Mod");
			break;
		case Formula.EXPN: //225
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("Exponentiation");
			break;
		case Formula.FUNIMAGE: //226
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("FunctionImage");
			break;
		case Formula.RELIMAGE: //223
			if (isGettingPredicateOrExpression)
				binaryExpression = new ASTBinaryExpression("RelationalImage");
			break;
		default:
			hasDefaultError = true;
			if (isGettingDataType) dataTypeBeingFound.setAsError();
		}
		
		if (!hasDefaultError) {
			if (isGettingDataType) {
				expression.getLeft().accept(this);
				binaryExpressionType.setLeftSide(dataTypeBeingFound);
				
				expression.getRight().accept(this);
				binaryExpressionType.setRightSide(dataTypeBeingFound);
			}
			if (isGettingPredicateOrExpression) {
				// For use in the Cpp final code, when getting a binary expression we need the types
//				getComplementaryDataType(expression.getLeft());
//				binaryExpression.setLeftDataType(dataTypeBeingFound);
//				
//				getComplementaryDataType(expression.getRight());
//				binaryExpression.setRightDataType(dataTypeBeingFound);
//				

				expression.getLeft().accept(this);
				binaryExpression.setLeftSide(predicateExpressionBeingFound);
				
				expression.getRight().accept(this);
				binaryExpression.setRightSide(predicateExpressionBeingFound);
			}
		}
		
		if (isGettingDataType)
			dataTypeBeingFound = binaryExpressionType;
		if (isGettingPredicateOrExpression) 
			predicateExpressionBeingFound = binaryExpression;
		
	}

	@Override
	public void visitBoolExpression(BoolExpression expression) {
		// TODO Auto-generated method stub
		System.out.println("Visited BoolExpression ");
	}

	@Override
	public void visitIntegerLiteral(IntegerLiteral expression) {
		// TODO Auto-generated method stub
		System.out.println("Visited IntegerLiteral ");
		
		if (isGettingPredicateOrExpression) {
			predicateExpressionBeingFound = new ASTIntegerLiteral(expression.getValue().toString());
		}
	}

	@Override
	public void visitQuantifiedExpression(QuantifiedExpression expression) {
		// TODO Auto-generated method stub
		// This visit is for a comprehension set
		System.out.println("Visited QuantifiedExpression ");
	}

	@Override
	public void visitSetExtension(SetExtension expression) {
		// TODO Auto-generated method stub
		System.out.println("Visited SetExtension ");
		
		//This visit is for an expression like {0,1}
		//We need to extract each element of the defined set
		
		Expression[] elements = expression.getMembers();
		int numberOfElements = elements.length;
		int index = 0;
		
		if (isGettingPredicateOrExpression) {
			ASTSetExtension setExtension = new ASTSetExtension(numberOfElements);
			
			while(index < numberOfElements) {
				//We need to learn the data type that this set contains
				//Needs to be done only once
//				if (index == 0) {				
//					setExtension.setSetType(getComplementaryDataType(elements[index]));
//				}
				
				elements[index].accept(this);
				setExtension.setElement(index, predicateExpressionBeingFound);
				index += 1;
			}
			
			predicateExpressionBeingFound = setExtension;
		}
		
		if (isGettingDataType && isGettingComplementaryType) {
			ASTUnaryExpressionType setExtensionType = new ASTUnaryExpressionType("PowerSet");
			
			elements[0].accept(this);
			setExtensionType.setInternalType(dataTypeBeingFound);
			
			dataTypeBeingFound = setExtensionType;
		}
	}

	@Override
	public void visitUnaryExpression(UnaryExpression expression) {
		// TODO Auto-generated method stub
		System.out.println("Visited UnaryExpression ");
		System.out.println(expression.getTag());
		
		int expressionTag = expression.getTag();
		
		ASTUnaryExpressionType unaryExpressionType;
		ASTUnaryExpression unaryExpression = new ASTUnaryExpression();
		
		boolean noDefaultError = false;
		
		switch(expressionTag) {
		case Formula.KCARD: //751
			if (isGettingPredicateOrExpression)
				unaryExpression = new ASTUnaryExpression("Cardinality");
			break;
		case Formula.POW: //752 PowerSet
			if (isGettingDataType) {
				unaryExpressionType = new ASTUnaryExpressionType("PowerSet");
				
				expression.getChild().accept(this);
				unaryExpressionType.setInternalType(dataTypeBeingFound);
				
				
				// If a data type is a PowerSet that internally has a binary expression
				// it is a relation. 
				if (unaryExpressionType.getInternalType().getTypeName().equals("BinaryExpression")) {
					ASTBinaryExpressionType internalBinary = (ASTBinaryExpressionType) unaryExpressionType.getInternalType();
					
					ASTBinaryExpressionType relationType = new ASTBinaryExpressionType("Relation");
					relationType.setLeftSide(internalBinary.getLeftSideDataType());
					relationType.setRightSide(internalBinary.getRightSideDataType());
					
					dataTypeBeingFound = relationType;
				}
				else {
					dataTypeBeingFound = unaryExpressionType;
				}
				
			}
			
			if (isGettingPredicateOrExpression)
				unaryExpression = new ASTUnaryExpression("PowerSet");
			break;
		case Formula.POW1: //753
			if (isGettingPredicateOrExpression)
				unaryExpression = new ASTUnaryExpression("PowerSet1");
			break;
		case Formula.KDOM: //756
			if (isGettingPredicateOrExpression)
				unaryExpression = new ASTUnaryExpression("Domain");
			break;
		case Formula.KRAN: //757
			if (isGettingPredicateOrExpression)
				unaryExpression = new ASTUnaryExpression("Range");
			break;
		case Formula.KMIN: //761
			if (isGettingPredicateOrExpression)
				unaryExpression = new ASTUnaryExpression("Minimum");
			break;
		case Formula.KMAX: //762
			if (isGettingPredicateOrExpression)
				unaryExpression = new ASTUnaryExpression("Maximum");
			break;
		case Formula.CONVERSE: //763
			if (isGettingPredicateOrExpression)
				unaryExpression = new ASTUnaryExpression("Inverse");
			break;
		case Formula.UNMINUS: //764
			if (isGettingPredicateOrExpression)
				unaryExpression = new ASTUnaryExpression("UnaryMinus");
			break;
		default:
			noDefaultError = true;
			if (isGettingDataType) dataTypeBeingFound.setAsError();
		}
		
		if (!noDefaultError) {
			if (isGettingPredicateOrExpression) {
				expression.getChild().accept(this);
				unaryExpression.setInternalExpression(predicateExpressionBeingFound);
				
				predicateExpressionBeingFound = unaryExpression;
			}
		}
	}

	@Override
	public void visitBoundIdentifier(BoundIdentifier identifierExpression) {
		// TODO Auto-generated method stub
		System.out.println("Visited BoundIdentifier ");
	}

	@Override
	public void visitFreeIdentifier(FreeIdentifier identifierExpression) {
		// TODO Auto-generated method stub
		//When getting a data type, this visit means it's a Carrier Set 
		System.out.println("Visited FreeIdentifier ");
		
		String identifierName = identifierExpression.getName();
		
		// If getting data type, the identifierName is the name of the set that the constant/variable... belongs to.
		// If getting expression/predicate, identifierName is the name of the constant/variable/carrierset.
		
		if (isGettingDataType) {
			if (isGettingComplementaryType) {
				dataTypeBeingFound = CppAST.getFreeIdentifiersTypes().get(identifierName);
			}
			else {
				dataTypeBeingFound = new ASTFreeIdentifierType(identifierName);
			}
		}
		else if (isGettingPredicateOrExpression) {
			predicateExpressionBeingFound = new ASTFreeIdentifier(identifierName);
		}
		
	}

	@Override
	public void visitAssociativePredicate(AssociativePredicate predicate) {
		// TODO Auto-generated method stub
		// Logical ANDs and ORs for predicates land here
		System.out.println("Visited AssociativePredicate ");
		
		int predicateTag = predicate.getTag();
		boolean hasDefaultError = false;
		
		System.out.println("Predicate tag: " + predicateTag);
		
		ASTAssociativePredicate newAssociativePredicate = new ASTAssociativePredicate();
		
		switch(predicateTag) {
		case Formula.LAND: //351
			if (isGettingPredicateOrExpression)
				newAssociativePredicate = new ASTAssociativePredicate("And");
			break;
		case Formula.LOR: //352
			if (isGettingPredicateOrExpression)
				newAssociativePredicate = new ASTAssociativePredicate("Or");
			break;
		default:
			hasDefaultError = true;
			if (isGettingPredicateOrExpression)
				predicateBeingFound.setAsError();
		}
		
		if (!hasDefaultError) {
			for (Predicate elementInPredicate : predicate.getChildren()) {
				elementInPredicate.accept(this);
				newAssociativePredicate.addPredicate(predicateBeingFound);
			}
			
			predicateBeingFound = newAssociativePredicate;
		}
	}

	@Override
	public void visitBinaryPredicate(BinaryPredicate predicate) {
		// TODO Auto-generated method stub
		System.out.println("Visited BinaryPredicate ");
		
		int predicateTag = predicate.getTag();
		boolean hasDefaultError = false;
		
		System.out.println("Predicate tag: " + predicateTag);
		
		ASTBinaryPredicate newBinaryPredicate = new ASTBinaryPredicate();
		
		switch(predicateTag) {
		case Formula.LIMP: //251
			if (isGettingPredicateOrExpression)
				newBinaryPredicate = new ASTBinaryPredicate("LogicalImplication");
			break;
		case Formula.LEQV: //252
			if (isGettingPredicateOrExpression)
				newBinaryPredicate = new ASTBinaryPredicate("LogicalEquivalence");
			break;
		default:
			hasDefaultError = true;
			if (isGettingPredicateOrExpression)
				predicateBeingFound.setAsError();
		}
		
		if (!hasDefaultError) {
			if (isGettingPredicateOrExpression) {
				predicate.getLeft().accept(this);
				newBinaryPredicate.setLeftSide(predicateBeingFound);
				
				predicate.getRight().accept(this);
				newBinaryPredicate.setRightSide(predicateBeingFound);
				
				predicateBeingFound = newBinaryPredicate;
			}
		}
	}

	@Override
	public void visitLiteralPredicate(LiteralPredicate predicate) {
		// TODO Auto-generated method stub
		System.out.println("Visited LiteralPredicate ");
		
		int predicateTag = predicate.getTag();
		
		System.out.println("Predicate tag: " + predicateTag);
		
		switch(predicateTag) {
		case Formula.BTRUE: //610
			predicateBeingFound = new ASTPredicate("True");
			break;
		case Formula.BFALSE: //611
			predicateBeingFound = new ASTPredicate("False");
			break;
		default:
			predicateBeingFound.setAsError();
		}
	}

	@Override
	public void visitMultiplePredicate(MultiplePredicate predicate) { //PARTITION
		// TODO Auto-generated method stub
		System.out.println("Visited MultiplePredicate ");
		
		//This is visited when doing a partition in predicates
		//i.e. partition(SET,{OPEN},{CLOSE})
		
		
		ASTMultiplePredicate newMultiplePredicate = new ASTMultiplePredicate();
		
		Expression setInPredicate = predicate.getChild(0);
		setInPredicate.accept(this);
		newMultiplePredicate.setSetToCheck(predicateExpressionBeingFound);

		int childrenIndex = 1;
		Expression child;
		while (childrenIndex < predicate.getChildCount()) {
			child = predicate.getChild(childrenIndex);
			if (childrenIndex == 1)
				newMultiplePredicate.setSetDataType(getComplementaryDataType(child));
			child.accept(this);
			newMultiplePredicate.addChild(predicateExpressionBeingFound);
			
			childrenIndex += 1;
		}
		
		predicateBeingFound = newMultiplePredicate;
	}

	@Override
	public void visitQuantifiedPredicate(QuantifiedPredicate predicate) {
		// TODO Auto-generated method stub
		System.out.println("Visited QuantifiedPredicate ");
		
		int predicateTag = predicate.getTag(); // If its ForAll, Exists
		
		// BoundIdentifiers are the variables defined inside a quantifier
		// that are used for the predicates and expressions inside the qualifier
		
		int numberOfBoundIdentifiers = predicate.getBoundIdentDecls().length;
		
		int boundIdentifierIndex;
		ASTQuantifiedPredicate quantPredicate = new ASTQuantifiedPredicate();
		
		boolean predicateWasFound = true;
		
		switch (predicateTag) {
		case Formula.FORALL: //851
			if (isGettingPredicateOrExpression) {
				quantPredicate = new ASTQuantifiedPredicate("ForAll", numberOfBoundIdentifiers);
			}
			break;
		case Formula.EXISTS: //852
			if (isGettingPredicateOrExpression) {
				quantPredicate = new ASTQuantifiedPredicate("Exists", numberOfBoundIdentifiers);
			}
			break;
		default:
			predicateWasFound = false;
			quantPredicate.setAsError();
		}
		
		if (predicateWasFound) {
			boundIdentifierIndex = numberOfBoundIdentifiers - 1;
			ASTDataType identifierType;
			
			// Obtain the data types of the identifiers defined and used in the quantifier
			for (BoundIdentDecl identifier : predicate.getBoundIdentDecls()) {
				
				isGettingPredicateOrExpression = false;
				isGettingDataType = false;
				identifierType = getDataType(identifier.getType().toString());
				isGettingDataType = false;
				isGettingPredicateOrExpression = true;
				
				quantPredicate.setBoundIdentifierType(boundIdentifierIndex, identifierType);
				
				boundIdentifierIndex -= 1;
			}
			
			// Obtain the predicate of the quantifier
			predicate.getPredicate().accept(this);
			quantPredicate.setInternalPredicate(predicateBeingFound);
			
			predicateBeingFound = quantPredicate;
		}
		
	}

	@Override
	public void visitRelationalPredicate(RelationalPredicate predicate) {
		// TODO Auto-generated method stub
		System.out.println("Visited RelationalPredicate ");
		
		int predicateTag = predicate.getTag();
		System.out.println(predicateTag);
		
		//We need to know if there was no error reading the tag, in order to
		//then read the left and right side expression of the predicate
		//Doing that read when the switch doesn't pick up the predicate type
		//would throw some error
		boolean predicateWasFound = true;
		
		ASTRelationalPredicate newRelationalPredicate = new ASTRelationalPredicate();
		
		if (isGettingPredicateOrExpression) {
			switch (predicateTag) {
			case Formula.EQUAL: //101
				newRelationalPredicate = new ASTRelationalPredicate("Equal");
				break;
			case Formula.NOTEQUAL: //102
				newRelationalPredicate = new ASTRelationalPredicate("NotEqual");
				break;
			case Formula.LT: //103
				newRelationalPredicate = new ASTRelationalPredicate("LessThan");
				break;
			case Formula.LE: //104
				newRelationalPredicate = new ASTRelationalPredicate("LessEqualThan");
				break;
			case Formula.GT: //105
				newRelationalPredicate = new ASTRelationalPredicate("GreaterThan");
				break;
			case Formula.GE: //106
				newRelationalPredicate = new ASTRelationalPredicate("GreaterEqualThan");
				break;
			case Formula.IN: //107
				newRelationalPredicate = new ASTRelationalPredicate("Belongs");
				break;
			case Formula.NOTIN: //108
				newRelationalPredicate = new ASTRelationalPredicate("NotBelongs");
				break;
			case Formula.SUBSET: //109
				newRelationalPredicate = new ASTRelationalPredicate("Subset");
				break;
			case Formula.NOTSUBSET: //110
				newRelationalPredicate = new ASTRelationalPredicate("NotSubset");
				break;
			case Formula.SUBSETEQ: //111
				newRelationalPredicate = new ASTRelationalPredicate("SubsetOrEqual");
				break;
			case Formula.NOTSUBSETEQ: //112
				newRelationalPredicate = new ASTRelationalPredicate("NotSubsetOrEqual");
				break;
			default:
				predicateWasFound = false;
				//Modify the predicate type being found with an error
				predicateBeingFound.setAsError();
			}
			
			if (predicateWasFound) {
				predicate.getLeft().accept(this);
				newRelationalPredicate.setLeftSideExpression(predicateExpressionBeingFound);
				
				predicate.getRight().accept(this);
				newRelationalPredicate.setRightSideExpression(predicateExpressionBeingFound);
				
				predicateBeingFound = newRelationalPredicate;
			}
		}
	}

	@Override
	public void visitSimplePredicate(SimplePredicate predicate) {
		// TODO Auto-generated method stub
		System.out.println("Visited SimplePredicate ");
		
		int predicateTag = predicate.getTag();
		ASTSimplePredicate newSimplePredicate = new ASTSimplePredicate();
		
		switch(predicateTag) {
		case Formula.KFINITE:
			newSimplePredicate = new ASTSimplePredicate("Finite");
			
			predicate.getExpression().accept(this);
			newSimplePredicate.setInternalExpression(predicateExpressionBeingFound);
			
			predicateBeingFound = newSimplePredicate;
			break;
		default:
			predicateBeingFound.setAsError();
		}
		
	}

	@Override
	public void visitUnaryPredicate(UnaryPredicate predicate) {
		// TODO Auto-generated method stub
		System.out.println("Visited UnaryPredicate ");
		
		int predicateTag = predicate.getTag();
		ASTUnaryPredicate newUnaryPredicate = new ASTUnaryPredicate();
		
		switch(predicateTag) {
		case Formula.NOT: //701
			newUnaryPredicate = new ASTUnaryPredicate("Not");
			
			predicate.getChild().accept(this);
			newUnaryPredicate.setInternalPredicate(predicateBeingFound);
			
			predicateBeingFound = newUnaryPredicate;
			
			break;
		default:
			predicateBeingFound.setAsError();
		}
	}

	@Override
	public void visitExtendedExpression(ExtendedExpression expression) {
		// TODO Auto-generated method stub
		System.out.println("Visited ExtendedExpression ");
	}

	@Override
	public void visitExtendedPredicate(ExtendedPredicate predicate) {
		// TODO Auto-generated method stub
		System.out.println("Visited ExtendedPredicate ");
	}

	@Override
	public void visitPredicateVariable(PredicateVariable predVar) {
		// TODO Auto-generated method stub
		System.out.println("Visited PredicateVariable ");
	}

}
