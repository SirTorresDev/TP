package simulator.model;

import org.json.JSONObject;
import simulator.exceptions.WrongValuesException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Junction extends SimulatedObject {
    protected List<Road> listaCarreterasEntrantes; //Lista de Carreteras que entran al Cruce.
    protected Map<Junction,Road> mapaCarreterasSalientes; //Mapa para seleccionar que carretera r llega al cruce j.
    protected List<List<Vehicle>> listaDeColas; //Lista de colas para las carreteras Entrantes.
    protected int indiceSemaforo; //Indice del semaforo de la carretera Entrante -1 indica rojo.
    protected int ultimoPasoDeCambio; //Paso en el cual el indice de Semaforo en verde ha cambiado de Valor.
    protected LightSwitchingStrategy estrategiaCambioSemaforos; //Estrategia para cambiar el color de los semaforos.
    protected DequeuingStrategy estrategiaEliminarCola; //Estrategia para eliminar vehiculos de las colas.
    protected int coordenadaX; //Coordenada X;
    protected int coordenadaY; //Coordenada Y;




    Junction(String id, LightSwitchStrategy lsStrategy, DequeingStrategy dqStrategy, int xCoor, int yCoor) throws WrongValuesException {
        super(id);
        if(!checkValores(lsStrategy,dqStrategy,xCoor,yCoor)){
            throw new WrongValuesException("Los valores introducidos son erroneos");
        }
        indiceSemaforo = 0;
        listaCarreterasEntrantes = new ArrayList<Road>();
        mapaCarreterasSalientes = new HashMap<Junction, Road>();
        ultimoPasoDeCambio  = 0;
    }

    private boolean checkValores(LightSwitchStrategy lsStrategy, DequeingStrategy dqStrategy, int xCoor, int yCoor) {
        if(lsStrategy == null || dqStrategy == null || xCoor < 0 || yCoor < 0) return false;
        else return true;
    }

    @Override
    protected void advance(int time) {

    }

    @Override
    public JSONObject report() {
        return null;
    }

    private void addIncommingRoad(Road r){
        listaCarreterasEntrantes.add(r);
        //TODO

    }

    private void addOutGoingRoad(Road r){
        mapaCarreterasSalientes.put(r.cruceDestino,r);

    }

    private void enter(Vehicle v){

    }

    private Road roadTo(Junction j){
        return mapaCarreterasSalientes.get(j);
    }
}
