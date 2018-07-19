package comt.icourt.example.jdk8;

import com.google.common.collect.ImmutableList;
import comt.icourt.example.jdk8.model.FlatStudentEntity;
import comt.icourt.example.jdk8.model.GradeEntity;
import comt.icourt.example.jdk8.model.SchoolEntity;
import comt.icourt.example.jdk8.model.StudentEntity;

import java.util.List;

public class BaseDataStore {

    /**
     * 学生信息
     */
    public static final StudentEntity STUDENT_ENTITY = StudentEntity.builder()
            .gradeId("grade_id_01")
                    .studentId("student_id_01")
                    .studentName("student_name_01")
                    .age(1)
                    .build();
    /**
     * 学生信息
     */
    public static final List<StudentEntity> STUDENT_ENTITY_LIST = ImmutableList.of(
            StudentEntity.builder()
                    .gradeId("grade_id_01")
                    .studentId("student_id_01")
                    .studentName("student_name_01")
                    .age(1)
                    .build()
            , StudentEntity.builder()
                    .gradeId("grade_id_02")
                    .studentId("student_id_02")
                    .studentName("student_name_02")
                    .age(2)
                    .build()
            , StudentEntity.builder()
                    .gradeId("grade_id_03")
                    .studentId("student_id_03")
                    .studentName("student_name_03")
                    .age(3)
                    .build()
    );

    /**
     * 班级信息
     */
    public static final List<GradeEntity> GRADE_ENTITY_LIST = ImmutableList.of(
            GradeEntity.builder()
                    .gradeId("grade_id_01")
                    .gradeName("grade_name_01`")
                    .schoolId("school_id_01")
                    .build()
            , GradeEntity.builder()
                    .gradeId("grade_id_02")
                    .gradeName("grade_name_02")
                    .schoolId("school_id_02")
                    .build()
            , GradeEntity.builder()
                    .gradeId("grade_id_03")
                    .gradeName("grade_name_03")
                    .schoolId("school_id_03")
                    .build()
    );

    /**
     * 学校信息
     */
    public static final List<SchoolEntity> SCHOOL_ENTITY_LIST = ImmutableList.of(
            SchoolEntity.builder()
                    .schoolId("school_id_01")
                    .schoolName("school_name_01")
                    .build()
            , SchoolEntity.builder()
                    .schoolId("school_id_02")
                    .schoolName("school_name_02")
                    .build()
            , SchoolEntity.builder()
                    .schoolId("school_id_03")
                    .schoolName("school_name_03")
                    .build()
    );

    /**
     * 扁平的学生信息
     */
    public static final List<FlatStudentEntity> FLAT_STUDENT_ENTITY_LIST = ImmutableList.of(
            FlatStudentEntity.builder()
                    .studentId("student_id_01")
                    .studentName("student_name_01")
                    .age(1)
                    .gradeId("grade_id_01")
                    .gradeName("grade_name_01")
                    .schoolId("school_id_01")
                    .schoolName("school_name_01")
                    .build()
            , FlatStudentEntity.builder()
                    .studentId("student_id_02")
                    .studentName("student_name_02")
                    .age(2)
                    .gradeId("grade_id_02")
                    .gradeName("grade_name_02")
                    .schoolId("school_id_02")
                    .schoolName("school_name_02")
                    .build()
            , FlatStudentEntity.builder()
                    .studentId("student_id_03")
                    .studentName("student_name_03")
                    .age(3)
                    .gradeId("grade_id_03")
                    .gradeName("grade_name_03")
                    .schoolId("school_id_03")
                    .schoolName("school_name_03")
                    .build()
    );

}
