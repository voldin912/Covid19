package kis.covid19;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author naoki
 */
public class Loader {
    public static final String COUNTER = "data/done.txt";
    public static final String URLS = "/urls.txt";
    
    public static void main(String[] args) throws IOException {
        String cstr = Files.readString(Path.of(COUNTER)).trim();
        int[] counter = {Integer.parseInt(cstr)};
        
        InputStream urlsText = Loader.class.getResourceAsStream(URLS);
        new BufferedReader(new InputStreamReader(urlsText))
                .lines()
                .skip(counter[0])
                .forEach(line -> {
                    String[] parts = line.split("\\s+");
                    try {
                        System.out.println(parts[0]);
                        Launcher.scrape(parts[0]);
                        ++counter[0];
                    } catch (IOException ex) {
                        throw new UncheckedIOException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    } finally {
                        try {
                            Files.writeString(Path.of(COUNTER), counter[0] + "\n");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
        CreateData.main(args);
                
        
    }
}
