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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ethan Welborn - ethan.welborn@go.tarleton.edu
 */

public class LexicalAnalyzer {

    private static boolean isThisAWord(String originString, int startingPosition, String guessedWord) {
        // Check that the string, beginning at starting position, is long enough to contain the guessed word
        if(originString.length() - startingPosition >= guessedWord.length()) {
            return originString.substring(startingPosition,startingPosition + guessedWord.length()).equals(guessedWord);
        } else {
            return false;
        }
    }
    
    public static List<Token> parseBufferedReader(BufferedReader reader) throws IOException,LexicalAnalyzerException {
        List<Token> tokens = new ArrayList<>();
        
        int currentLine = 0;
        int currentColumn;
        boolean blockComment = false;
        while(true) {
            String line = reader.readLine();
            if(line == null) {
                break;
            }
            
            currentLine++;
            currentColumn = 0;
            boolean lineComment = false;
            
            while(true) {
                if(currentColumn >= line.length() || lineComment) {
                    break;
                }
                
                char character = line.charAt(currentColumn);
                
                // If we're stuck in a block comment, then the only symbol that'll let us
                // escape is */ so we search for that symbol only. If we find it, then we
                // can start processing symbols on the next cycle! Regardless, we won't be
                // looking for any regular tokens this cycle.
                if(blockComment) {
                    if(character == '*' && isThisAWord(line,currentColumn,"*/")) {
                        blockComment = false;
                        currentColumn = currentColumn + 2;
                    } else {
                        currentColumn = currentColumn + 1;
                    }
                    continue;
                }
                
                switch(character) {
                    case ' ':
                        currentColumn++;
                        break;
                    case '/':
                        if(isThisAWord(line,currentColumn,"//") && blockComment == false) {
                            lineComment = true;
                            break;
                        } else if(isThisAWord(line,currentColumn,"/*")) {
                            blockComment = true;
                            currentColumn = currentColumn + 2;
                            break;
                        } else {
                            // /
                            tokens.add(new Token(TokenType.DIVIDE,currentLine,currentColumn));
                            currentColumn++;
                            break;
                        }
                    case '(':
                        if(isThisAWord(line,currentColumn,"(int)")) {
                            // (int)
                            tokens.add(new Token(TokenType.UNARY_INT_CONVERSION,currentLine,currentColumn));
                            currentColumn = currentColumn + 5;
                            break;
                        } else if (isThisAWord(line,currentColumn,"(float)")) {
                            // (float)
                            tokens.add(new Token(TokenType.UNARY_FLOAT_CONVERSION,currentLine,currentColumn));
                            currentColumn = currentColumn + 7;
                            break;
                        } else if (isThisAWord(line,currentColumn,"(string)")) {
                            // (string)
                            tokens.add(new Token(TokenType.UNARY_STRING_CONVERSION,currentLine,currentColumn));
                            currentColumn = currentColumn + 8;
                            break;
                        } else {
                            // (
                            tokens.add(new Token(TokenType.LEFT_PARENTHESES,currentLine,currentColumn));
                            currentColumn = currentColumn + 1;
                            break;
                        }
                    case ')':
                        tokens.add(new Token(TokenType.RIGHT_PARENTHESES,currentLine,currentColumn));
                        currentColumn++;
                        break;
                    case '{':
                        tokens.add(new Token(TokenType.LEFT_CURLY_BRACKET,currentLine,currentColumn));
                        currentColumn++;
                        break;
                    case '}':
                        tokens.add(new Token(TokenType.RIGHT_CURLY_BRACKET,currentLine,currentColumn));
                        currentColumn++;
                        break;
                    case '<':
                        if(isThisAWord(line,currentColumn,"<=")) {
                            // <=
                            tokens.add(new Token(TokenType.LESS_THAN_OR_EQUAL,currentLine,currentColumn));
                            currentColumn = currentColumn + 2;
                            break;
                        } else {
                            // <
                            tokens.add(new Token(TokenType.LESS_THAN,currentLine,currentColumn));
                            currentColumn = currentColumn + 1;
                            break;
                        }
                    case '>':
                        if(isThisAWord(line,currentColumn,">=")) {
                            // >=
                            tokens.add(new Token(TokenType.GREATER_THAN_OR_EQUAL,currentLine,currentColumn));
                            currentColumn = currentColumn + 2;
                            break;
                        } else {
                            // >
                            tokens.add(new Token(TokenType.GREATER_THAN,currentLine,currentColumn));
                            currentColumn = currentColumn + 1;
                            break;
                        }
                    case '!':
                        if(isThisAWord(line,currentColumn,"!=")) {
                            // !=
                            tokens.add(new Token(TokenType.NOT_EQUAL,currentLine,currentColumn));
                            currentColumn = currentColumn + 2;
                            break;
                        }
                        throw new LexicalAnalyzerException("Unrecogized token starting with ! at line " + currentLine + ", column " + currentColumn);
                    case '=':
                        if(isThisAWord(line,currentColumn,"==")) {
                            // ==
                            tokens.add(new Token(TokenType.EQUAL,currentLine,currentColumn));
                            currentColumn = currentColumn + 2;
                            break;
                        } else {
                            // =
                            tokens.add(new Token(TokenType.ASSIGNMENT,currentLine,currentColumn));
                            currentColumn = currentColumn + 1;
                            break;
                        }
                    case '+':
                        // +
                        tokens.add(new Token(TokenType.PLUS,currentLine,currentColumn));
                        currentColumn++;
                        break;
                    case '-':
                        // -
                        tokens.add(new Token(TokenType.MINUS,currentLine,currentColumn));
                        currentColumn++;
                        break;
                    case '*':
                        // *
                        tokens.add(new Token(TokenType.MULTIPLY,currentLine,currentColumn));
                        currentColumn++;
                        break;
                    case '%':
                        // %
                        tokens.add(new Token(TokenType.MODULO,currentLine,currentColumn));
                        currentColumn++;
                        break;
                    case '^':
                        // ^
                        tokens.add(new Token(TokenType.POWER,currentLine,currentColumn));
                        currentColumn++;
                        break;
                    case '.':
                        if(isThisAWord(line,currentColumn,"..")) {
                            // ..
                            tokens.add(new Token(TokenType.CONCATENATE,currentLine,currentColumn));
                            currentColumn = currentColumn + 2;
                            break;
                        }
                        throw new LexicalAnalyzerException("Unrecogized token starting with . at line " + currentLine + ", column " + currentColumn);
                    case ';':
                        // ;
                        tokens.add(new Token(TokenType.SEMI_COLON,currentLine,currentColumn));
                        currentColumn = currentColumn + 1;
                        break;
                    case '"':
                        // Must be a string value token. We're going to scan characters until we find a closing quote.
                        int originalColumn = currentColumn;
                        while(true) {
                            currentColumn++;
                            // If we reached the end of the line, then throw a syntax error
                            if(currentColumn < line.length()) {
                                character = line.charAt(currentColumn);
                                // If character is a quote symbol then end the token and submit the value without the quotes
                                if(character == '"') {
                                    tokens.add(new Token(TokenType.STRING_VALUE,currentLine,currentColumn,0,0,line.substring(originalColumn+1, currentColumn)));
                                    currentColumn++;
                                    break;
                                }
                            } else {
                                throw new LexicalAnalyzerException("String value missing a closing quote symbol at line " + currentLine + ", column " + originalColumn);
                            }
                        }
                        break;
                    default:
                        if((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z')) {
                            // Character is in the range a-z or A-Z
                            // It could be an int, float, or string token (for variable declarations), or
                            // it could be an ident token.
                            if(isThisAWord(line,currentColumn,"int ")) {
                                tokens.add(new Token(TokenType.INT_DECLARATION,currentLine,currentColumn));
                                currentColumn = currentColumn + 4;
                                break;
                            } else if(isThisAWord(line,currentColumn,"float ")) {
                                tokens.add(new Token(TokenType.FLOAT_DECLARATION,currentLine,currentColumn));
                                currentColumn = currentColumn + 6;
                                break;
                            } else if(isThisAWord(line,currentColumn,"string ")) {
                                tokens.add(new Token(TokenType.STRING_DECLARATION,currentLine,currentColumn));
                                currentColumn = currentColumn + 7;
                                break;
                            } else if(isThisAWord(line,currentColumn,"for")) {
                                tokens.add(new Token(TokenType.FOR,currentLine,currentColumn));
                                currentColumn = currentColumn + 3;
                                break;
                            } else if(isThisAWord(line,currentColumn,"if")) {
                                tokens.add(new Token(TokenType.IF,currentLine,currentColumn));
                                currentColumn = currentColumn + 2;
                                break;
                            } else if(isThisAWord(line,currentColumn,"else")) {
                                tokens.add(new Token(TokenType.ELSE,currentLine,currentColumn));
                                currentColumn = currentColumn + 4;
                                break;
                            } else {
                                // Must be an ident token. We're going to scan characters until we find a character
                                // that isn't alphanumeric.
                                originalColumn = currentColumn;
                                while(true) {
                                    currentColumn++;
                                    // If we reached the end of the line, then stop scanning and submit the ident token.
                                    if(currentColumn < line.length()) {
                                        character = line.charAt(currentColumn);
                                        // If the character isn't alphanumeric, then stop our scan and submit the ident token, not including this character.
                                        if(!((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || (character >= '0' && character <= '9'))) {
                                            tokens.add(new Token(TokenType.IDENT,currentLine,originalColumn,0,0,line.substring(originalColumn, currentColumn)));
                                            break;
                                        }
                                    } else {
                                        tokens.add(new Token(TokenType.IDENT,currentLine,originalColumn,0,0,line.substring(originalColumn, currentColumn)));
                                        break;
                                    }
                                }
                                break;
                            }
                        } else if(character >= '0' && character <= '9') {
                            // Must be a float or int value token. We're going to scan characters until we find a character
                            // that isn't a digit or a dot.
                            originalColumn = currentColumn;
                            int dots = 0;
                            while(true) {
                                currentColumn++;
                                // If we reached the end of the line, then stop scanning and submit the token.
                                if(currentColumn < line.length()) {
                                    character = line.charAt(currentColumn);
                                    // If the character isn't a digit or a dot, then stop our scan and submit the token, not including this character.
                                    if(!((character >= '0' && character <= '9') || character == '.')) {
                                        // If there's 0 dots, it's an int. If there's 1 dot, it's a float. If there's more than 1 dot, it's a syntax error.
                                        String tokenString = line.substring(originalColumn, currentColumn);
                                        if(dots == 0) {
                                            tokens.add(new Token(TokenType.INT_VALUE,currentLine,originalColumn,Integer.parseInt(tokenString),0,""));
                                        } else if(dots == 1) {
                                            tokens.add(new Token(TokenType.FLOAT_VALUE,currentLine,originalColumn,0,Double.parseDouble(tokenString),""));
                                        } else {
                                            throw new LexicalAnalyzerException("Unrecogized number value with multiple decimal points, " + tokenString + ", at line " + currentLine + ", column " + originalColumn);
                                        }
                                        break;
                                    } else {
                                        if(character == '.') {
                                            dots++;
                                        }
                                    }
                                } else {
                                    
                                }
                            }
                            break;
                        }
                        throw new LexicalAnalyzerException("Unrecogized token starting with " + character + " at line " + currentLine + ", column " + currentColumn);
                }
            }
        }
            
        return tokens;
    }
    
}