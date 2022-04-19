package com.example.impl2;

public class ExampleEventSink extends AbstractEPPStage {
    public ExampleEventSink(String name, String kind) {
        super(name, kind);
    }

    @Override
    public Event execSelf(Event e) {
        System.out.println("Sink1: Event called " + e.amountCalled);
        return e;
    }
}
