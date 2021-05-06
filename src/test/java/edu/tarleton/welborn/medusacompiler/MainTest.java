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
package edu.tarleton.welborn.medusacompiler;

import edu.tarleton.welborn.medusacompiler.compiler.ThreeAddressCode;
import edu.tarleton.welborn.medusacompiler.lexical.LexicalAnalyzer;
import edu.tarleton.welborn.medusacompiler.lexical.LexicalAnalyzerException;
import edu.tarleton.welborn.medusacompiler.lexical.Token;
import edu.tarleton.welborn.medusacompiler.syntax.ProgramTree;
import edu.tarleton.welborn.medusacompiler.syntax.SemanticAnalyzerException;
import edu.tarleton.welborn.medusacompiler.syntax.SyntaxAnalyzer;
import edu.tarleton.welborn.medusacompiler.syntax.SyntaxAnalyzerException;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.fail;

/**
 *
 * @author Ethan Welborn - ethan.welborn@go.tarleton.edu
 */
public class MainTest {
    
    public MainTest() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    private void testMedusaFile(String fileName) throws IOException,LexicalAnalyzerException,SyntaxAnalyzerException,SemanticAnalyzerException {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(fileName),Charset.forName("US-ASCII"))) {
            List<Token> tokens = LexicalAnalyzer.parseBufferedReader(reader);
            SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(tokens);
            ProgramTree programTree = syntaxAnalyzer.getProgramTree();
            List<ThreeAddressCode> threeAddressCode = edu.tarleton.welborn.medusacompiler.compiler.Compiler.generateCodeFromProgramTree(programTree);
        }
    }
    
    @org.junit.jupiter.api.Test
    public void testVariableDeclaration() throws Exception {
        testMedusaFile("src/test/java/edu/tarleton/welborn/medusacompiler/TestCases/variableDeclaration.med");
    }
    
    @org.junit.jupiter.api.Test
    public void testIncorrectVariableDeclaration() throws Exception {
        try {
            testMedusaFile("src/test/java/edu/tarleton/welborn/medusacompiler/TestCases/IncorrectCases/variableDeclaration.med");
            fail("SemanticAnalyzerException expected");
        } catch(SemanticAnalyzerException err) {}
    }
    
    @org.junit.jupiter.api.Test
    public void testIncorrectVariableDeclaration2() throws Exception {
        try {
            testMedusaFile("src/test/java/edu/tarleton/welborn/medusacompiler/TestCases/IncorrectCases/variableDeclaration2.med");
            fail("SemanticAnalyzerException expected");
        } catch(SemanticAnalyzerException err) {}
    }
    
    @org.junit.jupiter.api.Test
    public void testIfStatement() throws Exception {
        testMedusaFile("src/test/java/edu/tarleton/welborn/medusacompiler/TestCases/ifStatement.med");
    }
    
    @org.junit.jupiter.api.Test
    public void testForLoop() throws Exception {
        testMedusaFile("src/test/java/edu/tarleton/welborn/medusacompiler/TestCases/forLoop.med");
    }
    
    @org.junit.jupiter.api.Test
    public void testStringConcatenation() throws Exception {
        testMedusaFile("src/test/java/edu/tarleton/welborn/medusacompiler/TestCases/stringConcatenation.med");
    }
    
    @org.junit.jupiter.api.Test
    public void testFloatStringConversion() throws Exception {
        testMedusaFile("src/test/java/edu/tarleton/welborn/medusacompiler/TestCases/floatStringConversion.med");
    }
    
    @org.junit.jupiter.api.Test
    public void testFloatIntConversion() throws Exception {
        testMedusaFile("src/test/java/edu/tarleton/welborn/medusacompiler/TestCases/floatIntConversion.med");
    }
    
    @org.junit.jupiter.api.Test
    public void testIntStringConversion() throws Exception {
        testMedusaFile("src/test/java/edu/tarleton/welborn/medusacompiler/TestCases/intStringConversion.med");
    }
    
    @org.junit.jupiter.api.Test
    public void testSimpleExpression() throws Exception {
        testMedusaFile("src/test/java/edu/tarleton/welborn/medusacompiler/TestCases/simpleExpression.med");
    }
    
    @org.junit.jupiter.api.Test
    public void testIncorrectStringConcatenation() throws Exception {
        try {
            testMedusaFile("src/test/java/edu/tarleton/welborn/medusacompiler/TestCases/IncorrectCases/concatenation.med");
            fail("SemanticAnalyzerException expected");
        } catch(SemanticAnalyzerException err) {}
    }
    
    @org.junit.jupiter.api.Test
    public void testConcatenateWithConversion() throws Exception {
        testMedusaFile("src/test/java/edu/tarleton/welborn/medusacompiler/TestCases/concatenateWithConversion.med");
    }
    
    @org.junit.jupiter.api.Test
    public void testConcatenateWithConversion2() throws Exception {
        testMedusaFile("src/test/java/edu/tarleton/welborn/medusacompiler/TestCases/concatenateWithConversion2.med");
    }
    
    @org.junit.jupiter.api.Test
    public void testIncorrectConditional() throws Exception {
        try {
            testMedusaFile("src/test/java/edu/tarleton/welborn/medusacompiler/TestCases/IncorrectCases/conditional.med");
            fail("SemanticAnalyzerException expected");
        } catch(SemanticAnalyzerException err) {}
    }
    
    @org.junit.jupiter.api.Test
    public void testIncorrectForLoop() throws Exception {
        try {
            testMedusaFile("src/test/java/edu/tarleton/welborn/medusacompiler/TestCases/IncorrectCases/forLoop.med");
            fail("SemanticAnalyzerException expected");
        } catch(SemanticAnalyzerException err) {}
    }
    
    @org.junit.jupiter.api.Test
    public void testIncorrectSyntax() throws Exception {
        try {
            testMedusaFile("src/test/java/edu/tarleton/welborn/medusacompiler/TestCases/IncorrectCases/syntax.med");
            fail("SyntaxAnalyzerException expected");
        } catch(LexicalAnalyzerException err) {}
    }
    
    @org.junit.jupiter.api.Test
    public void testIncorrectSyntax2() throws Exception {
        try {
            testMedusaFile("src/test/java/edu/tarleton/welborn/medusacompiler/TestCases/IncorrectCases/syntax2.med");
            fail("SyntaxAnalyzerException expected");
        } catch(LexicalAnalyzerException err) {}
    }
}
