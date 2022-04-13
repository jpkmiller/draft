package com.example;

public class ExampleEventSink implements EPPStage {
    @Override
    public Event execute(Event e) {
        System.out.println("Event called " + e.amountCalled);
        return null;
    }
}
