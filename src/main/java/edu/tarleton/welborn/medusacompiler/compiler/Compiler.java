/*
 * The MIT License
 *
 * Copyright 2021 Ethan Welborn - ethan.welborn@go.tarleton.edu.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package edu.tarleton.welborn.medusacompiler.compiler;

import edu.tarleton.welborn.medusacompiler.lexical.TokenType;
import edu.tarleton.welborn.medusacompiler.syntax.AddTree;
import edu.tarleton.welborn.medusacompiler.syntax.AssignmentTree;
import edu.tarleton.welborn.medusacompiler.syntax.AtomTree;
import edu.tarleton.welborn.medusacompiler.syntax.ConcatTree;
import edu.tarleton.welborn.medusacompiler.syntax.ConditionTree;
import edu.tarleton.welborn.medusacompiler.syntax.DeclarationTree;
import edu.tarleton.welborn.medusacompiler.syntax.ExpTree;
import edu.tarleton.welborn.medusacompiler.syntax.ExpressionTree;
import edu.tarleton.welborn.medusacompiler.syntax.FactorTree;
import edu.tarleton.welborn.medusacompiler.syntax.ForLoopTree;
import edu.tarleton.welborn.medusacompiler.syntax.IfTree;
import edu.tarleton.welborn.medusacompiler.syntax.MultiplyTree;
import edu.tarleton.welborn.medusacompiler.syntax.PowerTree;
import edu.tarleton.welborn.medusacompiler.syntax.ProgramTree;
import edu.tarleton.welborn.medusacompiler.syntax.StatementTree;
import edu.tarleton.welborn.medusacompiler.syntax.StatementsTree;
import edu.tarleton.welborn.medusacompiler.syntax.TermTree;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ethan Welborn - ethan.welborn@go.tarleton.edu
 */
public class Compiler {

    //private void generateCodeFromIfTree(IfTree assignmentTree,List<ThreeAddressCode> TACResults) {
    //    
    //}
    
    private static String generateCodeFromConcatTreeAndReturnVariable(ConcatTree concatTree,CompilerContext context,String atomTreeIdentifier1) {
        // My thoughts for this function:
        // Take in two character arrays as pointers, atomTreeIdentifier1 and atomTreeIdentifier2,
        // initialize a character array, then generate a for loop (either manually or by creating a
        // pseudo AST and passing it through one of the functions in this class) to fill the new
        // array with the values in the left and righthand arrays.
        
        // Or maybe not, I don't know.
        
        String atomTreeIndentifier2 = generateCodeFromAtomTreeAndReturnVariable(concatTree.getAtom(),context);
        String identifier = context.getNextTempVariable();
        
        ThreeAddressCodeOperator op = ThreeAddressCodeOperator.CONCAT;
                
        context.getTACResults().add(new ThreeAddressCode(
                identifier,
                op,
                atomTreeIdentifier1,
                atomTreeIndentifier2
        ));
        
        if(concatTree.getConcat() != null) {
            return generateCodeFromConcatTreeAndReturnVariable(concatTree.getConcat(),context,identifier);
        } else {
            return identifier;
        }
    }
    
    private static String generateCodeFromAtomTreeAndReturnVariable(AtomTree atomTree,CompilerContext context) {
        String identifier = null;
        if(atomTree.getAtomValue().getType() == TokenType.IDENT) {
            identifier = atomTree.getAtomValue().getStringValue();
        } else {
            // _t
            identifier = context.getNextTempVariable();
            switch(atomTree.getAtomValue().getType()) {
                case INT_VALUE:
                    // _t = int
                    context.getTACResults().add(new ThreeAddressCode(
                            identifier,
                            ThreeAddressCodeOperator.ASSIGN,
                            String.valueOf(atomTree.getAtomValue().getIntValue())
                    ));
                    break;
                case FLOAT_VALUE:
                    // _t = float
                    context.getTACResults().add(new ThreeAddressCode(
                            identifier,
                            ThreeAddressCodeOperator.ASSIGN,
                            String.valueOf(atomTree.getAtomValue().getFloatValue())
                    ));
                    break;
                case STRING_VALUE:
                    // _t = string
                    context.getTACResults().add(new ThreeAddressCode(
                            identifier,
                            ThreeAddressCodeOperator.ASSIGN,
                            '"'+atomTree.getAtomValue().getStringValue()+'"'
                    ));
                    break;
                case LEFT_PARENTHESES:
                    // _t = ( expr )
                    String expressionIdentifier = generateCodeFromExpressionTreeAndReturnVariable(atomTree.getAtomValue().getExpression(),context);
                    context.getTACResults().add(new ThreeAddressCode(
                            identifier,
                            ThreeAddressCodeOperator.ASSIGN,
                            expressionIdentifier
                    ));
            }
        }
        
        // Leave all unary operations for the end of calculations
        if(atomTree.getUnaryOperator() != null) {
            String newIdentifier = context.getNextTempVariable();
            ThreeAddressCodeOperator op = null;
            
            TokenType convertToValue = atomTree.getUnaryOperator();
            TokenType convertFromValue = atomTree.getAtomValue().getType();
            if(convertFromValue == TokenType.IDENT) {
                convertFromValue = context.getSymbolTable().get(atomTree.getAtomValue().getStringValue());
            }
            
            switch(convertToValue) {
                case UNARY_INT_CONVERSION:
                    if(convertFromValue == TokenType.FLOAT_VALUE) {
                        op = ThreeAddressCodeOperator.CONVERT_FLOAT_TO_INT;
                    } else if(convertFromValue == TokenType.STRING_VALUE) {
                        op = ThreeAddressCodeOperator.CONVERT_STRING_TO_INT;
                    } else {
                        return identifier; // No conversion necessary
                    }
                    break;
                case UNARY_FLOAT_CONVERSION:
                    if(convertFromValue == TokenType.INT_VALUE) {
                        op = ThreeAddressCodeOperator.CONVERT_INT_TO_FLOAT;
                    } else if(convertFromValue == TokenType.STRING_VALUE) {
                        op = ThreeAddressCodeOperator.CONVERT_STRING_TO_FLOAT;
                    } else {
                        return identifier; // No conversion necessary
                    }
                    break;
                case UNARY_STRING_CONVERSION:
                    if(convertFromValue == TokenType.INT_VALUE) {
                        op = ThreeAddressCodeOperator.CONVERT_INT_TO_STRING;
                    } else if(convertFromValue == TokenType.FLOAT_VALUE) {
                        op = ThreeAddressCodeOperator.CONVERT_FLOAT_TO_STRING;
                    } else {
                        return identifier; // No conversion necessary
                    }
                    break;
            }
            
            context.getTACResults().add(new ThreeAddressCode(
                    newIdentifier,
                    op,
                    identifier
            ));
            
            return newIdentifier;
        } else {
            return identifier;
        }
    }
    
    private static String generateCodeFromPowerTreeAndReturnVariable(PowerTree powerTree,CompilerContext context) {
        // See comment in generateCodeFromFactorTreeAndReturnVariable
        
        if(powerTree.getPower() == null) {
            return generateCodeFromExpTreeAndReturnVariable(powerTree.getExp(),context);
        } else {
            // _t1
            String powerTreeIdentifier = generateCodeFromPowerTreeAndReturnVariable(powerTree.getPower(),context);
            // 3
            String expTreeIdentifier = generateCodeFromExpTreeAndReturnVariable(powerTree.getExp(),context);
            // 3 ^ _t1
            String powerTreeIdentifier2 = context.getNextTempVariable();
            context.getTACResults().add(new ThreeAddressCode(
                    powerTreeIdentifier2,
                    ThreeAddressCodeOperator.POW,
                    expTreeIdentifier,
                    powerTreeIdentifier
            ));
            return powerTreeIdentifier2;
        }
    }
    
    private static String generateCodeFromExpTreeAndReturnVariable(ExpTree expTree,CompilerContext context) {
        String atomTreeIdentifier = generateCodeFromAtomTreeAndReturnVariable(expTree.getAtom(),context);
        if(expTree.getConcat() != null) {
            return generateCodeFromConcatTreeAndReturnVariable(expTree.getConcat(),context,atomTreeIdentifier);
        } else {
            return atomTreeIdentifier;
        }
    }
    
    private static String generateCodeFromMultiplyTreeAndReturnVariable(MultiplyTree multiplyTree,CompilerContext context,String factorTreeIndentifier1) {
        String factorTreeIndentifier2 = generateCodeFromFactorTreeAndReturnVariable(multiplyTree.getFactor(),context);
        String identifier = context.getNextTempVariable();
        
        ThreeAddressCodeOperator op = null;
        switch(multiplyTree.getOperator()) {
            case MULTIPLY:
                op = ThreeAddressCodeOperator.MUL;
                break;
            case DIVIDE:
                op = ThreeAddressCodeOperator.DIV;
                break;
            case MODULO:
                op = ThreeAddressCodeOperator.MOD;
                break;
        }
                
        context.getTACResults().add(new ThreeAddressCode(
                identifier,
                op,
                factorTreeIndentifier1,
                factorTreeIndentifier2
        ));
        if(multiplyTree.getMultiply() != null) {
            return generateCodeFromMultiplyTreeAndReturnVariable(multiplyTree.getMultiply(),context,identifier);
        } else {
            return identifier;
        }
    }
    
    private static String generateCodeFromFactorTreeAndReturnVariable(FactorTree factorTree,CompilerContext context) {
        // This is a right associative operator, so we're doing something a little different.
        // If the power tree exists, then calculate it first and get a variable result, then pass
        // that to the exponent tree. Doing so will force the compiler to evaluate the rightmost exponent first,
        // then pass the result of that to the left exponent, and so on until the expression is complete.
        // E.g.
        // 2 ^ 3 ^ 5
        // 2^3 has a power tree, so we skip there
        // 3^5 has a power tree, so we skip there
        // 5 has no power tree, so we evaluate it and return it as _t1
        // 3^_t1 is evaluated and returned as _t2
        // 2^_t2 is evaluated and returned as _t3
        
        if(factorTree.getPower() == null) {
            return generateCodeFromExpTreeAndReturnVariable(factorTree.getExp(),context);
        } else {
            // _t1
            String powerTreeIdentifier = generateCodeFromPowerTreeAndReturnVariable(factorTree.getPower(),context);
            // 3
            String expTreeIdentifier = generateCodeFromExpTreeAndReturnVariable(factorTree.getExp(),context);
            // 3 ^ _t1
            String factorTreeIdentifier = context.getNextTempVariable();
            context.getTACResults().add(new ThreeAddressCode(
                    factorTreeIdentifier,
                    ThreeAddressCodeOperator.POW,
                    expTreeIdentifier,
                    powerTreeIdentifier
            ));
            return factorTreeIdentifier;
        }
    }
    
    private static String generateCodeFromAddTreeAndReturnVariable(AddTree addTree,CompilerContext context,String termTreeIdentifier1) {
        String termTreeIdentifier2 = generateCodeFromTermTreeAndReturnVariable(addTree.getTermTree(),context);
        String identifier = context.getNextTempVariable();
        context.getTACResults().add(new ThreeAddressCode(
                identifier,
                addTree.getOperator() == TokenType.PLUS ? ThreeAddressCodeOperator.ADD : ThreeAddressCodeOperator.SUB,
                termTreeIdentifier1,
                termTreeIdentifier2
        ));
        if(addTree.getAddTree() != null) {
            return generateCodeFromAddTreeAndReturnVariable(addTree.getAddTree(),context,identifier);
        } else {
            return identifier;
        }
    }
    
    private static String generateCodeFromTermTreeAndReturnVariable(TermTree termTree,CompilerContext context) {
        String factorTreeIdentifier = generateCodeFromFactorTreeAndReturnVariable(termTree.getFactor(),context);
        if(termTree.getMultiply() != null) {
            return generateCodeFromMultiplyTreeAndReturnVariable(termTree.getMultiply(),context,factorTreeIdentifier);
        } else {
            return factorTreeIdentifier;
        }
    }
    
    private static String generateCodeFromExpressionTreeAndReturnVariable(ExpressionTree expressionTree,CompilerContext context) {
        String termTreeIdentifier = generateCodeFromTermTreeAndReturnVariable(expressionTree.getTerm(),context);
        if(expressionTree.getAdd() != null) {
            return generateCodeFromAddTreeAndReturnVariable(expressionTree.getAdd(),context,termTreeIdentifier);
        } else {
            return termTreeIdentifier;
        }
    }
    
    private static String generateCodeFromConditionTreeAndReturnVariable(ConditionTree conditionTree,CompilerContext context) {
        String expressionIdent1 = generateCodeFromExpressionTreeAndReturnVariable(conditionTree.getExpression1(),context);
        String expressionIdent2 = generateCodeFromExpressionTreeAndReturnVariable(conditionTree.getExpression2(),context);
        String resultIdent = context.getNextTempVariable();
        
        ThreeAddressCodeOperator op = null;
        switch(conditionTree.getConditionalOperator()) {
            case LESS_THAN:
                op = ThreeAddressCodeOperator.IS_LESS_THAN;
                break;
            case GREATER_THAN:
                op = ThreeAddressCodeOperator.IS_GREATER_THAN;
                break;
            case LESS_THAN_OR_EQUAL:
                op = ThreeAddressCodeOperator.IS_LESS_THAN_OR_EQUAL;
                break;
            case GREATER_THAN_OR_EQUAL:
                op = ThreeAddressCodeOperator.IS_GREATER_THAN_OR_EQUAL;
                break;
            case EQUAL:
                op = ThreeAddressCodeOperator.IS_EQUAL;
                break;
            case NOT_EQUAL:
                op = ThreeAddressCodeOperator.IS_NOT_EQUAL;
                break;
        }
        
        context.getTACResults().add(new ThreeAddressCode(
                resultIdent,
                op,
                expressionIdent1,
                expressionIdent2
        ));
        
        return resultIdent;
    }
    
    private static void generateCodeFromForLoopTree(ForLoopTree forLoopTree,CompilerContext context) {
        // If we need to do any assignment to our for loop variable, then do it now
        if(forLoopTree.getForLoopVariable().getOptionalAssignment() != null) {
            String expressionIdent = generateCodeFromExpressionTreeAndReturnVariable(forLoopTree.getForLoopVariable().getOptionalAssignment().getExpression(),context);
            
            context.getTACResults().add(new ThreeAddressCode(
                    forLoopTree.getForLoopVariable().getIdent(),
                    ThreeAddressCodeOperator.ASSIGN,
                    expressionIdent
            ));
        }
        
        String forLoopBeginLabel = context.getNextTempVariable("label");
        String forLoopEndLabel = context.getNextTempVariable("label");
        
        // Check the condition, if it's false, skip the loop
        String conditionIdent = generateCodeFromConditionTreeAndReturnVariable(forLoopTree.getCondition(),context);
        context.getTACResults().add(new ThreeAddressCode(
                forLoopEndLabel,
                ThreeAddressCodeOperator.JMP_IF_FALSE,
                conditionIdent
        ));
        
        context.getTACResults().add(new ThreeAddressCode(
                forLoopBeginLabel,
                ThreeAddressCodeOperator.LABEL
        ));
        
        // Generate the body of the loop (if it's not epsilon)
        if(forLoopTree.getStatementBlock() != null) {
            generateCodeFromStatementsTree(forLoopTree.getStatementBlock(),context);
        }
        
        // Run the assignment
        generateCodeFromAssignmentTree(forLoopTree.getAssignment(),context);
        
        // Check the condition, if it's true, loop again
        String conditionIdent2 = generateCodeFromConditionTreeAndReturnVariable(forLoopTree.getCondition(),context);
        context.getTACResults().add(new ThreeAddressCode(
                forLoopBeginLabel,
                ThreeAddressCodeOperator.JMP_IF_TRUE,
                conditionIdent2
        ));
        
        context.getTACResults().add(new ThreeAddressCode(
                forLoopEndLabel,
                ThreeAddressCodeOperator.LABEL
        ));
    }
    
    private static void generateCodeFromDeclarationTree(DeclarationTree declarationTree,CompilerContext context) {
        // Generate code for declarations only if there's an optional expression.
        if(declarationTree.getOptionalExpression() != null) {
            // Generate the code for the expression, get the variable pointing to it, then
            // assign that value to the identifier
            String expressionIdent = generateCodeFromExpressionTreeAndReturnVariable(declarationTree.getOptionalExpression(),context);
            
            context.getTACResults().add(new ThreeAddressCode(
                    declarationTree.getIdent(),
                    ThreeAddressCodeOperator.ASSIGN,
                    expressionIdent
            ));
        }
    }
    
    private static void generateCodeFromAssignmentTree(AssignmentTree assignmentTree,CompilerContext context) {
        String expressionIdent = generateCodeFromExpressionTreeAndReturnVariable(assignmentTree.getExpression(),context);
            
        context.getTACResults().add(new ThreeAddressCode(
                assignmentTree.getIdent(),
                ThreeAddressCodeOperator.ASSIGN,
                expressionIdent
        ));
    }
    
    private static void generateCodeFromIfTree(IfTree ifTree,CompilerContext context) {
        if(ifTree.getElseStatement() != null) {
            // ifElse
            String ifStatementElseLabel = context.getNextTempVariable("label");
            String ifStatementEndLabel = context.getNextTempVariable("label");
        
            // Check the condition, if it's false, skip to else
            String conditionIdent = generateCodeFromConditionTreeAndReturnVariable(ifTree.getCondition(),context);
            context.getTACResults().add(new ThreeAddressCode(
                    ifStatementElseLabel,
                    ThreeAddressCodeOperator.JMP_IF_FALSE,
                    conditionIdent
            ));

            // Generate the body of the if statement (if it's not epsilon)
            if(ifTree.getStatements() != null) {
                generateCodeFromStatementsTree(ifTree.getStatements(),context);
                // Generate a jump so that we don't run the else condition statements
                context.getTACResults().add(new ThreeAddressCode(
                        ifStatementEndLabel,
                        ThreeAddressCodeOperator.JMP,
                        conditionIdent
                ));
            }
            
            // Generate the body of the else statement (if it's not epsilon)
            context.getTACResults().add(new ThreeAddressCode(
                    ifStatementElseLabel,
                    ThreeAddressCodeOperator.LABEL
            ));
            if(ifTree.getElseStatement().getStatementBlock() != null) {
                generateCodeFromStatementsTree(ifTree.getElseStatement().getStatementBlock(),context);
            }

            context.getTACResults().add(new ThreeAddressCode(
                    ifStatementEndLabel,
                    ThreeAddressCodeOperator.LABEL
            ));
        } else {
            // if
            String ifStatementEndLabel = context.getNextTempVariable("label");
        
            // Check the condition, if it's false, skip the statement
            String conditionIdent = generateCodeFromConditionTreeAndReturnVariable(ifTree.getCondition(),context);
            context.getTACResults().add(new ThreeAddressCode(
                    ifStatementEndLabel,
                    ThreeAddressCodeOperator.JMP_IF_FALSE,
                    conditionIdent
            ));

            // Generate the body of the if statement (if it's not epsilon)
            if(ifTree.getStatements() != null) {
                generateCodeFromStatementsTree(ifTree.getStatements(),context);
            }

            context.getTACResults().add(new ThreeAddressCode(
                    ifStatementEndLabel,
                    ThreeAddressCodeOperator.LABEL
            ));
        }
    }
    
    private static void generateCodeFromStatementTree(StatementTree statementTree,CompilerContext context) {
        // I know, I know, this is bad software engineering. It's OKAY, maybe, I'm not sure. I'll refactor later
        // if I have the time.
        if(statementTree instanceof AssignmentTree) {
            generateCodeFromAssignmentTree((AssignmentTree) statementTree,context);
        } else if (statementTree instanceof DeclarationTree) {
            generateCodeFromDeclarationTree((DeclarationTree) statementTree,context);
        } else if (statementTree instanceof ForLoopTree) {
            generateCodeFromForLoopTree((ForLoopTree) statementTree,context);
        } else if (statementTree instanceof IfTree) {
            generateCodeFromIfTree((IfTree) statementTree,context);
        }
    }
    
    private static void generateCodeFromStatementsTree(StatementsTree statementsTree,CompilerContext context) {
        generateCodeFromStatementTree(statementsTree.getStatement(),context);
        if(statementsTree.getStatements() != null) {
            generateCodeFromStatementsTree(statementsTree.getStatements(),context);
        }
    }
    
    public static List<ThreeAddressCode> generateCodeFromProgramTree(ProgramTree programTree) {
        CompilerContext context = new CompilerContext();
        context.setSymbolTable(programTree.getSymbolTable());
        
        generateCodeFromStatementsTree(programTree.getStatements(),context);
        
        return context.getTACResults();
    }
}
