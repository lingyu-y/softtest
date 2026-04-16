package com.example.demo;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * JUnit 5 测试套件，用于批量运行 NaturalRanking 相关的所有单元测试。
 */
@Suite
@SuiteDisplayName("NaturalRanking 模块测试集")
@SelectClasses({
    NaturalRankingJUnitTest.class
})
public class NaturalRankingTestSuite {
    // 类体保持空白，仅作为套件入口
}