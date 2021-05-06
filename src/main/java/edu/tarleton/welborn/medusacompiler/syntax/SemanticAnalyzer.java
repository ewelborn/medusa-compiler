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

package edu.tarleton.welborn.medusacompiler.syntax;

import edu.tarleton.welborn.medusacompiler.lexical.TokenType;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ethan Welborn - ethan.welborn@go.tarleton.edu
 */
public class SemanticAnalyzer {

    private ProgramTree programTree;
    
    // I never specified scope in the document. For now, I guess I'll just go with a
    // global scope for all variables: it's more simple to write, and evil for any
    // programmer that dares to use the language >:)
    private Map<String,TokenType> symbolTable;
    
    public SemanticAnalyzer(ProgramTree programTree) {
        this.programTree = programTree;
    }
    
    private TokenType getDataTypeOfConcat(ConcatTree concat) throws SemanticAnalyzerException {
        if(concat.getConcat() != null) {
            TokenType atomType = getDataTypeOfAtom(concat.getAtom());
            TokenType concatType = getDataTypeOfConcat(concat.getConcat());
            // Concatenation is only allowed between strings
            if(atomType != TokenType.STRING_VALUE || concatType != TokenType.STRING_VALUE) {
                throw new SemanticAnalyzerException("Concatenation is only allowed between strings");
            } else {
                return atomType; // They're both the same, doesn't matter which one we return
            }
        } else {
            return getDataTypeOfAtom(concat.getAtom());
        }
    }
    
    private TokenType getDataTypeOfAtom(AtomTree atom) throws SemanticAnalyzerException {
        // If we have a type conversion as our unary operator, then just return whatever
        // datatype we're converting this atom into
        if(atom.getUnaryOperator() != null) {
            switch(atom.getUnaryOperator()) {
                case UNARY_INT_CONVERSION:
                    return TokenType.INT_VALUE;
                case UNARY_FLOAT_CONVERSION:
                    return TokenType.FLOAT_VALUE;
                case UNARY_STRING_CONVERSION:
                    return TokenType.STRING_VALUE;
            }
        }
        
        // If atom is an expression, then get the datatype of that expression
        if(atom.getAtomValue().getType() == TokenType.LEFT_PARENTHESES) {
            return getDataTypeOfExpression(atom.getAtomValue().getExpression());
        }
        
        // If atom is an ident, then check if the variable exists in our symbol table,
        // if it does, then return its data type.
        if(atom.getAtomValue().getType() == TokenType.IDENT) {
            if(symbolTable.containsKey(atom.getAtomValue().getStringValue())) {
                return symbolTable.get(atom.getAtomValue().getStringValue());
            } else {
                throw new SemanticAnalyzerException("Reference made to undeclared variable "+atom.getAtomValue().getStringValue());
            }
        }
        
        // Otherwise, return the atom's inherent data type
        switch(atom.getAtomValue().getType()) {
            case INT_VALUE:
            case FLOAT_VALUE:
            case STRING_VALUE:
                return atom.getAtomValue().getType();
            default:
                throw new SemanticAnalyzerException("Could not determine data type of atom!");
        }
    }
    
    private TokenType getDataTypeOfPower(PowerTree power) throws SemanticAnalyzerException {
        if(power.getPower() != null) {
            TokenType expType = getDataTypeOfExp(power.getExp());
            TokenType powerType = getDataTypeOfPower(power.getPower());
            if(expType != powerType) {
                throw new SemanticAnalyzerException("Exp symbol with data type "+expType+" cannot operate with power symbol of data type "+powerType);
            } else {
                return expType; // They're both the same, doesn't matter which one we return
            }
        } else {
            return getDataTypeOfExp(power.getExp());
        }
    }
    
    private TokenType getDataTypeOfExp(ExpTree exp) throws SemanticAnalyzerException {
        if(exp.getConcat() != null) {
            TokenType atomType = getDataTypeOfAtom(exp.getAtom());
            TokenType concatType = getDataTypeOfConcat(exp.getConcat());
            // Concatenation is only allowed between strings
            if(atomType != TokenType.STRING_VALUE || concatType != TokenType.STRING_VALUE) {
                throw new SemanticAnalyzerException("Concatenation is only allowed between strings");
            } else {
                return atomType; // They're both the same, doesn't matter which one we return
            }
        } else {
            return getDataTypeOfAtom(exp.getAtom());
        }
    }
    
    private TokenType getDataTypeOfMultiply(MultiplyTree multiply) throws SemanticAnalyzerException {
        if(multiply.getMultiply() != null) {
            TokenType factorType = getDataTypeOfFactor(multiply.getFactor());
            TokenType multiplyType = getDataTypeOfMultiply(multiply.getMultiply());
            if(factorType != multiplyType) {
                throw new SemanticAnalyzerException("Factor symbol with data type "+factorType+" cannot operate with multiply symbol of data type "+multiplyType);
            } else {
                return factorType; // They're both the same, doesn't matter which one we return
            }
        } else {
            return getDataTypeOfFactor(multiply.getFactor());
        }
    }
    
    private TokenType getDataTypeOfFactor(FactorTree factor) throws SemanticAnalyzerException {
        if(factor.getPower() != null) {
            TokenType expType = getDataTypeOfExp(factor.getExp());
            TokenType powerType = getDataTypeOfPower(factor.getPower());
            if(expType != powerType) {
                throw new SemanticAnalyzerException("Exp symbol with data type "+expType+" cannot operate with power symbol of data type "+powerType);
            } else {
                return expType; // They're both the same, doesn't matter which one we return
            }
        } else {
            return getDataTypeOfExp(factor.getExp());
        }
    }
    
    private TokenType getDataTypeOfAdd(AddTree add) throws SemanticAnalyzerException {
        if(add.getAddTree() != null) {
            TokenType termType = getDataTypeOfTerm(add.getTermTree());
            TokenType addType = getDataTypeOfAdd(add.getAddTree());
            if(termType != addType) {
                throw new SemanticAnalyzerException("Term symbol with data type "+termType+" cannot operate with add symbol of data type "+addType);
            } else {
                return termType; // They're both the same, doesn't matter which one we return
            }
        } else {
            return getDataTypeOfTerm(add.getTermTree());
        }
    }
    
    private TokenType getDataTypeOfTerm(TermTree term) throws SemanticAnalyzerException {
        if(term.getMultiply() != null) {
            TokenType factorType = getDataTypeOfFactor(term.getFactor());
            TokenType multiplyType = getDataTypeOfMultiply(term.getMultiply());
            if(factorType != multiplyType) {
                throw new SemanticAnalyzerException("Factor symbol with data type "+factorType+" cannot operate with multiply symbol of data type "+multiplyType);
            } else {
                return factorType; // They're both the same, doesn't matter which one we return
            }
        } else {
            return getDataTypeOfFactor(term.getFactor());
        }
    }
    
    private TokenType getDataTypeOfExpression(ExpressionTree expression) throws SemanticAnalyzerException {
        if(expression.getAdd() != null) {
            TokenType termType = getDataTypeOfTerm(expression.getTerm());
            TokenType addType = getDataTypeOfAdd(expression.getAdd());
            if(termType != addType) {
                throw new SemanticAnalyzerException("Term symbol with data type "+termType+" cannot operate with add symbol of data type "+addType);
            } else {
                return termType; // They're both the same, doesn't matter which one we return
            }
        } else {
            return getDataTypeOfTerm(expression.getTerm());
        }
    }
    
    private void checkForLoopVariable(ForLoopVariableTree forLoopVariable) throws SemanticAnalyzerException {
        // forLoopVariable -> dataType ident = expression
        // forLoopVariable -> ident optionalAssignment
        
        // If the for loop variable is undeclared, check that the variable exists
        // in the symbol table. If it does, stuff the data type into the forLoopVariable tree
        // for the benefit of the compiler.
        // If it doesn't, add our new declared variable to the symbol table.
        
        if(forLoopVariable.getDataType() == null) {
            if(symbolTable.containsKey(forLoopVariable.getIdent())) {
                forLoopVariable.setDataType(symbolTable.get(forLoopVariable.getIdent()));
            } else {
                throw new SemanticAnalyzerException("Reference made to undeclared variable "+forLoopVariable.getIdent());
            }
        } else {
            symbolTable.put(forLoopVariable.getIdent(),forLoopVariable.getDataType());
        }
        
        // If there's an assignment, then check that the datatype of the variable and
        // the datatype of the expression are the same
        if(forLoopVariable.getOptionalAssignment() != null) {
            TokenType variableType = forLoopVariable.getDataType();
            TokenType expressionType = getDataTypeOfExpression(forLoopVariable.getOptionalAssignment().getExpression());
            if(variableType != expressionType) {
                throw new SemanticAnalyzerException("Variable "+forLoopVariable.getIdent()+" declared as "+variableType+" cannot be assigned to "+expressionType);
            }
        }
    }
    
    private void checkForLoop(ForLoopTree forLoop) throws SemanticAnalyzerException {
        checkForLoopVariable(forLoop.getForLoopVariable());
        checkCondition(forLoop.getCondition());
        checkAssignment(forLoop.getAssignment());
        
        // statements can be null
        if(forLoop.getStatementBlock() != null) {
            checkStatements(forLoop.getStatementBlock());
        }
    }
    
    private void checkDeclaration(DeclarationTree declaration) throws SemanticAnalyzerException {
        // Check that the variable hasn't already been declared
        if(symbolTable.containsKey(declaration.getIdent())) {
            throw new SemanticAnalyzerException("Variable "+declaration.getIdent()+" cannot be declared twice!");
        }
        
        // If we have an optional assignment, then check that the datatype
        // of the assignment is the same datatype as the declaration.
        
        if(declaration.getOptionalExpression() != null) {
            TokenType declarationType = declaration.getDataType();
            TokenType expressionType = getDataTypeOfExpression(declaration.getOptionalExpression());
            if(expressionType != declarationType) {
                throw new SemanticAnalyzerException("Variable "+declaration.getIdent()+" declared as "+declarationType+" cannot be assigned to "+expressionType);
            }
        }
        
        // Add it to the symbol table
        symbolTable.put(declaration.getIdent(),declaration.getDataType());
    }
    
    private void checkAssignment(AssignmentTree assignment) throws SemanticAnalyzerException {
        // Check that the variable referenced in the assignment exists
        if(!symbolTable.containsKey(assignment.getIdent())) {
            throw new SemanticAnalyzerException("Reference made to undeclared variable "+assignment.getIdent());
        }
        
        TokenType assignmentType = symbolTable.get(assignment.getIdent());
        TokenType expressionType = getDataTypeOfExpression(assignment.getExpression());
        if(assignmentType != expressionType) {
            throw new SemanticAnalyzerException("Variable "+assignment.getIdent()+" declared as "+assignmentType+" cannot be assigned to "+expressionType);
        }
    }
    
    private void checkCondition(ConditionTree condition) throws SemanticAnalyzerException {
        // Check that both sides of the condition are the same data type
        TokenType condition1 = getDataTypeOfExpression(condition.getExpression1());
        TokenType condition2 = getDataTypeOfExpression(condition.getExpression2());
        if(condition1 != condition2) {
            throw new SemanticAnalyzerException("Both sides of condition must be the same data type, cannot compare "+condition1+" with "+condition2);
        }
    }
    
    private void checkIfTree(IfTree ifTree) throws SemanticAnalyzerException {
        checkCondition(ifTree.getCondition());
    }
    
    private void checkStatement(StatementTree statement) throws SemanticAnalyzerException {
        if(statement instanceof AssignmentTree) {
            checkAssignment((AssignmentTree) statement);
        } else if (statement instanceof DeclarationTree) {
            checkDeclaration((DeclarationTree) statement);
        } else if (statement instanceof ForLoopTree) {
            checkForLoop((ForLoopTree) statement);
        } else if (statement instanceof IfTree) {
            checkIfTree((IfTree) statement);
        }
    }
    
    private void checkStatements(StatementsTree statements) throws SemanticAnalyzerException {
        checkStatement(statements.getStatement());
        if(statements.getStatements() != null) {
            checkStatements(statements.getStatements());
        }
    }
    
    private void checkProgramTree(ProgramTree programTree) throws SemanticAnalyzerException {
        checkStatements(programTree.getStatements());
    }
    
    public void checkSemantics() throws SemanticAnalyzerException {
        symbolTable = new HashMap<>();
        checkProgramTree(programTree);
    }

    public Map<String, TokenType> getSymbolTable() {
        return symbolTable;
    }
    
}
