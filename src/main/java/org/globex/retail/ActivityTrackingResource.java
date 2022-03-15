package org.globex.retail;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.smallrye.mutiny.Uni;
import org.everit.json.schema.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/track")
public class ActivityTrackingResource {

    private static final Logger log = LoggerFactory.getLogger(ActivityTrackingResource.class);

    @Inject
    JsonSchemaValidator validator;

    @Inject
    KafkaService kafkaService;

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> trackUserActivity(String payload) {
        return Uni.createFrom().item(() -> payload).onItem().invoke(p -> validator.validate(p))
                .onItem().invoke(p -> kafkaService.emit(p))
                .onItem().transform(p -> Response.status(200).build())
                .onFailure().recoverWithItem(throwable -> {
                    if (throwable instanceof ValidationException) {
                        log.error("Exception validating payload", throwable);
                        return Response.status(400, "Payload validation error").build();
                    } else {
                        log.error("Exception when processing payload", throwable);
                        return Response.status(500, "Processing error").build();
                    }
                });
    }

}
