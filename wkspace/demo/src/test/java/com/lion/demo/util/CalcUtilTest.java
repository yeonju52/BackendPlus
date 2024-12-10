package com.lion.demo.util;

import org.junit.jupiter.api.Test;      // JUnit 5

import static org.assertj.core.api.Assertions.assertThat;

public class CalcUtilTest {
    private final CalcUtil calcUtil = new CalcUtil();

    @Test
    void testAdd() {
        // Given: 테스트 준비 단계
        int x = 3, y = 4;

        // When: 테스트 실행 단계
        int result = calcUtil.add(x, y);

        // Then: 테스트 검증 단계
        assertThat(result).isEqualTo(7);
    }

    @Test
    void testMul() {
        int x = 3, y = 4;
        int result = calcUtil.mul(x, y);
        assertThat(result).isEqualTo(12);
    }
}
