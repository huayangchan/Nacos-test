package org.example.chy;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.remote.RemoteConstants;
import com.alibaba.nacos.client.naming.cache.ServiceInfoHolder;
import com.alibaba.nacos.client.naming.core.ServerListManager;
import com.alibaba.nacos.client.naming.remote.gprc.NamingPushRequestHandler;
import com.alibaba.nacos.common.remote.client.grpc.GrpcClient;
import com.alibaba.nacos.common.remote.client.grpc.GrpcSdkClient;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;

public abstract class JmeterRequest extends AbstractJavaSamplerClient {

    public static final Logger LOGGER  = LoggerFactory.getLogger(JmeterRequest.class);

    protected GrpcClient grpcClient;

    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument("serverAddr","localhost:8848");
        arguments.addArgument("namespace","public");
        return arguments;
    }

    public void setupTest(JavaSamplerContext context) {
        this.grpcClient = buildClient();
        String serverAddr = context.getParameter("serverAddr");
        String namespace = context.getParameter("namespace", "public");
        Properties properties = new Properties();
        properties.setProperty("serverAddr",serverAddr);
        properties.setProperty("namespace",namespace);
        ServerListManager serverListManager = new ServerListManager(properties, namespace);
        ServiceInfoHolder serviceInfoHolder = new ServiceInfoHolder(namespace, properties);
        NamingPushRequestHandler namingPushRequestHandler = new NamingPushRequestHandler(serviceInfoHolder);
        this.grpcClient.registerServerRequestHandler(namingPushRequestHandler);
        this.grpcClient.serverListFactory(serverListManager);
        try {
            this.grpcClient.start();
            LOGGER.info("scuess");
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    public static GrpcClient buildClient(){
        String uuid = UUID.randomUUID().toString();
        HashMap<String, String> labels = new HashMap<>();
        labels.put(RemoteConstants.LABEL_SOURCE,RemoteConstants.LABEL_SOURCE_SDK);
        labels.put(RemoteConstants.LABEL_MODULE, RemoteConstants.LABEL_MODULE_NAMING);
        GrpcSdkClient grpcSdkClient = new GrpcSdkClient(uuid);
        grpcSdkClient.setThreadPoolCoreSize(null);
        grpcSdkClient.setThreadPoolMaxSize(null);
        grpcSdkClient.labels(labels);
        return grpcSdkClient;
    }


}
