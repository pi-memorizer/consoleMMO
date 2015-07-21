import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class VirtualConsole
{
	private byte [][] colorBuffer = new byte[80][25];
	private char [][] charBuffer = new char[80][25];
	private int cursorX = 0;
	private int cursorY = 0;
	private byte cursorColor = 0x07;
	public static char BACKSPACE = 8;
	public static char ENTER = 10;
	ArrayList<Link> links = new ArrayList<Link>();
	
	public VirtualConsole()
	{
		for(int x = 0; x < 80; x++)
			for(int y = 0; y < 25; y++)
			{
				colorBuffer[x][y] = 0x07;
				charBuffer[x][y] = ' ';
			}
	}
	
	public void writeConsole(DataOutputStream d)
	{
		String s = "";
		byte lastColor = 0x07;
		for(int y = 0; y < 25; y++)
			for(int x = 0; x < 80; x++)
			{
				if(colorBuffer[x][y]!=lastColor)
				{
					s += (char)0;
					s += (char)colorBuffer[x][y];
					lastColor = colorBuffer[x][y];
				}
				s += charBuffer[x][y];
			}
		try {
			d.writeUTF(s);
		} catch (IOException e) {
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
				cursorX = 80-1;
				cursorY--;
				if(cursorY<0)
					cursorY = 0;
			}
		}
		if(cursorX==80) {
			cursorX = 0;
			cursorY++;
		}
		if(cursorY==25) {
			cursorY = 24;
			cursorX = 0;
		}
	}
	
	public void out(String s) {
		for(int i = 0; i < s.length(); i++)
		{
			if(s.charAt(i)==(char)0)
				System.out.println(s + " : " + i);
			out(s.charAt(i));
		}
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
		if(x<80&&x>=0)
			cursorX = x;
		if(y<25&&y>=0)
			cursorY = y;
		out(c);
	}
	
	public void out(String s, int x, int y) {
		if(x<80&&x>=0)
			cursorX = x;
		if(y<25&&y>=0)
			cursorY = y;
		out(s);
	}
	
	public void out(long l, int x, int y) {
		if(x<80&&x>=0)
			cursorX = x;
		if(y<25&&y>=0)
			cursorY = y;
		out(l);
	}
	
	public void out(double d, int x, int y) {
		if(x<80&&x>=0)
			cursorX = x;
		if(y<25&&y>=0)
			cursorY = y;
		out(d);
	}
	
	public void out(float f, int x, int y) {
		if(x<80&&x>=0)
			cursorX = x;
		if(y<25&&y>=0)
			cursorY = y;
		out(f);
	}
	
	public void out(int i, int x, int y) {
		if(x<80&&x>=0)
			cursorX = x;
		if(y<25&&y>=0)
			cursorY = y;
		out(i);
	}
	
	public void out(short s, int x, int y) {
		if(x<80&&x>=0)
			cursorX = x;
		if(y<25&&y>=0)
			cursorY = y;
		out(s);
	}
	
	public void out(byte b, int x, int y) {
		if(x<80&&x>=0)
			cursorX = x;
		if(y<25&&y>=0)
			cursorY = y;
		out(b);
	}
	
	public void out(char c, int x, int y, byte i) {
		if(x<80&&x>=0)
			cursorX = x;
		if(y<25&&y>=0)
			cursorY = y;
		cursorColor = i;
		out(c);
	}
	
	public void out(String s, int x, int y, byte i) {
		if(x<80&&x>=0)
			cursorX = x;
		if(y<25&&y>=0)
			cursorY = y;
		cursorColor = i;
		out(s);
	}
	
	public void out(long l, int x, int y, byte i) {
		if(x<80&&x>=0)
			cursorX = x;
		if(y<25&&y>=0)
			cursorY = y;
		cursorColor = i;
		out(l);
	}
	
	public void out(double d, int x, int y, byte i) {
		if(x<80&&x>=0)
			cursorX = x;
		if(y<25&&y>=0)
			cursorY = y;
		cursorColor = i;
		out(d);
	}
	
	public void out(float f, int x, int y, byte i) {
		if(x<80&&x>=0)
			cursorX = x;
		if(y<25&&y>=0)
			cursorY = y;
		cursorColor = i;
		out(f);
	}
	
	public void out(int i, int x, int y, byte c) {
		if(x<80&&x>=0)
			cursorX = x;
		if(y<25&&y>=0)
			cursorY = y;
		cursorColor = c;
		out(i);
	}
	
	public void out(short s, int x, int y, byte i) {
		if(x<80&&x>=0)
			cursorX = x;
		if(y<25&&y>=0)
			cursorY = y;
		cursorColor = i;
		out(s);
	}
	
	public void out(byte b, int x, int y, byte i) {
		if(x<80&&x>=0)
			cursorX = x;
		if(y<25&&y>=0)
			cursorY = y;
		cursorColor = i;
		out(b);
	}
	
	public void gotoxy(int x, int y) {
		if(x<80&&x>=0)
			cursorX = x;
		if(y<25&&y>=0)
			cursorY = y;
	}
	
	public void color(int i) {
		cursorColor = (byte)i;
	}
	
	public void addLink(int x1, int x2, int y1, int y2, ByteBuffer b, String altText)
	{
		Link l = new Link();
		l.type = (byte)0;
		l.x1 = (byte) x1;
		l.x2 = (byte) x2;
		l.y1 = (byte) y1;
		l.y2 = (byte) y2;
		l.data = b.array();
		l.altText = altText;
		links.add(l);
	}
	
	public void addLink(int type, int x1, int x2, int y1, int y2, ByteBuffer b, String altText)
	{
		Link l = new Link();
		l.type = (byte)type;
		l.x1 = (byte) x1;
		l.x2 = (byte) x2;
		l.y1 = (byte) y1;
		l.y2 = (byte) y2;
		l.data = b.array();
		l.altText = altText;
		links.add(l);
	}
	
	public void writeLinks(DataOutputStream d)
	{
		try {
			d.writeShort(links.size());
			for(int i = 0; i < links.size(); i++)
			{
				Link l = links.get(i);
				d.writeByte(l.type);
				d.writeByte(l.x1);
				d.writeByte(l.x2);
				d.writeByte(l.y1);
				d.writeByte(l.y2);
				d.writeShort(l.data.length);
				d.write(l.data);
				d.writeUTF(l.altText);
			}
		} catch(Exception e) {e.printStackTrace();}
	}
}

class Link
{
	byte type;
	byte x1,x2,y1,y2;
	byte [] data;
	String altText;
}