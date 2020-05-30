import javax.swing.*;
import java.awt.*;

public class UIManager extends JLayeredPane
{
    //Private Variables
    private boolean showBlackScreen = false;
    private Textbox textBox;

    public UIManager(JFrame f)
    {
        //Set up the frame
        setPreferredSize(new Dimension(490,470));
        setBounds(f.getBounds());
        setLayout(null);
        setOpaque(false);
        setBackground(new Color(255, 255, 255, 0));
        //Set up the textbox image and location
        textBox = new Textbox(GameObject.getResource("/Sprites/UI/TestTextbox.jpg"), new Position(0, 400));
        add(textBox);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //Screen black
        if(showBlackScreen)
        {
            setOpaque(true);
            setBackground(new Color(0,0,0,255));
        }
        else
        {
            if(isOpaque()) //Reset after showing black screen
            {
                setOpaque(false);
                setBackground(new Color(255, 255, 255, 0));
            }

            if(Textbox.isTextboxActive()) //Show the Textbox
            {
                textBox.paintComponent(g);
            }
            else if (DebugConsole.OVERRIDE_TEXTBOX_VISUALS)
            {
                textBox.paintOverride(g);
            }
        }
    }

    public void Update()
    {
        if(Textbox.isTextboxActive())
        {
            textBox.Update();
        }

        //Update black screen
        //TODO Screen wiping
    }

    //Various Sets and Gets
    public boolean isBlackScreenActive()
    {
        return showBlackScreen;
    }

    public void setShowBlackScreen(boolean showBlackScreen)
    {
        this.showBlackScreen = showBlackScreen;
    }

    public Textbox getTextbox()
    {
        return textBox;
    }
}
