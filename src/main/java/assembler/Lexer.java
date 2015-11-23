package assembler;

public interface Lexer {

	Token nextToken();

	/** Move to next non-whitespace character */
	void consume();

	/** Move one character; detect "end of file" */
	void advance();

	/** Ensure x is next character on the input stream */
	void match(char x);

}