package comt.icourt.example.jdk8.guava.collection;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import comt.icourt.example.jdk8.model.StudentEntity;
import org.junit.Test;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 创建集合的工具类测试
 *
 * @author fmeng
 * @see com.google.common.collect
 * @since 2018/07/17
 */
public class CollectionTest {

    private enum UserTypeEnum {

    }

    /**
     * guava提供了丰富的集合工具类
     * 该示例不能详尽的描述所有工具类的使用，请仔细阅读相关api
     *
     * @see com.google.common.collect
     */
    @Test
    public void createCollectionTest() {

        // 构建集合是指定初始化大小
        Set<StudentEntity> studentEntities = Sets.newHashSetWithExpectedSize(10);
        // 构建集合是指定初始化大小
        List<StudentEntity> studentEntities1 = Lists.newArrayListWithExpectedSize(10);
        // 有针对指定固定类型的集合，例如枚举、Class等
        EnumMap<UserTypeEnum, Object> userTypeEnumObjectEnumMap = Maps.newEnumMap(UserTypeEnum.class);
        // List 构建一个map
        Map<String, StudentEntity> immutableMap = Maps.uniqueIndex(studentEntities, StudentEntity::getStudentId);

        // copy on writ e模式
        Set<Object> objects = Sets.newCopyOnWriteArraySet();
        // 并集
        Set<Object> union = Sets.union(Collections.emptySet(), Collections.emptySet());
        // 交集
        Set<Object> intersection = Sets.intersection(Collections.emptySet(), Collections.emptySet());
    }
}
