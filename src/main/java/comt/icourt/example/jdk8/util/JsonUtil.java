package comt.icourt.example.jdk8.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

/**
 * Json 工具类
 *
 * @author fmeng
 * @since 2018/01/25
 */
public final class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /****************************** 工具方法 ******************************/

    /**
     * 序列化为JSON
     *
     * @param obj
     * @return
     */
    public static String encode(Object obj) throws IOException {
        if (obj == null) {
            return null;
        }
        return OBJECT_MAPPER.writeValueAsString(obj);
    }

    /**
     * 序列化为JSON, 不抛出异常，用于打印日志等不关心异常的场景
     *
     * @param obj
     * @return
     */
    public static String encodeQuietly(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return encode(obj);
        } catch (IOException e) {
            logger.error("序列化失败, obj:{}, reason:{}", obj, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 使用Class参数化类型
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T decode(String json, Class<T> clazz) throws IOException {
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    /**
     * 解析成JsonNode
     *
     * @param json
     * @return
     * @throws IOException
     */
    public static JsonNode decode(String json) throws IOException {
        return OBJECT_MAPPER.readTree(json);
    }

    /**
     * 使用Class参数化类型
     *
     * @param jsonNode
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T decode(JsonNode jsonNode, Class<T> clazz) throws IOException {
        return OBJECT_MAPPER.readValue(OBJECT_MAPPER.treeAsTokens(jsonNode), clazz);
    }

    /**
     * ParameterizedType 参数化类型
     *
     * @param json
     * @param parameterizedType
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T decode(String json, ParameterizedType parameterizedType) throws IOException {
        return (T) OBJECT_MAPPER.readValue(json, convertToJavaType(parameterizedType));
    }

    /**
     * TypeToken参数化类型
     *
     * @param json
     * @param typeToken
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T decode(String json, TypeToken<T> typeToken) throws IOException {
        return (T) OBJECT_MAPPER.readValue(json, convertToJavaType(typeToken));
    }

    /**
     * TypeToken参数化类型
     *
     * @param jsonNode
     * @param typeToken
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T decode(JsonNode jsonNode, TypeToken<T> typeToken) throws IOException {
        return (T) OBJECT_MAPPER.readValue(OBJECT_MAPPER.treeAsTokens(jsonNode), convertToJavaType(typeToken));
    }

    /**
     * 获得OBJECT_MAPPER 对象
     * 不要修改OBJECT_MAPPER的配置，如果要定制请使用 ObjectMapper.copy();
     *
     * @return
     */
    public static ObjectMapper getObjectMapper() {

        return OBJECT_MAPPER;
    }

    /****************************** 内部方法 ******************************/

    private static <T> JavaType convertToJavaType(TypeToken<T> typeToken) {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        return typeFactory.constructType(typeToken.getType());
    }

    private static JavaType convertToJavaType(ParameterizedType parameterizedType) {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        return typeFactory.constructType(parameterizedType);
    }

    /****************************** 初始化过程 ******************************/
    static {
        // 接受JSON中存在注释
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        // 接受JSON中不使用双引号包围Key
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 接受JSON中用单引号代替双引号
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 接受JSON中存在控制字符在非字符串中的情况
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        // 反序列化中忽略未知JSON字段
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 不序列化Map中值为null的数据
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        // 不序列化为null的属性
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 序列化空属性的bean不报错
        OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
}
