package comt.icourt.example.jdk8.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 班级信息
 *
 * @author fmeng
 * @since 2018/07/17
 */
@Setter
@Getter
@Builder
public class GradeEntity {

    /**
     * 班级ID
     */
    private String gradeId;

    /**
     * 班级名称
     */
    private String gradeName;

    /**
     * 学校ID
     */
    private String schoolId;
}
