package kis.covid19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

/**
 *
 * @author naoki
 */
public class Launcher {
    public static void main(String[] args) throws IOException, InterruptedException {
        // var tokyo = "https://www.bousai.metro.tokyo.lg.jp/_res/projects/default_project/_page_/001/007/825/20200505.pdf"; // 5/5
        //var tokyo = "https://www.bousai.metro.tokyo.lg.jp/_res/projects/default_project/_page_/001/007/826/2020050601.pdf"; // 5/6
        // var tokyo = "https://www.bousai.metro.tokyo.lg.jp/_res/projects/default_project/_page_/001/007/834/2020050701.pdf"; // 5/7
        var tokyo = "https://www.bousai.metro.tokyo.lg.jp/_res/projects/default_project/_page_/001/007/842/2020050801.pdf"; // 5/8

        var url = "https://www.mhlw.go.jp/stf/newpage_13673.html"; // 9/20

        if (true) {
            scrape(url);
        } else {
            // until 5/8
            LocalDate date;
            if (url.endsWith(".pdf")) {
                date = ScrapeDetailFromMhlwPDF.scrape(url);
            } else {
                date = ScrapeDetailFromMhlw.scrape(url);
            }
            ScrapeFromTokyo.amendTokyo(tokyo, date);
        }
        CreateData.main(args);
    }

    static void scrape(String url) throws IOException, InterruptedException {
        // since 5/9
        if (url.endsWith("tsv")) {
            String text = Files.readString(Path.of(url));
            ScrapeFromMhlwPDF2.dataToJson(text);
        } else {
            if (url.endsWith("html")) {
                url = ScrapeFromMhlwPDF2.getPDFUrl(url);
            }
            ScrapeFromMhlwPDF2.scrape(url);
        }
    }
}
