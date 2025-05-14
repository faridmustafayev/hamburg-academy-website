package az.hamburg.it.hamburg.academy.website.util;

import lombok.NoArgsConstructor;

import java.net.URI;
import java.net.URISyntaxException;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class FileUtils {

    public static String extractFileNameFromUrl(String url) {
        try {
            var uri = new URI(url);
            var path = uri.getPath();
            return path.substring(path.lastIndexOf("/") + 1);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URL düzgün çevrilmədi: " + url, e);
        }
    }
}
