package www.qige.com.word;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.DocxRenderData;
import com.deepoove.poi.data.PictureRenderData;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.xmlbeans.impl.common.ConcurrentReaderHashMap;
import www.qige.com.App;
import www.qige.com.entity.ReportEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiaqi on 2018/8/27 0027.
 */
public class CreateWordReportImpl {

    public void createReport(ReportEntity entity) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name",entity.getName());
        map.put("sex",entity.getSex());
        map.put("sicks",entity.getSicks());
        map.put("p53Res",entity.getP53Res());
        map.put("p53Type",entity.getP53Type());
        map.put("apoeRes",entity.getApoeRes());
        map.put("apoeType",entity.getApoeType());
        map.put("lungRisk",entity.getLungRisk());
        map.put("lungLevel",entity.getLungLevel());
        map.put("liverRisk",entity.getLiverRisk());
        map.put("liverLevel",entity.getLiverLevel());
        map.put("coloRisk",entity.getColoRisk());
        map.put("coloLevel",entity.getColoLevel());
        map.put("prosRisk",entity.getProsRisk());
        map.put("prosLevel",entity.getProsLevel());
        map.put("hyperRisk",entity.getHyperRisk());
        map.put("hypeLevel",entity.getHypeLevel());
        map.put("alzhRisk",entity.getAlzhRisk());
        map.put("alzhLevel",entity.getAlzhLevel());
        map.put("lscheRisk",entity.getLscheRisk());
        map.put("lscheLevel",entity.getLscheLevel());
        map.put("cereRisk",entity.getCereRisk());
        map.put("cereLevel",entity.getCereLevel());
        map.put("infaRisk",entity.getInfaRisk());
        map.put("infaLevel",entity.getInfaLevel());
        map.put("ovarianRisk",entity.getOvarianRisk());
        map.put("ovarianLevel",entity.getOvarianLevel());
        map.put("endomRisk",entity.getEndomRisk());
        map.put("endomLevel",entity.getEndomLevel());
        map.put("breastRisk",entity.getBreastRisk());
        map.put("breastLevel",entity.getBreastLevel());
        map.put("femoralRisk",entity.getFemoralRisk());
        map.put("femoralLevel",entity.getFemoralLevel());
        map.put("cataRisk",entity.getCataRisk());
        map.put("cataLevel",entity.getCataLevel());
        map.put("resOnes",entity.getResOnes());
        map.put("resTwos",entity.getResTwos());
//        map.put("helthManage",entity.getHelthManage());
        map.put("helthManageContent",new DocxRenderData(new File("template\\"+entity.getHelthManageContent())));
        map.put("familySicks",entity.getFamilySicks());
        PictureData p53Pic = (PictureData) entity.getP53Pic1();
        PictureData apoePic = (PictureData) entity.getAopePic1();

        File picTmpPath = new File("D:\\tmp\\") ;
        if(!picTmpPath.exists()){
            picTmpPath.mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(
                    picTmpPath+entity.getNum()+"p53"+".png");
            out.write(p53Pic.getData());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream out = new FileOutputStream(
                    picTmpPath+entity.getNum()+"apoe"+".png");
            out.write(apoePic.getData());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        map.put("p53Pic",new PictureRenderData(465, 80,picTmpPath+ entity.getNum()+"p53"+".png"));
        map.put("aopePic",new PictureRenderData(465, 80,picTmpPath+ entity.getNum()+"apoe"+".png"));
        String template1;
        if("男".equals((String)map.get("sex"))){
//            template1 = App.class.getClassLoader().getResource("man.docx").getPath();
            template1 = "template\\man.docx";
        }else if("女".equals((String)map.get("sex"))){
//            template1 = App.class.getClassLoader().getResource("woman.docx").getPath();
            template1 = "template\\woman.docx";
        }else {
            return;
        }

        XWPFTemplate template = XWPFTemplate.compile(template1).render(map);
        try {
            FileOutputStream out = new FileOutputStream("report\\" + entity.getNum()+entity.getName()+".docx");
            template.write(out);
            out.flush();
            out.close();
            template.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

