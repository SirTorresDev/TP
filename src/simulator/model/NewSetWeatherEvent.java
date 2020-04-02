package simulator.model;

import simulator.exceptions.WrongValuesContamination;
import simulator.exceptions.WrongValuesWeather;
import simulator.misc.Pair;

import java.util.List;

public class NewSetWeatherEvent extends Event {
    protected List<Pair<String,Weather>> ws;

    public NewSetWeatherEvent(int time, List<Pair<String,Weather>> ws) throws WrongValuesWeather {
        super(time);
        this.ws = ws;
        checkContClass();
    }

    @Override
    void execute(RoadMap map) throws Exception {
        for(Pair p : ws){
            map.getRoad(p.getFirst().toString()).setWeather((Weather) p.getSecond());
        }

    }
    @Override
    public String toString() {
        return "New Weather '"+ws.toString()+"'"; //Todo revisar si esta bien este ws.toString
    }

    void checkContClass() throws WrongValuesWeather {
        if(this.ws == null) throw new WrongValuesWeather("El dato del tiempo es un valor nulo");
    }
}
