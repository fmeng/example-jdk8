package comt.icourt.example.jdk8.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 学校信息
 *
 * @author fmeng
 * @since 2018/07/17
 */
@Setter
@Getter
@Builder
public class SchoolEntity {

    /**
     * 学校ID
     */
    private String schoolId;

    /**
     * 学校名称
     */
    private String schoolName;
}
