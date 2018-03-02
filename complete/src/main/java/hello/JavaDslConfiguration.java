package hello;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.endpoint.MethodInvokingMessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableIntegration
public class JavaDslConfiguration {

    @Bean
    public MessageSource<?> integerMessageSource() {
        MethodInvokingMessageSource source = new MethodInvokingMessageSource();
        source.setObject(new AtomicInteger());
        source.setMethodName("getAndIncrement");
        return source;
    }


    @Bean
    public IntegrationFlow myFlow(SampleTransformer sampleTransformer) {
        return IntegrationFlows.from(this.integerMessageSource(), c ->
                c.poller(Pollers.fixedRate(100)))
                .transform(Object::toString)
                .enrichHeaders( headers -> headers.header("newHeaderKey","newHeaderValue"))
                .transform(sampleTransformer)
                .handle(handler())
                .get();
    }

    /*
     *  Anonymous function can be replaced with lambda.
     *  Lambda can be replaced with method reference System.out::println
     *  Method reference could be inlined into myFlow.
     *
     */
    private MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println(message);
            }
        };
    }
}
