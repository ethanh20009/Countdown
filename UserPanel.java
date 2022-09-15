import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;

public class UserPanel extends JPanel {
    
    private JLabel inputLabel;
    private JTextField letterInput;
    private JButton solveButton;

    public UserPanel()
    {
        letterInput = new JTextField(10);
        inputLabel = new JLabel("Input Letters:");
        solveButton = new JButton("Solve");
        solveButton.addActionListener(e->Solve(this.letterInput.getText()));
        add(inputLabel);
        add(letterInput);
        add(solveButton);

    }

    public void Solve(String letters)
    {
        System.out.println("Letters: " + letters);
        if (letters.length() != 9)
        {
            JOptionPane.showMessageDialog(null, "Please enter all 9 letters", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(letters);
            return;
        }
        
        //Solve
        HashMap<Character, Integer> letterDict = getDictionary(letters);

        try{

            ArrayList<String> words = FileHandler.getAllWords("words.txt");
            for (int i = words.size()-1; i > -1; i--)
            {
                String word = words.get(i);
                HashMap<Character, Integer> dictWord = getDictionary(word);
                boolean failed = false;
                for (char c : dictWord.keySet())
                {
                    if (!letterDict.containsKey(c))
                    {
                        failed = true;
                        break;
                    }
                    if (dictWord.get(c) > letterDict.get(c))
                    {
                        failed = true;
                        break;
                    }
                }
                if (!failed)
                {
                    JOptionPane.showMessageDialog(null, "Answer: " + word, "Solved!", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                
            }
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "File read error", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    private HashMap<Character, Integer> getDictionary(String string)
    {
        HashMap<Character, Integer> dict = new HashMap<>();
        for (char c : string.toCharArray())
        {
            if (dict.containsKey(c))
            {
                dict.put(c, dict.get(c) + 1);
            }
            else{
                dict.put(c, 1);
            }
        }
        return dict;
    }
    
}
