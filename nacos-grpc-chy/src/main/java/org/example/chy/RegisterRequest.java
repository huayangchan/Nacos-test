package org.example.chy;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.remote.NamingRemoteConstants;
import com.alibaba.nacos.api.naming.remote.request.InstanceRequest;
import com.alibaba.nacos.api.remote.response.Response;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class RegisterRequest extends JmeterRequest{
    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        Instance instance = new Instance();
        instance.setIp("127.0.0.1");
        instance.setPort(8080);
        instance.setWeight(1.0);
        instance.setClusterName("DEFAULT");
        InstanceRequest instanceRequest = new InstanceRequest(javaSamplerContext.getParameter("namespace"), "nacos", "DEFAULT_GROUP", NamingRemoteConstants.REGISTER_INSTANCE, instance);
        try {
            sampleResult.sampleStart();
            Response response = this.grpcClient.request(instanceRequest, 3000L);
            if (response.isSuccess()) {
                LOGGER.info("registerSUCE");
                sampleResult.sampleEnd();
                sampleResult.setSuccessful(true);
            }
        } catch (NacosException e) {
            LOGGER.info("registerSUCE1");
            sampleResult.setSuccessful(false);
            sampleResult.sampleEnd();
            throw new RuntimeException(e);
        }
        return sampleResult;
    }
}
