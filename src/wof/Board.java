package wof;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Board
{
    private static final String DEFAULT_FILE = "dictionary.txt";
    /**
     * Delimiter for loadfile() to split the phrase and the type
     */
    private char DELIMITER = '|';

    /**
     * A random number generator to be used wherever necessary.
     */
    private static final Random RANDOM = new Random();

    /**
     * The phrase currently being guessed.
     */
    private String myPhrase;

    /**
     * The current phrase with only the guessed letters and punctuation.
     */
    private String myGuessedPhrase;

    /**
     * The question of the phrase.  
     */
    private String myQuestion;

    /**
     * The letters guessed so far.
     */
    private boolean[] myGuesses;

    /**
     * The set of phrases to choose from.  A list of 2-element string arrays, with the first
     * element being the category and the second element being the phrase itself.
     */
    private List<String[]> myPhraseList;

    /**
     * A constructor that loads the default dictionary file.
     * 
     * @throws FileNotFoundException if the default file does not exist
     */
    public Board() throws FileNotFoundException
    {
        this(new File(DEFAULT_FILE));
    }

    /**
     * A constructor that takes a dictionary file name and loads it into memory.
     *
     * @param dictionary_filename The filename to read
     * @throws FileNotFoundException if dictionary_file does not exist
     */
    public Board(final String dictionary_filename) throws FileNotFoundException
    {
        this(new File(dictionary_filename));
    }

    /**
     * A constructor that takes a dictionary file and loads it into memory.
     * 
     * @param dictionary_file The file to read
     * @throws FileNotFoundException if dictionary_file does not exist
     */
    public Board(final File dictionary_file) throws FileNotFoundException
    {
        this(dictionary_file, '|');
    }


    /**
     * A constructor that takes a dictionary file and custom delimiter and loads it into memory.
     *
     * @param dictionary_file The file to read
     * @param delimiter A single character to delimit the phrase from its type
     * @throws FileNotFoundException if dictionary_file does not exist
     */
    public Board(final File dictionary_file, char delimiter) throws FileNotFoundException
    {
        DELIMITER = delimiter;
        myPhraseList = new ArrayList<String[]>();
        loadFile(dictionary_file);
        myGuesses = new boolean[26];
        newPhrase();
    }

    /**
     * Picks a new phrase and starts a new board.
     */
    public void newPhrase()
    {
        // Remove the phrase so that it doesn't get repeated later
        final String[] next = myPhraseList.remove(RANDOM.nextInt(myPhraseList.size()));

        //TODO: Devise appropriate behavior when phrase list becomes exhausted
        myQuestion = next[0];
        myPhrase = next[1];
        for (int i = 0; i < myGuesses.length; i++)
        {
            myGuesses[i] = false;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < myPhrase.length(); i++)
        {
            final char letter = myPhrase.charAt(i);
            if (letter < 'A' || letter > 'Z')
            {
                // This is some form of punctuation (space, apostrophe, whatever)
                sb.append(letter);
            }
            else
            {
                // This is a letter, and we want a blank character
                sb.append('_');
            }
        }
        myGuessedPhrase = sb.toString();
    }

    /**
     * Loads a provided file into the phrase list.
     * 
     * @param the_file The file to load
     * @throws FileNotFoundException if the_file does not exist
     */
    private void loadFile(final File the_file) throws FileNotFoundException
    {
        final Scanner scan = new Scanner(the_file);

        while (scan.hasNextLine())
        {
            final String str = scan.nextLine();

            if(str.length() > 5 && str.contains(DELIMITER+"")) //no short phrases and has to have our type delimiter
            {
                String[] phrase = new String[2];
                for (int i = 0; i < str.length(); i++)
                {
                    if (str.charAt(i) == DELIMITER)
                    {
                        phrase[0] = str.substring(0, i);
                        phrase[1] = str.substring(i + 1).toUpperCase();
                        break;
                    }
                }
            myPhraseList.add(phrase);
            }
        }
    }

    /**
     * Returns the current phrase as it has been guessed so far.  Letters left to guess are
     * provided as underscores.
     * 
     * @return The phrase with guessed letters filled in
     */
    public String getCurrentPhrase()
    {
        return myGuessedPhrase;
    }

    
    public String getQuestion()
    {
        return myQuestion;
    }
    
    public String getMyPhrase()
    {
        return myPhrase;
    }

    public boolean guessPhrase(String input)
    {
        input=input.toUpperCase();
        System.out.println(input);
        System.out.println(myPhrase);
        return (input.equals(myPhrase));
    }
    
    /**
     * Evaluates how many occurrences of a letter are in the current phrase.
     * 
     * @param letter The guessed letter
     * @return The number of times that letter appears in the phrase, or -1 if the letter has
     *         already been guessed.
     */
    public int guessLetter(char letter)
    {
        letter = Character.toUpperCase(letter);
        if(letter > 'Z' || letter < 'A')
            return 0;
        if(myGuesses[letter - 'A'])
            return -1;

        int count = 0;
        myGuesses[letter - 'A'] = true;
        for(int i = 0; i < myPhrase.length(); i++)
        {
            if(myPhrase.charAt(i) == letter)
            {
                count++;
                // Damn you, immutable strings!!! - Cameron
                myGuessedPhrase = myGuessedPhrase.substring(0, i) + letter +
                                    myGuessedPhrase.substring(i + 1);
            }
        }
        return count;
    }

    /**
     * Checks is a provided string is the current phrase being guessed.
     * 
     * <BR><BR>NOTE: Punctuation is not ignored
     * 
     * @param guess The guessed phrase
     * @return true if the phrase is correct, false otherwise
     */
    public boolean tryToSolve(final String guess)
    {
        return (guess.equalsIgnoreCase(myPhrase));
    }
}