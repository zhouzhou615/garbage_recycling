package com.stu.util;

import org.springframework.stereotype.Component;

/**
 * 雪花算法 ID 生成器
 * 64位ID结构：
 * 0 | 41位时间戳 | 10位机器ID | 12位序列号
 */
@Component
public class SnowflakeIdGenerator {
    // 自定义起始时间戳
    private static final long START_TIMESTAMP = 1672531200000L;
    // 各部分占用的位数
    private static final long SEQUENCE_BITS = 12;
    private static final long MACHINE_BITS = 10;
    // 各部分的最大值
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);
    private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_BITS);
    // 位移偏移量
    private static final long MACHINE_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_BITS;

    private final long machineId; // 机器ID
    private long sequence = 0L;   // 序列号
    private long lastTimestamp = -1L; // 上一次生成ID的时间戳

    public SnowflakeIdGenerator() {
      this(1);
    }
    public SnowflakeIdGenerator(long machineId) {
        if (machineId > MAX_MACHINE_ID || machineId < 0) {
            throw new IllegalArgumentException("机器ID不能大于 " + MAX_MACHINE_ID + " 或小于 0");
        }
        this.machineId = machineId;
    }
    /**
     * 生成下一个唯一ID（线程安全）
     */
    public synchronized long nextId() {
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("系统时钟回拨，拒绝生成ID");
        }

        if (currentTimestamp == lastTimestamp) {
            // 同一毫秒内，序列号递增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                // 序列号溢出，等待下一毫秒
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0L; // 毫秒内第一次生成，序列号置 0
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT)
                | (machineId << MACHINE_SHIFT)
                | sequence;
    }

    /**
     * 阻塞直到下一个毫秒
     */
    private long waitNextMillis(long currentTimestamp) {
        while (currentTimestamp <= lastTimestamp) {
            currentTimestamp = System.currentTimeMillis();
        }
        return currentTimestamp;
    }
}
