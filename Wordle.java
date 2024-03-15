import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;
import java.text.Normalizer;
import javax.swing.*;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.*;
import javax.swing.border.Border;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public abstract class Wordle {
    // Declaring variables and arrayLists
    protected String chosenWordListFileName;
    protected String chosenWordListWithoutAccentsFileName;
    protected String userDictionaryWithoutAccentsFileName;
    protected String chosenWord;
    protected String chosenWordWithoutAccents;
    protected List<String> chosenWordListWithoutAccents;
    protected List<String> chosenWordList;
    protected List<String> userWordListWithoutAccents;
    protected String result;
    protected String youWonMessage;
    protected String youLostMessage;
    protected String Again;

    // constructor
    public Wordle() {
    }

    // METHODS

    // Read the dictionary and assemble the dictionary arrayList from which to
    // choose the random chosen word
    public List<String> readDictionary(String fileName) {

        List<String> wordList = new ArrayList<>();

        try {
            // Open and read the dictionary file
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
            assert in != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String strLine;

            // Read file line By line
            while ((strLine = reader.readLine()) != null) {
                wordList.add(strLine);
            }
            // Close the input stream
            in.close();

        } catch (Exception e) {// Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        return wordList;
    }

    // get a random word from the dictionary arraylist
    public String getRandomWord(List<String> wordList) {
        Random rand = new Random(); // instance of random class
        int upperbound = wordList.size();
        // generate random values from 0 to arrayList size
        int int_random = rand.nextInt(upperbound);
        // System.out.println(wordList.get(int_random));
        return wordList.get(int_random);
    }

    // remove accents, some words have this
    public String removeAccents(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    // print definition
    public abstract String printDefinitionLink(String randomChosenWord);

    // GUi Design
    public void GUI(JFrame frame, JLabel[] Jp) {
        // Menu design
        MenuBar menubar = new MenuBar();
        Menu menu = new Menu("Menu");

        MenuItem HpMenu = new MenuItem("Help");

        MenuItem HtMenu = new MenuItem("Hint");
        // "Help" introduce the rule of Wordle
        HpMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "The game has chosen a 5-letter word for you to guess." + '\n' +
                                "You have 6 tries. In each guess, the game will confirm which letters the chosen word and the guessed word have in common:"
                                + '\n' +
                                "- Letters highlighted in " + "green"
                                + " are in the correct place." +
                                "- Letters highlighted in " + "yellow"
                                + " appear in the chosen word but in a different spot." + '\n' +
                                "- Letters highlighted in " + "grey"
                                + " do not appear in the chosen word." + '\n' +
                                "Let's try a game now! ");

            }
        });
        // "Hint" would hint a random letter of the word
        HtMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random rand = new Random();
                int int_random = rand.nextInt(5);
                switch (int_random) {
                    case 0:
                        JOptionPane.showMessageDialog(null,
                                "The " + (int_random + 1) + "st letter is " + chosenWord.charAt(int_random));
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null,
                                "The " + (int_random + 1) + "nd letter is " + chosenWord.charAt(int_random));
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(null,
                                "The " + (int_random + 1) + "rd letter is " + chosenWord.charAt(int_random));
                        break;
                    default:
                        JOptionPane.showMessageDialog(null,
                                "The " + (int_random + 1) + "th letter is " + chosenWord.charAt(int_random));
                }

            }
        });

        // assemble the menu bar
        menu.add(HpMenu);
        menu.add(HtMenu);
        menubar.add(menu);

        // add the menu bar to the frame
        frame.setMenuBar(menubar);

        // design the title panel
        Border blackline = BorderFactory.createLineBorder(new Color(185, 185, 185), 3);
        JPanel p1 = new JPanel();
        JLabel label = new JLabel("Wordle");
        p1.add(label);
        label.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
        label.setForeground(new Color(255, 255, 255));
        p1.setBackground(new Color(73, 71, 69));
        // add the Panel to the frame
        frame.add(p1, BorderLayout.NORTH);

        // create a Panel with GridLayout
        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(6, 5, 7, 7));
        p2.setBackground(new Color(73, 71, 69));

        for (int i = 0; i < 30; i++) {
            Jp[i] = new JLabel("", JLabel.CENTER);
            Jp[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
            Jp[i].setForeground(new Color(255, 255, 255));
            Jp[i].setBackground(new Color(73, 71, 69));
            Jp[i].setBorder(blackline);
            Jp[i].setOpaque(true);
            p2.add(Jp[i]);
        }
        // add the Panel to the frame
        frame.add(p2);
        // change the icon
        Toolkit tk = Toolkit.getDefaultToolkit();
        java.awt.Image img = tk.getImage("icon.jpg");
        frame.setIconImage(img);

        frame.setLocation(600, 200);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // read and judge the answer
    public void loopThroughSixGuesses(List<String> wordList, JFrame frame, JLabel[] Jp) {
        String[] a = new String[1];
        a[0] = "";
        int[] word = new int[2];
        word[0] = 0;
        word[1] = 0;
        // add a KeyListener to read in inputs
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char keyCode = e.getKeyChar();
                // support "delet"
                if (keyCode == '\b' && word[0] > 0) {
                    word[0]--;
                    Jp[word[1] * 5 + word[0]].setText("");
                    a[0] = a[0].substring(0, a[0].length() - 1);
                }
                // read in and display
                if ((keyCode >= 'a' && keyCode <= 'z') || (keyCode >= 'A' && keyCode <= 'Z')) {
                    // remove accents except the 1st letter since some words may be the first letter
                    // uppercase
                    if (keyCode >= 'A' && keyCode <= 'Z' && word[0] > 0) {
                        keyCode -= ('A' - 'a');
                    }
                    a[0] += keyCode;
                    Jp[word[1] * 5 + word[0]].setText(keyCode + "");
                    word[0]++;
                }
                // when get a word
                if (word[0] == 5) {
                    word[0] = 0;
                    if (!(wordList.contains(a[0]))) {
                        /* The word is not in the dictionary. Please, submit a new 5-letter word. */
                        a[0] = "";
                        for (int i = 0; i < 5; i++) {
                            Jp[word[1] * 5 + i].setText("");
                        }
                        JOptionPane.showMessageDialog(null,
                                "The word is not in the dictionary. Please, submit a new 5-letter word.");
                    } else {
                        // the word is answer
                        if (a[0].equals(chosenWordWithoutAccents)) {
                            /* Win */
                            for (int i = 0; i < 5; i++) {
                                Jp[word[1] * 5 + i].setBackground(Color.green);
                            }
                            // ask whether like to play another game
                            int res = JOptionPane.showConfirmDialog(null,
                                    youWonMessage + '\n' + printDefinitionLink(chosenWord) + '\n' + Again, "You Win!",
                                    JOptionPane.YES_NO_OPTION);
                            // Yes
                            if (res == JOptionPane.YES_OPTION) {
                                // reset the JLabel arrary , Frame and other variables
                                word[0] = 0;
                                word[1] = 0;
                                a[0] = "";
                                chosenWord = getRandomWord(chosenWordList);
                                chosenWordWithoutAccents = removeAccents(chosenWord);
                                for (int i = 0; i < 30; i++) {
                                    Jp[i].setText("");
                                    Jp[i].setBackground(new Color(73, 71, 69));
                                }
                            }
                            // No
                            else {
                                System.exit(0);
                            }
                        } else {
                            /* find and print the color of letters */
                            for (int i = 0; i < 5; i++) {
                                // if a letter is green, it would not be other colors
                                // if a letter is yello, it would not be gray but might be green
                                // a gray letter can be any cases
                                int IsYellow = 0, IsGreen = 0;
                                for (int j = 0; j < 5; j++) {
                                    if (a[0].charAt(i) == chosenWordWithoutAccents.charAt(j) && i == j) {
                                        Jp[word[1] * 5 + i].setBackground(Color.green);
                                        IsGreen = 1;
                                        break;
                                    } else if (a[0].charAt(i) == chosenWordWithoutAccents.charAt(j) && i != j
                                            && IsGreen == 0) {
                                        Jp[word[1] * 5 + i].setBackground(Color.yellow);
                                        IsYellow = 1;
                                    } else {
                                        if (IsGreen + IsYellow == 0)
                                            Jp[word[1] * 5 + i].setBackground(Color.gray);
                                    }
                                }
                            }
                            word[1]++;
                            a[0] = "";
                        }
                    }
                }
                // got 6 words
                if (word[1] == 6) {
                    /* lose */
                    int res = JOptionPane.showConfirmDialog(null,
                            youLostMessage + '\n' + "The answer is " + chosenWord + '\n' + Again, "Sadly!",
                            JOptionPane.YES_NO_OPTION);
                    if (res == JOptionPane.YES_OPTION) {
                        word[0] = 0;
                        word[1] = 0;
                        a[0] = "";
                        chosenWord = getRandomWord(chosenWordList);
                        chosenWordWithoutAccents = removeAccents(chosenWord);
                        for (int i = 0; i < 30; i++) {
                            Jp[i].setText("");
                            Jp[i].setBackground(new Color(73, 71, 69));
                        }
                    } else {
                        System.exit(0);
                    }
                }
            }

        });
    }

    // play method that calls on all other methods.
    public void play() {
        JFrame frame = new JFrame("Wordle");
        JLabel[] Jp = new JLabel[30];

        // Open and read the dictionary file with accents and without
        chosenWordList = this.readDictionary(chosenWordListFileName);
        chosenWordListWithoutAccents = this.readDictionary(chosenWordListWithoutAccentsFileName);
        userWordListWithoutAccents = this.readDictionary(userDictionaryWithoutAccentsFileName);
        // Selecting a random word from the dictionary
        chosenWord = getRandomWord(chosenWordList);

        // remove the accents from the word, if any
        chosenWordWithoutAccents = removeAccents(chosenWord);

        // product the GUI
        this.GUI(frame, Jp);

        // read in and judge
        this.loopThroughSixGuesses(userWordListWithoutAccents, frame, Jp);

    }

}
