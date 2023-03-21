package school.hei.haapi.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;
import school.hei.haapi.SentryConf;
import school.hei.haapi.endpoint.rest.api.TeachingApi;
import school.hei.haapi.endpoint.rest.client.ApiClient;
import school.hei.haapi.endpoint.rest.client.ApiException;
import school.hei.haapi.endpoint.rest.model.Course;
import school.hei.haapi.endpoint.rest.model.EnableStatus;
import school.hei.haapi.endpoint.rest.model.Group;
import school.hei.haapi.endpoint.rest.model.Teacher;
import school.hei.haapi.endpoint.rest.security.cognito.CognitoComponent;
import school.hei.haapi.integration.conf.AbstractContextInitializer;
import school.hei.haapi.integration.conf.TestUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static school.hei.haapi.integration.conf.TestUtils.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
@ContextConfiguration(initializers = CoursesIT.ContextInitializer.class)
@AutoConfigureMockMvc
public class CoursesIT {

  @MockBean
  private SentryConf sentryConf;

  @MockBean
  private CognitoComponent cognitoComponentMock;

  private static ApiClient anApiClient(String token) {
    return TestUtils.anApiClient(token, ContextInitializer.SERVER_PORT);
  }

  public static Course course1(){
    Course course = new Course();
    course.setId("courses1_id");
    course.setCode("PROG1");
    course.setName("Algorithmics");
    course.setCredits(2);
    course.setTotalHours(6);
    course.setMainTeacher(teacher1());
    return course;
  }

  public static Course course2(){
    Course course = new Course();
    course.setId("courses2_id");
    course.setCode("PROG2");
    course.setName("Function");
    course.setCredits(2);
    course.setTotalHours(6);
    course.setMainTeacher(teacher2());
    return course;
  }

  public static Course course3(){
    Course course = new Course();
    course.setId("courses3_id");
    course.setCode("PROG3");
    course.setName("Test");
    course.setCredits(2);
    course.setTotalHours(6);
    course.setMainTeacher(teacher3());
    return course;
  }

  public static Teacher teacher1() {
    Teacher teacher = new Teacher();
    teacher.setId("teacher1_id");
    teacher.setFirstName("One");
    teacher.setLastName("Teacher");
    teacher.setEmail("test+teacher1@hei.school");
    teacher.setRef("TCR21001");
    teacher.setPhone("0322411125");
    teacher.setStatus(EnableStatus.ENABLED);
    teacher.setSex(Teacher.SexEnum.F);
    teacher.setBirthDate(LocalDate.parse("1990-01-01"));
    teacher.setEntranceDatetime(Instant.parse("2021-10-08T08:27:24.00Z"));
    teacher.setAddress("Adr 3");
    return teacher;
  }

  public static Teacher teacher2() {
    Teacher teacher = new Teacher();
    teacher.setId("teacher2_id");
    teacher.setFirstName("Two");
    teacher.setLastName("Teacher");
    teacher.setEmail("test+teacher2@hei.school");
    teacher.setRef("TCR21002");
    teacher.setPhone("0322411126");
    teacher.setStatus(EnableStatus.ENABLED);
    teacher.setSex(Teacher.SexEnum.M);
    teacher.setBirthDate(LocalDate.parse("1990-01-02"));
    teacher.setEntranceDatetime(Instant.parse("2021-10-09T08:28:24Z"));
    teacher.setAddress("Adr 4");
    return teacher;
  }

  public static Teacher teacher3() {
    Teacher teacher = new Teacher();
    teacher.setId("teacher3_id");
    teacher.setFirstName("Three");
    teacher.setLastName("Teach");
    teacher.setEmail("test+teacher3@hei.school");
    teacher.setRef("TCR21003");
    teacher.setPhone("0322411126");
    teacher.setStatus(EnableStatus.ENABLED);
    teacher.setSex(Teacher.SexEnum.M);
    teacher.setBirthDate(LocalDate.parse("1990-01-02"));
    teacher.setEntranceDatetime(Instant.parse("2021-10-09T08:28:24Z"));
    teacher.setAddress("Adr 4");
    return teacher;
  }

  @BeforeEach
  public void setUp() {
    setUpCognito(cognitoComponentMock);
  }

  @Test
  void badtoken_read_ko() {
    ApiClient anonymousClient = anApiClient(BAD_TOKEN);

    TeachingApi api = new TeachingApi(anonymousClient);
    assertThrowsForbiddenException(api::getGroups);
  }

  @Test
  void badtoken_write_ko() {
    ApiClient anonymousClient = anApiClient(BAD_TOKEN);

    TeachingApi api = new TeachingApi(anonymousClient);
    assertThrowsForbiddenException(() -> api.createOrUpdateGroups(List.of()));
  }

  @Test
  void student_read_ok() throws ApiException {
    ApiClient student1Client = anApiClient(STUDENT1_TOKEN);

    TeachingApi api = new TeachingApi(student1Client);
    List<Course> actualCourse = api.getCourses(1,20);

    assertTrue(actualCourse.contains(course1()));
    assertTrue(actualCourse.contains(course2()));
    assertTrue(actualCourse.contains(course3()));
  }

  @Test
  void teacher_read_ok() throws ApiException {
    ApiClient teacher1Client = anApiClient(TEACHER1_TOKEN);

    TeachingApi api = new TeachingApi(teacher1Client);
    List<Course> actualCourse = api.getCourses(1,20);

    assertEquals(3, actualCourse.size());
  }

  @Test
  void manager_read_ok() throws ApiException {
    ApiClient manager1Client = anApiClient(MANAGER1_TOKEN);

    TeachingApi api = new TeachingApi(manager1Client);
    List<Course> actualCourse = api.getCourses(1,20);

    assertTrue(actualCourse.contains(course1()));
    assertTrue(actualCourse.contains(course2()));
    assertTrue(actualCourse.contains(course3()));
  }

  static class ContextInitializer extends AbstractContextInitializer {
    public static final int SERVER_PORT = anAvailableRandomPort();

    @Override
    public int getServerPort() {
      return SERVER_PORT;
    }
  }
}
