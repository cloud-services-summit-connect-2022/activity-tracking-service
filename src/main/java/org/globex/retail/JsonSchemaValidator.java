package org.globex.retail;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class JsonSchemaValidator {

    private static final Logger log = LoggerFactory.getLogger(JsonSchemaValidator.class);

    @ConfigProperty(name = "json.schema")
    private String schemaFile;

    private Schema schema;

    @PostConstruct
    public void loadSchema() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(schemaFile)) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            schema = SchemaLoader.load(rawSchema);
        } catch (IOException e) {
            log.error("Exception loading json schema", e);
            throw new RuntimeException(e);
        }
    }

    public void validate(String json) {
        schema.validate(new JSONObject(json));
    }

}
