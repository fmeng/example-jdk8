package comt.icourt.example.jdk8.optional;

import com.google.common.base.Preconditions;
import comt.icourt.example.jdk8.BaseDataStore;
import comt.icourt.example.jdk8.model.StudentEntity;
import comt.icourt.example.jdk8.model.StudentVO;
import comt.icourt.example.jdk8.other.BizException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Objects;
import java.util.Optional;

import static java.lang.System.out;

/**
 * optional 测试
 *
 * @author fmeng
 * @since 2018/07/17
 */
public class OptionalTest {

    /**
     * 复制过程模版
     *
     * @param srcStudentEntity 从entity复制
     * @param dstStudentVO     需要拷贝到到VO
     */
    private void doCopyTemplate(StudentVO dstStudentVO, StudentEntity srcStudentEntity) {
        Preconditions.checkArgument(Objects.nonNull(dstStudentVO), "dstStudentVO不能为空");
        Preconditions.checkArgument(Objects.nonNull(srcStudentEntity), "srcStudentEntity不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(srcStudentEntity.getGradeId()), "gradeId不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(srcStudentEntity.getStudentId()), "studentId不能为空");
        //TODO copy
    }

    /**
     * 当src中对数据数据非空时，copy属性数据到dst(JDK7)
     */
    @Test
    public void srcNotNullCopyGeneral() {
        StudentEntity srcStudentEntity = BaseDataStore.STUDENT_ENTITY;
        StudentVO.StudentVOBuilder dstStudentBuilder = StudentVO.builder();
        /****************************** 关键数据(例如ID,如果为空直接抛异常) ******************************/
        if (srcStudentEntity.getStudentId() == null) {
            throw new BizException();
        } else {
            dstStudentBuilder.studentId(srcStudentEntity.getStudentId());
        }
        if (srcStudentEntity.getGradeId() == null) {
            throw new BizException();
        } else {
            dstStudentBuilder.gradeId(srcStudentEntity.getGradeId());
        }
        /****************************** 非关键数据(空数据,不copy) ******************************/
        if (Objects.nonNull(srcStudentEntity.getStudentName())) {
            dstStudentBuilder.studentName(srcStudentEntity.getStudentName());
        }
        if (Objects.nonNull(srcStudentEntity.getAge())) {
            dstStudentBuilder.age(srcStudentEntity.getAge());
        }
        out.println(dstStudentBuilder.build());
    }

    /**
     * 当src中对数据数据非空时，copy属性数据到dst(JDK8)
     */
    @Test
    public void srcNotNullCopyOptional() {
        StudentEntity srcStudentEntity = BaseDataStore.STUDENT_ENTITY;
        StudentVO.StudentVOBuilder dstStudentBuilder = StudentVO.builder();
        /****************************** 关键数据(例如ID,如果为空直接抛异常) ******************************/
        Optional.of(srcStudentEntity.getStudentId()).ifPresent(dstStudentBuilder::studentId);
        Optional.of(srcStudentEntity.getGradeId()).ifPresent(dstStudentBuilder::gradeId);
        /****************************** 非关键数据(空数据,不copy) ******************************/
        Optional.ofNullable(srcStudentEntity.getStudentName()).ifPresent(dstStudentBuilder::studentName);
        Optional.ofNullable(srcStudentEntity.getAge()).ifPresent(dstStudentBuilder::age);
        out.println(dstStudentBuilder.build());
    }

}
