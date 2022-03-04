package io.delfidx.sample.controllers;

import io.delfidx.sample.features.SampleFeature;
import io.delfidx.sample.models.PageableSample;
import io.delfidx.sample.models.Sample;
import io.delfidx.serviceutils.pagination.MaxPageable;
import io.delfidx.serviceutils.pagination.Pagination;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Named;

import javax.validation.constraints.NotNull;
import java.util.Optional;

import static io.delfidx.common.Roles.SAMPLE_READ;

@Controller("/samples")
@Tag(name = "Sample", description = "Sample service")
public class SampleController {
    private final SampleFeature sampleFeature;
    private final Pagination pagination;

    public SampleController(SampleFeature sampleFeature, @Named("default") Pagination pagination) {
        this.sampleFeature = sampleFeature;
        this.pagination = pagination;
    }

    @RolesAllowed(SAMPLE_READ)
    @Get("/{?pageable*}")
    @Operation(description = "Returns a paged list of samples",
            parameters = {
                    @Parameter(name = "number", description = "the page number to return",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer")),
                    @Parameter(name = "size", description = "the number of items per page to return",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer"))
            }
    )
    //WARNING!!! Need to use a custom PageableExample interface for OpenApi to document these query params correctly, otherwise
    //the generated doc treats the search and pageable arguments as Json objects instead of query params
    @ApiResponse(content = @Content(schema = @Schema(implementation = PageableSample.class)), description = "paged entities")
    // Need to hide these parameters for OpenApi doc purposes as we are defining them explicitly. the default generation
    // doesn't seem to handle this well.
    public Page<Sample> samples(@Parameter(hidden = true) @QueryValue @MaxPageable Optional<Pageable> pageable) {
        return sampleFeature.getSamples(pageable.orElseGet(pagination::defaultPaged));
    }

    @RolesAllowed(SAMPLE_READ)
    @Get("/{id}")
    public Sample sample(@NotNull long id) {
        return sampleFeature.findById(id).orElse(null);
    }
}
