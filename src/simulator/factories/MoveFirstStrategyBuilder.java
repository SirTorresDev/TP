package simulator.factories;

import org.json.JSONObject;
import simulator.model.MoveFirstStrategy;

public class MoveFirstStrategyBuilder extends Builder<MoveFirstStrategy> {
    MoveFirstStrategyBuilder() {
        super("move_first_dqs");
    }

    @Override
    protected MoveFirstStrategy createTheInstance(JSONObject data) {
        return new MoveFirstStrategy();
    }
}