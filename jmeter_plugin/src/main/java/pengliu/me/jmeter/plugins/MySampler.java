package pengliu.me.jmeter.plugins;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.util.Iterator;
import java.util.Random;

public class MySampler extends AbstractJavaSamplerClient
{
    private static final Logger log = LoggingManager.getLoggerForClass();

    @Override
    public void setupTest(JavaSamplerContext context)
    {
        Iterator<String> it = context.getParameterNamesIterator();
        while (it.hasNext())
        {
            String paramName =  it.next();
            this.log.info(String.format("-----------------paramName: %s, paramValue: %s-----------------",
                    paramName,
                    context.getParameter(paramName)));
        }

        this.log.info(String.format("-----------------Get param 'name' from JMeter: %s-----------------",
                context.getParameter("name", "Default Name")));
        this.log.info(String.format("-----------------Current Thread Id: %s, Start 'setupTest(JavaSamplerContext context)'--------------------",
                Thread.currentThread().getId()));
    }

    @Override
    public void teardownTest(JavaSamplerContext context)
    {
        this.log.info(String.format("-----------------Current Thread Id: %s, Start 'teardownTest(JavaSamplerContext context)'--------------------",
                Thread.currentThread().getId()));
    }

    public SampleResult runTest(JavaSamplerContext javaSamplerContext)
    {
        SampleResult result = new SampleResult();
        result.sampleStart();
        try
        {
            Random r = new Random();
            long sleepTime = (long)r.nextInt(5000);
            this.log.info(String.format("-----------------Current Thread Id: %s, Running Test, sleep time is %s--------------------",
                    Thread.currentThread().getId(),
                    sleepTime));
            Thread.sleep(sleepTime);

            if(sleepTime%2 == 0)
            {
                throw new Exception("--------This is my exception from runTest!!!---------");
            }

            result.sampleEnd();
            result.setSuccessful(true);
            result.setSampleLabel("My Lable: SUCCESS: " + Thread.currentThread().getId());
        }
        catch (Throwable e)
        {
            result.sampleEnd();
            result.setSuccessful(false);
            result.setSampleLabel("My Lable: FAIL: " + Thread.currentThread().getId());

            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Arguments getDefaultParameters() {

        Arguments params = new Arguments();

        params.addArgument("name", "edw");

        return params;
    }
}
