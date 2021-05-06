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
public enum TokenType {
    UNARY_INT_CONVERSION, // (int)
    UNARY_FLOAT_CONVERSION, // (float)
    UNARY_STRING_CONVERSION, // (string)
    LEFT_PARENTHESES, // (
    RIGHT_PARENTHESES, // )
    LEFT_CURLY_BRACKET, // {
    RIGHT_CURLY_BRACKET, // }
    ASSIGNMENT, // =
    LESS_THAN, // <
    LESS_THAN_OR_EQUAL, // <=
    GREATER_THAN, // >
    GREATER_THAN_OR_EQUAL, // >=
    EQUAL, // ==
    NOT_EQUAL, // !=
    PLUS, // +
    MINUS, // -
    MULTIPLY, // *
    DIVIDE, // /
    MODULO, // %
    POWER, // ^
    CONCATENATE, // ..
    INT_DECLARATION, // int
    FLOAT_DECLARATION, // float
    STRING_DECLARATION, // string
    SEMI_COLON, // ;
    IDENT, // *user generated alphanumeric string, ex. var1*
    INT_VALUE, // *user generated int value, ex. 50*
    FLOAT_VALUE, // *user generated float value, ex. 2.40*
    STRING_VALUE, // *user generated string value, ex. "Hello, world!"*
    FOR, // for
    IF, // if
    ELSE, // else
    ;
    
    @Override
    public String toString() {
        switch(this) {
            case UNARY_INT_CONVERSION:
                return "(int)";
            case UNARY_FLOAT_CONVERSION:
                return "(float)";
            case UNARY_STRING_CONVERSION:
                return "(string)";
            case LEFT_PARENTHESES:
                return "(";
            case RIGHT_PARENTHESES:
                return ")";
            case LEFT_CURLY_BRACKET:
                return "{";
            case RIGHT_CURLY_BRACKET:
                return "}";
            case ASSIGNMENT:
                return "=";
            case LESS_THAN:
                return "<";
            case LESS_THAN_OR_EQUAL:
                return "<=";
            case GREATER_THAN:
                return ">";
            case GREATER_THAN_OR_EQUAL:
                return ">=";
            case EQUAL:
                return "==";
            case NOT_EQUAL:
                return "!=";
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case MULTIPLY:
                return "*";
            case DIVIDE:
                return "/";
            case MODULO:
                return "%";
            case POWER:
                return "^";
            case CONCATENATE:
                return "..";
            case INT_DECLARATION:
                return "intDeclaration";
            case FLOAT_DECLARATION:
                return "floatDeclaration";
            case STRING_DECLARATION:
                return "stringDeclaration";
            case SEMI_COLON:
                return ";";
            case IDENT:
                return "ident";
            case INT_VALUE:
                return "intValue";
            case FLOAT_VALUE:
                return "floatValue";
            case STRING_VALUE:
                return "stringValue";
            case FOR:
                return "for";
            case IF:
                return "if";
            case ELSE:
                return "else";
        }
        return "unknown token";
    }
}
