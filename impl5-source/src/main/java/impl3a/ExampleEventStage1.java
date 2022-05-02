package impl3a;

public class ExampleEventStage1 extends AbstractEPPStage {

    public ExampleEventStage1(String name, String kind) {
        super(name, kind);
    }

    @Override
    public Event execSelf(Event e) {
        e.amountCalled++;
        return e;
    }
}
