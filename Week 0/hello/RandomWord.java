/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        double i = 1;
        String currentWord;
        String championWord = null;
        while (!StdIn.isEmpty()) {
            currentWord = StdIn.readString();
            //System.out.println("Just Read '" + currentWord + "'");
            if (StdRandom.bernoulli(1 / i)) {
                championWord = currentWord;
            }
            i++;
        }
        System.out.println(championWord);
    }
}
