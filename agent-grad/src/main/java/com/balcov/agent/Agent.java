package com.balcov.agent;

import java.lang.instrument.Instrumentation;

public class Agent {
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("Agent is listening >>>");
        Spy transformer = new Spy();
        instrumentation.addTransformer(transformer);
    }
}
