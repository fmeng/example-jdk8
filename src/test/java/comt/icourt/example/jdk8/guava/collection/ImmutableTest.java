package comt.icourt.example.jdk8.guava.collection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 不可变对象
 * 1. 对内，不可变集合，可以维护集合状态的一致性
 * 2. 对外，在多线程环境下，使用Immutable对象可以快速暴露并发问题
 * 3. 性能，copy on write 的模式解决添加和删除元素，增加了开销、但合理降低了问题的复杂度
 *
 * @author fmeng
 * @since 2018/07/17
 */
public class ImmutableTest {

    /**
     * 不可变集合测试
     */
    @Test
    public void immutableTest() {
        /****************************** Map ******************************/
        Map<String, String> keyValues = ImmutableMap.of("userName", "fmeng"
                , "password", "pwd"
                , "address", "beijing");
        // 把可变集合复制为不可变集合
        Map<String, String> immutableMap = ImmutableMap.copyOf(keyValues);

        /****************************** List ******************************/
        List<String> nameList = ImmutableList.of("fmeng1", "fmeng2", "fmeng3");
        ImmutableList<String> immutableList = ImmutableList.copyOf(nameList);
        ImmutableList<String> immutableList1 = nameList.stream().collect(ImmutableList.toImmutableList());

        /****************************** Set ******************************/
        Set<String> nameSet = ImmutableSet.of("fmeng1", "fmeng2", "fmeng3");
        ImmutableSet<String> immutableSet = ImmutableSet.copyOf(nameSet);
        ImmutableSet<String> immutableSet1 = nameSet.stream().collect(ImmutableSet.toImmutableSet());

    }


}
