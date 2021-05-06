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

/**
 *
 * @author Ethan Welborn - ethan.welborn@go.tarleton.edu
 */
public class ExpressionTree extends AbstractSyntaxTree {

    private TermTree term;
    private AddTree add;

    public ExpressionTree(TermTree term, AddTree add) {
        this.term = term;
        this.add = add;
    }

    public TermTree getTerm() {
        return term;
    }

    public AddTree getAdd() {
        return add;
    }
    
    @Override
    public void printTree(int level) {
        super.printClassIndented("ExpressionTree",level);
        term.printTree(level+1);
        if(add != null) {
            add.printTree(level+1);
        }
    }
    
    // Pseudocode
    /*
    
    public Value visit() {
        Value left = term.visit();
        Value right = add.visit();
        
        // If we don't have a right value, then just return the left
        if(right == null) { return left; }
    
        // If the two sides of the expression aren't the same type, throw an error
        if(left.type != right.type) { throw error("Must be same type!"); }
    
        // The righthand side of the expression has our operator, as well as any
        // further terms in the expression, so they need to be given responsibility
        // for evaluating the expression.
        return right.operate(left);
    }
    
    */

    /*@Override
    public ExpressionValue visitNode() throws SemanticAnalyzerException {
        ExpressionValue left = term.visitNode();
        ExpressionValue right = add.visitNode();
        
        if(right == null) { return left; }
        
        if(left.getType() != right.getType()) {
            throw new SemanticAnalyzerException("Must be same type!");
        }
        
        return right.operate(left);
    }*/

}
