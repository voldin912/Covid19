package kis.covid19;

import java.time.LocalDate;
import java.util.List;
import kis.covid19.CreateData.Pref;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author naoki
 */
public class ScrapeFromMhlwPDF2Test {
    
    public ScrapeFromMhlwPDF2Test() {
    }

    @Test
    public void testRemoveSpaceFromPref() {
        assertEquals("兵庫 681 8,654 194 24 454 32 1",
                ScrapeFromMhlwPDF2.removeSpaceFromPref("兵    庫 681 8,654 194 24 454 32 1"));
        assertEquals("長野 74 2,054 37 2 37 不明 0",
                ScrapeFromMhlwPDF2.removeSpaceFromPref("?    野 74 2,054 37 2 37 不明 0"));
        assertEquals("北海道 928 8,013 487 23 394 47 0",
                ScrapeFromMhlwPDF2.removeSpaceFromPref("北 海 道 928 8,013 487 23 394 47 0"));
        assertEquals("青森 27 728 不明 不明 17 不明 10",
                ScrapeFromMhlwPDF2.removeSpaceFromPref("青　  森 27 728 不明 不明 17 不明 10"));
    }

    @Test
    public void testParseLine() {
        assertArrayEquals(new String[]{"沖縄","142","2752","10","4","127","6","0"},
                ScrapeFromMhlwPDF2.parseLine("沖    縄 142 2,752      10 4 　　     127 6 0"));
        assertArrayEquals(new String[]{"兵庫","681","8654","194","24","454","32","1"},
                ScrapeFromMhlwPDF2.parseLine("兵    庫 681 8,654   194 24 454 32 1"));
        assertArrayEquals(new String[]{"長野","74","2054","37","2","37","0","0"},
                ScrapeFromMhlwPDF2.parseLine("⾧    野 74 2,054 37 2 37 不明 0"));
        assertArrayEquals(new String[]{"長野","74","2054","37","2","37","0","0"},
                ScrapeFromMhlwPDF2.parseLine("?    野 74 2,054 37 2 37 不明 0"));
        assertArrayEquals(new String[]{"北海道","928","8013","487","23","394","47","0"},
                ScrapeFromMhlwPDF2.parseLine("北 海 道 928 8,013 487 23 394 47 0"));
        assertArrayEquals(new String[]{"青森","27","728","0","0","17","0","10"},
                ScrapeFromMhlwPDF2.parseLine("青　  森 27 728 不明 不明 17 不明 10"));
        assertArrayEquals(new String[]{"東京","4810","12990","2503","78","2136","171","0"},
                ScrapeFromMhlwPDF2.parseLine("東    京 4,810 12,990 2,503 78 2,136 171 0"));
    }

    @Test
    public void testCreatePref() {
        assertEquals(new Pref("東京都", 4810, 2503, 2136, 171, 78, 12990), 
                ScrapeFromMhlwPDF2.parsePref("東    京 4,810 12,990 2,503 78 2,136 171 0"));
        assertEquals(new Pref("青森県",27, 27-17, 17,0,0,728),
                ScrapeFromMhlwPDF2.parsePref("青　  森 27 728 不明 不明 17 不明 10"));
        assertEquals(new Pref("青森県",27, 27-17, 17,0,0,728),
                ScrapeFromMhlwPDF2.parsePref("青　  森 27 728 - - 17 - 10"));
    }
    
    @Test
    public void testPDFテキスト読み込み() {
        List<Pref> data = ScrapeFromMhlwPDF2.readData(data20200509);
        assertEquals(49, data.size());
        assertEquals(new Pref("東京都", 4810, 2503, 2136, 171, 78, 12990), 
                data.stream().filter(p -> p.pref.equals("東京都")).findFirst().get());
        assertEquals(new Pref("青森県",27, 27-17, 17,0,0,728),
                data.stream().filter(p -> p.pref.equals("青森県")).findFirst().get());
    }
    
    private <T> T peek(T arg) {
        System.out.println(arg);
        return arg;
    }
    
    @Test
    public void test日付読み込み() {
        assertEquals(LocalDate.of(2020, 5, 8), Util.readReiwaDate(data20200509));
    }
    
    String data20200509 = 
            "各都道府県の検査陽性者の状況（空港検疫、チャーター便案件を除く国内事例）\n" +
            "令和2年5月8日 24時時点 の各都道府県ウェブサイト上に公表されているデータ等に基づく数値\n" +
            "うち重症\n" +
            "北 海 道 928 8,013 487 23 394 47 0\n" +
            "青　  森 27 728 不明 不明 17 不明 10\n" +
            "岩    手 0 397 0 0 0 0 0\n" +
            "宮    城 88 2,183 13 不明 75 0 0\n" +
            "秋　　田 16 861 不明 不明 0 不明 16\n" +
            "山    形 69 2,401 13 不明 56 不明 0\n" +
            "福    島 81 2,237 38 不明 43 0 0\n" +
            "茨    城 168 3,826 71 3 78 9 10\n" +
            "栃    木 54 2,287 不明 不明 25 不明 29\n" +
            "群    馬 147 2,930 74 不明 55 18 0\n" +
            "埼    玉 946 11,394 391 10 514 41 0\n" +
            "千    葉 876 11,882 237 14 423 38 178\n" +
            "東    京 4,810 12,990 2,503 78 2,136 171 0\n" +
            "神 奈 川 1,150 1,947 417 34 645 47 41\n" +
            "新    潟 82 3,345 36 不明 46 不明 0\n" +
            "富    山 216 2,664 126 不明 77 13 0\n" +
            "石    川 275 2,175 132 不明 127 16 0\n" +
            "福    井 122 1,991 25 3 89 8 0\n" +
            "山    梨 56 2,806 8 1 48 0 0\n" +
            "?    野 74 2,054 37 2 37 不明 0\n" +
            "岐    阜 150 3,404 30 1 114 6 0\n" +
            "静    岡 73 3,036 26 2 46 1 0\n" +
            "愛    知 491 7,958 126 5 328 34 3\n" +
            "三    重 45 2,093 14 0 30 1 0\n" +
            "滋    賀 97 1,419 36 1 60 1 0\n" +
            "京    都 347 5,267 107 不明 227 13 0\n" +
            "大    阪 1,716 20,263 675 58 978 59 4\n" +
            "兵    庫 681 8,654 194 24 454 32 1\n" +
            "奈    良 89 2,035 25 不明 62 2 0\n" +
            "和 歌 山 62 3,310 14 不明 46 2 0\n" +
            "鳥    取 3 1,142 2 不明 1 0 0\n" +
            "島    根 24 888 不明 不明 10 不明 14\n" +
            "岡    山 23 1,321 8 不明 15 不明 0\n" +
            "広    島 165 5,746 102 不明 61 2 0\n" +
            "山    口 37 1,498 4 不明 33 不明 0\n" +
            "徳    島 5 569 不明 不明 3 不明 2\n" +
            "香    川 28 1,787 15 0 13 0 0\n" +
            "愛    媛 48 1,238 7 2 38 3 0\n" +
            "高    知 74 1,529 10 0 61 3 0\n" +
            "福    岡 651 10,246 184 不明 443 24 0\n" +
            "佐    賀 45 1,209 23 2 20 0 2\n" +
            "?    崎 17 2,878 2 0 14 1 0\n" +
            "熊    本 48 3,375 29 3 17 2 0\n" +
            "大    分 60 3,382 不明 不明 51 1 9\n" +
            "宮    崎 17 1,213 6 不明 11 不明 0\n" +
            "鹿 児 島 11 1,446 不明 不明 7 不明 4\n" +
            "沖    縄 142 2,403 55 不明 82 5 0\n" +
            "（その他）※3 149 623 不明 不明 0 不明 149\n" +
            "合計 15,483 179,043 6,302 266 8,110 600 472\n" +
            "※１ PCR検査実施人数は、一部自治体について件数を計上しているため、実際の人数より過大である。\n" +
            "※２ PCR検査陽性者数から入院治療等を要する者の数、退院又は療養となった者の数、死亡者の数を減じて厚労省において作成したもの\n" +
            "※３ その他は、?崎県のクルーズ船における陽性者\n" +
            "死亡（累積）\n" +
            "（人）\n" +
            "確認中※2\n" +
            "（人）\n" +
            "都道府県名\n" +
            "PCR検査陽\n" +
            "性者※1\n" +
            "PCR検査実\n" +
            "施人数\n" +
            "入院治療等を\n" +
            "要する者\n" +
            "（人）\n" +
            "退院又は療養解除\n" +
            "となった者の数\n" +
            "（人）";
    
}
