package comt.icourt.example.jdk8.stream;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import comt.icourt.example.jdk8.BaseDataStore;
import comt.icourt.example.jdk8.model.FlatStudentEntity;
import comt.icourt.example.jdk8.model.GradeEntity;
import comt.icourt.example.jdk8.model.StudentEntity;
import comt.icourt.example.jdk8.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.System.out;

/**
 * 扁平的学生信息 -> 各种需要的结构
 *
 * @author fmeng
 * @since 2018/07/17
 */
public class FlatStudentToEntityTest {

    /**
     * 扁平的学生信息
     */
    private List<FlatStudentEntity> flatStudentEntities = ImmutableList.copyOf(BaseDataStore.FLAT_STUDENT_ENTITY_LIST);

    /****************************** 扁平数据->单维度结构 ******************************/

    /**
     * 扁平的学生信息 -> 班级Entity
     */
    @Test
    public void getGradeEntity() {
        List<GradeEntity> gradeEntities = flatStudentEntities.stream()
                .filter(Objects::nonNull)
                .map(e -> GradeEntity.builder()
                        .gradeId(e.getGradeId())
                        .gradeName(e.getGradeName())
                        .schoolId(e.getSchoolId())
                        .build()
                )
                .distinct()
                .collect(Collectors.toList());
        out.println(JsonUtil.encodeQuietly(gradeEntities));
    }

    /**
     * 扁平的学生信息 -> 学生信息（需要按年龄排序）
     */
    @Test
    public void getStudentEntitySortByAge() {
        List<StudentEntity> gradeEntities = flatStudentEntities.stream()
                .filter(Objects::nonNull)
                .map(e -> StudentEntity.builder()
                        .gradeId(e.getGradeId())
                        .studentId(e.getStudentId())
                        .studentName(e.getStudentName())
                        .age(e.getAge())
                        .build()
                )
                // 按年龄排序
                .sorted(Comparator.comparingInt(StudentEntity::getAge))
                .collect(Collectors.toList());
        out.println(JsonUtil.encodeQuietly(gradeEntities));
    }

    /****************************** 扁平数据->多维度聚合 ******************************/

    /**
     * 扁平的学生信息 -> <班级ID, 学生信息>
     */
    @Test
    public void groupByGrade() {
        // <班级ID, 学生信息>
        Map<String, List<FlatStudentEntity>> students = flatStudentEntities.stream()
                .collect(Collectors.groupingBy(FlatStudentEntity::getGradeId, Collectors.toList()));
        out.println(JsonUtil.encodeQuietly(students));
    }

    /**
     * 扁平的学生信息 -> <班级ID, 最大年龄的学生信息(班级中可能没有学生)>
     */
    @Test
    public void groupByGradeCountMaxAge() {
        // <班级ID, 最大年龄的学生信息(班级中可能没有学生)>
        Map<String, Optional<FlatStudentEntity>> students = flatStudentEntities.stream()
                .collect(Collectors.groupingBy(FlatStudentEntity::getGradeId
                        , Collectors.maxBy(Comparator.comparingInt(FlatStudentEntity::getAge))));
        out.println(JsonUtil.encodeQuietly(students));
    }

    /**
     * 扁平的学生信息 -> <学校ID, <班级ID, 基础学生信息>>
     */
    @Test
    public void groupBySchoolAndGrade() {
        // <学校ID, <班级ID, 基础学生信息>>
        Map<String, Map<String, List<StudentEntity>>> students = flatStudentEntities.stream()
                .collect(Collectors.groupingBy(FlatStudentEntity::getSchoolId
                        , Collectors.groupingBy(FlatStudentEntity::getGradeId
                                , Collectors.mapping(e ->
                                                StudentEntity.builder()
                                                        .gradeId(e.getGradeId())
                                                        .studentId(e.getStudentId())
                                                        .studentName(e.getStudentName())
                                                        .age(e.getAge())
                                                        .build()
                                        , Collectors.toList()))));
        out.println(JsonUtil.encodeQuietly(students));
    }

    /**
     * 查找某个学校内多所有学生，并按班级封装成multiMap，方便使用
     * 扁平的学生信息 -> <班级ID, 基础学生信息>
     */
    @Test
    public void toMultiMap() {
        // 使用Guava的Multimap方便的模拟 Map<班级ID, List<学生信息>>
        Multimap<String, StudentEntity> gradeStudentMap = flatStudentEntities.stream()
                .filter(e -> StringUtils.equals(e.getSchoolId(), "school_id_01"))
                .map(e -> StudentEntity.builder()
                        .gradeId(e.getGradeId())
                        .studentId(e.getStudentId())
                        .studentName(e.getStudentName())
                        .age(e.getAge())
                        .build())
                .collect(Multimaps.toMultimap(StudentEntity::getGradeId, o -> o, ArrayListMultimap::create));
        out.println(gradeStudentMap);
        Collection<StudentEntity> studentEntities = gradeStudentMap.get("school_id_01");
        out.println(studentEntities);
    }
}
