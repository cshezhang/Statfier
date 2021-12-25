
import java.sql.ResultSet;
public class Foo {
    public void bar() {
        ResultSet results = null;
        String answer;
        List<String> stringList =  new ArrayList<String>();

        //check the result set.  Yes, this moves the cursor to the first position
        if (results.first()) {
            //do a little logic
            do {
                //this is handeling paging
                if (results.getInt("RowNum") >= firstEntry && results.getInt("RowNum") <= lastEntry) {
                    answer  = results.getString("answer");
                    stringList.add(answer);
                } else {
                    results.last();
                }
            } while(results.next());  //advance the cursor to the next position
        }
    }
}
        