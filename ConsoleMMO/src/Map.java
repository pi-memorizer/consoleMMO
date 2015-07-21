public class Map
{
	int size;
	short [][] map;
	Door [] doors;
	String name;
	MapData data;
}

class Door {
	short inX;
	short inY;
	short outX;
	short outY;
	short outZ;
}

class MapData {
	boolean canBeOwned;
	boolean isOwned;
	int owner;
	long price;
	byte accessLevel;
}