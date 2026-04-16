package com.example.demo;

import org.apache.commons.statistics.ranking.NaNStrategy;
import org.apache.commons.statistics.ranking.NaturalRanking;
import org.apache.commons.statistics.ranking.TiesStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * JUnit 5 单元测试：验证 NaturalRanking 类的功能，包括对注入缺陷的检测。
 */
@DisplayName("NaturalRanking 单元测试")
public class NaturalRankingJUnitTest {

    @Test
    @DisplayName("AVERAGE 策略 + 重复值（应触发缺陷）")
    void testAverageTiesWithDuplicates() {
        NaturalRanking ranking = new NaturalRanking(TiesStrategy.AVERAGE);
        double[] data = {2.0, 5.0, 5.0, 8.0};
        double[] expected = {1.0, 2.5, 2.5, 4.0};
        double[] actual = ranking.apply(data);
        assertArrayEquals(expected, actual,
            "AVERAGE 策略下，重复值应得到平均排名");
    }

    @Test
    @DisplayName("AVERAGE 策略 + 无重复值")
    void testAverageTiesWithoutDuplicates() {
        NaturalRanking ranking = new NaturalRanking(TiesStrategy.AVERAGE);
        double[] data = {3.0, 1.0, 4.0, 2.0};
        double[] expected = {3.0, 1.0, 4.0, 2.0};
        double[] actual = ranking.apply(data);
        assertArrayEquals(expected, actual);
    }

    @Test
    @DisplayName("MINIMUM 策略")
    void testMinimumTies() {
        NaturalRanking ranking = new NaturalRanking(TiesStrategy.MINIMUM);
        double[] data = {2.0, 5.0, 5.0, 8.0};
        double[] expected = {1.0, 2.0, 2.0, 4.0};
        double[] actual = ranking.apply(data);
        assertArrayEquals(expected, actual);
    }

    @Test
    @DisplayName("MAXIMUM 策略")
    void testMaximumTies() {
        NaturalRanking ranking = new NaturalRanking(TiesStrategy.MAXIMUM);
        double[] data = {2.0, 5.0, 5.0, 8.0};
        double[] expected = {1.0, 3.0, 3.0, 4.0};
        double[] actual = ranking.apply(data);
        assertArrayEquals(expected, actual);
    }

    @Test
    @DisplayName("NaN 策略 MAXIMAL")
    void testNaNMaximal() {
        NaturalRanking ranking = new NaturalRanking(NaNStrategy.MAXIMAL);
        double[] data = {2.0, Double.NaN, 1.0};
        double[] expected = {2.0, 3.0, 1.0};
        double[] actual = ranking.apply(data);
        assertArrayEquals(expected, actual);
    }

    @Test
    @DisplayName("空数组边界测试")
    void testEmptyArray() {
        NaturalRanking ranking = new NaturalRanking();
        double[] data = {};
        double[] expected = {};
        double[] actual = ranking.apply(data);
        assertArrayEquals(expected, actual);
    }
}