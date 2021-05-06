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
public class ConditionTree extends AbstractSyntaxTree {

    private ExpressionTree expression1;
    private TokenType conditionalOperator;
    private ExpressionTree expression2;

    public ConditionTree(ExpressionTree expression1, TokenType conditionalOperator, ExpressionTree expression2) {
        this.expression1 = expression1;
        this.conditionalOperator = conditionalOperator;
        this.expression2 = expression2;
    }

    public ExpressionTree getExpression1() {
        return expression1;
    }

    public TokenType getConditionalOperator() {
        return conditionalOperator;
    }

    public ExpressionTree getExpression2() {
        return expression2;
    }

    @Override
    public void printTree(int level) {
        System.out.println(" ".repeat(level*2)+"ConditionTree - "+level);
        expression1.printTree(level+1);
        System.out.println(" ".repeat(level*2)+"conditionalOperator: "+conditionalOperator);
        expression2.printTree(level+1);
    }
    
}
