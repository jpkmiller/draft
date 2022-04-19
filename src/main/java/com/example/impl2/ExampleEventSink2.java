package com.example.impl2;

public class ExampleEventSink2 extends AbstractEPPStage {

    public ExampleEventSink2(String name, String kind) {
        super(name, kind);
    }

    @Override
    public Event execSelf(Event e) {
        System.out.println("Sink 2: Event called " + e.amountCalled);
        return e;
    }
}
