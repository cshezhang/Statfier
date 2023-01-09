
// https://github.com/FenixEdu/fenixedu-learning/blob/master/src/main/java/org/fenixedu/learning/domain/executionCourse/components/InitialPageComponent.java#L50
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class InitialPageComponent extends BaseExecutionCourseComponent {
    public void handle(Page page, TemplateContext componentContext, TemplateContext globalContext) {
        ExecutionCourse executionCourse = ((ExecutionCourseSite) page.getSite()).getExecutionCourse();
        globalContext.put(
                "professorships",
                executionCourse
                        .getProfessorshipsSet()
                        .stream()
                        .sorted(Comparator.comparing(Professorship::isResponsibleFor).reversed()
                                .thenComparing(Professorship.COMPARATOR_BY_PERSON_NAME)).collect(Collectors.toList()));
    }
}
        