package kis.covid19;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author naoki
 */
public class ScrapeFromTokyoTest {
    
    public ScrapeFromTokyoTest() {
    }
/*
    @org.junit.jupiter.api.Test
    public void testSomeMethod() {
    }
  */  
    @Test
    public void test該当行読み込み() {
        var data = ScrapeFromTokyo.readMortality(text);
        assertArrayEquals(new String[]{"4,712","93","150"}, data);
    }
    
    public void test退院読み込み() {
        var data = ScrapeFromTokyo.readDischarge(text);
        assertEquals("1,612", data);
    }
    
    public void testデータ読み込み() {
        var data = ScrapeFromTokyo.readData(text);
        assertEquals(2950, data.hospitalizations);
        assertEquals(4712, data.patients);
        assertEquals(150, data.mortality);
        assertEquals(1612, data.discharges);
    }
    
    public void test日付読み込み() {
        var date = Util.readReiwaDate(text);
        assertEquals(LocalDate.of(2020, 5, 5), date);
    }
    
    String text = 
            "別紙\n" +
            "◆令和２年５月５日１８時３０分時点\n" +
            "１　患者の発生状況　\n" +
            "※１濃厚接触者：確定患者との接触歴があるもの\n" +
            "＊2つの欄に該当する場合があるため、内訳と総数が一致しない場合がある。\n" +
            "＜属性＞\n" +
            "〇年代\n" +
            "〇性別\n" +
            "２　都内患者数\n" +
            "※退院には、療養期間経過を含む \n" +
            "【参考】区市町村別患者数（都内発生分）　（５月４日時点の累計値）\n" +
            "2 1 13 8 7 8\n" +
            "100歳以上 不明\n" +
            "8 4 3 4 0 0\n" +
            "◆速報値のため、今後の調査状況により、変動の可能性があります。\n" +
            "総数\n" +
            "（内訳）\n" +
            "うち重症者\n" +
            "濃厚接触者※１ 海外渡航歴 調査中\n" +
            "60代 70代 80代 90代10歳未満 10代 20代 30代 40代 50代\n" +
            "58 29 0 29 1\n" +
            "総数（累計） 重症者 死亡（累計）\n" +
            "4,712 93 150\n" +
            "男性 女性 不明\n" +
            "28 30 0\n" +
            "31 90 289 336 70\n" +
            "千代田 中央 港 新宿 文京\n" +
            "75 116 128 177 129 197\n" +
            "墨田 江東 品川 目黒 大田台東\n" +
            "415 160 173 215 127\n" +
            "世田谷 渋谷 中野 杉並 豊島\n" +
            "75 45 99 191 114 109\n" +
            "荒川 板橋 練馬 足立 葛飾北\n" +
            "123 41 13 17 27\n" +
            "江戸川 八王子 立川 武蔵野 三鷹\n" +
            "3 66 9 35 46 14\n" +
            "府中 昭島 調布 町田 小金井青梅\n" +
            "16 18 10 11 6\n" +
            "小平 日野 東村山 国分寺 国立\n" +
            "1 19 6 12 12 2\n" +
            "狛江 東大和 清瀬 東久留米武蔵村山福生\n" +
            "瑞穂\n" +
            "29 10 5 7 40\n" +
            "多摩 稲城 羽村 あきる野 西東京\n" +
            "0 0 0 0\n" +
            "日の出 檜原 奥多摩 大島 利島\n" +
            "退院（累計）\n" +
            "1,612\n" +
            "147 547\n" +
            "※永寿総合病院関連195を含む\n" +
            "小笠原 都外 調査中※\n" +
            "0 0 0 0 0 0 0\n" +
            "新島 神津島 三宅 御蔵島 八丈 青ヶ島\n" +
            "今後の調査の状況により、数値は変更される可能性があります\n" +
            "1 0\n" +
            "№ リリース日 居住地 年代 性別 属性（職業等） 渡航歴 接触歴 備考 重症\n" +
            "4665 5月5日（火） 90代 男性\n" +
            "4666 5月5日（火） 50代 男性\n" +
            "4667 5月5日（火） 20代 女性\n" +
            "4720 5月5日（火） 30代 女性\n" +
            "4721 5月5日（火） 30代 男性\n" +
            "4722 5月5日（火） 70代 女性\n" +
            "都内感染者数（東京都発表）　　本日判明分：58名　総数：4712名\n" +
            "都内コロナウイルス陽性患者　報道数（５月５日１８時３０分 現在）\n" +
            "居住地、属性、渡航歴、接触歴等は、\n" +
            "現在、調査中です。\n" +
            "※ No.815、1192、1439、1440、1441、1621、1634、2356、2520、4470は欠番\n" +
            "2/2";    
}
