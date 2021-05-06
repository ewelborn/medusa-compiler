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
public class ForLoopVariableTree extends AbstractSyntaxTree {

    private TokenType dataType;
    private String ident;
    private ExpressionTree expression;
    private OptionalAssignmentTree optionalAssignment;

    public ForLoopVariableTree(TokenType dataType, String ident, ExpressionTree expression) {
        this.dataType = dataType;
        this.ident = ident;
        this.expression = expression;
    }

    public ForLoopVariableTree(String ident, OptionalAssignmentTree optionalAssignment) {
        this.ident = ident;
        this.optionalAssignment = optionalAssignment;
    }

    public TokenType getDataType() {
        return dataType;
    }

    public void setDataType(TokenType dataType) {
        this.dataType = dataType;
    }

    public String getIdent() {
        return ident;
    }

    public ExpressionTree getExpression() {
        return expression;
    }

    public OptionalAssignmentTree getOptionalAssignment() {
        return optionalAssignment;
    }

    @Override
    public void printTree(int level) {
        super.printClassIndented("ForLoopVariableTree",level);
        super.printVariableIndented("dataType",dataType.toString(),level);
        super.printVariableIndented("ident",ident,level);
        if(optionalAssignment != null) {
            optionalAssignment.printTree(level+1);
        }
    }

}
