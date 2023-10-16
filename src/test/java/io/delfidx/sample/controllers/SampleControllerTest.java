package io.delfidx.sample.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.delfidx.helpers.Fixtures;
import io.delfidx.helpers.WithMockedToken;
import io.delfidx.sample.features.SampleFeature;
import io.delfidx.sample.models.AccessionSample;
import io.delfidx.sample.models.Sample;
import io.delfidx.sample.repositories.SampleRepo;
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
class SampleControllerTest extends WithMockedToken {

    @Inject
    @Client("/samples")
    HttpClient client;

    @Inject
    Fixtures fixtures;
    @Inject
    SampleRepo sampleRepo;
    List<Sample> samples;
    int numSamples;

    @Inject
    SampleFeature sampleFeature;

    @BeforeEach
    void setUp() {
        tokenMock.setOverrideRoles(List.of("sample:read"));
    }

    @Test
    void should_not_succeed_if_no_token() {
        HttpRequest<?> req = HttpRequest.GET("/");
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> client.toBlocking().exchange(req));
        assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void should_not_succeed_if_does_not_have_allowed_roles() {
        tokenMock.setOverrideRoles(List.of("no:role"));
        HttpRequest<?> req = HttpRequest.GET("/").bearerAuth(validToken);
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> client.toBlocking().exchange(req));
        assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void should_succeed_if_there_token_has_allowed_roles() {
        HttpRequest<?> req = HttpRequest.GET("/").bearerAuth(validToken);
        String resp = client.toBlocking().retrieve(req);
        assertNotNull(resp);
    }
    @Test
    @Order(1)
    void should_return_a_default_paged_list_of_samples() throws JsonProcessingException {
        samples = fixtures.loadSamples("samples");
        numSamples = samples.size();
        sampleRepo.saveAll(samples);

        HttpRequest<?> request = HttpRequest.GET("").bearerAuth(validToken);
        Page<Sample> response = client.toBlocking().retrieve(request, Page.class);
        assertNotNull(response);
        assertEquals(samples.size(), response.getTotalSize());
    }

    @Test
    @Order(2)
    void should_return_a_paged_list_of_samples() {
        URI uri = UriBuilder.of("")
                .queryParam("number", 1)
                .queryParam("size", 2)
                .build();
        HttpRequest<?> request = HttpRequest.GET(uri).bearerAuth(validToken);
        Page<Sample> response = client.toBlocking().retrieve(request, Page.class);
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
    @Order(4)
    void should_return_not_found_when_sample_doesnt_exist() {
        URI uri = UriBuilder.of("").path("-1")
                .build();
        HttpRequest<Object> req = HttpRequest.GET(uri).bearerAuth(validToken);
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> client.toBlocking().retrieve(req));
        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Order(5)
    void should_return_found_sample() {
        URI uri = UriBuilder.of("").path("1")
                .build();
        HttpRequest<Object> req = HttpRequest.GET(uri).bearerAuth(validToken);
        Sample response = client.toBlocking().retrieve(req, Sample.class);
        assertNotNull(response);
        assertEquals(1, response.getId());
    }

     @Test
    void should_return_a_list_of_samples_for_accession_samples() throws JsonProcessingException {
        List<AccessionSample> accessionSamples = fixtures.loadAccessionSamples("accessionSamples");
      
        URI uri = UriBuilder.of("").path("accession-samples")
                .build();
        HttpRequest<?> request = HttpRequest.POST(uri,accessionSamples).bearerAuth(validToken);
        List<Sample> response = client.toBlocking().retrieve(request, Argument.of(List.class, Argument.of(Sample.class)));
        assertNotNull(response);
        assertEquals(2, response.size());
    }
}