package org.example.chy;

import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;

import java.io.IOException;

public class APP {
    public static void main(String[] args) throws IOException {
        RegisterRequest registerRequest = new RegisterRequest();
        JavaSamplerContext context = new JavaSamplerContext(registerRequest.getDefaultParameters());
        registerRequest.setupTest(context);
        registerRequest.runTest(context);
        System.in.read();
    }
}
