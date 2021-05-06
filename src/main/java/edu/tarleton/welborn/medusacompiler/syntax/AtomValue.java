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
public class AtomValue {

    // Only one of these values will ever be used at any given time for this class
    private int intValue;
    private double floatValue;
    private String stringValue;
    private boolean ident;
    private ExpressionTree expression;
    private TokenType type;

    public AtomValue(TokenType type, int intValue) {
        this.type = type;
        this.intValue = intValue;
    }

    public AtomValue(TokenType type, double floatValue) {
        this.type = type;
        this.floatValue = floatValue;
    }

    public AtomValue(TokenType type, String stringValue) {
        this.type = type;
        this.stringValue = stringValue;
    }
    
    public AtomValue(TokenType type, String stringValue, boolean ident) {
        this.type = type;
        this.stringValue = stringValue;
        this.ident = ident;
    }

    public AtomValue(TokenType type, ExpressionTree expression) {
        this.type = type;
        this.expression = expression;
    }

    public int getIntValue() {
        return intValue;
    }

    public double getFloatValue() {
        return floatValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public boolean isIdent() {
        return ident;
    }

    public ExpressionTree getExpression() {
        return expression;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        switch(type) {
            case INT_VALUE:
                return "INT_VALUE( "+intValue+" )";
            case FLOAT_VALUE:
               return "FLOAT_VALUE( "+floatValue+" )";
            case STRING_VALUE:
                return "STRING_VALUE( "+stringValue+" )";
            case IDENT:
                return "IDENT( "+stringValue+" )";
            case LEFT_PARENTHESES:
                return "EXPRESSION (Use printTree for more info)";
        }
        return "null";
    }
    
    
}
