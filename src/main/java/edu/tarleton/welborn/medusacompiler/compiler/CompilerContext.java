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

import edu.tarleton.welborn.medusacompiler.lexical.TokenType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ethan Welborn - ethan.welborn@go.tarleton.edu
 */
public class CompilerContext {

    private List<ThreeAddressCode> TACResults = new ArrayList<>();
    private int tempVariables = 0;
    private Map<String,TokenType> symbolTable = new HashMap<>();

    public List<ThreeAddressCode> getTACResults() {
        return TACResults;
    }
    
    public String getNextTempVariable(String prefix) {
        String ident = prefix + tempVariables;
        tempVariables = tempVariables + 1;
        return ident;
    }
    
    public String getNextTempVariable() {
        return getNextTempVariable("_t");
    }

    public Map<String, TokenType> getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(Map<String, TokenType> symbolTable) {
        this.symbolTable = symbolTable;
    }
}
