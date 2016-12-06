package benjamin.shoppingapplication.Controller;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This is a singleton to ensure that multiple instantiations of this program are not used
 * Created by Benjamin on 12/2/2016.
 */

public class TextCommandController {
    private static final TextCommandController instance = new TextCommandController();
    private List<String> search_words = new LinkedList<>();

    private TextCommandController () {
        //ensure the user cannot instantiate this controller
    }

    public static TextCommandController getInstance () {
        return instance;
    }

    /*****
     * This will find the command that needs to be executed if it does not find a command then
     * "none" will be returned and the program can decide how to act accordingly.
     * @param inputText - This is the text from the search box to ensure that the commands are found
     *                  based on the user input
     * @return - This will return a command { "none", "camera", "search" }
     */
    public String decideAction(String inputText) {

        populateSearchWords(inputText);

        String command = "none";    // defaults to null
        boolean camera = false;

        for (int currentWordIndex = 0; currentWordIndex < search_words.size(); currentWordIndex++) {

            String word = search_words.get(currentWordIndex);
            if (word.equalsIgnoreCase("search")) {
                Log.i("TextCommandController", "Will open the search feature will execute a search");

                // Make sure the word will not be included in the new search value
                search_words.remove(currentWordIndex);

                command = "search";
                break;
            } else if (word.equalsIgnoreCase("open")) {

                // make sure the word will not be included in the next search value
                search_words.remove(currentWordIndex);
                // make sure that all the words are checked
                currentWordIndex--;

                camera = true;
            }  else if (camera && word.equalsIgnoreCase("camera")) {
                Log.i("TextCommandController", "Opens the camera so that the user can scan a upc");

                // Check that any command word is removed from the search value
                search_words.remove(currentWordIndex);

                command = "camera";
                break;
            }
        }

        return command;
    }


    private void populateSearchWords(String inputText) {
        inputText = inputText.replace(".", "");
        inputText = inputText.replace(",", "");
        inputText = inputText.replace(";", "");
        inputText = inputText.replace("!", "");
        inputText = inputText.replace("#", "");

        String [] words = inputText.split(" ");


        // create a linked list that will be able to remove the search words
        for (int i = 0; i < words.length; i++) {
            search_words.add(words[i]);
        }
    }

    /**
     * This will generate the new search value without the commands
     * @return - the searchString without the commands
     */
    public String generateNewSearchValue() {
        Log.i("TextCommandController", "Nothing found do the normal search");

        String searchString = "";
        for (int i = 0; i < search_words.size(); i++) {
            searchString += search_words.get(i);
            if (i != search_words.size() - 1) {
                searchString += " ";
            }
        }

        return searchString;   // pass by reference it should return the updated search string
    }
}

