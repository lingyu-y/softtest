package com.example.demo;

import org.apache.commons.statistics.ranking.NaNStrategy;
import org.apache.commons.statistics.ranking.NaturalRanking;
import org.apache.commons.statistics.ranking.TiesStrategy;

import java.util.Arrays;

/**
 * 不依赖测试框架的单元测试类，用于验证 NaturalRanking 类的功能正确性。
 * 通过 main 方法驱动，对比期望输出与实际输出，暴露注入的缺陷。
 */
public class NaturalRankingTest {

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        System.out.println("=== NaturalRanking 单元测试开始 ===\n");

        // 测试1：默认策略（AVERAGE）下包含重复值的情况（缺陷触发点）
        if (testAverageTiesWithDuplicates()) {
            passed++;
        } else {
            failed++;
        }

        // 测试2：默认策略下数据全部互异（不应触发缺陷）
        if (testAverageTiesWithoutDuplicates()) {
            passed++;
        } else {
            failed++;
        }

        // 测试3：MINIMUM 策略（不应受缺陷影响）
        if (testMinimumTies()) {
            passed++;
        } else {
            failed++;
        }

        // 测试4：MAXIMUM 策略
        if (testMaximumTies()) {
            passed++;
        } else {
            failed++;
        }

        // 测试5：NaN 处理策略 MAXIMAL
        if (testNaNMaximal()) {
            passed++;
        } else {
            failed++;
        }

        // 测试6：空数组边界测试
        if (testEmptyArray()) {
            passed++;
        } else {
            failed++;
        }

        System.out.println("\n=== 测试结果汇总 ===");
        System.out.println("通过: " + passed);
        System.out.println("失败: " + failed);

        if (failed > 0) {
            System.out.println("\n[警告] 存在未通过的测试用例，请检查缺陷！");
        } else {
            System.out.println("\n[成功] 所有测试用例均通过（若已注入缺陷，则预期部分用例应失败）。");
        }
    }

    /**
     * 测试用例1：AVERAGE 策略 + 包含重复值。
     * 输入: [2.0, 5.0, 5.0, 8.0]
     * 期望输出 (正确行为): [1.0, 2.5, 2.5, 4.0]
     * 实际输出 (缺陷版本): [1.0, 2.0, 2.0, 4.0]
     */
    private static boolean testAverageTiesWithDuplicates() {
        System.out.println("测试1: AVERAGE 策略 + 重复值 [2.0, 5.0, 5.0, 8.0]");
        NaturalRanking ranking = new NaturalRanking(TiesStrategy.AVERAGE);
        double[] data = {2.0, 5.0, 5.0, 8.0};
        double[] expected = {1.0, 2.5, 2.5, 4.0};
        double[] actual = ranking.apply(data);
        boolean result = Arrays.equals(expected, actual);
        printResult(result, expected, actual);
        return result;
    }

    /**
     * 测试用例2：AVERAGE 策略 + 数据全部互异。
     * 输入: [3.0, 1.0, 4.0, 2.0]
     * 期望输出: [3.0, 1.0, 4.0, 2.0] (排名对应值的大小顺序)
     */
    private static boolean testAverageTiesWithoutDuplicates() {
        System.out.println("测试2: AVERAGE 策略 + 无重复值 [3.0, 1.0, 4.0, 2.0]");
        NaturalRanking ranking = new NaturalRanking(TiesStrategy.AVERAGE);
        double[] data = {3.0, 1.0, 4.0, 2.0};
        double[] expected = {3.0, 1.0, 4.0, 2.0}; // 最小值1排第1，最大值4排第4
        double[] actual = ranking.apply(data);
        boolean result = Arrays.equals(expected, actual);
        printResult(result, expected, actual);
        return result;
    }

    /**
     * 测试用例3：MINIMUM 策略。
     * 输入: [2.0, 5.0, 5.0, 8.0]
     * 期望输出: [1.0, 2.0, 2.0, 4.0]
     */
    private static boolean testMinimumTies() {
        System.out.println("测试3: MINIMUM 策略 [2.0, 5.0, 5.0, 8.0]");
        NaturalRanking ranking = new NaturalRanking(TiesStrategy.MINIMUM);
        double[] data = {2.0, 5.0, 5.0, 8.0};
        double[] expected = {1.0, 2.0, 2.0, 4.0};
        double[] actual = ranking.apply(data);
        boolean result = Arrays.equals(expected, actual);
        printResult(result, expected, actual);
        return result;
    }

    /**
     * 测试用例4：MAXIMUM 策略。
     * 输入: [2.0, 5.0, 5.0, 8.0]
     * 期望输出: [1.0, 3.0, 3.0, 4.0]
     */
    private static boolean testMaximumTies() {
        System.out.println("测试4: MAXIMUM 策略 [2.0, 5.0, 5.0, 8.0]");
        NaturalRanking ranking = new NaturalRanking(TiesStrategy.MAXIMUM);
        double[] data = {2.0, 5.0, 5.0, 8.0};
        double[] expected = {1.0, 3.0, 3.0, 4.0};
        double[] actual = ranking.apply(data);
        boolean result = Arrays.equals(expected, actual);
        printResult(result, expected, actual);
        return result;
    }

    /**
     * 测试用例5：NaN 策略 MAXIMAL (将 NaN 视为最大值)。
     * 输入: [2.0, Double.NaN, 1.0]
     * 期望输出: [2.0, 3.0, 1.0]  (NaN最大排第3，1.0最小排第1)
     */
    private static boolean testNaNMaximal() {
        System.out.println("测试5: NaNStrategy.MAXIMAL [2.0, NaN, 1.0]");
        NaturalRanking ranking = new NaturalRanking(NaNStrategy.MAXIMAL);
        double[] data = {2.0, Double.NaN, 1.0};
        double[] expected = {2.0, 3.0, 1.0};
        double[] actual = ranking.apply(data);
        boolean result = Arrays.equals(expected, actual);
        printResult(result, expected, actual);
        return result;
    }

    /**
     * 测试用例6：空数组边界测试。
     * 输入: [] (空数组)
     * 期望输出: [] (空数组)
     */
    private static boolean testEmptyArray() {
        System.out.println("测试6: 空数组");
        NaturalRanking ranking = new NaturalRanking();
        double[] data = {};
        double[] expected = {};
        double[] actual = ranking.apply(data);
        boolean result = Arrays.equals(expected, actual);
        printResult(result, expected, actual);
        return result;
    }

    /**
     * 辅助方法：打印测试结果。
     */
    private static void printResult(boolean passed, double[] expected, double[] actual) {
        if (passed) {
            System.out.println("  => 通过");
        } else {
            System.out.println("  => 失败");
            System.out.println("     期望: " + Arrays.toString(expected));
            System.out.println("     实际: " + Arrays.toString(actual));
        }
        System.out.println();
    }
}