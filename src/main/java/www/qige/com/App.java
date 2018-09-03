package www.qige.com;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.util.BytePictureUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Map<String, Object> stringObjectHashMap = new HashMap<String, Object>();
        stringObjectHashMap.put("name","张岚");
        stringObjectHashMap.put("sex","男");
        stringObjectHashMap.put("sicks","肝炎");
        stringObjectHashMap.put("p53Res","CG");
        stringObjectHashMap.put("p53Type","纯合突变");
        stringObjectHashMap.put("apoeRes","CT");
        stringObjectHashMap.put("apoeType","杂合突变");
        stringObjectHashMap.put("lungRisk","1.7200");
        stringObjectHashMap.put("lungLevel","关注");
        stringObjectHashMap.put("p53Pic",new PictureRenderData(465, 75, App.class.getClassLoader().getResource("p53.png").getPath()));
        stringObjectHashMap.put("aopePic",new PictureRenderData(465, 75, App.class.getClassLoader().getResource("apoe.png").getPath()));
        stringObjectHashMap.put("resOnes","研究显示桔橘橙类水果、葱蒜类是结肠癌的保护因素，香蕉是结肠癌的危险因素；白菜类是直肠癌的保护因素，芋薯类是直肠癌的危险因素；膳食营养素中的钙是结直肠癌最重要的保护因素；既往肠息肉、血吸虫病史是结直肠癌的危险因素。鉴于您有直肠炎、便秘（偶尔便血），建议您去医疗机构详细检查，并积极治疗，同时在饮食结构上多食用桔橘橙类水果、葱蒜类、白菜类食物，减少香蕉、芋薯类食物摄入。");
        stringObjectHashMap.put("helthManage","健康管理");
        stringObjectHashMap.put("helthManageContent","1.饮食健康管理\n" +
                "第一是全面平衡，即样样都吃，不挑食，不偏食。众所周知，任何一种单一的天然食物都不能提供人体所需要的全部营养素。因此，合理膳食必须由多种食物组成，才能达到平衡膳食之目的。保持以植物性食物为主，动物性食物为辅，热能来源以粮食为主的特点，避免西方国家膳食模式所带来的脂肪过多和热能太高的弊端。");
        String template1 = App.class.getClassLoader().getResource("man.docx").getPath();
        XWPFTemplate template = XWPFTemplate.compile(template1).render(stringObjectHashMap);
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
