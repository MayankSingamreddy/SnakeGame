/**
 *  This program reads from an html file and
 *  outputs the number of tokens.
 *  Prints the output to the user.
 *  Tokenizes the HTML file's strings into tags, numbers, punctuation,
 *  words, etc. Progrm uses each char and puts
 *  it into a token string,then adds each toekn string
 *  into an arragy of strings, then prints that.
 *
 *  @author  Mayank Singamreddy
 *  @since   October 26, 2017
 */
public class HTMLUtilities
{
    // NONE = not nested in a block
    // COMMENT = inside a comment block
    // PREFORMAT = inside a pre-format block
	private enum TokenState { NONE, COMMENT, PREFORMAT };
	// the current tokenizer state
	private static TokenState state;

	public HTMLUtilities()
	{
		state = TokenState.NONE;
	}
    /**
     *  Parses the html file, searching for tokens
     *
     *  @param The String reading the file
     *  @return
     */
	public static String[] tokenizeHTMLString(String str)
	{
		String[] arr = new String[str.length()];
		int ind = 0;
		for(int i = 0; i < str.length(); i++)
		{
            //searches for comments using the comment tags "-->" and "<!--"
            //tokenize comments
            if(state == TokenState.COMMENT)
				if(i + 3 <= str.length() && str.substring(i, i + 3).equals("-->"))
				{
					state = TokenState.NONE;//reset token state
					i = i + 2;//add increment
				}
				else continue;
                //checks for ending comment
            else if(i + 4 <= str.length() && str.substring(i, i + 4).equals("<!--"))
			{
				state = TokenState.COMMENT;
				i = i + 3;//add increment
			}
            //EC: preformat
            //Checks for if the state is preformat
            //Tokenize the whole line as one token
			else if(state == TokenState.PREFORMAT)
			{
				if(i + 6 <= str.length() && str.substring(i, i + 6).equals("</pre>"))
				{
					state = TokenState.NONE;//reset token state to none
					arr[ind++] = str.substring(i, i + 6);
					i = i + 5; //add increment
				}
				else
				{
					arr[ind++] = str.substring(i);
					i = str.length();
				}
			}
			//checks for starting the preformat
            else if(i + 5 <= str.length() && str.substring(i, i + 5).equals("<pre>"))
			{
				state = TokenState.PREFORMAT;
				arr[ind++] = str.substring(i, i + 5);
				i = i + 4;//add increment
			}
            //searches for tags using the less and greater than symbols
            //tokenize tags
			else if(str.charAt(i) == '<')
			{
				int j = i + 1;
				while(j < str.length() && str.charAt(j) != '>')
					j++;
				if(j == str.length())
					j--;
				arr[ind++] = str.substring(i, j + 1);
				i = j;
			}
			//check for the start of a word by checking for a letter at the end
			else if(Character.isLetter(str.charAt(i)) &&
                    i + 1 < str.length() &&
                    Character.isLetter(str.charAt(i + 1)))
			{
				int j = i + 1;
				while(j < str.length() && Character.isLetter(str.charAt(j)))
					j++;
				arr[ind++] = str.substring(i, j);
				i = j - 1;
			}
            //EC: Weird number
            //searches for weird numbers by searching inclusively for '_'
            //and '.'
			else if(Character.isDigit(str.charAt(i)) || ((str.charAt(i) == '-' ||
                          str.charAt(i) == '.') && i + 1 < str.length() &&
                         Character.isDigit(str.charAt(i + 1))))
			{
				int j = i + 1;
				boolean cancel = false; //boolean to escape loop
				while(j < str.length() && !cancel)
				{
					if(Character.isDigit(str.charAt(j)) || str.charAt(j) == 'e' || str.charAt(j) == '-')
						j++;
					else if(str.charAt(j) == '.')
					{
						if(j + 1 == str.length())
							j++;
						else if(Character.isDigit(str.charAt(j + 1)))
							j++;
					}
					else
						cancel = true;
				}
				arr[ind++] = str.substring(i, j);
				i = j - 1;
			}

            //EC: ellipses
            //Check for ellipses by catching three consecutive periods or "..."
			else if(str.charAt(i) == '.' && i + 1 < str.length() && str.charAt(i + 1) == str.charAt(i))
			{
				int j = i + 1;
                //until a non "." character is found, increment j
				while(j < str.length() && str.charAt(j) == str.charAt(i))
					j++;
				arr[ind++] = str.substring(i, j);
				i = j - 1;
			}
			//single punctuation
			else if(isPunctuation(str.charAt(i)))
				arr[ind++] = str.substring(i, i + 1);
			else if(str.charAt(i) != ' ')
			{
				int j = i + 1;
				while(j < str.length() && str.charAt(j) != ' ' && !isPunctuation(str.charAt(j)))
					j++;
				arr[ind++] = str.substring(i, j);
				i = j - 1;
			}
		}
		String[] result = new String[ind];
		for(int i = 0; i < result.length; i++)
			result[i] = arr[i];
		return result;
	}

    /**
     *    Checks for if the punctuation is a printable character:
     *     not a whitespace, digit, or letter.
     *     @param the character being read
     *     @return return whether puncutation or not
     */
	public static boolean isPunctuation(char c){
		return c >= '!' && c <= '~' && c != ' ' && !Character.isDigit(c) && !Character.isLetter(c);
	}

    /**
     *  This method prints the amount of tokens to the user.
     *  If there are no tokens it prints nothing.
     *  @param an array of String tokens
     *  @return prints out the tokens in a loop
     */
	public void printTokens(String[] tokens) {
		if (tokens == null) return;
		for (int a = 0; a < tokens.length; a++) {
			if (a % 5 == 0) System.out.print("\n  ");
			System.out.print("[token " + a + "]: " + tokens[a] + " ");
		}
		System.out.println();
	}

}