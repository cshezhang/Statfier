
            public class Sample {

                private static final LocalTime ONE_PAST_MIDNIGHT = MIDNIGHT.plusMinutes(1);

                public ZonedDateTime sample(final String date, final ZonedDateTime paymentTimeStamp) {
                    final ZonedDateTime startOfDayOnInceptionDate = zonedDateTimeAtOnePastMidnight(date, paymentTimeStamp.getZone());
                    final ZonedDateTime startOfDayOnPaymentDate = zonedDateTimeAtOnePastMidnight(paymentTimeStamp.toLocalDate(), paymentTimeStamp.getZone());
                    if (startOfDayOnInceptionDate.isAfter(paymentTimeStamp)) {
                        return startOfDayOnInceptionDate;
                    } else if (paymentTimeStamp.isAfter(startOfDayOnPaymentDate)) {
                        return paymentTimeStamp;
                    } else {
                        return startOfDayOnPaymentDate;
                    }
                }

                public ZonedDateTime zonedDateTimeAtOnePastMidnight(final String date, final ZoneId zoneId) {
                    return zonedDateTimeAtOnePastMidnight(parseStringWithIsoDateFormat(date), zoneId);
                }

                private ZonedDateTime zonedDateTimeAtOnePastMidnight(final LocalDate localDate, final ZoneId zoneId) {
                    return ZonedDateTime.of(localDate, ONE_PAST_MIDNIGHT, zoneId);
                }
            }

        