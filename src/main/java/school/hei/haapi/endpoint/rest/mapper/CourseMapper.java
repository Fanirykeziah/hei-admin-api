package school.hei.haapi.endpoint.rest.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.haapi.model.Course;

@Component
@AllArgsConstructor
public class CourseMapper {

    private final UserMapper userMapper;

    public school.hei.haapi.endpoint.rest.model.Course toRest(Course course){
        var restCourse = new school.hei.haapi.endpoint.rest.model.Course();
        restCourse.setId(course.getId());
        restCourse.setCode(course.getCode());
        restCourse.setName(course.getName());
        restCourse.setCredits(course.getCredits());
        restCourse.setTotalHours(course.getTotal_hours());
        restCourse.setMainTeacher(userMapper.toRestTeacher(course.getMain_teacher()));
        return restCourse;
    }

    public Course toDomain(school.hei.haapi.endpoint.rest.model.Course course) {
        return Course.builder()
                .id(course.getId())
                .code(course.getCode())
                .name(course.getName())
                .credits(course.getCredits())
                .total_hours(course.getTotalHours())
                .main_teacher(userMapper.toDomain(
                        course.getMainTeacher()
                ))
                .build();
    }
}
