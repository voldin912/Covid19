package kis.covid19;

import java.io.IOException;

/**
 *
 * @author naoki
 */
public class Launcher {
    public static void main(String[] args) throws IOException, InterruptedException {
        //var url = "https://www.mhlw.go.jp/content/10906000/000622869.pdf"; // 4/19
        // var url = "https://www.mhlw.go.jp/content/10906000/000623120.pdf"; // 4/20
        var url = "https://www.mhlw.go.jp/content/10906000/000623669.pdf"; // 4/21
        
        // ScrapeDetailFromMhlwPDF.main(url);
        CreateData.main(args);
    }
}
