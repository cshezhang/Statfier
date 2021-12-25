
import java.util.Locale;
import org.springframework.context.MessageSource;

public class StringBuilderWithMessageRetrieval {
    private final MessageSource messageSource;

    public StringBuilderWithMessageRetrieval(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void run(String[] strings) {
        StringBuilder builder = new StringBuilder();
        boolean notFirst = false;
        for (String string : strings) {
            if (notFirst) {
                builder.append('\n');
            }

            builder.append(messageSource.getMessage("some.long.label." + string, null, Locale.ENGLISH));
            notFirst = true;
        }
    }

}
        