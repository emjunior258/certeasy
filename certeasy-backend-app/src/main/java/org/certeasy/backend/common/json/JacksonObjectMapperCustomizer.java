package org.certeasy.backend.common.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.quarkus.jackson.ObjectMapperCustomizer;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.certeasy.Certificate;
import org.certeasy.backend.common.cert.CertificateInfo;
import org.certeasy.backend.common.cert.CreatedCertInfo;

import javax.inject.Singleton;
import javax.json.stream.JsonGenerator;
import java.io.IOException;

@Singleton
public class JacksonObjectMapperCustomizer implements ObjectMapperCustomizer  {

    @Override
    public void customize(ObjectMapper mapper) {
        //mapper.addHandler(new JacksonDeserializationProblemHandler());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
