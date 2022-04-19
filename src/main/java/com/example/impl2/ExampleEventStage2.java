package com.example.impl2;

public class ExampleEventStage2 extends AbstractEPPStage {

    public ExampleEventStage2(String name, String kind) {
        super(name, kind);
    }

    @Override
    public Event execSelf(Event e) {
        e.amountCalled++;
        return e;
    }
}
