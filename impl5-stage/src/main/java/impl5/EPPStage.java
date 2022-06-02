package impl5;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EPPStage extends AbstractEPPStage {

    public EPPStage() {
        super(new ArrayList<>());
    }


    public EPPStage(List<String> stagesLocations) {
        super(stagesLocations);
    }

    @Override
    protected Event execSelf(Event e) {
        return new Event(e.data + " hallo ");
    }
}
