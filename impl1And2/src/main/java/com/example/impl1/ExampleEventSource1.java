package com.example.impl1;

public class ExampleEventSource1 implements EPPStage {

    @Override
    public Event execute(Event e) {
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
