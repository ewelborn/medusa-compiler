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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ethan Welborn - ethan.welborn@go.tarleton.edu
 */
public class Interpreter {

    /*private Map<String,Integer> intTable;
    private Map<String,String> stringTable;
    private Map<String,Double> floatTable;
    
    private void assign(ThreeAddressCode instruction) {
        if(instruction.getAddress2().matches("-?\\d+")) {
            intTable.put(instruction.getAddress1(), Integer.MIN_VALUE)
        }
    }
    
    // This method is meant for testing .med files that have been compiled to three address code
    // for correctness. E.g. if our code is supposed to end with a = 157, this method will
    // interpret the three address code and return a so we can see if the value really is 157.
    
    // Obviously, if you wanted to use medusa in a serious environment (?!), you'd want
    // to compile it directly to a portable executable or an optimized interpreter.
    public Map<String,String> interpretThreeAddressCode(List<ThreeAddressCode> code,List<String> variablesToOutput) {
        intTable = new HashMap<>();
        stringTable = new HashMap<>();
        floatTable = new HashMap<>();
        
        for(ThreeAddressCode instruction : code) {
            switch(instruction.getOperator()) {
                case ASSIGN:
                    assign(instruction);
                    break;
            }
        }
        
        Map<String,String> output = new HashMap<>();
        
        return output;
    }*/
    
}
