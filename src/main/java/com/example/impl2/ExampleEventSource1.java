package com.example.impl2;

public class ExampleEventSource1 extends AbstractEPPStage {

    public ExampleEventSource1(String name, String kind) {
        super(name, kind);
    }

    @Override
    public Event execSelf(Event e) {
        return new Event() {
            @Override
            public String toString() {
                return "Event from Source 1";
            }

            @Override
            public int hashCode() {
                return super.hashCode();
            }
        };
    }
}
