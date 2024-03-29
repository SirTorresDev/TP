package pr1;

public class Missile {
	private Game game;
	private UCMShip player;
	private int row;
	private int column;
	public boolean active;
	
	public Missile(Game game) {
		game = game;
		this.player = new UCMShip(game, game.DIM_X / 2, game.DIM_Y - 1);
		row = player.UCMShipPositionX() - 1;
		column = player.UCMShipPositionY();
		active = false;
	}
	
	public int missileMove() {
		return row--;
	}

	public int missilePositionX() {
		return row;
	}
	
	public int missilePositionY() {
		return column;
	}

	public int setPositionX(int x){
		return row = x;
	}

	public int setPositionY(int y){
		return column = y;
	}
	
	public String toString() {
		return "oo";
	}
	
	public void setEnable() {
		active = true;
	}
	
	public boolean isEnable() {
		return this.active;
	}
	
	public void reset() {
		row = player.UCMShipPositionX() - 1;
		column = player.UCMShipPositionY();
	}
}
