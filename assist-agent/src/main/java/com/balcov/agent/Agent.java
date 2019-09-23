package com.balcov.agent;

import java.lang.instrument.Instrumentation;

public class Agent {
    public static void premain(String args, Instrumentation instrumentation) throws Exception{

        System.out.printf("Starting %s\n",
                Agent.class.getSimpleName());
        instrumentation.addTransformer(new MetricsTransformer());
    }
}
