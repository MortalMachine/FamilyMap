package Model;

import java.util.Random;

/**
 * RandomAlphanumeric creates random alphanumeric strings to be used inside the AuthToken class for creating an AuthToken object.
 */
public class RandomAlphanumeric
{
	/**
	 * This string contains the alphabet.
	 */
    private final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
	/**
	 * This char array holds uppercase letters, lowercase letters, and single digit numbers.
	 */
    private final char[] ALPHANUMERIC = (LETTERS + LETTERS.toUpperCase() + "0123456789").toCharArray();

	/**
	 * Creates a StringBuilder object and appends a randomly-selected value from the ALPHANUMERIC char array 8 times.
	 * @return Returns a randomly-assembled alphanumeric string of length 8.
	 */
    public String generateRandomAlphanumeric()
    {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 8; i++)
        {
            result.append(ALPHANUMERIC[new Random().nextInt(ALPHANUMERIC.length)]);
        }
        return result.toString();
    }
}
