package org.globex.retail;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

@ApplicationScoped
public class KafkaService {

    @Inject
    @Channel("tracking-event")
    Emitter<String> emitter;

    public void emit(String payload) {
        emitter.send(toMessage(payload));
    }

    private Message<String> toMessage(String payload) {
        return Message.of(payload);
    }
}
