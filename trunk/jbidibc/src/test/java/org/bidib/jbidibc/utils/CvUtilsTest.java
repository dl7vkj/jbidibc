package org.bidib.jbidibc.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

public class CvUtilsTest {

    @Test
    public void preparePomBitCvValue() {
        Assert.assertEquals(CvUtils.preparePomBitCvValue(0, 1), 0xF8);
        Assert.assertEquals(CvUtils.preparePomBitCvValue(1, 1), 0xF9);
        Assert.assertEquals(CvUtils.preparePomBitCvValue(2, 1), 0xFA);
        Assert.assertEquals(CvUtils.preparePomBitCvValue(7, 1), 0xFF);

        Assert.assertEquals(CvUtils.preparePomBitCvValue(0, 0), 0xF0);
        Assert.assertEquals(CvUtils.preparePomBitCvValue(1, 0), 0xF1);
        Assert.assertEquals(CvUtils.preparePomBitCvValue(2, 0), 0xF2);
        Assert.assertEquals(CvUtils.preparePomBitCvValue(7, 0), 0xF7);
    }

    @Test
    public void preparePtBitCvValue() {
        // read
        Assert.assertEquals(CvUtils.preparePtBitCvValue(false, 0, 1), 0xE8);
        Assert.assertEquals(CvUtils.preparePtBitCvValue(false, 1, 1), 0xE9);
        Assert.assertEquals(CvUtils.preparePtBitCvValue(false, 2, 1), 0xEA);
        Assert.assertEquals(CvUtils.preparePtBitCvValue(false, 7, 1), 0xEF);

        Assert.assertEquals(CvUtils.preparePtBitCvValue(false, 0, 0), 0xE0);
        Assert.assertEquals(CvUtils.preparePtBitCvValue(false, 1, 0), 0xE1);
        Assert.assertEquals(CvUtils.preparePtBitCvValue(false, 2, 0), 0xE2);
        Assert.assertEquals(CvUtils.preparePtBitCvValue(false, 7, 0), 0xE7);

        // write
        Assert.assertEquals(CvUtils.preparePtBitCvValue(true, 0, 1), 0xF8);
        Assert.assertEquals(CvUtils.preparePtBitCvValue(true, 1, 1), 0xF9);
        Assert.assertEquals(CvUtils.preparePtBitCvValue(true, 2, 1), 0xFA);
        Assert.assertEquals(CvUtils.preparePtBitCvValue(true, 7, 1), 0xFF);

        Assert.assertEquals(CvUtils.preparePtBitCvValue(true, 0, 0), 0xF0);
        Assert.assertEquals(CvUtils.preparePtBitCvValue(true, 1, 0), 0xF1);
        Assert.assertEquals(CvUtils.preparePtBitCvValue(true, 2, 0), 0xF2);
        Assert.assertEquals(CvUtils.preparePtBitCvValue(true, 7, 0), 0xF7);
    }
}
