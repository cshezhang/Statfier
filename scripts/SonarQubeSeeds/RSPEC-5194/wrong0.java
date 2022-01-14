
void day_of_week(DoW day) {
    int numLetters;
    switch (day) {  // Noncompliant
      case MONDAY:
      case FRIDAY:
      case SUNDAY:
        numLetters = 6;
        break;
      case TUESDAY:
        numLetters = 7;
        break;
      case THURSDAY:
      case SATURDAY:
        numLetters = 8;
        break;
      case WEDNESDAY:
        numLetters = 9;
        break;
      default:
        throw new IllegalStateException("Wat: " + day);
    }
}

int return_switch(int x) {
    switch (x) { // Noncompliant
      case 1:
        return 1;
      case 2:
        return 2;
      default:
        throw new IllegalStateException();
    }
}
