import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class Testing {
    
    @Test
    public void testFile()
    {
        String filePath = "C:\\Users\\ethan\\Documents\\Java\\Countdown\\words.txt";
        try {
            ArrayList<String> allWords = FileHandler.getAllWords(filePath);
            System.out.println(allWords.get(allWords.size()-1));
        }
        catch(IOException e){
            e.printStackTrace();
            fail("Error occured");
        }

    }   
}
