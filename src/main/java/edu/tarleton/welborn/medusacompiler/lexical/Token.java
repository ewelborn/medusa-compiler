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

package edu.tarleton.welborn.medusacompiler.lexical;

/**
 *
 * @author Ethan Welborn - ethan.welborn@go.tarleton.edu
 */
public class Token {

    private final TokenType tokenType;
    private final int line;
    private final int column;

    private final int intValue;
    private final double floatValue;
    private final String stringValue;
    
    public TokenType getTokenType() {
        return tokenType;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public Token(TokenType tokenType, int line, int column) {
        this.tokenType = tokenType;
        this.line = line;
        this.column = column;
        this.intValue = 0;
        this.floatValue = 0.0;
        this.stringValue = "";
    }
    
    public Token(TokenType tokenType, int line, int column, int intValue, double floatValue, String stringValue) {
        this.tokenType = tokenType;
        this.line = line;
        this.column = column;
        this.intValue = intValue;
        this.floatValue = floatValue;
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return tokenType.toString();
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
    
}
