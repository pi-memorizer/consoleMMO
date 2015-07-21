public class Player
{
	//1 = loggedIn
	String username = "";
	byte flags = 0;
	int id = 0;
	byte [] address;
	long money = 0;
	int maxHealth = 100;
	int health = 100;
	int x = 0;
	int y = 0;
	int z = 0;
	
	public Player(String u, int i)
	{
		username = u;
		id = i;
	}
	
	public Player() {}
}