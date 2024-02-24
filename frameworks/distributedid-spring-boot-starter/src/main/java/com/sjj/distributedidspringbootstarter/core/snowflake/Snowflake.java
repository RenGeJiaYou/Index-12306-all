package com.sjj.distributedidspringbootstarter.core.snowflake;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.sjj.distributedidspringbootstarter.core.IdGenerator;

import java.io.Serializable;
import java.util.Date;

/**
 * 雪花码实体类
 *
 * @Author Island_World
 */

public class Snowflake implements Serializable, IdGenerator {

    private static final long serialVersionUID = 1L;

    // 默认起始时间戳,设置为北京时间 2020-01-01 00:00:00
    private static final long DEFAULT_START_TIMESTAMP = 1577808000L;

    // 默认回拨时间，2S
    private static final long DEFAULT_TIME_OFFSET = 2000L;

    // 标识码-workId 的 bit 位数
    private static final long WORKER_ID_BITS = 5L;

    /**
     * 最大支持机器节点数0~31，一共32个
     * <p>
     * `@SuppressWarnings` 是 Java 中的一个注解，用于告诉编译器忽略指定的警告。它不会影响程序的执行，只是关于编译器输出信息的提示。
     * <p>
     * 在这段代码中，`@SuppressWarnings({"PointlessBitwiseExpression", "FieldCanBeLocal"})` 用于抑制 "PointlessBitwiseExpression" 和 "FieldCanBeLocal" 这两种类型的警告。
     * <p>
     * - "PointlessBitwiseExpression" 警告是指编译器认为某个位运算表达式没有意义，可能是因为运算结果与原值相同，或者运算结果始终为某个固定值。在这段代码中，可能是因为某些位运算表达式的结果并没有被使用或者改变程序的行为。
     * <p>
     * - "FieldCanBeLocal" 警告是指编译器认为某个字段可以被声明为局部变量，因为它只在当前方法或代码块中被使用，没有被其他方法或代码块引用。在这段代码中，可能是因为某些字段只在当前方法或代码块中被使用。
     * <p>
     * 这两种警告通常不会影响程序的功能，但可能会影响代码的可读性和维护性。使用 `@SuppressWarnings` 注解可以抑制这些警告，使编译器输出的信息更加清晰。
     */
    @SuppressWarnings({"PointlessBitwiseExpression", "FieldCanBeLocal"})
    private static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);

    // 标识码-dataCenterId 的 bit 位数
    private static final long DATA_CENTER_ID_BITS = 5L;

    // 最大支持数据中心数0~31，一共32个
    @SuppressWarnings({"PointlessBitwiseExpression", "FieldCanBeLocal"})
    private static final long MAX_DATA_CENTER_ID = -1L ^ (-1L << DATA_CENTER_ID_BITS);

    // 序列号12位（表示只允许workId的范围为：0-4095）
    private static final long SEQUENCE_BITS = 12L;

    // 机器节点左移12位
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    // 数据中心节点左移17位
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    // 时间毫秒数左移22位
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    // 序列掩码，用于限定序列最大值不能超过4095，二进制值是右起 12 个 1
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    // 基准时间点，twitter-epoch,基准时间点 + 时间戳 = 实际生成时间
    private final long twepoch;

    private final long workerId;

    private final long dataCenterId;

    private final boolean useSystemClock;

    // 允许的时钟回拨毫秒数
    private final long timeOffset;

    /**
     * 当在低频模式下时，序号始终为0，导致生成ID始终为偶数<br>
     * 此属性用于限定一个随机上限，在不同毫秒下生成序号时，给定一个随机数，避免偶数问题。<br>
     * 注意次数必须小于{@link #SEQUENCE_MASK}，{@code 0}表示不使用随机数。<br>
     * 这个上限不包括值本身。
     */
    private final long randomSequenceLimit;

    /**
     * 同一毫秒内的自增序号<p>当高频模式下时，同一毫秒内生成N个ID，则这个序号在同一毫秒下，自增以避免ID重复。
     */
    private long sequence = 0L;

    private long lastTimestamp = -1L;

    /**
     * 构造函数<p>
     * workerId = HuTool.IdUtil 生成 <p>
     * dataCenterId = HuTool.IdUtil 生成 <p>
     * useSystemClock = false<p>
     * epochDate = null，将调用默认常量 <p>
     * timeOffset = DEFAULT_TIME_OFFSET(2000ms)<p>
     * randomSequenceLimit = 0，不使用随机数
     */
    public Snowflake() {
        this(IdUtil.getWorkerId(IdUtil.getDataCenterId(MAX_DATA_CENTER_ID), MAX_WORKER_ID));
    }

    /**
     * 构造函数<p>
     * dataCenterId = HuTool.IdUtil 生成 <p>
     * useSystemClock = false<p>
     * epochDate = null，将调用默认常量 <p>
     * timeOffset = DEFAULT_TIME_OFFSET(2000ms)<p>
     * randomSequenceLimit = 0，不使用随机数
     *
     * @param workerId 工作机器节点id
     */
    public Snowflake(long workerId) {
        this(workerId, IdUtil.getDataCenterId(MAX_DATA_CENTER_ID));
    }

    /**
     * 构造函数<p>
     * useSystemClock = false<p>
     * epochDate = null，将调用默认常量 <p>
     * timeOffset = DEFAULT_TIME_OFFSET(2000ms)<p>
     * randomSequenceLimit = 0，不使用随机数
     *
     * @param workerId     工作机器节点id
     * @param dataCenterId 数据中心id
     */
    public Snowflake(long workerId, long dataCenterId) {
        this(workerId, dataCenterId, false);
    }

    /**
     * 构造函数<p>
     * epochDate = null，将调用默认常量 <p>
     * timeOffset = DEFAULT_TIME_OFFSET(2000ms)<p>
     * randomSequenceLimit = 0，不使用随机数
     *
     * @param epochDate      初始化时间起点（null表示默认起始日期）,后期修改会导致id重复,如果要修改连workerId dataCenterId，慎用
     * @param workerId       工作机器节点id
     * @param dataCenterId   数据中心id
     * @param useSystemClock 是否使用{@link SystemClock} 获取当前时间戳
     */
    public Snowflake(long workerId, long dataCenterId, boolean useSystemClock) {
        this(null, workerId, dataCenterId, useSystemClock, DEFAULT_TIME_OFFSET);
    }

    /**
     * 构造函数<p>
     * timeOffset = DEFAULT_TIME_OFFSET(2000ms)<p>
     * randomSequenceLimit = 0，不使用随机数
     *
     * @param epochDate      初始化时间起点（null表示默认起始日期）,后期修改会导致id重复,如果要修改连workerId dataCenterId，慎用
     * @param workerId       工作机器节点id
     * @param dataCenterId   数据中心id
     * @param useSystemClock 是否使用{@link SystemClock} 获取当前时间戳
     */
    public Snowflake(Date epochDate, long workerId, long dataCenterId, boolean useSystemClock) {
        this(epochDate, workerId, dataCenterId, useSystemClock, DEFAULT_TIME_OFFSET);
    }

    /**
     * 构造函数<p>
     * randomSequenceLimit = 0，不使用随机数
     *
     * @param epochDate      初始化时间起点（null表示默认起始日期）,后期修改会导致id重复,如果要修改连workerId dataCenterId，慎用
     * @param workerId       工作机器节点id
     * @param dataCenterId   数据中心id
     * @param useSystemClock 是否使用{@link SystemClock} 获取当前时间戳
     * @param timeOffset     允许时间回拨的毫秒数
     */
    public Snowflake(Date epochDate, long workerId, long dataCenterId, boolean useSystemClock, long timeOffset) {
        this(epochDate, workerId, dataCenterId, useSystemClock, timeOffset, 0L);
    }

    /**
     * 全参构造
     *
     * @param epochDate           初始化时间起点（null表示默认起始日期）,后期修改会导致id重复,如果要修改连workerId dataCenterId，慎用
     * @param workerId            工作机器节点id
     * @param dataCenterId        数据中心id
     * @param useSystemClock      是否使用{@link SystemClock} 获取当前时间戳
     * @param timeOffset          允许时间回拨的毫秒数
     * @param randomSequenceLimit 随机序列上限，用于避免高频模式下ID都为偶数问题
     */
    public Snowflake(Date epochDate, long workerId, long dataCenterId, boolean useSystemClock, long timeOffset, long randomSequenceLimit) {
        this.twepoch = (null != epochDate) ? epochDate.getTime() : DEFAULT_START_TIMESTAMP;
        this.workerId = Assert.checkBetween(workerId, 0, MAX_WORKER_ID, "Worker ID must be between 0 and " + MAX_WORKER_ID);
        this.dataCenterId = Assert.checkBetween(dataCenterId, 0, MAX_DATA_CENTER_ID, "DataCenter ID must be between 0 and " + MAX_DATA_CENTER_ID);
        this.useSystemClock = useSystemClock;
        this.timeOffset = timeOffset;
        this.randomSequenceLimit = Assert.checkBetween(randomSequenceLimit, 0, SEQUENCE_MASK, "Random sequence limit must be between 0 and " + SEQUENCE_MASK);
    }

    /**
     * 根据Snowflake的ID，获取机器id
     *
     * @param id snowflake算法生成的id
     * @return 所属机器的id
     */
    public long getWorkerId(long id) {
        return id >> WORKER_ID_SHIFT & ~(-1L << WORKER_ID_BITS);
    }

    /**
     * 根据Snowflake的ID，获取数据中心id
     *
     * @param id snowflake算法生成的id
     * @return 所属数据中心
     */
    public long getDataCenterId(long id) {
        return id >> DATA_CENTER_ID_SHIFT & ~(-1L << DATA_CENTER_ID_BITS);
    }

    /**
     * 根据Snowflake的ID，获取生成时间
     *
     * @param id snowflake算法生成的id
     * @return 生成的时间
     */
    public long getGenerateDateTime(long id) {
        return id >> DATA_CENTER_ID_SHIFT & ~(-1L << DATA_CENTER_ID_BITS);
    }

    /**
     * 生成时间戳
     */
    private long genTime() {
        return this.useSystemClock ? SystemClock.now() : System.currentTimeMillis();
    }

    /**
     * 循环等待下一个时间
     *
     * @param lastTimestamp 上次记录的时间
     * @return 下一个时间
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = genTime();
        // 循环直到操作系统时间戳变化
        while (timestamp == lastTimestamp) {
            timestamp = genTime();
        }
        if (timestamp < lastTimestamp) {
            // 如果发现新的时间戳比上次记录的时间戳数值小，说明操作系统时间发生了倒退，报错
            throw new IllegalStateException(StrUtil.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        return timestamp;
    }

    /**
     * 生成时间戳并返回完整雪花 ID
     * */
    @Override
    public synchronized long nextId() {
        long timestamp = genTime();
        // 分类讨论时间戳早于/等于/晚于上次记录时间戳的情况
        if (timestamp < this.lastTimestamp) {
            // 如果发现新的时间戳比上次记录的时间戳数值小，说明操作系统时间发生了倒退，报错
            if (this.lastTimestamp - timestamp < this.timeOffset) {
                // 容忍指定的回拨，避免NTP校时造成的异常
                timestamp = lastTimestamp;
            } else {
                throw new IllegalStateException(StrUtil.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
            }
        }
        if (timestamp == this.lastTimestamp) {
            // 获取下一个序号
            final long sequence = (this.sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0 ){
                timestamp = tilNextMillis(lastTimestamp);
            }
            this.sequence = sequence;
        } else {
            if (randomSequenceLimit > 1) {
                sequence = RandomUtil.randomLong(randomSequenceLimit);
            } else {
                sequence = 0L;
            }
        }
        lastTimestamp = timestamp;
        return (timestamp - twepoch)<< TIMESTAMP_LEFT_SHIFT
                | (dataCenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 下一个ID（字符串形式）
     *
     * @return ID 字符串形式
     */
    public String nextIdStr() {
        return Long.toString(nextId());
    }

    /**
     * 解析雪花算法生成的 ID 为对象
     *
     * @param snowflakeId 雪花算法生成的 ID
     * @return 雪花算法对象
     */
    public SnowflakeIdInfo parseSnowflakeId(long snowflakeId) {
        return SnowflakeIdInfo
                .builder()
                .timestamp((snowflakeId >> TIMESTAMP_LEFT_SHIFT) + twepoch)
                .dataCenterId((int) (snowflakeId >> DATA_CENTER_ID_SHIFT & ~(-1L << DATA_CENTER_ID_BITS)))
                .workerId((int) (snowflakeId >> WORKER_ID_SHIFT & ~(-1L << WORKER_ID_BITS)))
                .sequence((int) (snowflakeId & SEQUENCE_MASK))
                .build();
    }
}
