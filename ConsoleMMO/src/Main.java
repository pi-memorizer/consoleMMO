import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main
{
	public Player [] players;
	public int numPlayers = 0;
	public int maxPlayers = 0;
	public final int maxUsers = 10000;
	public int numUsers = 0;
	public Console console = new Console();
	public int port = 0;
	public ArrayList<Player> users;
	ServerSocket servSock;
	Map [] maps;
	int numMaps;
	Block [] blocks;
	
	public static void main(String [] args)
	{
		Main main = new Main();
		main.run();
	}
	
	public void run()
	{
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream("server.dat"));
			numUsers = dis.readInt();
			if(numUsers>=maxUsers*7/10)
				users = new ArrayList<Player>(maxUsers);
			else
				users = new ArrayList<Player>(numUsers*3/2);
			for(int i = 0; i < numUsers; i++)
				users.add(i,readPlayer(dis,i));
			dis.close();
		} catch(Exception e) {users = new ArrayList<Player>(100);}
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream("server-maps.dat"));
			numMaps = dis.readInt();
			maps = new Map[numMaps];
			for(int i = 0; i < numMaps; i++)
			{
				maps[i] = new Map();
				DataInputStream dis2 = new DataInputStream(new FileInputStream("maps/" + i + ".mmap"));
				maps[i].size = dis2.readInt();
				maps[i].map = new short[maps[i].size][maps[i].size];
				for(int x = 0; x < maps[i].size; x++)
					for(int y = 0; y < maps[i].size; y++)
						maps[i].map[x][y] = dis2.readShort();
				maps[i].doors = new Door[dis2.readInt()];
				for(int x = 0; x < maps[i].doors.length; x++)
				{
					maps[i].doors[x] = new Door();
					maps[i].doors[x].inX = dis2.readShort();
					maps[i].doors[x].inY = dis2.readShort();
					maps[i].doors[x].outX = dis2.readShort();
					maps[i].doors[x].outY = dis2.readShort();
					maps[i].doors[x].outZ = dis2.readShort();
				}
				maps[i].name = dis2.readUTF();
				dis2.close();
				maps[i].data = new MapData();
				maps[i].data.canBeOwned = dis.readBoolean();
				maps[i].data.isOwned = dis.readBoolean();
				maps[i].data.owner = dis.readInt();
				maps[i].data.price = dis.readLong();
				maps[i].data.accessLevel = dis.readByte();
			}
			dis.close();
		} catch(Exception e) {e.printStackTrace(); System.exit(0);}
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream("blocks.dat"));
			int numBlocks = dis.readInt();
			blocks = new Block[numBlocks];
			for(int i = 0; i < numBlocks; i++)
			{
				blocks[i] = new Block();
				blocks[i].character = dis.readChar();
				blocks[i].color = dis.readByte();
				blocks[i].action = dis.readShort();
				blocks[i].altText = dis.readUTF();
			}
			dis.close();
		} catch (Exception e) {e.printStackTrace(); System.exit(0);}
		console.out("Max Players: ");
		console.display();
		maxPlayers = console.getInt();
		players = new Player[maxPlayers];
		console.out("Port: ");
		console.display();
		port = console.getInt();
		new Server(this).start();
		try {
			servSock = new ServerSocket(port);
			while(true)
			{
				Socket sock = servSock.accept();
				sock.setSoTimeout(10000);
				sock.setSoLinger(true, 10000);
				new Request(this,sock).start();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Player readPlayer(DataInputStream dis, int id)
	{
		Player p = new Player();
		p.id = id;
		try {
			p.username = dis.readUTF();
			p.money = dis.readLong();
			p.health = dis.readInt();
			p.maxHealth = dis.readInt();
			p.x = dis.readInt();
			p.y = dis.readInt();
			p.z = dis.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public void writePlayer(DataOutputStream dos, Player p)
	{
		try {
			dos.writeUTF(p.username);
			dos.writeLong(p.money);
			dos.writeInt(p.health);
			dos.writeInt(p.maxHealth);
			dos.writeInt(p.x);
			dos.writeInt(p.y);
			dos.writeInt(p.z);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class Server extends Thread {
	Main main;
	
	public Server(Main main)
	{
		this.main = main;
	}
	
	public void run()
	{
		while(true)
		{
			String s = main.console.getString();
			if(s.equalsIgnoreCase("stop"))
			{
				try {
					main.servSock.close();
					DataOutputStream dos = new DataOutputStream(new FileOutputStream("server.dat"));
					dos.writeInt(main.numUsers);
					for(int i = 0; i < main.numUsers; i++)
						main.writePlayer(dos,main.users.get(i));
					dos.close();
					System.exit(0);
				} catch (Exception e) {e.printStackTrace();}
			}
		}
	}
}

class Block
{
	char character;
	byte color;
	short action;
	String altText;
}