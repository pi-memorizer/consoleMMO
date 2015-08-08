import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Request extends Thread
{
	Main main;
	Socket sock;
	int id = -1;
	
	public Request(Main main, Socket sock)
	{
		this.main = main;
		this.sock = sock;
	}
	
	public void run()
	{
		BufferedOutputStream bos = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		try
		{
			bos = new BufferedOutputStream(sock.getOutputStream(), 5000);
			dos = new DataOutputStream(bos);
			dis = new DataInputStream(sock.getInputStream());
			short command = -1;
			boolean readBool = dis.readBoolean();
			//if(dis.readBoolean())
			if(readBool)
			{
				id = dis.readInt();
				if(main.players[id]!=null)
					if((main.users.get(main.players[id].id).flags&1)==1)
					{
						if(Arrays.equals(sock.getInetAddress().getAddress(),main.users.get(main.players[id].id).address))
						{
							command = dis.readShort();
						} else command = -4;
					}
			} else {
				if(main.numPlayers<main.maxPlayers)
				{
					String username = dis.readUTF();
					String password = dis.readUTF();
					File file = new File("players/" + username.charAt(0) + "/" + username.charAt(1) + "/" + username.substring(2));
					if(file.exists())
					{
						FileInputStream fis = new FileInputStream(file);
						try {
							DataInputStream dis2 = new DataInputStream(fis);
							if(password.equals(dis2.readUTF()))
							{
								for(int i = 0; i < main.maxPlayers; i++)
									if(main.players[i]==null)
									{
										main.players[i] = main.users.get(dis2.readInt());
										main.numPlayers++;
										id = i;
										//id = main.players[i].id;
										main.players[id].flags |= 1;
										main.players[id].address = sock.getInetAddress().getAddress();
										command = 1;
										break;
									}
							} else command = -2;
							dis2.close();
						} catch(Exception e)
						{
							e.printStackTrace();
						}
					} else {
						if(main.numUsers<main.maxUsers)
						{
							new File("players/" + username.charAt(0) + "/" + username.charAt(1)).mkdirs();
							FileOutputStream fos = new FileOutputStream(file);
							DataOutputStream dos2 = new DataOutputStream(fos);
							dos2.writeUTF(password);
							int playerNum = main.numUsers++;
							dos2.writeInt(playerNum);
							dos2.close();
							fos.close();
							Player p = new Player(username,playerNum);
							main.users.add(p);
							for(int i = 0; i < main.maxPlayers; i++)
								if(main.players[i]==null)
								{
									main.players[i] = p;
									main.numPlayers++;
									id = i;
									//id = p.id;
									p.flags |= 1;
									p.address = sock.getInetAddress().getAddress();
									command = 1;
									break;
								}
						} else command = -3;
					}
				}
				dos.writeInt(id);
				bos.flush();
			}
			VirtualConsole v = new VirtualConsole();
			switch(command)
			{
			case -5:
				main.players[id] = new Player();
				main.numPlayers--;
				main.players[id].flags &= 0xFE;
				v.out("Thank you for logging out!",(byte)7);
				v.out("Doing so allows other players to get in immediately, so please always do this",0,1);
				v.writeConsole(dos);
				v.writeLinks(dos);
				bos.flush();
				break;
			case -4:
				v.out("Invalid claim or server has automatically kicked you for being idle");
				v.writeConsole(dos);
				v.writeLinks(dos);
				bos.flush();
				break;
			case -1:
				v.out("Server full.");
				v.writeConsole(dos);
				v.writeLinks(dos);
				bos.flush();
				break;
			case -2:
				v.out("Password incorrect.");
				v.writeConsole(dos);
				v.writeLinks(dos);
				bos.flush();
				break;
			case -3:
				v.out("Sorry, the max amount of user accounts has been reached :(");
				v.writeConsole(dos);
				v.writeLinks(dos);
				bos.flush();
				break;
			case 0:
				main.players[id].x = dis.readInt();
				main.players[id].y = dis.readInt();
				drawHeader(v);
				drawMap(v);
				v.writeConsole(dos);
				v.writeLinks(dos);
				bos.flush();
				break;
			case 1:
				drawHeader(v);
				drawMap(v);
				v.writeConsole(dos);
				v.writeLinks(dos);
				bos.flush();
				break;
			case 2:
			{
				int x = dis.readInt();
				int y = dis.readInt();
				Map m = main.maps[main.players[id].z];
				for(int i = 0; i < m.doors.length; i++)
					if(m.doors[i].inX==x&&m.doors[i].inY==y)
					{
						//TODO add access restrictions
						main.players[id].x = m.doors[i].outX;
						main.players[id].y = m.doors[i].outY;
						main.players[id].z = m.doors[i].outZ;
					}
				drawHeader(v);
				drawMap(v);
				v.writeConsole(dos);
				v.writeLinks(dos);
				bos.flush();
			}
				break;
			default:
				{
					String s = "The page you have requested is not available, click to go to main screen";
					v.out(s);
					v.writeConsole(dos);
					v.addLink(0, s.length()-1, 0, 0, ByteBuffer.allocate(2).putShort((short)2), "Main Screen");
					v.writeLinks(dos);
					bos.flush();
				}
				break;
			}
			sock.close();
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void drawHeader(VirtualConsole v)
	{
		Player player = main.players[id];
		Map map = main.maps[player.z];
		v.color(7);
		v.gotoxy(0, 0);
		v.out(player.username);
		v.out('�',20,0);
		v.out(map.name);
		v.out("�$",54,0);
		v.out(player.money);
		v.out('�',68,0);
		v.out(player.health*100/player.maxHealth);
		v.out('%');
		v.out("쿗ogout",73,0);
		v.addLink(74, 79, 0, 0, ByteBuffer.allocate(2).putShort((short)-5), "Remember to log out when you're done!");
		v.out("컴컴컴컴컴컴컴컴컴컴좔컴컴컴컴컴컴컴컴컴컴컴컴컴컴컴컴좔컴컴컴컴컴컴좔컴컨컴컴컴",0,1);
	}
	
	public void drawMap(VirtualConsole v)
	{
		Player p = main.players[id];
		Map map = main.maps[p.z];
		Block [] blocks = main.blocks;
		for(int x = p.x - 11; x <= p.x + 11; x++)
			for(int y = p.y - 11; y <= p.y + 11; y++)
				if(x>=0&&x<map.size&&y>=0&&y<map.size)
				{
					Block b = blocks[map.map[x][y]];
					v.out(b.character,x-p.x+11,y-p.y+13,b.color);
					if(b.action>=0)
						v.addLink(x-p.x+11, x-p.x+11, y-p.y+13, y-p.y+13, ByteBuffer.allocate(10).putShort((short)b.action).putInt(x).putInt(y), b.altText);
				}
		for(int i = 0; i < main.maxPlayers; i++)
			if(main.players[i]!=null)
			{
				int x = 11 - p.x + main.players[i].x;
				int y = 11 - p.y + main.players[i].y;
				if(main.players[i].z==p.z&&x>=0&&x<23&&y>=0&&y<23)
					v.out('@',x,y+2,(byte)7);
			}
	}
}