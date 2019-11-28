package pr2;

public class Ovni extends EnemyShip implements IExecuteRandomActions {
    private int row;
    private int column;
    private static int life = 1;
    private Game game;
    private int points = 25;
    private boolean active;

    public Ovni(Game game, int x, int y){
        super(game,x,y,life);
        game = game;
        row = x;
        column = y;
        life = life;
        active = false;
    }

    public String toString(){
        String nave;
        if (life == 0){
            nave = " ";
        }
        else nave = "O[" + life + "]";
        return nave ;
    }

    public void moveLeft() {
        if (column > 0) column--;
    }


    public int getPositionX(){
        return row;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public void incrementPositionY(){
        this.column++;
    }

    public void move(){
        if(column == 0){
          deleteOvni();
        }
        else {
        	moveLeft();
        	System.out.println(column);
        }
    }

    private void deleteOvni() {
        row = 0;
        column = 8;
        active = false;
        life = 1;
    }


    @Override
    public void computerAction() {
        if(!active){
            if(IExecuteRandomActions.canGenerateRandomOvni(game)){
                active = true;
            }
        }
    }

    @Override
    public void onDelete() {
        deleteOvni();
        game.enableShockWave();
        game.receivePoints(points);
    }
}
