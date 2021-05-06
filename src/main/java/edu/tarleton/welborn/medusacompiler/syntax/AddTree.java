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

/**
 *
 * @author Ethan Welborn - ethan.welborn@go.tarleton.edu
 */
public class AddTree extends AbstractSyntaxTree {

    private TokenType operator;
    private TermTree termTree;
    private AddTree addTree;

    public AddTree(TokenType operator, TermTree termTree, AddTree addTree) {
        this.operator = operator;
        this.termTree = termTree;
        this.addTree = addTree;
    }

    public TokenType getOperator() {
        return operator;
    }

    public TermTree getTermTree() {
        return termTree;
    }

    public AddTree getAddTree() {
        return addTree;
    }

    /*@Override
    public ExpressionValue visitNode() throws SemanticAnalyzerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ExpressionValue operate(ExpressionValue left, ExpressionValue right) {
        if(operator == TokenType.PLUS) {
            return left.plus(right);
        } else if(operator == TokenType.MINUS) {
            return left.minus(right);
        }
    }*/

    @Override
    public void printTree(int level) {
        super.printClassIndented("AddTree",level);
        super.printVariableIndented("operator",operator.toString(),level);
        termTree.printTree(level+1);
        if(addTree != null) {
            addTree.printTree(level+1);
        }
    }

}
