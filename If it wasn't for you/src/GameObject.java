/*
The GameObject is the basis for which all renderable things are built. A GameObject is anything that is rendered on screen and exists within the game space.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class GameObject extends JComponent
{
    //Transformation variables
    protected Position myPos;
    private Image sprite;
    private String ogImg;
    private Position offset = new Position();
    private double horizontalScaleFactor = 1;
    private double verticalScaleFactor = 1;

    public GameObject()
    {
        setLayout(null);
    }


    protected void paintComponent(Graphics g) //Paint function
    {
        super.paintComponent(g);
        g.drawImage(sprite, myPos.getX() - offset.x, myPos.getY() - offset.y, this);
        //Draw circle on top of Sprite to show center
        if(DebugConsole.SHOW_OBJECT_ORIGIN)
        {
            g.setColor(Color.ORANGE);
            g.drawOval(myPos.getX(), myPos.getY(),4,4);
        }
        //Show Object Position
        if(DebugConsole.SHOW_OBJECT_POSITION)
        {
            g.setColor(Color.BLACK);
            g.drawString(myPos.toString(), myPos.x, myPos.y);
        }
    }

    //Sets and gets
    public void setSprite(String path)
    {
        ogImg = path;
        try
        {
            sprite = ImageIO.read(getClass().getResource(path));
            ScaleSprite();
        }
        catch (IOException e)
        {
            GameRunner.debugConsole.AddTextToView("Image load failed: " + path);
        }
    }

    public void setSprite(Image i)
    {
        sprite = i;
        ScaleSprite();
    }

    /*
    All image resources use this file format: 'resources/Sprites/<Type>/<Type>_<Animation>_<Direction>_<Frame #>.png'
     */
    public static Image getResource(String path)
    {
        try
        {
            return ImageIO.read(GameObject.class.getResource(path));
        }
        catch (IOException e)
        {
            GameRunner.debugConsole.AddTextToView(e.toString());
        }
        return null;
    }


    public Image getSprite()
    {
        return sprite;
    }

    public Dimension getSpriteSize()
    {
        return new Dimension(sprite.getWidth(this), sprite.getHeight(this));
    }

    public void setPosition(int x, int y)
    {
        myPos.setX(x);
        myPos.setY(y);
    }

    public void setPosition(Position pos)
    {
        myPos = pos;
    }

    public Position getPosition()
    {
        return myPos;
    }

    public UIManager getUIManager()
    {
        return GameFrame.uiManager;
    }

    //Sprite Alterations
    public void ReverseSprite()
    {
        double rotationRequired = Math.toRadians(180);
        double locationX = sprite.getWidth(this) / 2;
        double locationY = sprite.getHeight(this) / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        Image revIm = op.filter((BufferedImage) sprite, null);
        sprite = revIm;
    }

    public void ScaleSprite(double factor)
    {
        //Set the variable
        horizontalScaleFactor = factor;
        verticalScaleFactor = factor;
        //Actual Math
        int width = (int) Math.round(sprite.getWidth(this) * factor);
        int height = (int) Math.round(sprite.getHeight(this) * factor);
        Image tmp = sprite.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        sprite = resized;
    }

    /*
    This method uses the internal scaleFactor variables to do its math
     */
    public void ScaleSprite()
    {
        //Actual Math
        int width = (int) Math.round(sprite.getWidth(this) * horizontalScaleFactor);
        int height = (int) Math.round(sprite.getHeight(this) * verticalScaleFactor);
        Image tmp = sprite.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        sprite = resized;
    }

    public void ScaleHorizontally(double factor)
    {
        horizontalScaleFactor = factor;
        //Actual Math
        int width = (int) Math.round(sprite.getWidth(this) * factor);
        int height = Math.round(sprite.getHeight(this));
        Image tmp = sprite.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        sprite = resized;
    }

    public void ScaleVertically(double factor)
    {
        verticalScaleFactor = factor;
        //Actual Math
        int width = Math.round(sprite.getWidth(this));
        int height = (int) Math.round(sprite.getHeight(this) * factor);
        Image tmp = sprite.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        sprite = resized;
    }

    public void ResetSprite()
    {
        try
        {
            sprite = ImageIO.read(getClass().getResource(ogImg));
        }
        catch(IOException e)
        {
            System.out.println("Reset failed");
        }
    }

    public String toString()
    {
        return myPos.toString() + "  Sprite: " + ogImg;
    }

    public boolean isEqual(GameObject g)
    {
        if(myPos == g.myPos && sprite == g.getSprite())
        {
            return true;
        }
        return false;
    }
}

//Physics things
class Position
{
    int x;
    int y;
    public final static Position ORIGIN = new Position(0,0);
    public enum Direction {NORTH, EAST, WEST, SOUTH}

    public Position()
    {
        x = 0;
        y = 0;
    }

    public Position(int sx, int sy)
    {
        x = sx;
        y = sy;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void setX(int sx)
    {
        x = sx;
    }

    public void setY(int sy)
    {
        y = sy;
    }

    public String toString()
    {
        return "( " + x + ", " + y + " )";
    }

    public boolean equals(Position other)
    {
        boolean ret = false;
        if(x == other.x && y == other.y)
        {
            ret = true;
        }
        return ret;
    }

    public static Direction getOppositeDirection(Direction dir)
    {
        switch (dir)
        {
            case NORTH:
                return Direction.SOUTH;

            case SOUTH:
                return Direction.NORTH;

            case EAST:
                return Direction.WEST;

            case WEST:
                return Direction.EAST;
        }
        return null; //Something went wrong
    }

    public static Direction getOppositeDirection(char dir)
    {
        switch (dir)
        {
            case 'S':
                return Direction.SOUTH;

            case 'W':
                return Direction.NORTH;

            case 'A':
                return Direction.WEST;

            case 'D':
                return Direction.EAST;
        }
        return null; //Something went wrong
    }

    public static char getDirectionAbbrev(Direction dir)
    {
        switch (dir)
        {
            case NORTH:
                return 'N';

            case SOUTH:
                return 'S';

            case EAST:
                return 'E';

            case WEST:
                return 'W';
        }
        return '\u0000'; //Something went wrong
    }

    public static Direction charToDirection(char c)
    {
        c = Character.toUpperCase(c);
        switch (c)
        {
            case 'W': //North
                return Direction.NORTH;

            case 'A': //West
                return Direction.WEST;

            case 'S': //South
                return Direction.SOUTH;

            case 'D': //East
                return Direction.EAST;
        }
        return null;
    }
}