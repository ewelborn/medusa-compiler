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

/**
 *
 * @author Ethan Welborn - ethan.welborn@go.tarleton.edu
 */
public enum ThreeAddressCodeOperator {
    ADD,
    SUB,
    MUL,
    DIV,
    MOD,
    POW,
    CONCAT,
    ASSIGN,
    ARRAY_ASSIGN,
    LABEL,
    JMP,
    IS_GREATER_THAN,
    IS_LESS_THAN,
    IS_GREATER_THAN_OR_EQUAL,
    IS_LESS_THAN_OR_EQUAL,
    IS_EQUAL,
    IS_NOT_EQUAL,
    JMP_IF_TRUE,
    JMP_IF_FALSE,
    CONVERT_INT_TO_STRING,
    CONVERT_INT_TO_FLOAT,
    CONVERT_FLOAT_TO_INT,
    CONVERT_FLOAT_TO_STRING,
    CONVERT_STRING_TO_INT,
    CONVERT_STRING_TO_FLOAT,
}
