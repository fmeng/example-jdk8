package comt.icourt.example.jdk8.lambda;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.System.out;

/**
 * lambda表达式基于jdk7以上版本的更新
 *
 * @author fmeng
 * @since 2018/07/17
 */
public class ReplaceJdk7Test {

    private static class UserEntity {

    }

    /**** 1. 过程抽象 ****/

    /**
     * 合并过程的抽象
     */
    private interface IdMerger<T> {

        /**
         * 合并ID
         *
         * @param lIds id的集合可以为空不能为null
         * @param rIds id的集合可以为空不能为null
         * @return 合并结果
         */
        Set<T> merge(Set<String> lIds, Set<String> rIds);
    }


    /**
     * 合并过程的触发器
     */
    private interface MergerLauncher<T> extends IdMerger<T> {
        /**
         * 获得合并过程
         *
         * @return 合并过程
         */
        void setIdMerger(IdMerger<T> idMerger);
    }

    /**** 2.实现合并过程 ****/
    /**
     * 并集
     */
    private static class UnionIdMerger implements IdMerger<UserEntity> {

        @Override
        public Set<UserEntity> merge(Set<String> lIds, Set<String> rIds) {
            Set<String> ids = Sets.intersection(lIds, rIds);
            // TODO 执行相关的业务操作
            return idsToEntities(ids);
        }
    }

    /**
     * 交集
     */
    private static class IntersectionIdMerger implements IdMerger<UserEntity> {

        @Override
        public Set<UserEntity> merge(Set<String> lIds, Set<String> rIds) {
            Set<String> ids = Sets.intersection(lIds, rIds);
            // TODO 执行相关的业务操作
            return idsToEntities(ids);
        }
    }

    /**
     * 简单的合并执行的模版
     */
    private static class MergerLauncherTemplate implements MergerLauncher<UserEntity> {

        private IdMerger<UserEntity> idMerger;

        @Override
        public Set<UserEntity> merge(Set<String> lIds, Set<String> rIds) {
            return idMerger.merge(lIds, rIds);
        }

        @Override
        public void setIdMerger(IdMerger<UserEntity> idMerger) {
            this.idMerger = idMerger;
        }
    }

    /**
     * 面向对象的编程
     * 1. 需要关注范型的抽象
     * 2. 为了抽象过程，构建了多个中间类
     * 3. 需要实现相关的类或继承接口，扩展业务
     */
    @Test
    public void oopMergeTest() {
        Set<String> lIds = Collections.emptySet();
        Set<String> rIds = Collections.emptySet();
        MergerLauncherTemplate launcher = new MergerLauncherTemplate();
        launcher.setIdMerger(new IntersectionIdMerger());
        Set<UserEntity> res = launcher.merge(lIds, rIds);
        out.println(res);
    }

    /**
     * 面向函数的编程
     * 1. 不需要关注范型的抽象
     * 2. 不需要多个中间类
     * 3. 通过不同的函数灵活的控制业务
     */
    @Test
    public void oofMergeTest() {
        Set<String> lIds = Collections.emptySet();
        Set<String> rIds = Collections.emptySet();
        Set<UserEntity> res = Sets.union(lIds, rIds).stream()
                // 做相关业务
//                .sorted()
//                .filter()
//                .parallel()
                .map(ReplaceJdk7Test::idToEntity)
                .collect(Collectors.toSet());
        out.println(res);
    }

    private static Set<UserEntity> idsToEntities(Set<String> ids) {
        Preconditions.checkArgument(Objects.nonNull(ids), "ids不能为null");
        Set<UserEntity> res = Sets.newHashSetWithExpectedSize(ids.size());
        for (String id : ids) {
            res.add(idToEntity(id));
        }
        return res;
    }

    private static UserEntity idToEntity(String id) {
        // TODO
        return null;
    }

}
