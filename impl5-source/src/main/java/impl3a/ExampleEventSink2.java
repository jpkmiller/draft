package impl3a;

public class ExampleEventSink2 extends AbstractEPPStage {

    public ExampleEventSink2(String name, String kind) {
        super(name, kind);
    }

    @Override
    public Event execSelf(Event e) {
        System.out.println("Sink 2: Event called " + e.amountCalled);
        System.out.println("Sink 2: Event " + e);
        return e;
    }
}
