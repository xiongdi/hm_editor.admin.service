package com.hm.editor.common.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * HashIdGenerator
 * 基于内容的确定性 ID 生成器 (Java 8 无依赖版)
 * * 特性：
 * 1. 核心算法：SHA-512
 * 2. 默认长度：8位
 * 3. 长度范围：6 - 12 位 (业务强制约束)
 */
public class HashIdGenerator {

    // 安全字典：移除了 I, L, O, U
    private static final char[] ALPHABET = "0123456789ABCDEFGHJKMNPQRSTVWXYZ".toCharArray();
    private static final int BASE = ALPHABET.length;

    // 默认长度常量 (8位是最佳平衡点)
    private static final int DEFAULT_LENGTH = 8;

    // ==========================================
    // 公共接口 (Public Methods)
    // ==========================================

    /**
     * 生成 ID (String 输入, 默认 8 位)
     */
    public static String generate(String content) {
        return generate(content, DEFAULT_LENGTH);
    }

    /**
     * 生成 ID (String 输入, 指定长度)
     */
    public static String generate(String content, int length) {
        if (content == null) {
            throw new IllegalArgumentException("输入内容不能为空");
        }
        return generate(content.getBytes(StandardCharsets.UTF_8), length);
    }

    /**
     * 生成 ID (byte[] 输入, 默认 8 位)
     */
    public static String generate(byte[] data) {
        return generate(data, DEFAULT_LENGTH);
    }

    /**
     * 核心实现：生成 ID (byte[] 输入, 指定长度)
     */
    public static String generate(byte[] data, int length) {
        // 参数校验
        if (data == null) {
            throw new IllegalArgumentException("输入数据不能为空");
        }

        // ✅ 修改点：严格限制长度在 6-12 之间
        // 6位: 最低安全线 (约10亿组合)
        // 12位: 工业级安全 (约100亿亿组合)
        if (length < 6 || length > 12) {
            throw new IllegalArgumentException("长度必须在 6 到 12 之间 (业务约束)");
        }

        try {
            // 1. 使用 SHA-512 计算哈希
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hashBytes = digest.digest(data);

            // 2. 转换为正大数 (BigInteger)
            BigInteger number = new BigInteger(1, hashBytes);

            StringBuilder sb = new StringBuilder();

            // 3. Base32 编码转换
            for (int i = 0; i < length; i++) {
                BigInteger[] result = number.divideAndRemainder(BigInteger.valueOf(BASE));
                sb.append(ALPHABET[result[1].intValue()]);
                number = result[0];
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前 JVM 不支持 SHA-512", e);
        }
    }

    // ==========================================
    // 测试演示
    // ==========================================
    public static void main(String[] args) {
        String input = "test_limit";

        System.out.println("=== HashIdGenerator 演示 (限制 6-12) ===");

        try {
            // 1. 正常范围测试
            System.out.println("6位 (最小): " + generate(input, 6));
            System.out.println("8位 (默认): " + generate(input));
            System.out.println("12位(最大): " + generate(input, 12));
            // 2. 越界测试
            // System.out.println("5位 (太短): " + generate(input, 5)); // 抛异常
            // System.out.println("13位(太长): " + generate(input, 13)); // 抛异常
        } catch (IllegalArgumentException e) {
            System.err.println("错误捕获: " + e.getMessage());
        }
    }
}
