package kis.covid19;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.sql.Connection;
import java.time.LocalDate;

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
        // var url = "https://www.mhlw.go.jp/content/10906000/000627448.pdf"; //5/2
        // var url = "https://www.mhlw.go.jp/content/10906000/000627489.pdf"; // 5/3
        // var url = "https://www.mhlw.go.jp/content/10906000/000627542.pdf"; // 5/4
        // var url = "https://www.mhlw.go.jp/content/10906000/000627581.pdf"; // 5/5

        // var tokyo = "https://www.bousai.metro.tokyo.lg.jp/_res/projects/default_project/_page_/001/007/825/20200505.pdf"; // 5/5
        // var url= "https://www.mhlw.go.jp/content/10906000/000627630.pdf"; // 5/6

        //var tokyo = "https://www.bousai.metro.tokyo.lg.jp/_res/projects/default_project/_page_/001/007/826/2020050601.pdf"; // 5/6
        // var url = "https://www.mhlw.go.jp/stf/newpage_11189.html"; // 5/7

        // var tokyo = "https://www.bousai.metro.tokyo.lg.jp/_res/projects/default_project/_page_/001/007/834/2020050701.pdf"; // 5/7
        // var url = "https://www.mhlw.go.jp/stf/newpage_11222.html"; // 5/8

        var tokyo = "https://www.bousai.metro.tokyo.lg.jp/_res/projects/default_project/_page_/001/007/842/2020050801.pdf"; // 5/8
        // var url = "https://www.mhlw.go.jp/content/10906000/000628667.pdf"; // 5/9

        // var url = "https://www.mhlw.go.jp/content/10906000/000628697.pdf"; // 5/10
        // var url = "https://www.mhlw.go.jp/content/10906000/000628917.pdf"; // 5/11
        // var url = "https://www.mhlw.go.jp/content/10906000/000629544.pdf"; // 5/12
        // var url = "https://www.mhlw.go.jp/content/10906000/000630162.pdf"; // 5/13
        // var url = "https://www.mhlw.go.jp/content/10906000/000630627.pdf"; // 5/14
        // var url = "https://www.mhlw.go.jp/content/10906000/000630926.pdf"; // 5/15
        // var url = "https://www.mhlw.go.jp/content/10906000/000631063.pdf"; // 5/16
        // var url = "https://www.mhlw.go.jp/content/10906000/000631149.pdf"; // 5/17
        // var url = "https://www.mhlw.go.jp/content/10906000/000631432.pdf"; // 5/18
        // var url = "https://www.mhlw.go.jp/content/10906000/000631887.pdf"; // 5/19
        // var url = "https://www.mhlw.go.jp/content/10906000/000632235.pdf"; // 5/20
        // var url = "https://www.mhlw.go.jp/content/10906000/000632553.pdf"; // 5/21
        // var url = "https://www.mhlw.go.jp/content/10906000/000632894.pdf"; // 5/22
        // var url = "https://www.mhlw.go.jp/content/10906000/000633030.pdf"; // 5/23
        // var url = "https://www.mhlw.go.jp/content/10906000/000633053.pdf"; // 5/24
        // var url = "https://www.mhlw.go.jp/content/10906000/000633317.pdf"; // 5/25
        // var url = "https://www.mhlw.go.jp/content/10906000/000633681.pdf"; // 5/26
        // var url = "https://www.mhlw.go.jp/content/10906000/000634251.pdf"; // 5/27
        // var url = "https://www.mhlw.go.jp/content/10906000/000634785.pdf"; // 5/28
        // var url = "https://www.mhlw.go.jp/content/10906000/000635195.pdf"; // 5/29
        // var url = "https://www.mhlw.go.jp/content/10906000/000635510.pdf"; // 5/30
        // var url = "https://www.mhlw.go.jp/content/10906000/000635537.pdf"; // 5/31
        // var url = "https://www.mhlw.go.jp/content/10906000/000635776.pdf"; // 6/1
        // var url = "https://www.mhlw.go.jp/content/10906000/000636132.pdf"; // 6/2
        // var url = "https://www.mhlw.go.jp/content/10906000/000636582.pdf"; // 6/3
        // var url = "https://www.mhlw.go.jp/content/10906000/000636975.pdf"; // 6/4
        // var url = "https://www.mhlw.go.jp/content/10906000/000637374.pdf"; // 6/5
        // var url = "https://www.mhlw.go.jp/content/10906000/000637517.pdf"; // 6/6
        // var url = "https://www.mhlw.go.jp/content/10906000/000637546.pdf"; // 6/7
        // var url = "https://www.mhlw.go.jp/content/10906000/000637876.pdf"; // 6/8
        // var url = "https://www.mhlw.go.jp/content/10906000/000638332.pdf"; // 6/9
        // var url = "https://www.mhlw.go.jp/content/10906000/000638691.pdf"; // 6/10
        // var url = "https://www.mhlw.go.jp/content/10906000/000638967.pdf"; // 6/11
        // var url = "https://www.mhlw.go.jp/content/10906000/000639342.pdf"; // 6/12
        // var url = "https://www.mhlw.go.jp/content/10906000/000639746.pdf"; // 6/13
        // var url = "https://www.mhlw.go.jp/content/10906000/000639769.pdf"; // 6/14
        // var url = "https://www.mhlw.go.jp/content/10906000/000640010.pdf"; // 6/15
        // var url = "https://www.mhlw.go.jp/content/10906000/000640393.pdf"; // 6/16
        // var url = "https://www.mhlw.go.jp/content/10906000/000640745.pdf"; // 6/17
        // var url = "https://www.mhlw.go.jp/content/10906000/000641280.pdf"; // 6/18
        // var url = "https://www.mhlw.go.jp/content/10906000/000641750.pdf"; // 6/19
        // var url = "https://www.mhlw.go.jp/content/10906000/000641944.pdf"; //6/20
        // var url = "https://www.mhlw.go.jp/content/10906000/000641965.pdf"; // 6/21
        // var url = "https://www.mhlw.go.jp/content/10906000/000642091.pdf"; // 6/22
        // var url = "https://www.mhlw.go.jp/content/10906000/000642428.pdf"; // 6/23
        // var url = "https://www.mhlw.go.jp/content/10906000/000642772.pdf"; // 6/24
        // var url = "https://www.mhlw.go.jp/content/10906000/000643528.pdf"; // 6/25
        // var url = "https://www.mhlw.go.jp/content/10906000/000644139.pdf"; // 6/26
        // var url = "https://www.mhlw.go.jp/content/10906000/000644329.pdf"; // 6/27
        // var url = "https://www.mhlw.go.jp/content/10906000/000644367.pdf"; // 6/28
        // var url = "https://www.mhlw.go.jp/content/10906000/000644583.pdf"; // 6/29
        // var url = "https://www.mhlw.go.jp/content/10906000/000645007.pdf"; // 6/30
        // var url = "https://www.mhlw.go.jp/content/10906000/000645320.pdf"; // 7/1
        // var url = "https://www.mhlw.go.jp/content/10906000/000645663.pdf"; // 7/2
        // var url = "https://www.mhlw.go.jp/content/10906000/000646194.pdf"; // 7/3 日付がとれず。文字化けあり
        // var url = "https://www.mhlw.go.jp/content/10906000/000646571.pdf"; // 7/4 日付がとれず。文字化けあり
        // var url = "https://www.mhlw.go.jp/stf/newpage_12250.html"; // 7/5
        // var url = "https://www.mhlw.go.jp/stf/newpage_12269.html"; // 7/6 // 日付エラー。文字化けあり
        // var url = "https://www.mhlw.go.jp/stf/newpage_12287.html"; // 7/7
        // var url = "https://www.mhlw.go.jp/stf/newpage_12312.html"; // 7/8
        // var url = "https://www.mhlw.go.jp/stf/newpage_12339.html"; // 7/9
        // var url = "https://www.mhlw.go.jp/stf/newpage_12360.html"; // 7/10
        // var url = "https://www.mhlw.go.jp/stf/newpage_12375.html"; // 7/11
        // var url = "https://www.mhlw.go.jp/stf/newpage_12378.html"; // 7/12
        // var url = "https://www.mhlw.go.jp/stf/newpage_12392.html"; // 7/13
        // var url = "https://www.mhlw.go.jp/stf/newpage_12427.html"; // 7/14
        // var url = "https://www.mhlw.go.jp/stf/newpage_12450.html"; // 7/15
        // var url = "https://www.mhlw.go.jp/stf/newpage_12467.html"; // 7/16
        // var url = "https://www.mhlw.go.jp/stf/newpage_12500.html"; // 7/17
        // var url = "https://www.mhlw.go.jp/stf/newpage_12515.html"; // 7/18
        // var url = "https://www.mhlw.go.jp/stf/newpage_12519.html"; // 7/19
        // var url = "https://www.mhlw.go.jp/stf/newpage_12545.html"; // 7/20
        // var url = "https://www.mhlw.go.jp/stf/newpage_12570.html"; // 7/21
        // var url = "https://www.mhlw.go.jp/stf/newpage_12601.html"; // 7/22
        // var url = "https://www.mhlw.go.jp/stf/newpage_12612.html"; // 7/23
        // var url = "https://www.mhlw.go.jp/stf/newpage_12616.html"; // 7/24
        // var url = "https://www.mhlw.go.jp/stf/newpage_12620.html"; // 7/25
        // var url = "https://www.mhlw.go.jp/stf/newpage_12623.html"; // 7/26
        // var url = "https://www.mhlw.go.jp/stf/newpage_12642.html"; // 7/27
        // var url = "https://www.mhlw.go.jp/stf/newpage_12668.html"; // 7/28
        // var url = "https://www.mhlw.go.jp/stf/newpage_12716.html"; // 7/29
        // var url = "https://www.mhlw.go.jp/stf/newpage_12742.html"; // 7/30
        // var url = "https://www.mhlw.go.jp/stf/newpage_12763.html"; // 7/31
        // var url = "https://www.mhlw.go.jp/stf/newpage_12776.html"; // 8/1
        // var url = "https://www.mhlw.go.jp/stf/newpage_12781.html"; // 8/2
        // var url = "https://www.mhlw.go.jp/stf/newpage_12800.html"; // 8/3
        // var url = "https://www.mhlw.go.jp/stf/newpage_12817.html"; // 8/4
        // var url = "https://www.mhlw.go.jp/stf/newpage_12851.html"; // 8/5
        // var url = "https://www.mhlw.go.jp/stf/newpage_12870.html"; // 8/6
        // var url = "https://www.mhlw.go.jp/stf/newpage_12895.html"; // 8/7
        // var url = "https://www.mhlw.go.jp/stf/newpage_12900.html"; // 8/8
        // var url = "https://www.mhlw.go.jp/stf/newpage_12907.html"; // 8/9
        // var url = "https://www.mhlw.go.jp/stf/newpage_12910.html"; // 8/10
        // var url = "https://www.mhlw.go.jp/stf/newpage_12918.html"; // 8/11
        // var url = "https://www.mhlw.go.jp/stf/newpage_12947.html"; // 8/12
        // var url = "https://www.mhlw.go.jp/stf/newpage_12978.html"; // 8/13
        // var url = "https://www.mhlw.go.jp/stf/newpage_12988.html"; // 8/14
        // var url = "https://www.mhlw.go.jp/stf/newpage_12999.html"; // 8/15
        // var url = "https://www.mhlw.go.jp/stf/newpage_13001.html"; // 8/16
        // var url = "https://www.mhlw.go.jp/stf/newpage_13013.html"; // 8/17
        // var url = "https://www.mhlw.go.jp/stf/newpage_13032.html"; // 8/18
        // var url = "https://www.mhlw.go.jp/stf/newpage_13047.html"; // 8/19
        // var url = "https://www.mhlw.go.jp/stf/newpage_13073.html"; // 8/20
        // var url = "https://www.mhlw.go.jp/stf/newpage_13115.html"; // 8/21
        // var url = "https://www.mhlw.go.jp/stf/newpage_13122.html"; // 8/22
        // var url = "https://www.mhlw.go.jp/stf/newpage_13127.html"; // 8/23
        // var url = "https://www.mhlw.go.jp/stf/newpage_13139.html"; // 8/24
        // var url = "https://www.mhlw.go.jp/stf/newpage_13172.html"; // 8/25
        // var url = "https://www.mhlw.go.jp/stf/newpage_13212.html"; // 8/26
        // var url = "https://www.mhlw.go.jp/stf/newpage_13239.html"; // 8/27
        // var url = "https://www.mhlw.go.jp/stf/newpage_13263.html"; // 8/28
        // var url = "https://www.mhlw.go.jp/stf/newpage_13271.html"; // 8/29
        var url = "https://www.mhlw.go.jp/stf/newpage_13273.html"; // 8/30

        if (true) {
            // since 5/9
            if (url.endsWith("html")) {
                url = ScrapeFromMhlwPDF2.getPDFUrl(url);
            }
            ScrapeFromMhlwPDF2.scrape(url);
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
}
