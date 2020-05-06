/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kis.covid19;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author naoki
 */
public class UtilTest {
    
    public UtilTest() {
    }

    @Test
    public void test全角数字変換() {
        assertEquals("今日は5月6日です", Util.zenDigitToHan("今日は５月６日です"));
        assertEquals("01234567890123456789", Util.zenDigitToHan("０１２３４５６７８９０１２３４５６７８９"));
    }
    
}
