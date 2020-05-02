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
        // var url = "https://www.mhlw.go.jp/stf/newpage_10989.html"; // 4/22
        // var url = "https://www.mhlw.go.jp/stf/newpage_11012.html"; // 4/23
        // var url = "https://www.mhlw.go.jp/content/10906000/000624953.pdf"; // 4/24
        // var url = "https://www.mhlw.go.jp/content/10906000/000625183.pdf"; // 4/25 format error
        // var url = "https://www.mhlw.go.jp/content/10906000/000625313.pdf"; // 4/26
        // var url = "https://www.mhlw.go.jp/stf/newpage_11070.html"; // 4/27
        // var url = "https://www.mhlw.go.jp/stf/newpage_11096.html"; // 4/28
        // var url = "https://www.mhlw.go.jp/content/10906000/000626141.pdf"; // 4/29
        // var url = "https://www.mhlw.go.jp/stf/newpage_11118.html"; // 4/30
        // var url = "https://www.mhlw.go.jp/stf/newpage_11146.html"; // 5/1
        var url = "https://www.mhlw.go.jp/content/10906000/000627448.pdf"; //5/2
        if (url.endsWith(".pdf")) {
            ScrapeDetailFromMhlwPDF.main(url);
        } else {
            ScrapeDetailFromMhlw.main(url);
        }
        CreateData.main(args);
    }
}
