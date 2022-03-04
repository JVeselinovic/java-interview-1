package io.delfidx;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "sample-service",
                version = "0.2"
        )
)
public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
