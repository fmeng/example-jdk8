package comt.icourt.example.jdk8.guava.util;

import com.google.common.reflect.TypeToken;
import org.junit.Test;

import java.util.List;

/**
 * 1. 抽象Java类型,
 * 2. Java类型的Holder,
 *
 * @author fmeng
 * @since 2018/07/17
 */
public class TypeTokenTest {

    /**
     *
     */
    @Test
    public void typeTokenTest() {

    }

    private static class UserEntity {

    }

    private static abstract class AbstractSearchTemplate<T, S, U> {

        protected abstract List<T> searchByIds(T... ids);
    }

    private static class CustomSearchTemplate extends AbstractSearchTemplate<UserEntity> {
        @Override
        protected List<UserEntity> searchByIds(UserEntity... ids) {
            // TODO

            return null;
        }
    }
}
