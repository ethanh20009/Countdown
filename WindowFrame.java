import javax.swing.JFrame;

public class WindowFrame extends JFrame{

    private UserPanel userPanel;
    
    public WindowFrame()
    {
        userPanel = new UserPanel();
        add(userPanel);
        pack();
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
