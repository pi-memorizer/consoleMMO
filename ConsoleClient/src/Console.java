import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Console extends JFrame
{
	private Font font;
	private int width = 640, height = 300;
	private int bufferWidth, bufferHeight;
	private byte colorBuffer[][];
	private char charBuffer[][];
	private int cursorX, cursorY;
	private byte cursorColor = 0x07;
	private Color[] colors = {new Color(0,0,0), new Color(0,0,128), new Color(0,128,0), new Color(0,128,128), new Color(128,0,0), new Color(128,0,128), new Color(128,128,0), new Color(192,192,192), new Color(128,128,128), new Color(0,0,255), new Color(0,255,0), new Color(0,255,255), new Color(255,0,0), new Color(255,0,255), new Color(255,255,0), new Color(255,255,255)};
	private static final long serialVersionUID = 1L;
	private BufferedImage offscreenImage;
	private Graphics2D offscreenG;
	private BufferedImage overImage;
	private Graphics2D overImageG;
	private int lastKey = 0;
	private char lastChar = 0;
	private boolean keyPressed = false;
	public static char BACKSPACE = 8;
	public static char ENTER = 10;
	public boolean [] keys = new boolean[256];
	
	public Console() {
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getClassLoader().getResource("TerminalVector.ttf").openStream());
			font = font.deriveFont(12.0F);
			bufferWidth = width / 8;
			bufferHeight = height / 12;
			colorBuffer = new byte[bufferWidth][bufferHeight];
			charBuffer = new char[bufferWidth][bufferHeight];
			for(int x = 0; x < bufferWidth; x++)
				for(int y = 0; y < bufferHeight; y++)
				{
					colorBuffer[x][y] = 0x07;
					charBuffer[x][y] = ' ';
				}
			offscreenImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			offscreenG = offscreenImage.createGraphics();
			offscreenG.setFont(font);
			overImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			overImageG = overImage.createGraphics();
			addWindowListener(new WindowListener(){
				public void windowActivated(WindowEvent arg0) {}
				public void windowClosed(WindowEvent arg0) {}
				public void windowClosing(WindowEvent arg0) {System.exit(0);}
				public void windowDeactivated(WindowEvent arg0) {}
				public void windowDeiconified(WindowEvent arg0) {}
				public void windowIconified(WindowEvent arg0) {}
				public void windowOpened(WindowEvent arg0) {}
			});
			addKeyListener(new KeyListener(){
				public void keyPressed(KeyEvent arg0) {
					lastKey = arg0.getKeyCode();
					lastChar = arg0.getKeyChar();
					keyPressed = true;
					keys[lastChar] = true;
				}
				public void keyReleased(KeyEvent arg0) {
					keys[arg0.getKeyChar()] = false;
				}
				public void keyTyped(KeyEvent arg0) {}
			});
			setResizable(false);
			setVisible(true);
			setSize(width + getInsets().left+getInsets().right, height + getInsets().top+getInsets().bottom);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void out(char c) {
		if(c!=ENTER&&c!=BACKSPACE)
		{
			charBuffer[cursorX][cursorY] = c;
			colorBuffer[cursorX][cursorY] = cursorColor;
			cursorX++;
		} else if(c==ENTER)
		{
			cursorY++;
			cursorX = 0;
		} else {
			cursorX--;
			if(cursorX<0)
			{
				cursorX = bufferWidth-1;
				cursorY--;
				if(cursorY<0)
					cursorY = 0;
			}
		}
		if(cursorX==bufferWidth) {
			cursorX = 0;
			cursorY++;
		}
		if(cursorY==bufferHeight) {
			cursorX = 0;
			cursorY--;
		}
	}
	
	public void out(String s) {
		for(int i = 0; i < s.length(); i++)
			out(s.charAt(i));
	}
	
	public void out(long l) {
		out(Long.toString(l));
	}
	
	public void out(double d) {
		out(Double.toString(d));
	}
	
	public void out(float f) {
		out(Float.toString(f));
	}
	
	public void out(int i) {
		out((long)i);
	}
	
	public void out(short s) {
		out((long)s);
	}
	
	public void out(byte b) {
		out((long)b);
	}
	
	public void out(char c, byte i) {
		cursorColor = i;
		out(c);
	}
	
	public void out(String s, byte i) {
		cursorColor = i;
		out(s);
	}
	
	public void out(long l, byte i) {
		cursorColor = i;
		out(l);
	}
	
	public void out(double d, byte i) {
		cursorColor = i;
		out(d);
	}
	
	public void out(float f, byte i) {
		cursorColor = i;
		out(f);
	}
	
	public void out(int i, byte c) {
		cursorColor = c;
		out(i);
	}
	
	public void out(short s, byte i) {
		cursorColor = i;
		out(s);
	}
	
	public void out(byte b, byte i) {
		cursorColor = i;
		out(b);
	}
	
	public void out(char c, int x, int y) {
		if(x<bufferWidth&&x>=0)
			cursorX = x;
		if(y<bufferHeight&&y>=0)
			cursorY = y;
		out(c);
	}
	
	public void out(String s, int x, int y) {
		if(x<bufferWidth&&x>=0)
			cursorX = x;
		if(y<bufferHeight&&y>=0)
			cursorY = y;
		out(s);
	}
	
	public void out(long l, int x, int y) {
		if(x<bufferWidth&&x>=0)
			cursorX = x;
		if(y<bufferHeight&&y>=0)
			cursorY = y;
		out(l);
	}
	
	public void out(double d, int x, int y) {
		if(x<bufferWidth&&x>=0)
			cursorX = x;
		if(y<bufferHeight&&y>=0)
			cursorY = y;
		out(d);
	}
	
	public void out(float f, int x, int y) {
		if(x<bufferWidth&&x>=0)
			cursorX = x;
		if(y<bufferHeight&&y>=0)
			cursorY = y;
		out(f);
	}
	
	public void out(int i, int x, int y) {
		if(x<bufferWidth&&x>=0)
			cursorX = x;
		if(y<bufferHeight&&y>=0)
			cursorY = y;
		out(i);
	}
	
	public void out(short s, int x, int y) {
		if(x<bufferWidth&&x>=0)
			cursorX = x;
		if(y<bufferHeight&&y>=0)
			cursorY = y;
		out(s);
	}
	
	public void out(byte b, int x, int y) {
		if(x<bufferWidth&&x>=0)
			cursorX = x;
		if(y<bufferHeight&&y>=0)
			cursorY = y;
		out(b);
	}
	
	public void out(char c, int x, int y, byte i) {
		if(x<bufferWidth&&x>=0)
			cursorX = x;
		if(y<bufferHeight&&y>=0)
			cursorY = y;
		cursorColor = i;
		out(c);
	}
	
	public void out(String s, int x, int y, byte i) {
		if(x<bufferWidth&&x>=0)
			cursorX = x;
		if(y<bufferHeight&&y>=0)
			cursorY = y;
		cursorColor = i;
		out(s);
	}
	
	public void out(long l, int x, int y, byte i) {
		if(x<bufferWidth&&x>=0)
			cursorX = x;
		if(y<bufferHeight&&y>=0)
			cursorY = y;
		cursorColor = i;
		out(l);
	}
	
	public void out(double d, int x, int y, byte i) {
		if(x<bufferWidth&&x>=0)
			cursorX = x;
		if(y<bufferHeight&&y>=0)
			cursorY = y;
		cursorColor = i;
		out(d);
	}
	
	public void out(float f, int x, int y, byte i) {
		if(x<bufferWidth&&x>=0)
			cursorX = x;
		if(y<bufferHeight&&y>=0)
			cursorY = y;
		cursorColor = i;
		out(f);
	}
	
	public void out(int i, int x, int y, byte c) {
		if(x<bufferWidth&&x>=0)
			cursorX = x;
		if(y<bufferHeight&&y>=0)
			cursorY = y;
		cursorColor = c;
		out(i);
	}
	
	public void out(short s, int x, int y, byte i) {
		if(x<bufferWidth&&x>=0)
			cursorX = x;
		if(y<bufferHeight&&y>=0)
			cursorY = y;
		cursorColor = i;
		out(s);
	}
	
	public void out(byte b, int x, int y, byte i) {
		if(x<bufferWidth&&x>=0)
			cursorX = x;
		if(y<bufferHeight&&y>=0)
			cursorY = y;
		cursorColor = i;
		out(b);
	}
	
	public void cls() {
		for(int x = 0; x < bufferWidth; x++)
			for(int y = 0; y < bufferHeight; y++)
			{
				colorBuffer[x][y] = cursorColor;
				charBuffer[x][y] = ' ';
			}
		cursorX = 0;
		cursorY = 0;
	}
	
	public void gotoxy(int x, int y) {
		if(x<bufferWidth&&x>=0)
			cursorX = x;
		if(y<bufferHeight&&y>=0)
			cursorY = y;
	}
	
	public void color(int i) {
		cursorColor = (byte)i;
	}
	
	public void display() {
		repaint();
	}
	
	public void paint(Graphics g) {
		for(int x = 0; x < bufferWidth; x++)
			for(int y = 0; y < bufferHeight; y++)
			{
				offscreenG.setColor(colors[(colorBuffer[x][y]&0xF0)/16]);
				offscreenG.fillRect(x*8, y*12, 8, 12);
				offscreenG.setColor(colors[colorBuffer[x][y]&0xF]);
				offscreenG.drawString("" + charBuffer[x][y],x*8,y*12+10);
			}
		offscreenG.drawImage(overImage,getInsets().left,getInsets().top,this);
		g.drawImage(offscreenImage, getInsets().left, getInsets().top, this);
	}
	
	public void update(Graphics g)
	{
		paint(g);
	}
	
	public void changeSize(int x, int y) {
		width = x * 8;
		height = y * 12;
		bufferWidth = x;
		bufferHeight = y;
		colorBuffer = new byte[bufferWidth][bufferHeight];
		charBuffer = new char[bufferWidth][bufferHeight];
		for(int v = 0; v < bufferWidth; v++)
			for(int w = 0; w < bufferHeight; w++)
			{
				colorBuffer[v][w] = 0x07;
				charBuffer[v][w] = ' ';
			}
		offscreenImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		offscreenG = offscreenImage.createGraphics();
		offscreenG.setFont(font);
		overImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		overImageG = overImage.createGraphics();
		setSize(width + getInsets().left+getInsets().right, height + getInsets().top+getInsets().bottom);
	}
	
	public void clearOverImage() {
		overImageG.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		overImageG.fillRect(0,0,width,height);
		overImageG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
	}
	
	public Graphics2D getOverImageGraphics() {
		return overImageG;
	}
	
	public char getChar() {
		while(!keyPressed||!isLastCharValid()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
		}
		keyPressed = false;
		return lastChar;
	}
	
	public char getExtendedChar() {
		while(!keyPressed||(!(isLastCharValid()||lastChar==8||lastChar==10))) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
		}
		keyPressed = false;
		return lastChar;
	}
	
	public int getKeyCode() {
		if(keyPressed)
			return lastKey;
		else
			return 0;
	}
	
	public String getString() {
		return getString(-1);
	}
	
	public String getString(int limit) {
		String s = "";
		int sl = 0;
		char c = getExtendedChar();
		while(c!=ENTER) {
			if(c==BACKSPACE)
			{
				if(sl>0) {
					if(sl!=1)
						s = s.substring(0,sl-1);
					else
						s = "";
					sl--;
					cursorX--;
					if(cursorX<0)
					{
						cursorX = bufferWidth-1;
						cursorY--;
						if(cursorY<0)
							cursorY = 0;
					}
					out(' ');
					cursorX--;
					if(cursorX<0)
					{
						cursorX = bufferWidth-1;
						cursorY--;
						if(cursorY<0)
							cursorY = 0;
					}
					display();
				}
			} else {
				if(sl<limit||limit==-1) {
					s += c;
					sl++;
					out(c);
					display();
				}
			}
			c = getExtendedChar();
		}
		out(ENTER);
		return s;
	}
	public long getLong() {
		String s = getString();
		return Long.parseLong(s);
	}
	
	public double getDouble() {
		String s = getString();
		return Double.parseDouble(s);
	}
	
	public float getFloat() {
		return (float)getDouble();
	}
	
	public int getInt() {
		return (int)getLong();
	}
	
	public short getShort() {
		return (short)getLong();
	}
	
	public byte getByte() {
		return (byte)getLong();
	}
	
	private boolean isLastCharValid() {
		if(lastChar>='a'&&lastChar<='z')
			return true;
		if(lastChar>='A'&&lastChar<='Z')
			return true;
		if(lastChar>='0'&&lastChar<='9')
			return true;
		if(lastChar=='`'||lastChar=='~'||lastChar=='!'||lastChar=='@'||lastChar=='#'||lastChar=='$'
			||lastChar=='%'||lastChar=='^'||lastChar=='&'||lastChar=='*'||lastChar=='('
			||lastChar==')'||lastChar=='-'||lastChar=='_'||lastChar=='+'||lastChar=='='
			||lastChar=='['||lastChar=='{'||lastChar==']'||lastChar==']'||lastChar=='\\'
			||lastChar=='|'||lastChar==';'||lastChar==':'||lastChar=='\''||lastChar=='"'
			||lastChar==','||lastChar=='<'||lastChar=='.'||lastChar=='>'||lastChar=='/'
			||lastChar=='?'||lastChar==' ')
			return true;
		return false;
	}
	
	public void changeColor(int i, Color c) {
		if(i>=0&&i<=15)
			colors[i] = c;
	}

	public String getPassword(int limit) {
		String s = "";
		int sl = 0;
		char c = getExtendedChar();
		while(c!=ENTER) {
			if(c==BACKSPACE)
			{
				if(sl>0) {
					if(sl!=1)
						s = s.substring(0,sl-1);
					else
						s = "";
					sl--;
					cursorX--;
					if(cursorX<0)
					{
						cursorX = bufferWidth-1;
						cursorY--;
						if(cursorY<0)
							cursorY = 0;
					}
					out(' ');
					cursorX--;
					if(cursorX<0)
					{
						cursorX = bufferWidth-1;
						cursorY--;
						if(cursorY<0)
							cursorY = 0;
					}
					display();
				}
			} else {
				if(sl<limit||limit==-1) {
					s += c;
					sl++;
					out('*');
					display();
				}
			}
			c = getExtendedChar();
		}
		out(ENTER);
		return s;
	}
}