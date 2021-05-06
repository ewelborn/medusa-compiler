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

import edu.tarleton.welborn.medusacompiler.lexical.Token;
import edu.tarleton.welborn.medusacompiler.lexical.TokenType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ethan Welborn - ethan.welborn@go.tarleton.edu
 */
public class SyntaxAnalyzer {
    
    private List<Token> tokenList;
    private Token currentToken;
    
    private void consumeToken() throws SyntaxAnalyzerException {
        if(tokenList.isEmpty()) {
            throw new SyntaxAnalyzerException("Token expected, found EOF");
        }
        tokenList.remove(0);
        if(tokenList.isEmpty() == false) {
            currentToken = tokenList.get(0);
        }
    }
    
    private void consumeToken(TokenType tokenType) throws SyntaxAnalyzerException {
        if(currentToken.getTokenType() == tokenType) {
            consumeToken();
        } else {
            throw new SyntaxAnalyzerException(tokenType + " expected, found " + currentToken.getTokenType() + " at line " + currentToken.getLine() + ", column " + currentToken.getColumn());
        }
    }
    
    // program -> statements
    private ProgramTree programTree() throws SyntaxAnalyzerException {
        StatementsTree statements = statementsTree();
        
        return new ProgramTree(statements);
    }
    
    // statements -> statement statements
    // statements -> epsilon
    private StatementsTree statementsTree() throws SyntaxAnalyzerException {
        // If the next token is FIRST of statement, then the production is statements -> statement statements
        // otherwise, the production is statements -> epsilon
        switch(currentToken.getTokenType()) {
            // statement -> declaration ;
            case INT_DECLARATION:
            case FLOAT_DECLARATION:
            case STRING_DECLARATION:
            // statement -> assignment ;
            case IDENT:
            // statement -> ifStatement ;
            case IF:
            // statement -> forLoop ;
            case FOR:
                break;
            default:
                return null;
        }
        StatementTree statement = statementTree();
        StatementsTree statements = statementsTree();
        
        return new StatementsTree(statement,statements);
    }
    
    // statement -> declaration ;
    // statement -> assignment ;
    // statement -> ifStatement ;
    // statement -> forLoop ;
    private StatementTree statementTree() throws SyntaxAnalyzerException {
        StatementTree statement = null;
        switch(currentToken.getTokenType()) {
            // statement -> declaration ;
            case INT_DECLARATION:
            case FLOAT_DECLARATION:
            case STRING_DECLARATION:
                statement = declarationTree();
                consumeToken(TokenType.SEMI_COLON);
                return statement;
                
            // statement -> assignment ;
            case IDENT:
                statement = assignmentTree();
                consumeToken(TokenType.SEMI_COLON);
                return statement;
                
            // statement -> ifStatement
            case IF:
                return ifTree();
                
            // statement -> forLoop
            case FOR:
                return forLoopTree();
                
            default:
                throw new SyntaxAnalyzerException("Statement expected, found " + currentToken.getTokenType() + " at line " + currentToken.getLine() + ", column " + currentToken.getColumn());
        }
    }
    
    // declaration -> dataType ident optionalAssignment ;
    private DeclarationTree declarationTree() throws SyntaxAnalyzerException {
        TokenType dataType = null;
        switch(currentToken.getTokenType()) {
            case INT_DECLARATION:
                dataType = TokenType.INT_VALUE;
                break;
            case FLOAT_DECLARATION:
                dataType = TokenType.FLOAT_VALUE;
                break;
            case STRING_DECLARATION:
                dataType = TokenType.STRING_VALUE;
                break;
            default:
                throw new SyntaxAnalyzerException("DataType expected, found " + currentToken.getTokenType() + " at line " + currentToken.getLine() + ", column " + currentToken.getColumn());
        }
        consumeToken();
        String ident = currentToken.getStringValue();
        consumeToken(TokenType.IDENT);
        if(currentToken.getTokenType() == TokenType.ASSIGNMENT) {
            consumeToken();
            ExpressionTree expression = expressionTree();
            return new DeclarationTree(dataType,ident,expression);
        } else {
            return new DeclarationTree(dataType,ident);
        }
    }
    
    // assignment -> ident = expression
    private AssignmentTree assignmentTree() throws SyntaxAnalyzerException {
        String ident = currentToken.getStringValue();
        consumeToken(TokenType.IDENT);
        consumeToken(TokenType.ASSIGNMENT);
        ExpressionTree expression = expressionTree();
        return new AssignmentTree(ident,expression);
    }
    
    private ElseStatementOrNullTree elseStatementOrNullTree() throws SyntaxAnalyzerException {
        if(currentToken.getTokenType() == TokenType.ELSE) {
            consumeToken();
            consumeToken(TokenType.LEFT_CURLY_BRACKET);
        
            StatementsTree statements = statementsTree();

            consumeToken(TokenType.RIGHT_CURLY_BRACKET);
            
            return new ElseStatementOrNullTree(statements);
        } else {
            return null;
        }
    }
    
    private IfTree ifTree() throws SyntaxAnalyzerException {
        consumeToken(TokenType.IF);
        consumeToken(TokenType.LEFT_PARENTHESES);
        
        ConditionTree condition = conditionTree();
        
        consumeToken(TokenType.RIGHT_PARENTHESES);
        consumeToken(TokenType.LEFT_CURLY_BRACKET);
        
        StatementsTree statements = statementsTree();
        
        consumeToken(TokenType.RIGHT_CURLY_BRACKET);
        
        ElseStatementOrNullTree elseStatement = elseStatementOrNullTree();
        
        return new IfTree(condition,statements,elseStatement);
    }
    
    // forLoop -> for ( forLoopVariable ; condition ; assignment ) { statements }
    private ForLoopTree forLoopTree() throws SyntaxAnalyzerException {
        consumeToken(TokenType.FOR);
        consumeToken(TokenType.LEFT_PARENTHESES);
        
        ForLoopVariableTree forLoopVariable = forLoopVariableTree();
        
        consumeToken(TokenType.SEMI_COLON);
        
        ConditionTree condition = conditionTree();
        
        consumeToken(TokenType.SEMI_COLON);
        
        AssignmentTree assignment = assignmentTree();
        
        consumeToken(TokenType.RIGHT_PARENTHESES);
        consumeToken(TokenType.LEFT_CURLY_BRACKET);
        
        StatementsTree statements = statementsTree();
        
        consumeToken(TokenType.RIGHT_CURLY_BRACKET);
        
        return new ForLoopTree(forLoopVariable,condition,assignment,statements);
    }
    
    // forLoopVariable -> dataType ident = expression
    // forLoopVariable -> ident optionalAssignment
    private ForLoopVariableTree forLoopVariableTree() throws SyntaxAnalyzerException {
        if(currentToken.getTokenType() == TokenType.INT_DECLARATION || currentToken.getTokenType() == TokenType.FLOAT_DECLARATION || currentToken.getTokenType() == TokenType.STRING_DECLARATION) {
            TokenType dataType = null;
            switch(currentToken.getTokenType()) {
                case INT_DECLARATION:
                    dataType = TokenType.INT_VALUE;
                    break;
                case FLOAT_DECLARATION:
                    dataType = TokenType.FLOAT_VALUE;
                    break;
                case STRING_DECLARATION:
                    dataType = TokenType.STRING_VALUE;
                    break;
            }
            consumeToken();
            String ident = currentToken.getStringValue();
            consumeToken(TokenType.IDENT);
            consumeToken(TokenType.ASSIGNMENT);
            ExpressionTree expression = expressionTree();
            return new ForLoopVariableTree(dataType,ident,expression);
        } else if(currentToken.getTokenType() == TokenType.IDENT) {
            String ident = currentToken.getStringValue();
            consumeToken();
            OptionalAssignmentTree optionalAssignment = optionalAssignmentTree();
            return new ForLoopVariableTree(ident,optionalAssignment);
        } else {
            throw new SyntaxAnalyzerException("Ident or DataType expected, found " + currentToken.getTokenType() + " at line " + currentToken.getLine() + ", column " + currentToken.getColumn());
        }
    }
    
    // optionalAssignment -> = expression
    // optionalAssignment -> epsilon
    private OptionalAssignmentTree optionalAssignmentTree() throws SyntaxAnalyzerException {
        if(currentToken.getTokenType() == TokenType.ASSIGNMENT) {
            consumeToken();
            ExpressionTree expression = expressionTree();
            return new OptionalAssignmentTree(expression);
        } else {
            return null;
        }
    }
    
    // condition -> expression conditionalOperator expression
    private ConditionTree conditionTree() throws SyntaxAnalyzerException {
        ExpressionTree expression1 = expressionTree();
        switch(currentToken.getTokenType()) {
            case LESS_THAN:
            case GREATER_THAN:
            case LESS_THAN_OR_EQUAL:
            case GREATER_THAN_OR_EQUAL:
            case EQUAL:
            case NOT_EQUAL:
                break;
            default:
                throw new SyntaxAnalyzerException("Conditional operator expected, found " + currentToken.getTokenType() + " at line " + currentToken.getLine() + ", column " + currentToken.getColumn());
        }
        TokenType conditionalOperator = currentToken.getTokenType();
        consumeToken();
        ExpressionTree expression2 = expressionTree();
        return new ConditionTree(expression1,conditionalOperator,expression2);
    }
    
    // expression -> term add
    private ExpressionTree expressionTree() throws SyntaxAnalyzerException {
        TermTree term = termTree();
        AddTree add = addTree();
        return new ExpressionTree(term,add);
    }
    
    // term -> factor multiply
    private TermTree termTree() throws SyntaxAnalyzerException {
        FactorTree factorTree = factorTree();
        MultiplyTree multiplyTree = multiplyTree();
        return new TermTree(factorTree,multiplyTree);
    }
    
    // add -> + term add
    // add -> - term add
    // add -> epsilon
    private AddTree addTree() throws SyntaxAnalyzerException {
        if(currentToken.getTokenType() == TokenType.PLUS || currentToken.getTokenType() == TokenType.MINUS) {
            TokenType operator = currentToken.getTokenType();
            consumeToken();
            TermTree termTree = termTree();
            AddTree addTree = addTree();
            return new AddTree(operator,termTree,addTree);
        } else {
            return null;
        }
    }
    
    // factor -> exp power
    private FactorTree factorTree() throws SyntaxAnalyzerException {
        ExpTree expTree = expTree();
        PowerTree powerTree = powerTree();
        return new FactorTree(expTree,powerTree);
    }
    
    // power -> ^ exp power
    // power -> epsilon
    private PowerTree powerTree() throws SyntaxAnalyzerException {
        if(currentToken.getTokenType() == TokenType.POWER) {
            consumeToken();
            ExpTree expTree = expTree();
            PowerTree powerTree = powerTree();
            return new PowerTree(expTree,powerTree);
        } else {
            return null;
        }
    }
    
    // multiply -> * factor multiply
    // multiply -> / factor multiply
    // multiply -> % factor multiply
    // multiply -> epsilon
    private MultiplyTree multiplyTree() throws SyntaxAnalyzerException {
        if(currentToken.getTokenType() == TokenType.MULTIPLY || currentToken.getTokenType() == TokenType.DIVIDE || currentToken.getTokenType() == TokenType.MODULO) {
            TokenType operator = currentToken.getTokenType();
            consumeToken();
            FactorTree factorTree = factorTree();
            MultiplyTree multiplyTree = multiplyTree();
            return new MultiplyTree(operator,factorTree,multiplyTree);
        } else {
            return null;
        }
    }
    
    // exp -> atom concat
    private ExpTree expTree() throws SyntaxAnalyzerException {
        AtomTree atomTree = atomTree();
        ConcatTree concatTree = concatTree();
        return new ExpTree(atomTree,concatTree);
    }
    
    // concat -> .. atom concat
    // concat -> epsilon
    private ConcatTree concatTree() throws SyntaxAnalyzerException {
        if(currentToken.getTokenType() == TokenType.CONCATENATE) {
            consumeToken();
            AtomTree atomTree = atomTree();
            ConcatTree concatTree = concatTree();
            return new ConcatTree(atomTree,concatTree);
        } else {
            return null;
        }
    }
    
    // atom -> unaryOperator atomValue
    // atom -> atomValue
    private AtomTree atomTree() throws SyntaxAnalyzerException {
        TokenType operator = null;
        if(currentToken.getTokenType() == TokenType.UNARY_FLOAT_CONVERSION || currentToken.getTokenType() == TokenType.UNARY_INT_CONVERSION || currentToken.getTokenType() == TokenType.UNARY_STRING_CONVERSION) {
            operator = currentToken.getTokenType();
            consumeToken();
        }
        
        AtomValue atomValue;
        switch(currentToken.getTokenType()) {
            case IDENT:
                atomValue = new AtomValue(currentToken.getTokenType(),currentToken.getStringValue(),true);
                consumeToken();
                break;
            case INT_VALUE:
                atomValue = new AtomValue(currentToken.getTokenType(),currentToken.getIntValue());
                consumeToken();
                break;
            case FLOAT_VALUE:
                atomValue = new AtomValue(currentToken.getTokenType(),currentToken.getFloatValue());
                consumeToken();
                break;
            case STRING_VALUE:
                atomValue = new AtomValue(currentToken.getTokenType(),currentToken.getStringValue());
                consumeToken();
                break;
            case LEFT_PARENTHESES:
                consumeToken();
                atomValue = new AtomValue(TokenType.LEFT_PARENTHESES,expressionTree());
                consumeToken(TokenType.RIGHT_PARENTHESES);
                break;
            default:
                throw new SyntaxAnalyzerException("AtomValue expected, found " + currentToken.getTokenType() + " at line " + currentToken.getLine() + ", column " + currentToken.getColumn());
        }
        
        if(operator != null) {
            return new AtomTree(operator,atomValue);
        } else {
            return new AtomTree(atomValue);
        }
    }
    
    // program -> statements
    // This function will generate the AST of the entire program
    // This function will also call a semantic analyzer to check the tree and
    // ensure that everything is semantically correct and to fill in any semantic
    // details that the AST may not already have.
    
    // For example, in a for loop, the variable used for iterating may be an already
    // declared variable, in which case, the programmer doesn't give the datatype there.
    // For further semantic analysis and code generation, we need to know the datatype,
    // so the semantic analyzer will check if the declared variable exists, and if it
    // does, then it will give the datatype to the AST.
    public ProgramTree getProgramTree() throws SyntaxAnalyzerException, SemanticAnalyzerException {
        ProgramTree programTree = programTree();
        if(tokenList.isEmpty() == false) {
            throw new SyntaxAnalyzerException("EOF expected, found " + currentToken.getTokenType() + " at line " + currentToken.getLine() + ", column " + currentToken.getColumn());
        }
        
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(programTree);
        semanticAnalyzer.checkSemantics();
        programTree.setSymbolTable(semanticAnalyzer.getSymbolTable());
        
        return programTree;
    }

    public SyntaxAnalyzer(List<Token> tokenList) {
        this.tokenList = tokenList;
        this.currentToken = tokenList.get(0);
    }
    
}
