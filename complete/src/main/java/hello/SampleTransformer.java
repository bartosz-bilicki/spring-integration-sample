package hello;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.handler.annotation.Payload;

@MessageEndpoint
public class SampleTransformer {

    @Transformer
    public String transform(@Payload String payload) {
        return payload+" transformed by SampleTransformer";
    }
}
