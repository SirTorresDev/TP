package pr2;

import pr2.Commands.Command;

public class GameObjectBoard {
    private GameObject[] objects;
    private int currentObjects;

    public GameObjectBoard (int width, int height) {
        currentObjects = 1;
        objects = new GameObject[width * height];
    }

    private int getCurrentObjects () {
        return currentObjects;
    }

    public void add (GameObject object) {
        objects[currentObjects] = object;
        currentObjects++;
    }

    private GameObject getObjectInPosition (int x, int y) {
        GameObject object = null;
        for(int i = 1; i < currentObjects; i++){
            if(x == objects[i].x && y == objects[i].y){
                object = objects[i];
            }
        }
        return object;
    }

    private int getIndex(int x, int y) {
        int index = 0;
        for(int i = 1; i < currentObjects; i++){
            if(x == objects[i].x && y == objects[i].y){
                index = i;
            }
        }
        return index;
    }

    private void remove (GameObject object) {
        int i = 0;
        while (objects[i] != object && i < currentObjects) {
            if (objects[i] != null) {
                i++;
                if (objects[i] == object) {
                    for (int j = i; i < currentObjects - 1; j++) {
                        objects[j] = objects[j + 1];
                    }
                    currentObjects--;
                }
            }
        }
    }


    public void update() {
        for (int i = 0; i < currentObjects; i++) {
            if(objects[i] != null){
                objects[i].move();
                checkAttacks(objects[i]);
            }
        }
        removeDead();
    }

    private void checkAttacks(GameObject object) {
        for(GameObject object1 : objects){ //Mira colisiones con el objeto
            if(object1 != null) {
                if (object1 != object) {
                    if (object.performAttack(object1)) return;
                }
            }
        }

    }

    public void computerAction() {
        for(GameObject object : objects){
            if(object != null)
                object.computerAction();
        }

    }

    private void removeDead() {
        for(int i = currentObjects; i >= 0; i--){
            if(objects[i] != null){
                if(!objects[i].isAlive()){
                    objects[i].onDelete(); //Revisar ya que que dos objetos impacten no significa que lo tengas que borrar.
                    remove(objects[i]);
                }
            }
        }
    }

    public String toString(int x, int y) {
        String chain;
        GameObject object = getObjectInPosition(x,y);
        if (object == null){
            chain = " ";
        }
        else{
            chain = object.toString();
        }
        return chain;
    }

}
