package kis.covid19;

import java.io.IOException;

/**
 *
 * @author naoki
 */
public class Launcher {
    public static void main(String[] args) throws IOException, InterruptedException {
        var url = "https://www.mhlw.go.jp/content/10906000/000622869.pdf"; // 4/19
        
        //ScrapeDetailFromMhlwPDF.main(url);
        CreateData.main(args);
    }
}
