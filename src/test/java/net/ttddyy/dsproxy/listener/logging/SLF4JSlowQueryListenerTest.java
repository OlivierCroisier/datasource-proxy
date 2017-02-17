package net.ttddyy.dsproxy.listener.logging;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Tadaya Tsuyukubo
 * @since 1.4.1
 */
public class SLF4JSlowQueryListenerTest {

    private SLF4JSlowQueryListener listener;

    @Before
    public void setup() throws Exception {
        this.listener = new SLF4JSlowQueryListener();
        this.listener.setLogger(new InMemorySLF4JLogger());
    }


    @Test
    public void testLogMessage() throws Exception {
        this.listener.setThreshold(50);
        this.listener.setThresholdTimeUnit(TimeUnit.MILLISECONDS);

        ExecutionInfo executionInfo = new ExecutionInfo();
        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setQuery("SELECT 1");
        List<QueryInfo> queryInfos = new ArrayList<QueryInfo>();
        queryInfos.add(queryInfo);

        this.listener.beforeQuery(executionInfo, queryInfos);
        TimeUnit.MILLISECONDS.sleep(100);
        this.listener.afterQuery(executionInfo, queryInfos);

        InMemorySLF4JLogger logger = (InMemorySLF4JLogger) this.listener.getLogger();
        List<String> messages = logger.getDebugMessages();
        assertThat(messages).hasSize(1);
        assertThat(messages.get(0)).contains("SELECT 1");
    }

}
