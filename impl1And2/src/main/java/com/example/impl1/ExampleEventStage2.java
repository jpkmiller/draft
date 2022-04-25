package com.example.impl1;

public class ExampleEventStage2 implements EPPStage {
    @Override
    public Event execute(Event e) {
        e.amountCalled++;
        return e;
    }
}
