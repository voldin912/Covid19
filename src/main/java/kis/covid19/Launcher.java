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
        // var url = "https://www.mhlw.go.jp/content/10906000/000623669.pdf"; // 4/21
        // var url = "https://www.mhlw.go.jp/content/10906000/000624002.pdf"; // 4/22 same as 4/21
        var url = "https://www.mhlw.go.jp/stf/newpage_10989.html"; // 4/22
        var newdata = false;
        
        if (newdata) {
            if (url.endsWith(".pdf")) {
                ScrapeDetailFromMhlwPDF.main(url);
            } else {
                ScrapeDetailFromMhlw.main(url);
            }
        }
        CreateData.main(args);
    }
}
