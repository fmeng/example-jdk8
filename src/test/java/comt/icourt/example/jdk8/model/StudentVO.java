package comt.icourt.example.jdk8.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 学生VO
 *
 * @author fmeng
 * @since 2018/07/17
 */
@Setter
@Getter
@Builder
public class StudentVO {

    /****************************** 学生信息 ******************************/

    /**
     * 学生ID
     */
    private String studentId;

    /**
     * 学生名字
     */
    private String studentName;

    /**
     * 年龄
     */
    private Integer age;

    /****************************** 班级信息 ******************************/

    /**
     * 班级ID
     */
    private String gradeId;

    /**
     * 班级名称
     */
    private String gradeName;


    /****************************** 学校信息 ******************************/

    /**
     * 学校ID
     */
    private String schoolId;

    /**
     * 学校名称
     */
    private String schoolName;
}
