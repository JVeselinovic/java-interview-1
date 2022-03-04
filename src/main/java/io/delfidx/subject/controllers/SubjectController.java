package io.delfidx.subject.controllers;


import io.delfidx.serviceutils.pagination.MaxPageable;
import io.delfidx.serviceutils.pagination.Pagination;
import io.delfidx.subject.features.SubjectFeature;
import io.delfidx.subject.models.PageableSubject;
import io.delfidx.subject.models.Subject;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Named;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static io.delfidx.common.Roles.SUBJECT_READ;

@Validated
@Controller("/subjects")
@Tag(name = "Subjects", description = "Subject service")
public class SubjectController {
    private final SubjectFeature subjectFeature;
    private final Pagination pagination;

    public SubjectController(SubjectFeature subjectFeature, @Named("default") Pagination pagination) {
        this.subjectFeature = subjectFeature;
        this.pagination = pagination;
    }

    @RolesAllowed(SUBJECT_READ)
    @Get("/{?pageable*}")
    @Operation(description = "Returns a paged list of subjects",
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
    @ApiResponse(content = @Content(schema = @Schema(implementation = PageableSubject.class)), description = "paged entities")
    // Need to hide these parameters for OpenApi doc purposes as we are defining them explicitly. the default generation
    // doesn't seem to handle this well.
    public Page<Subject> subjects(@Parameter(hidden = true) @QueryValue @Valid @MaxPageable Optional<Pageable> pageable) {
        return subjectFeature.getSubjects(pageable.orElseGet(pagination::defaultPaged));
    }
    @RolesAllowed(SUBJECT_READ)
    @Get("/{id}")
    public Subject subject(@NotNull Long id) {
        return subjectFeature.findSubjectById(id).orElse(null);
    }

    @RolesAllowed(SUBJECT_READ)
    @Get("/externalId/{id}")
    public List<Subject> subjectsByExternalId(String id) {
        return subjectFeature.findSubjectByExternalId(id);
    }
}
