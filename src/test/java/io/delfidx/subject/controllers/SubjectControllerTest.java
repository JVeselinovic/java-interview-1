package io.delfidx.subject.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.delfidx.helpers.Fixtures;
import io.delfidx.helpers.WithMockedToken;
import io.delfidx.sample.features.SampleFeature;
import io.delfidx.sample.models.AccessionSample;
import io.delfidx.sample.models.Sample;
import io.delfidx.subject.models.Subject;
import io.delfidx.subject.repositories.SubjectRepo;
import io.micronaut.core.type.Argument;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SuppressWarnings("unchecked")
class SubjectControllerTest extends WithMockedToken {

    @Inject
    @Client("/subjects")
    HttpClient client;

    @Inject
    SubjectRepo subjectRepo;

    @Inject
    Fixtures fixtures;

    @Inject
    SampleFeature sampleFeature;


    @BeforeEach
    void setUp() {
        tokenMock.setOverrideRoles(List.of("subject:read"));
    }

    @Test
    @Order(1)
    void should_return_a_default_paged_list_of_subjects() throws JsonProcessingException {
        List<Subject> subjects = fixtures.loadSubjects("subjects");
        subjectRepo.saveAll(subjects);

        HttpRequest<?> request = HttpRequest.GET("").bearerAuth(validToken);
        Page<Subject> response = client.toBlocking().retrieve(request, Page.class);
        assertNotNull(response);
        assertEquals(subjects.size(), response.getTotalSize());
    }

    @Test
    @Order(2)
    void should_return_a_paged_list_of_subjects() {
        URI uri = UriBuilder.of("")
                .queryParam("number", 1)
                .queryParam("size", 2)
                .build();
        HttpRequest<?> request = HttpRequest.GET(uri).bearerAuth(validToken);
        Page<Subject> response = client.toBlocking().retrieve(request, Page.class);
        assertNotNull(response);
        assertEquals(3, response.getTotalSize());
        assertEquals(1, response.getPageNumber());
        assertEquals(2, response.getOffset());
        assertEquals(2, response.getTotalPages());
        assertEquals(2, response.getSize());
    }

    @Test
    @Order(3)
    void should_fail_when_requested_page_size_exceeds_max_allowed() {
        URI uri = UriBuilder.of("")
                .queryParam("number", 1)
                .queryParam("size", 500)
                .build();
        HttpRequest<Object> req = HttpRequest.GET(uri).bearerAuth(validToken);
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> client.toBlocking().retrieve(req));
        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @Order(5)
    void should_return_forbidden_when_not_sufficient_roles_on_the_user() {
        URI uri = UriBuilder.of("").path("sub-1")
                .build();
        tokenMock.setOverrideRoles(List.of("bad:role"));
        HttpRequest<Object> req = HttpRequest.GET(uri).bearerAuth(validToken);
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> client.toBlocking().retrieve(req));
        assertNotNull(exception);
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    @Order(6)
    void should_return_not_found_when_subject_doesnt_exist() {
        URI uri = UriBuilder.of("").path("-1")
                .build();
        HttpRequest<Object> req = HttpRequest.GET(uri).bearerAuth(validToken);
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> client.toBlocking().retrieve(req));
        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Order(7)
    void should_return_found_subject() {
        URI uri = UriBuilder.of("").path("1")
                .build();
        HttpRequest<Object> req = HttpRequest.GET(uri).bearerAuth(validToken);
        Subject response = client.toBlocking().retrieve(req, Subject.class);
        assertNotNull(response);
        assertEquals(1, response.getId());
    }

    @Test
    void should_get_subjects_by_subject_external_id() throws JsonProcessingException {
        List<AccessionSample> accessionSamples = fixtures.loadAccessionSamples("accessionSamples");
        List<Sample> samples = sampleFeature.accessionSamples(accessionSamples);
        assertEquals(accessionSamples.size(), samples.size());

        URI uri = UriBuilder.of("").path("externalId").path("ext-1")
                .build();
        HttpRequest<?> request = HttpRequest.GET(uri).bearerAuth(validToken);
        List<Subject> response = client.toBlocking().retrieve(request, Argument.of(List.class, Argument.of(Subject.class)));
        assertNotNull(response);
        assertEquals(2, response.size());
    }
}