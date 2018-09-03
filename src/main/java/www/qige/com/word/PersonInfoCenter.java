package www.qige.com.word;

import java.util.Map;

/**
 * Created by Administrator on 2018/8/27 0027.
 */
public class PersonInfoCenter {
    private static String path;

    public PersonInfoCenter(String path) {
        this.path = path;
    }

    private static ReadPersonExcelUtils readExcelUtils = new ReadPersonExcelUtils(path);

    public Map<Integer, Map<Integer,Object>> getPersonInfo(){
        try {
            return readExcelUtils.readExcelContent();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
