
import java.util.ArrayList;
import java.util.List;

public record Reproducer(List<String> smsIds) {

  public Reproducer createMe(final String smsId) {
    final List<String> newList = new ArrayList<>(smsIds);
    newList.add(smsId);
    return new Reproducer(newList);
  }
}
        