package comt.icourt.example.jdk8.design;

import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * Bean属性复制工具，对apache的BeanUtils和PropertyUtils进行二次封装
 * 支持属性复制前条件判断，条件成立复制属性，反之不复制
 *
 * @author fmeng
 * @see org.apache.commons.beanutils.BeanUtils
 * @see org.apache.commons.beanutils.PropertyUtils
 * @since 2018/06/28
 */
public class Copys {

    /****************************** 内部使用的属性 ******************************/

    private static final Consumer<CopyItem> NOT_NULL_COPY = (item) -> copyProperties(item.getDst(), item.getSrc(), initNotNullFilter(), item.isAutoTypeConvert());
    private static final Consumer<CopyItem> NOT_EMPTY_COPY = (item) -> copyProperties(item.getDst(), item.getSrc(), initNotEmptyFilter(), item.isAutoTypeConvert());

    /****************************** 外部使用的工具方法 ******************************/

    /**
     * 属性复制
     * 对象非空，Collection，Map，数组，NotEmpty
     *
     * @param dst             目的对象
     * @param src             被复制对象
     * @param autoTypeConvert true:类型自动转换;false:类型不会自动转换(类型不一致会抛异常)
     */
    public static void copyPropertiesNotEmpty(Object dst, Object src, boolean autoTypeConvert) {
        NOT_NULL_COPY.accept(CopyItem.builder().dst(dst).src(src).autoTypeConvert(autoTypeConvert).build());
    }

    /**
     * 属性复制
     * 复制条件，src和dst属性类型一致，对象非空
     *
     * @param dst             目的对象
     * @param src             被复制对象
     * @param autoTypeConvert true:类型自动转换;false:类型不会自动转换(类型不一致会抛异常)
     */
    public static void copyPropertiesNotNull(Object dst, Object src, boolean autoTypeConvert) {
        NOT_EMPTY_COPY.accept(CopyItem.builder().dst(dst).src(src).autoTypeConvert(autoTypeConvert).build());

    }

    /**
     * @param dst             目的对象
     * @param src             被复制对象
     * @param filter          熟悉过滤器
     * @param autoTypeConvert true:类型自动转换;false:类型不会自动转换(类型不一致会抛异常)
     */
    private static void copyProperties(Object dst, Object src, BiPredicate<String, Object> filter, boolean autoTypeConvert) {
        Preconditions.checkNotNull(dst, "Copys params check, dst not null");
        Preconditions.checkNotNull(src, "Copys params check, src not null");
        if (autoTypeConvert) {
            // 开启类型自动转换
            try {
                ConditionBeanUtilsWrapper.newInstance().addFilter(filter)
                        .copyProperties(dst, src);
            } catch (IllegalAccessException e) {
                // 一般来说，是由于java在反射时调用了private方法所导致的
                throw new RuntimeException(getExceptionReason(dst, src, "访问权限受限"), e);
            } catch (InvocationTargetException e) {
                // 在setter getter方法调用是抛出的异常 可能是业务异常
                throw new RuntimeException(getExceptionReason(dst, src, "setter,getter业务方法抛出异常"), e);
            }

        } else {
            // 类型不一致会抛出异常
            try {
                ConditionPropertyUtilsWrapper.newInstance().addFilter(filter)
                        .copyProperties(dst, src);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(getExceptionReason(dst, src, "访问权限受限"), e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(getExceptionReason(dst, src, "setter,getter业务方法抛出异常"), e);
            } catch (NoSuchMethodException e) {
                // 未找到相对应的setter getter方法
                throw new RuntimeException(getExceptionReason(dst, src, "未找到相对应的setter,getter方法"), e);
            }
        }

    }

    /****************************** 初始化静态变量方法 ******************************/

    /**
     * 初始化notNull过滤器
     */
    private static BiPredicate<String, Object> initNotNullFilter() {
        return (name, value) -> {
            if (value == null) {
                return false;
            }
            return true;
        };
    }

    /**
     * 初始化notEmpty过滤器
     */
    private static BiPredicate<String, Object> initNotEmptyFilter() {
        return (name, value) -> {
            if (value == null) {
                return false;
            }
            Class<?> srcClass = value.getClass();
            if (srcClass.isAssignableFrom(Collection.class)) {
                Collection cast = Collection.class.cast(value);
                if (CollectionUtils.isEmpty(cast)) {
                    return false;
                }
            }
            if (srcClass.isAssignableFrom(Map.class)) {
                Map cast = Map.class.cast(value);
                if (MapUtils.isEmpty(cast)) {
                    return false;
                }
            }
            if (srcClass.isAssignableFrom(String.class)) {
                String cast = String.class.cast(value);
                if (StringUtils.isEmpty(cast)) {
                    return false;
                }
            }
            if (srcClass.isArray()) {
                Object[] cast = Object[].class.cast(value);
                if (ArrayUtils.isEmpty(cast)) {
                    return false;
                }
            }
            return true;
        };
    }

    /**
     * 获得异常的描述信息
     */
    private static String getExceptionReason(Object dst, Object src, String desc) {
        String srcClassName = ClassUtils.getShortClassName(src, "");
        String dstClassName = ClassUtils.getShortClassName(dst, "");
        return new StringBuilder()
                .append("Copys属性复制异常,").append(desc)
                .append("srcClassName:").append(srcClassName)
                .append("dstClassName:").append(dstClassName)
                .toString();
    }

    /****************************** 内部类 ******************************/

    /**
     * BeanUtils Wrapper, 支持属性过滤器
     */
    private static class ConditionBeanUtilsWrapper extends BeanUtilsBean {
        private BiPredicate<String, Object> propertyFilter;

        public ConditionBeanUtilsWrapper() {
            super();
        }

        public static ConditionBeanUtilsWrapper newInstance() {
            return new ConditionBeanUtilsWrapper();
        }

        public ConditionBeanUtilsWrapper addFilter(BiPredicate<String, Object> filter) {
            propertyFilter = filter;
            return this;
        }

        @Override
        public void copyProperty(Object dst, String name, Object value)
                throws IllegalAccessException, InvocationTargetException {
            if (propertyFilter == null) {
                super.copyProperty(dst, name, value);
                return;
            }
            boolean pass = propertyFilter.test(name, value);
            if (pass) {
                super.copyProperty(dst, name, value);
            }
        }

    }

    /**
     * PropertyUtils Wrapper, 支持属性过滤器
     */
    private static class ConditionPropertyUtilsWrapper extends PropertyUtilsBean {
        private BiPredicate<String, Object> propertyFilter;

        public ConditionPropertyUtilsWrapper() {
            super();
        }

        public static ConditionPropertyUtilsWrapper newInstance() {
            return new ConditionPropertyUtilsWrapper();
        }

        public ConditionPropertyUtilsWrapper addFilter(BiPredicate<String, Object> filter) {
            propertyFilter = filter;
            return this;
        }

        @Override
        public void setSimpleProperty(Object dst, String name, Object value)
                throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            if (propertyFilter == null) {
                super.setSimpleProperty(dst, name, value);
                return;
            }
            boolean pass = propertyFilter.test(name, value);
            if (pass) {
                super.setSimpleProperty(dst, name, value);
            }
        }

    }

    @Getter
    @Builder
    private static class CopyItem {
        private Object src;
        private Object dst;
        private boolean autoTypeConvert;
    }
}
