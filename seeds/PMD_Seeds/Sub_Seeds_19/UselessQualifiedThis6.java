
public class ApiExceptionCtrlAdvice {
    public final List<ApiError> handleApiAggregateException(final ApiAggregateException e) {
        return e.getCauses().stream().map(
            ApiExceptionCtrlAdvice::toApiError
        ).collect(Collectors.toList());
    }
    private static ApiError toApiError(final Throwable e) {
        return new ApiError()
                .withException(e.getClass().getName())
                .withCause(Optional.ofNullable(e.getCause()).map(Throwable::getClass).map(Class::getName).orElse(EMPTY))
                .withMessage(Optional.ofNullable(e.getMessage()).orElse(EMPTY));
    }
}
        