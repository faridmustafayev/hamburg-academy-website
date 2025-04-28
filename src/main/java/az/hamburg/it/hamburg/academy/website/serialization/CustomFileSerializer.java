package az.hamburg.it.hamburg.academy.website.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class CustomFileSerializer extends JsonSerializer<MultipartFile> {
    @Override
    public void serialize(MultipartFile value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            gen.writeStartObject();
            gen.writeStringField("originalFilename", value.getOriginalFilename());
            gen.writeNumberField("size", value.getSize());
            gen.writeEndObject();
        }
    }
}
