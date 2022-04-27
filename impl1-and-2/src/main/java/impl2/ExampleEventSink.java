package impl2;

public class ExampleEventSink extends AbstractEPPStage {
    public ExampleEventSink(String name, String kind) {
        super(name, kind);
    }

    @Override
    public Event execSelf(Event e) {
        System.out.println("Sink 1: Event called " + e.amountCalled);
        System.out.println("Sink 1: Event " + e);
        return e;
    }
}
