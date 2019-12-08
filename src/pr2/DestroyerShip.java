package pr2;

public class DestroyerShip extends AlienShip{
    private Game game;
    private static int points = 10;
    private boolean bomb;
    private static boolean floor;
    private Bomb b;

    public DestroyerShip(Game game, int x, int y){
        super(game,x,y,1);
        floor = false;
        this.game = game;
    }


    public String toString(){
        String nave;
        if (life == 0){
            nave = " ";
        }
        else nave = "D[" + life +"]";
        return nave;
    }

    public void recibeDamage(int damage){
        this.life = this.life - damage;
    }

    @Override
    public void onDelete() {
        game.receivePoints(points);
        AlienShip.setterRemaingAliens(AlienShip.getRemainingAliens()-1);
    }

    @Override
    public boolean receiveShockWaveAttack(int damage) {
        boolean ok = false;
        if(this.life > 0) {
            recibeDamage(1);
            ok = true;
        }
        return ok;
    }

    @Override
    public void computerAction() {
        if(IExecuteRandomActions.canGenerateRandomBomb(game) && !bomb){
            b = new Bomb(game,x,y,1,this);
            game.addObject(b);
            bomb = true;
        }
    }

    @Override
    public boolean performAttack(GameObject other) {
        return false;
    }


    public void enableBomb(){
        bomb = false;
    }



}