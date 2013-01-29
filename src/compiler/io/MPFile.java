package compiler.io;

import java.util.HashMap;

/**
 * Represents the micropascal file by containing a mapping from line number to the actual line
 */
public class MPFile {

    private HashMap<Integer, Line> lines = new HashMap<Integer, Line>();

    private int lineIndex = 1; // 1-based

    /**
     * Adds a line to the file and maps it to the next line number
     * 
     * @param line
     */
    public void addLine(Line line) {
        lines.put(lines.size() + 1, line);
    }

    public Line getLineAt(int number) {
        return lines.get(number);
    }

    public char getCurrentCharacter() {
        return getCurrentLine().getCurrentCharacter();
    }
    
    //    /**
    //     * Finds a useful character by skipping whitespace but not new lines
    //     *
    //     * @return current character if its useful, else the next useful character
    //     */
    //    public char getUsefulCharacter() {
    //        char usefulChar = 0;
    //
    //        if (!WhiteSpace.isWhitespace(getCurrentCharacter())) { //if not whitespace, return self
    //            usefulChar = getCurrentCharacter();
    //        } else { //current character is whitespace (not useful), find next useful character
    //            while (WhiteSpace.isWhitespace(getCurrentCharacter())) {
    //                //getCurrentCharacter()
    //            }
    //        }
    //
    //        return usefulChar;
    //    }

    /**
     * Checks if the current line has any characters remaining
     * @return true if a character is found otherwise false
     */
    public boolean hasNextChar() {
        //        boolean lastline = lineIndex > lines.size();
        //        System.out.println("lineIndex: " + lineIndex + " > " + lines.size() + " = " + lastline);
        //        if (lastline) {
        //            return false;
        //        }
        //        if (getCurrentLine() == null) {
        //            System.out.println("current line is null");
        //        }
        return hasNextChar(false);
    }

    /**
     * Checks if the file has any characters remaining
     * @param linewrap if true it will continue checking for characters until there are no more lines otherwise it will only check the current line
     * @return true if a character was found otherwise false
     */
    public boolean hasNextChar(boolean linewrap)
    {
    	if(linewrap)
    	{
    		for(int line=lineIndex; line <= lines.size(); line++)
    		{
    			Line tmpLine = getLineAt(line);
    			if(tmpLine.hasNextChar())
    			{
    				return true; //found a character on a line
    			}
    		}
    		return false; //checked remaining lines and they are all blank
    	}
    	else
    	{
    		return getCurrentLine().hasNextChar();
    	}
    }

    public boolean hasNextLine() {
        return lineIndex < lines.size();
    }

    public char getNextCharacter() {
        return getNextCharacter(false);
    }

    /**
     * Gets the next character in the file and update the indices
     * @param linewrap if true it will continue until the end of file otherwise the end of the line
     * @return the character found
     * @throws EOFException - only when linewrap is true and it cannot find any more characters
     * @post lineIndex will be on the line with the returned character if there are more characters, the next line that has characters, or the last line in the file 
     */
    public char getNextCharacter(boolean linewrap)
    {
    	if(linewrap)
    	{
    		for(int line=lineIndex; line <= lines.size(); line++)
    		{
    			Line tmpLine = getLineAt(line);
    			if(tmpLine.hasNextChar())
    			{
    				char tmpChar = tmpLine.getNextChar();
    				lineIndex = line;
    				while(lineIndex < numberOfLines() && !getCurrentLine().hasNextChar())
    				{
    					lineIndex++;
    				}
    				return tmpChar; //found a character
    			}
    		}
    		lineIndex = lines.size();
    		throw new EOFException("Checked the rest of the file no characters remain");
    	}
    	else
    	{
    		return getCurrentLine().getNextChar();
    	}
    	
    }
    
    public void increaseLineIndex() {
        lineIndex++;
    }

    /**
     * 
     * @return true if this is the last line; false otherwise
     */
    public boolean isOnLastLine() {
        return lineIndex >= lines.size();
    }

    /**
     * Prints the file
     * 
     * @param lineNums
     *            whether or not to include line numbers
     */
    public void print(boolean lineNums) {
        for (int i = 1; i <= lines.size(); i++) {
            if (lineNums) {
                System.out.println(i + ": " + lines.get(i).getText());
            } else {
                System.out.println(lines.get(i).getText());
            }

        }
    }

    /**
     * 
     * @return the line at the current line index
     */
    public Line getCurrentLine() {
        return lines.get(lineIndex);
    }

    public int getColumnIndex() {
        return getCurrentLine().getColumnIndex();
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public int numberOfLines() {
        return lines.size();
    }
}
