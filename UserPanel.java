import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.List;
import java.awt.*;
import javax.swing.*;

public class UserPanel extends JPanel {
    
    //Letters
    private JLabel inputLabel;
    private JTextField letterInput;
    private JButton solveButton;

    //Numbers
    private JLabel numberInputLabel;
    private JTextField numberInput;
    private JButton solveNumbersButton;
    private JTextField targetInput;
    private JLabel targetLabel;

    //Design
    JPanel letterInputPanel;
    JPanel numberInputPanel;
    JPanel targetInputPanel;

    public UserPanel()
    {

        //Setup Panels
        letterInputPanel = new JPanel();
        numberInputPanel = new JPanel();
        targetInputPanel = new JPanel();
        //Experimental
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        letterInput = new JTextField(10);
        inputLabel = new JLabel("Input Letters:");
        solveButton = new JButton("Solve");
        solveButton.addActionListener(e->Solve(this.letterInput.getText()));
        letterInputPanel.add(inputLabel);
        letterInputPanel.add(letterInput);
        add(letterInputPanel);
        add(solveButton);


        //Numbers
        numberInputLabel = new JLabel("Enter numbers (comma seperated)");
        numberInput = new JTextField(30);
        targetInput = new JTextField(3);
        solveNumbersButton = new JButton("Solve");
        targetLabel = new JLabel("Target");

        solveNumbersButton.addActionListener(e -> {
            
            try{
                //Get string of numbers
                String numbersStr = numberInput.getText();
                String[] numbersList = numbersStr.split(", *");
                List<Integer> numbersInt = Arrays.asList(numbersList).stream().map(Integer::parseInt).collect(Collectors.toList());
                if (numbersInt.size() != 6)
                {
                    throw new NumberFormatException();
                }
                String result = SolveNumbers(numbersInt.toArray(new Integer[0]), Integer.parseInt(targetInput.getText()));
                if (result == null)
                {
                    JOptionPane.showMessageDialog(null, "Cannot be solved", "Solving failed", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(null, result, "Solved!", JOptionPane.INFORMATION_MESSAGE);
                System.out.println(result);

            }
            catch(NumberFormatException err)
            {
                JOptionPane.showMessageDialog(null, "Please enter valid numbers", "Invalid numbers", JOptionPane.ERROR_MESSAGE);
            }
        });

        numberInputPanel.add(numberInputLabel);
        numberInputPanel.add(numberInput);
        add(numberInputPanel);
        targetInputPanel.add(targetLabel);
        targetInputPanel.add(targetInput);
        add(targetInputPanel);
        add(solveNumbersButton);

    }

    /**
     * For every step required, adds step to return string.
     * Returns empty string if number already exists in list.
     * returns None if not possible on route.
     * @param numbers
     * @param target
     * @return "" if found, null if impossible and string of steps if steps required
     */
    private String SolveNumbers(Integer[] numbers, int target)
    {
        List<Integer> numbersList = Arrays.asList(numbers);
        for (int n : numbers)
        {
            if (n == target)
            {
                return ""; //Works, return Success
            }
        }
        if (numbers.length == 1){return null;}
        for (int i = 1; i < numbers.length; i++)
        {
            for (int j = 0; j < i; j++)
            {
                //Every possible combination not including itself
                int a = numbers[i];
                int b = numbers[j];
                String resultString;
                ArrayList<Integer> ammended = new ArrayList<Integer>(numbersList);
                ammended.remove((Object)a);
                ammended.remove((Object)b);

                String step;
                int newValue;
                List<Integer> preparedList;

                //Do every possible order of operation

                //Add
                step = "Add " + String.valueOf(a) + " and " + String.valueOf(b);
                newValue = a+b;
                preparedList = new ArrayList<>(ammended);
                preparedList.add(newValue);
                resultString = SolveNumbers(preparedList.toArray(new Integer[0]), target);
                if (resultString != null) //Success
                {
                    return step + ",\n" + resultString;
                }

                //Multiply
                step = "Multiply " + String.valueOf(a) + " and " + String.valueOf(b);
                newValue = a*b;
                preparedList = new ArrayList<>(ammended);
                preparedList.add(newValue);
                resultString = SolveNumbers(preparedList.toArray(new Integer[0]), target);
                if (resultString != null) //Success
                {
                    return step + ",\n" + resultString;
                }

                //Divide
                //Make sure for a/b -> a > b then check if divides evenly.
                if (a < b){ //Ensures a >= b
                    int temp = a;
                    a = b;
                    b = temp;
                }
                
                if (b != 0 && a%b == 0 ){
                    step = "Divide " + String.valueOf(a) + " by " + String.valueOf(b);
                    newValue = a/b;
                    preparedList = new ArrayList<>(ammended);
                    preparedList.add(newValue);
                    resultString = SolveNumbers(preparedList.toArray(new Integer[0]), target);
                    if (resultString != null) //Success
                    {
                        return step + ",\n" + resultString;
                    }
                }

                //Subtract
                //Make sure for a-b -> a > b
                if (a < b){ //Ensures a >= b
                    int temp = a;
                    a = b;
                    b = temp;
                }
                step = "Subtract " + String.valueOf(b) + " from " + String.valueOf(a);
                newValue = a-b;
                preparedList = new ArrayList<>(ammended);
                preparedList.add(newValue);
                resultString = SolveNumbers(preparedList.toArray(new Integer[0]), target);
                if (resultString != null) //Success
                {
                    return step + ",\n" + resultString;
                }

            }
        }
        return null;
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
