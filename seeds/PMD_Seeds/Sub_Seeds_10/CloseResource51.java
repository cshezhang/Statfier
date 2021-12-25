
import java.util.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

public class CloseResourceStream {
     public static <T> Stream<MatchResult<T>> filterResults(List<T> candidates, Function<T, String> matchExtractor, String query, MatchSelector<T> limiter) {
        if (query.length() < MIN_QUERY_LENGTH) {
            return Stream.empty();
        }

        // violation here
        Stream<MatchResult<T>> base = candidates.stream()
                                                .map(it -> {
                                                    String cand = matchExtractor.apply(it);
                                                    return new MatchResult<>(0, it, cand, query, new TextFlow(makeNormalText(cand)));
                                                });
        return limiter.selectBest(base);
    }
}
        