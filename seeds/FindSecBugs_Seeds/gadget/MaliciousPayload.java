import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

interface CacheData {
    void refresh();
    String getValue();
}

class SpecialCacheData implements CacheData {

    String command;
    String value;

    public SpecialCacheData(String command) {
        this.command = command;
    }

    @Override
    public void refresh() {
        value = executeCommand(command);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String executeCommand(String command, int arg2, long arg3) {
        return executeCommand(command);
    }

    private String executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }
}

class SuperMap extends HashMap<String,CacheData> {

    @Override
    public CacheData get(Object key) {
        CacheData data = super.get(key);
        data.refresh();
        return data;
    }

}

public class MaliciousPayload {
    
    public static void main(String[] args) {
//        SuperMap map = new SuperMap();
//        map.put("1234",new FileCacheData("C:/Code/secret.txt"));
//        System.out.println(map.get("1234").getValue());

        SuperMap map = new SuperMap();
        map.put("1234",new SpecialCacheData("calc.exe"));
        System.out.println(map.get("1234").getValue());
    }

}
