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

import edu.tarleton.welborn.medusacompiler.compiler.Compiler;
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

/**
 *
 * @author Ethan Welborn - ethan.welborn@go.tarleton.edu
 */
public class Main {

    public static void main(String[] args) throws LexicalAnalyzerException,SyntaxAnalyzerException,SemanticAnalyzerException {
        try (BufferedReader reader = Files.newBufferedReader(Path.of("input.med"),Charset.forName("US-ASCII"))) {
            List<Token> tokens = LexicalAnalyzer.parseBufferedReader(reader);
            for(Token token : tokens) {
                System.out.println(token + " at line " + token.getLine() + ", column " + token.getColumn());
            }
            SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(tokens);
            ProgramTree programTree = syntaxAnalyzer.getProgramTree();
            programTree.printTree(0);
            List<ThreeAddressCode> threeAddressCode = Compiler.generateCodeFromProgramTree(programTree);
            for(ThreeAddressCode TAC : threeAddressCode) {
                System.out.println(TAC);
            }
        } catch (IOException err) {
            err.printStackTrace();
        }
    }
    
}
