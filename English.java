
public class English extends Wordle {

    // variables

    public English() { //constructor
        super(); //use the constructor from parent class
        chosenWordListFileName = "dictionary.txt";
        chosenWordListWithoutAccentsFileName = "dictionary.txt";
        userDictionaryWithoutAccentsFileName = "extended-dictionary.txt";
        result = "Result: ";
        youWonMessage = "CONGRATULATIONS! YOU WON! :)";
        youLostMessage = "YOU LOST :( THE WORD CHOSEN BY THE GAME IS: ";
        Again = "Do you want to play again?";
    }

    // METHODS


    public String printDefinitionLink (String randomChosenWord) { // prints the link to the dictionary definition of the chosen word
        //JOptionPane.showMessageDialog(null, "The word's definition: https://www.merriam-webster.com/dictionary/" + randomChosenWord);
        return "The word's definition: https://www.merriam-webster.com/dictionary/" + randomChosenWord;
    }

}