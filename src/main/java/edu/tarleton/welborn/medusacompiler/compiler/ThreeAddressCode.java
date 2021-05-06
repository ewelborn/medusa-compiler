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
public class ThreeAddressCode {

    private String address1;
    private String address2;
    private String address3;
    private ThreeAddressCodeOperator operator;

    public ThreeAddressCode(String address1, ThreeAddressCodeOperator operator) {
        this.address1 = address1;
        this.operator = operator;
    }

    public ThreeAddressCode(String address1, ThreeAddressCodeOperator operator, String address2) {
        this.address1 = address1;
        this.address2 = address2;
        this.operator = operator;
    }

    public ThreeAddressCode(String address1, ThreeAddressCodeOperator operator, String address2, String address3) {
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.operator = operator;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public ThreeAddressCodeOperator getOperator() {
        return operator;
    }

    public void setOperator(ThreeAddressCodeOperator operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        switch(operator) {
            case LABEL:
                return address1+":";
            case ASSIGN:
                return address1+" = "+address2;
            case ARRAY_ASSIGN:
                return address1+"["+address2+"] = "+address3;
            case POW:
                return address1+" = "+address2+" POW "+address3;
            case ADD:
                return address1+" = "+address2+" ADD "+address3;
            case SUB:
                return address1+" = "+address2+" SUB "+address3;
            case MUL:
                return address1+" = "+address2+" MUL "+address3;
            case DIV:
                return address1+" = "+address2+" DIV "+address3;
            case MOD:
                return address1+" = "+address2+" MOD "+address3;
            case IS_EQUAL:
                return address1+" = "+address2+" == "+address3;
            case IS_NOT_EQUAL:
                return address1+" = "+address2+" != "+address3;
            case IS_GREATER_THAN_OR_EQUAL:
                return address1+" = "+address2+" >= "+address3;
            case IS_LESS_THAN_OR_EQUAL:
                return address1+" = "+address2+" <= "+address3;
            case IS_GREATER_THAN:
                return address1+" = "+address2+" > "+address3;
            case IS_LESS_THAN:
                return address1+" = "+address2+" < "+address3;
            case JMP:
                return "JMP to "+address1;
            case JMP_IF_TRUE:
                return "JMP to "+address1+" if "+address2+" is true";
            case JMP_IF_FALSE:
                return "JMP to "+address1+" if "+address2+" is false";
            case CONVERT_INT_TO_FLOAT:
                return address1+" = INT_TO_FLOAT("+address2+")";
            case CONVERT_INT_TO_STRING:
                return address1+" = INT_TO_STRING("+address2+")";
            case CONVERT_FLOAT_TO_INT:
                return address1+" = FLOAT_TO_INT("+address2+")";
            case CONVERT_FLOAT_TO_STRING:
                return address1+" = FLOAT_TO_STRING("+address2+")";
            case CONVERT_STRING_TO_INT:
                return address1+" = STRING_TO_INT("+address2+")";
            case CONVERT_STRING_TO_FLOAT:
                return address1+" = STRING_TO_FLOAT("+address2+")";
            case CONCAT:
                return address1+" = "+address2+" CONCAT "+address3;
        }   
        return "Unknown operator "+operator;
    }
}
