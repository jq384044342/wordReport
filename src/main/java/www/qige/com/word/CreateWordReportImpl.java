package www.qige.com.word;

import com.deepoove.poi.XWPFTemplate;
import www.qige.com.App;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/27 0027.
 */
public class CreateWordReportImpl {

    public void createReport(Map<String, Object> map) {
        String template1;
        if("男".equals((String)map.get("sex"))){
            template1 = App.class.getClassLoader().getResource("man.docx").getPath();
        }else if("女".equals((String)map.get("sex"))){
            template1 = App.class.getClassLoader().getResource("woman.docx").getPath();
        }else {
            return;
        }

        XWPFTemplate template = XWPFTemplate.compile(template1).render(map);
        try {
            FileOutputStream out = new FileOutputStream("out_template.docx");
            template.write(out);
            out.flush();
            out.close();
            template.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

