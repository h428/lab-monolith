package com.lab.common.builder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * 用于构造 Jackson 的 ObjectMapper 对象的构造器；
 * 由于 web/util/redis 经常需要不同参数的 ObjectMapper，但绝大部分参数又是相同，因此统一编写构造器根据不同参数进行序列化
 */
public class ObjectMapperBuilder {

    /**
     * 指定时区，默认为东八区
     */
    private String timeZone = "GMT+8:00";

    /**
     * 日期时间格式，LocalDateTime 和 Date 采用该格式
     */
    private String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * java8 LocalDate 的日期格式
     */
    private String datePattern = "yyyy-MM-dd";

    /**
     * java8 LocalTime 的时间格式
     */
    private String timePattern = "HH:mm:ss";

    /**
     * PrettyPrinter 格式化输出，开启后 Json 自动被格式化，在控制台打印可直接看到格式化后的 json；
     * 使用该特性可以方便 json 日志的查阅，但注意会导致 json 比原有的内容多出很多空格，增加额外的传输和存储，需要自行斟酌是否启用
     */
    private boolean indentOutput = false;

    /**
     * 是否将 long 类型转换为 string 类型进行序列化，web 层为了避免 js 的 long 精度丢失而可能需要
     */
    private boolean longToString = false;

    /**
     * 是否保存类型信息，如果是，Jackson 会默认以属性形式保存类型信息，字段名为 @class
     */
    private boolean saveType = false;


    public static ObjectMapperBuilder builder() {
        return new ObjectMapperBuilder();
    }

    public ObjectMapper build() {
        ObjectMapper objectMapper = new ObjectMapper();


        /* 统一的通用设置 */

        // 设置接收到未知属性忽略该属性，不报错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 设置忽略无法转换的对象
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 不序列化 null 属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        /* 参数化设置 */

        // 根据 indentOutput 控制是否格式化 json
        // PrettyPrinter 格式化输出，Json 自动被格式化，在控制台打印可直接看到格式化后的 json；
        // 使用该特性可以方便 json 日志的查阅，但注意会导致 json 比原有的内容多出很多空格，增加额外的传输和存储，需要自行斟酌是否启用
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, this.indentOutput);

        // 根据 longToString 控制是否将 long 转化为 string
        if (longToString) {
            // 将 Long, BigInteger 序列化的时候,转化为 String，以处理 js 的 Long 精度丢失的问题
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
            simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
            simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
            // 将该 simpleModel 注册到 ObjectMapper
            objectMapper.registerModule(simpleModule);
        }

        if (saveType) {
            // 指定允许序列化所有非 final 属性，同时序列化是以属性形式添加类型信息
            // Redis 采用 Json 方式序列化时，建议开启以保证类嵌套属性反序列化的正确
            objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
        }

        /* 时间格式设置：根据时间相关变量设置时间序列化格式 */

        // 指定时区为东八区
        objectMapper.setTimeZone(TimeZone.getTimeZone(this.timeZone));
        // 设置 Date 类型的时间格式
        objectMapper.setDateFormat(new SimpleDateFormat(this.dateTimePattern));
        // java8 日期日期处理
        JavaTimeModule javaTimeModule = new JavaTimeModule(); // 构造 javaTimeModule
        // 设置 LocalDateTime 的序列化和反序列化的时间格式
        javaTimeModule.addSerializer(LocalDateTime.class,
            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(this.dateTimePattern)));
        javaTimeModule.addDeserializer(LocalDateTime.class,
            new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(this.dateTimePattern)));
        // 设置 LocalDate 的序列化和反序列化的日期格式
        javaTimeModule.addSerializer(LocalDate.class,
            new LocalDateSerializer(DateTimeFormatter.ofPattern(this.datePattern)));
        javaTimeModule.addDeserializer(LocalDate.class,
            new LocalDateDeserializer(DateTimeFormatter.ofPattern(this.datePattern)));
        // 设置 LocalTime 的序列化和反序列化的时间格式
        javaTimeModule.addSerializer(LocalTime.class,
            new LocalTimeSerializer(DateTimeFormatter.ofPattern(this.timePattern)));
        javaTimeModule.addDeserializer(LocalTime.class,
            new LocalTimeDeserializer(DateTimeFormatter.ofPattern(this.timePattern)));
        // 将 javaTimeModule 注册到 objectMapper
        objectMapper.registerModule(javaTimeModule);


        return objectMapper;
    }

    public ObjectMapperBuilder timeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public ObjectMapperBuilder dateTimePattern(String dateTimePattern) {
        this.dateTimePattern = dateTimePattern;
        return this;
    }

    public ObjectMapperBuilder datePattern(String datePattern) {
        this.datePattern = datePattern;
        return this;
    }

    public ObjectMapperBuilder timePattern(String timePattern) {
        this.timePattern = timePattern;
        return this;
    }

    public ObjectMapperBuilder indentOutput(boolean indentOutput) {
        this.indentOutput = indentOutput;
        return this;
    }

    public ObjectMapperBuilder longToString(boolean longToString) {
        this.longToString = longToString;
        return this;
    }

    public ObjectMapperBuilder saveType(boolean saveType) {
        this.saveType = saveType;
        return this;
    }

    /**
     * 备份的其他 ObjectMapper 设置
     *
     * @param objectMapper 序列化器
     */
    private void backup(ObjectMapper objectMapper) {
        // 指定要序列化的域，field, get 和 set，以及修饰符范围，ANY 是都有包括 private 和 public
        // 一般不建议开启
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        // 指定允许序列化所有非 final 属性，同时序列化是以属性形式添加类型信息
        // Redis 采用 Json 方式序列化时，建议开启以保证类嵌套属性反序列化的正确
        objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);

    }
}
