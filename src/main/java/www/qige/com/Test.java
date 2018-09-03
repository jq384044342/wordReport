package www.qige.com;

import org.apache.poi.ss.usermodel.PictureData;
import www.qige.com.word.ReadExcelUtils;

import java.io.FileOutputStream;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/27 0027.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        ReadExcelUtils readExcelUtils = new ReadExcelUtils(App.class.getClassLoader().getResource("result.xlsx").getPath());
        Map<Integer, Map<Integer, Object>> integerMapMap = readExcelUtils.readExcelContent();
        System.out.println(integerMapMap);
        for(Map.Entry<Integer, Map<Integer, Object>> entry : integerMapMap.entrySet()){
            PictureData pictureData = (PictureData) entry.getValue().get(7);
            FileOutputStream out = new FileOutputStream(
                    "D:\\gitProject\\worReport\\src\\main\\pict"+entry.getKey()+".jpg");
            out.write(pictureData.getData());
            out.close();
        }

    }
}
