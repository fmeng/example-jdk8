package comt.icourt.example.jdk8.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 学生实体
 *
 * @author fmeng
 * @since 2018/07/17
 */
@Setter
@Getter
@Builder
public class StudentEntity {

    /**
     * 学生ID
     */
    private String studentId;

    /**
     * 学生名字
     */
    private String studentName;

    /**
     * 班级ID
     */
    private String gradeId;

    /**
     * 年龄
     */
    private Integer age;
}
