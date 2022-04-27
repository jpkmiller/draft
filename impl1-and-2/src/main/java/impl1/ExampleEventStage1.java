package impl1;

public class ExampleEventStage1 implements EPPStage {

    @Override
    public Event execute(Event e) {
        e.amountCalled++;
        return e;
    }
}
