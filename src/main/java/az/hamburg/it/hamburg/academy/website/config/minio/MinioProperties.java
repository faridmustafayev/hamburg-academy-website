package az.hamburg.it.hamburg.academy.website.config.minio;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "minio")
@Getter
@Setter
public class MinioProperties {
    private String url;
    private Access access;
    private Secret secret;
    private Buckets buckets;

    @Getter
    @Setter
    public static class Access {
        private String key;
    }

    @Getter
    @Setter
    public static class Secret {
        private String key;
    }

    @Getter
    @Setter
    public static class Buckets {
        private Bucket instructors;
        private Bucket courseImages;
        private Bucket graduates;
        private Bucket reviews;
        private Bucket syllabuses;
        private Bucket diplomas;

        @Getter
        @Setter
        public static class Bucket {
            private String name;
        }
    }
}
