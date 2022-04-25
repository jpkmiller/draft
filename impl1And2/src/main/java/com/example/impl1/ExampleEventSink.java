package com.example.impl1;

public class ExampleEventSink implements EPPStage {
    @Override
    public Event execute(Event e) {
        System.out.println("Event called " + e.amountCalled);
        return e;
    }
}
