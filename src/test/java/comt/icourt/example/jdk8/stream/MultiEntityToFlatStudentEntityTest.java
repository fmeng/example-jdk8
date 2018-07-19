package comt.icourt.example.jdk8.stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import comt.icourt.example.jdk8.BaseDataStore;
import comt.icourt.example.jdk8.model.FlatStudentEntity;
import comt.icourt.example.jdk8.model.GradeEntity;
import comt.icourt.example.jdk8.model.SchoolEntity;
import comt.icourt.example.jdk8.model.StudentEntity;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.System.out;

/**
 * 多个维度的数据合并成扁平的数据
 *
 * @author fmeng
 * @since 2018/07/17
 */
public class MultiEntityToFlatStudentEntityTest {

    /**
     * 学校信息
     */
    private List<SchoolEntity> schoolEntities = ImmutableList.copyOf(BaseDataStore.SCHOOL_ENTITY_LIST);

    /**
     * 班级信息
     */
    private List<GradeEntity> gradeEntities = ImmutableList.copyOf(BaseDataStore.GRADE_ENTITY_LIST);

    /**
     * 学生信息
     */
    private List<StudentEntity> studentEntities = ImmutableList.copyOf(BaseDataStore.STUDENT_ENTITY_LIST);


    /**
     * 多个维度的信息 -> 扁平的学生信息（只使用Stream实现）
     */

    @Test
    public void toFlatStudentEntitiesStream() {
        // 数据准备
        // <学校ID, 学校信息>
        Map<String, SchoolEntity> schoolEntityMap = schoolEntities.stream()
                .collect(Collectors.toMap(SchoolEntity::getSchoolId, o -> o));
        out.println(schoolEntityMap);
        // <班级ID, 班级信息>
        Map<String, GradeEntity> gradeEntityMap = gradeEntities.stream()
                .collect(Collectors.toMap(GradeEntity::getGradeId, e -> e));
        out.println(gradeEntityMap);
        List<FlatStudentEntity> students = studentEntities.stream()
                // 填充原始学生信息
                .map(e -> FlatStudentEntity.builder()
                        .gradeId(e.getGradeId())
                        .studentId(e.getStudentId())
                        .studentName(e.getStudentName())
                        .age(e.getAge())
                        .build()
                )
                // 填充班级和学校信息
                .peek(e -> {
                    String gradeId = e.getGradeId();
                    String gradeName = gradeEntityMap.get(gradeId).getGradeName();
                    String schoolId = gradeEntityMap.get(gradeId).getSchoolId();
                    String schoolName = schoolEntityMap.get(schoolId).getSchoolName();
                    e.setSchoolId(schoolId);
                    e.setGradeId(gradeId);
                    e.setGradeName(gradeName);
                    e.setSchoolName(schoolName);
                })
                .collect(Collectors.toList());
        out.println(students);
    }

    /**
     * 多个维度的信息 -> 扁平的学生信息（只使用Guava实现）
     */

    @Test
    public void toFlatStudentEntitiesGuava() {
        // 数据准备
        // <学校ID, 学校信息>
        Map<String, SchoolEntity> schoolEntityMap = Maps.uniqueIndex(schoolEntities, SchoolEntity::getSchoolId);
        out.println(schoolEntityMap);
        // <班级ID, 班级信息>
        Map<String, GradeEntity> gradeEntityMap = Maps.uniqueIndex(gradeEntities, GradeEntity::getGradeId);
        out.println(gradeEntityMap);
        List<FlatStudentEntity> students = studentEntities.stream()
                // 填充原始学生信息
                .map(e -> FlatStudentEntity.builder()
                        .gradeId(e.getGradeId())
                        .studentId(e.getStudentId())
                        .age(e.getAge())
                        .studentName(e.getStudentName())
                        .build()
                )
                // 填充班级和学校信息
                .peek(e -> {
                    String gradeId = e.getGradeId();
                    String gradeName = gradeEntityMap.get(gradeId).getGradeName();
                    String schoolId = gradeEntityMap.get(gradeId).getSchoolId();
                    String schoolName = schoolEntityMap.get(schoolId).getSchoolName();
                    e.setGradeName(gradeName);
                    e.setGradeId(gradeId);
                    e.setSchoolId(schoolId);
                    e.setSchoolName(schoolName);
                })
                .collect(Collectors.toList());
        out.println(students);
    }
}
