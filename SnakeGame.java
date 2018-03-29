import java.io.PrintWriter;
import java.util.Scanner;
/**
 *	turn-by-turn gameplay
 *	wasd to move around
 *	can save file to save.txt
 *	classic game you've all played in arcade
 *
 *	@author Mayank Singamreddy
 *	@since	13 November, 2017
 */
public class SnakeGame {

	private Snake snake;		// snake of the game
	private SnakeBoard board;	// the game's board
	private Coordinate target;	// snake's current target
	private int totalScore;			// score count of the game

		/**
			 *  construtor for the snake game
			 *  Generates a random spot for snake to start
			 *  math.random to generate spawn
			 *  r and c generate target
       */
	public SnakeGame()
	{
		board = new SnakeBoard(20, 25);

		int row = 1 + (int)(Math.random() * 16); 
		// snake has length 4, shortened
		// so that it isn't too long
		int col = 1 + (int)(Math.random() * 25); 
		// genates spot for snake to spawn
		snake = new Snake(row, col);

		int r = 1 + (int)(Math.random() * 20);
		int c = 1 + (int)(Math.random() * 25);
		boolean restart = false; // intiializes restart as false, 
								//the condition to generate a new target										  
		do
		{
			restart = false;
			for(Coordinate temp: snake)
				if(r == temp.getRow() && c == temp.getCol())
				{
					r = 1 + (int)(Math.random() * 20); // width position
					c = 1 + (int)(Math.random() * 25); // length position
					restart = true;
				}
		}while(restart); // while target isn't on the snake
						 //	generate another target
		target = new Coordinate(r, c); 
						//the target is on a specific coordinate
		totalScore = 0; //initial game score
	}
			/**
       *  This is main method, to run the game
       */
	public static void main(String[] args)
	{
		SnakeGame sg = new SnakeGame();
		sg.run();
	}
	/**
         *  This method runs the game.
				 *  Prompts user for game to start from save file
         *  Prints board out to user as each turn goes by
				 *  Plays the game
				 *  Takes user input based on case and moves snake accordingly
         */
	public void run()
	{
		System.out.println("Welcome to the SnakeGame!\n"); // introduction
		System.out.println("Let's get started.\n"); // introduction
		char userInput = 
		Prompt.getChar("Would you like to load the last save?(y or n)");
									// prompt to start from save file

		if(userInput == 'y') // if yes
		{
			snake.clear(); // empties the board
			Scanner infile  = OpenFile.openToRead("save.txt"); 
												// open save file
			String str = infile.nextLine(); //str = next line of file
			for(int i = 0; i < str.length(); i++) //reading save file
												// finding coordinates
				if(!Character.isDigit(str.charAt(i)))
					str = str.substring(0, i) + " " 
									+ str.substring(i + 1);
			String[] items = HTMLUtilities.tokenizeHTMLString(str); 
											// stores into items
			for(int i = 0; i < items.length - 1; i += 2)
			{
    				int x = Integer.parseInt(items[i].trim());
    				int y = Integer.parseInt(items[i + 1].trim());
   					snake.add(new Coordinate(x, y));
   			}
			str = infile.nextLine(); //reading save file
			int x = Integer.parseInt(str.substring(1, str.indexOf(",")));
								//recovering coordinate of the snake
			int y = Integer.parseInt(str.substring(str.indexOf(",")
											+ 1, str.indexOf(")")));
							// recovering coordinate of the snake
			target = new Coordinate(x, y); // new target
			totalScore = Integer.parseInt(infile.nextLine()); 
			// recover the Score
		}
		board.printBoard(snake, target);
		userInput = Prompt.getChar
		("totalScore: " + totalScore +
			   " (w - North, s - South, d - East, a - West, h - Help)");

		do{
			int row = snake.get(0).getRow(); 
					// finds coordinate of first snake point
			int col = snake.get(0).getCol(); 
					// finds coordinate of first snake point
			Coordinate last = new Coordinate(1, 1);
			switch(userInput)
			{
				case 'w': //case to move North
					if(isValid(new Coordinate(row - 1, col)))
					{
						snake.add(0, new Coordinate(row - 1, col)); 
										// add North Coordinate
						last = snake.remove(snake.size() - 1);
										// reduce by one snake length
					}
					break;
				case 's': // case to move South
					if(isValid(new Coordinate(row + 1, col)))
					{
						snake.add(0, new Coordinate(row + 1, col));
											// add new South Coodinate
						last = snake.remove(snake.size() - 1); 
										// reduce by one snake length
					}
					break;
				case 'd': // case to move East
					if(isValid(new Coordinate(row, col + 1)))
					{
						snake.add(0, new Coordinate(row, col + 1)); 
											// new east coordinate
						last = snake.remove(snake.size() - 1); 
										// reduce snake length by one
					}
					break;
			case 'a': // case to move West
				if(isValid(new Coordinate(row, col - 1)))
				{
					snake.add(0, new Coordinate(row, col - 1)); 
					// new west coordinate
					last = snake.remove(snake.size() - 1); 
					// reduce snake length by one
				}
				break;
			case 'h': // if user wants commands again
				System.out.println("\nCommands:");
				System.out.println(" w - move north");
				System.out.println(" s - move south ");
				System.out.println(" d - move east ");
				System.out.println(" a - move west ");
				System.out.println(" h - help");
				System.out.println(" f - save game to file");
				System.out.println(" q - quit ");
				Prompt.getString("Press enter to continue");
				break;
			case 'f':
				PrintWriter outfile = OpenFile.openToWrite("save.txt");
				//open save file
				outfile.println(snake); // print Snake
				outfile.println(target); // print the target
				outfile.println(totalScore); // print user their score
				outfile.close(); //close the file
				System.out.println("Saved!");
				break;
			}
			//if snake eats target
			if(snake.get(0).equals(target)) // if they coincide
			{
				totalScore++; // increment score
				snake.add(last);
				int r = 1 + (int)(Math.random() * 20); 
				// create new length position
				int c = 1 + (int)(Math.random() * 25); 
				// create new width position
				boolean restart = false;
				do
				{
					restart = false;
					for(Coordinate temp: snake)
						if(r == temp.getRow() && c == temp.getCol())
						{
							r = 1 + (int)(Math.random() * 20); 
							// create new length psoition
							c = 1 + (int)(Math.random() * 25); 
							// create new weight position
						// set restart case to true, so it doesn't redo
							restart = true; 
						}
				}while(restart); // while on same position
				target = new Coordinate(r, c); 
				//creates new coordinate for target
			}
			board.printBoard(snake, target);
			userInput = Prompt.getChar("totalScore: " + totalScore +
				" (w - North, s - South, d - East, a - West, h - Help)");
			if(userInput == 'q') // what happens if user presses quit
			{
			char choice = Prompt.getChar("Do you want to quit? (y or n)");
														//confirmation
			if(choice == 'n') // cancel
				userInput = Prompt.getChar("totalScore: " + totalScore +
				" (w - North, s - South, d - East, a - West, h - Help)");
							//prints possible commands
			}
		} while(userInput != 'q'); // while the user input is not quit
		System.out.println("Final totalScore = " + totalScore);
										// prints out score to user
		System.out.println("\nThanks for playing the SnakeGame!!!"); 
														//game ends
	}
		/**
         *  This method checks for if the move 
         *  that the snake makes is valid or not
         *
         *  @param Coodinate c  The coordinate that the snake is on
         *  @return   True if not part of the border
         * 				 and false if otherwise
		 *				 Snake cannot move onto the border
         */
		public boolean isValid(Coordinate c)
		{
			for(Coordinate temp: snake)
				if(temp.equals(c))
					return false;
			return 
				c.getRow() >= 1 && c.getRow() <= 20 
						&& c.getCol() >= 1 && c.getCol() <= 25; 
		}
}