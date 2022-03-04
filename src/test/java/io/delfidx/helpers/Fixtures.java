package io.delfidx.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.delfidx.sample.models.AccessionSample;
import io.delfidx.sample.models.Sample;
import io.delfidx.subject.models.Subject;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.core.io.ResourceLoader;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Requires(env = Environment.TEST)
@Singleton
public class Fixtures {
    public static final Logger log = LoggerFactory.getLogger(Fixtures.class);
    private final ResourceLoader loader;
    private final ObjectMapper mapper;

    public Fixtures(ResourceLoader loader, ObjectMapper mapper) {
        this.loader = loader;
        this.mapper = mapper;
    }

    public Optional<JsonNode> fromFile(String fileName) {
        Optional<InputStream> in = loader.getResourceAsStream(fileName);
        if (in.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(mapper.readTree(in.get()));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    public <T> List<T> listFromFile(String fileName, String key, Class<T> classType) throws JsonProcessingException {
        Optional<JsonNode> jsonNode = fromFile(fileName);
        if (jsonNode.isEmpty()) {
            return Collections.emptyList();
        }

        String str = jsonNode.get().get(key).toString();
        return mapper.readValue(str, TypeFactory.defaultInstance().constructCollectionLikeType(List.class, classType));

    }


    public List<Subject> loadSubjects(String key) throws JsonProcessingException {
        return listFromFile("fixtures/subjects.json", key, Subject.class)
                .stream()
                .peek(s-> {
                    s.setCreateBy("test");
                    s.setModBy("test");
                    s.setCreateDt(Instant.now());
                    s.setModDt(Instant.now());
                })
                .toList();
    }

    public List<Sample> loadSamples(String key) throws JsonProcessingException {
        return listFromFile("fixtures/samples.json", key, Sample.class)
                .stream()
                .peek(s-> {
                    s.setCreateBy("test");
                    s.setModBy("test");
                    s.setCreateDt(Instant.now());
                    s.setModDt(Instant.now());
                })
                .toList();
    }

    public List<AccessionSample> loadAccessionSamples(String key) throws JsonProcessingException {
        return listFromFile("fixtures/samples.json", key, AccessionSample.class);
    }
}
