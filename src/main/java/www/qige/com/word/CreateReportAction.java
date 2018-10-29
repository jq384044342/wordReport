package www.qige.com.word;


import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.PictureData;
import www.qige.com.entity.ReportEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CreateReportAction extends JFrame implements ActionListener {
    JButton jbPerson = new JButton("导入个人信息");
    JButton jbResult = new JButton("导入检测结果");
    JButton jbReport = new JButton("生成报告");
    JTextArea textarea = new JTextArea("\n"
            + "使用说明\n" +
            "\n" +
            "注意事项：请勿删除任何文件夹！！！\n" +
            "\n" +
            "1.点击start.bat启动程序，启动可能需要1-5s时间，请耐心等待\n" +
            "\n" +
            "2.点击导入个人信息按钮，导入文件，弹出导入个人信息成功后进入下一步\n" +
            "\n" +
            "3.点击导入检测结果，导入文件，弹出导入检测成功后进入下一步\n" +
            "\n" +
            "4.点击生成报告，弹出生成报告成功后，在report文件夹下生成报告\n");
    private Map<Integer, Map<Integer, Object>> personInfo;
    private Map<Integer, Map<Integer, Object>> resultInfo;
    private java.util.List<String> resultTime = new ArrayList<>();
    private Map<String, Map<Integer, Object>> resultMap = new HashMap<>();
    private Map<Integer, Map<Integer, Object>> config = new HashMap<>();
    java.util.List<ReportEntity> reportList = new ArrayList<ReportEntity>();

    public CreateReportAction() {
        jbPerson.setActionCommand("openPerson");
        jbResult.setActionCommand("openResult");
        jbReport.setActionCommand("createReport");
        jbPerson.setBackground(Color.CYAN);//设置按钮颜色
        jbResult.setBackground(Color.CYAN);//设置按钮颜色
        jbReport.setBackground(Color.CYAN);//设置按钮颜色
        jbPerson.setSize(20, 20);
        jbResult.setSize(20, 20);
        jbReport.setSize(20, 20);
        //初始化一个文字区域

        textarea.setFont(new Font("黑体", Font.BOLD, 16));
        textarea.setEditable(false);
        Container container = this.getContentPane();
        container.setLayout(new FlowLayout());
        container.add(jbPerson);//建立容器使用边界布局
        container.add(jbResult);//建立容器使用边界布局
        container.add(jbReport);//建立容器使用边界布局
        container.add(textarea);
        //
        jbPerson.addActionListener(this);
        jbResult.addActionListener(this);
        jbReport.addActionListener(this);
        this.setTitle("检测报告生成工具");
        this.setSize(600, 400);
        this.setLocation(200, 200);
        //显示窗口true  
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            config = new ReadConfigExcelUtils("template\\config.xlsx").readExcelContent();
//            config = new ReadConfigExcelUtils("D:\\ideaWork\\out\\artifacts\\worReport_jar\\template\\config.xlsx").readExcelContent();
        } catch (Exception e) {
            System.out.println("加载配置文件出错");
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("openPerson")) {
            JFileChooser jf = new JFileChooser();
            jf.showOpenDialog(this);//显示打开的文件对话框  
            File f = jf.getSelectedFile();//使用文件类获取选择器选择的文件
            String path = f.getAbsolutePath();//返回路径名
            ReadPersonExcelUtils readExcelUtils = new ReadPersonExcelUtils(path);
            try {
                personInfo = readExcelUtils.readExcelContent();
                JOptionPane.showMessageDialog(this, "成功导入个人信息", "标题", JOptionPane.WARNING_MESSAGE);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "导入个人信息错误", "标题", JOptionPane.WARNING_MESSAGE);
                e1.printStackTrace();
            }
        } else if (e.getActionCommand().equals("openResult")) {
            JFileChooser jf = new JFileChooser();
            jf.showOpenDialog(this);//显示打开的文件对话框
            File f = jf.getSelectedFile();//使用文件类获取选择器选择的文件
            String path = f.getAbsolutePath();//返回路径名
            ReadExcelUtils readExcelUtils = new ReadExcelUtils(path);
            try {
                //男性P53 rs1042522 APOE rs429358 女性 P53 rs1042522 MTHFR rs1801133
                resultInfo = readExcelUtils.readExcelContent();
                for (Map.Entry<Integer, Map<Integer, Object>> entry : resultInfo.entrySet()) {
                    if (StringUtils.isNotEmpty((String) entry.getValue().get(1))) {
                        String num = (String) entry.getValue().get(1);
                        if (num.contains(".0")) {
                            num = num.replace(".0", "");
                        }
                        String geneNum = (String) entry.getValue().get(3);
                        resultTime.add(num);
                        resultMap.put(num + geneNum, entry.getValue());
                    }
                }
                JOptionPane.showMessageDialog(this, "成功导入检测结果", "标题", JOptionPane.WARNING_MESSAGE);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "导入检测结果错误", "标题", JOptionPane.WARNING_MESSAGE);
                e1.printStackTrace();
            }
        } else if (e.getActionCommand().equals("createReport")) {
            try {

                for (Map.Entry<Integer, Map<Integer, Object>> entry : personInfo.entrySet()) {
                    if (StringUtils.isNotEmpty((String) entry.getValue().get(1))) {
                        ReportEntity entity = new ReportEntity();
                        String num = (String) entry.getValue().get(1);
                        if (num.contains(".0")) {
                            num = num.replace(".0", "");
                        }
                        if (2 == Collections.frequency(resultTime, num)) {
                            entity.setName((String) entry.getValue().get(2));
                            entity.setFileName((String) entry.getValue().get(0));
                            entity.setNum(num);
                            String sex = (String) entry.getValue().get(3);
                            entity.setSex(sex);
                            String age = StringUtils.isEmpty((String) entry.getValue().get(4))?"":(String) entry.getValue().get(4);
                            if (age.contains(".0")) {
                                age = age.replace(".0", "");
                            }
                            entity.setAge(age);
                            StringBuffer sbSicks = new StringBuffer();
                            for (int i = 5; i <= 34; i++) {
                                if (StringUtils.isNoneEmpty((String) entry.getValue().get(i))) {
                                    if (((String) entry.getValue().get(i)).endsWith("、")) {
                                        sbSicks.append((String) entry.getValue().get(i));
                                    } else {
                                        sbSicks.append((String) entry.getValue().get(i) + "、");
                                    }
                                }
                            }
                            String thesicks = sbSicks.toString();

                            entity.setSicks(thesicks);
                            StringBuffer setFamilySicks = new StringBuffer();
                            for (int j = 35; j <= 54; j++) {
                                if (StringUtils.isNoneEmpty((String) entry.getValue().get(j))) {
                                    if(((String) entry.getValue().get(j)).endsWith("、")){
                                        setFamilySicks.append((String) entry.getValue().get(j));
                                    }else {
                                        setFamilySicks.append((String) entry.getValue().get(j) + "、");
                                    }
                                }
                            }
                            String familySicks = setFamilySicks.toString();

                            entity.setFamilySicks(familySicks);
                            String p53Res = "";
                            String p53Type = "";
                            String apoeRes = "";
                            String apoeType = "";
                            try {
                                p53Res = (String) resultMap.get(num + "rs1042522").get(5);
                                entity.setP53Res(p53Res);
                                p53Type = getP53Type(p53Res);
                                if (p53Type.isEmpty()) continue;//跳出循环
                                entity.setP53Type(p53Type);
                                entity.setP53Pic1((PictureData) resultMap.get(num + "rs1042522").get(7));
                                PictureData apoePic1;
                                if ("男".equals(sex)) {
                                    apoeRes = (String) resultMap.get(num + "rs429358").get(5);
                                    apoeType = getApoeType(apoeRes);
                                    apoePic1 = (PictureData) resultMap.get(num + "rs429358").get(7);
                                } else {
                                    apoeRes = (String) resultMap.get(num + "rs1801133").get(5);
                                    apoeType = getMTHFRType(apoeRes);
                                    apoePic1 = (PictureData) resultMap.get(num + "rs1801133").get(7);
                                }
                                entity.setApoeRes(apoeRes);
                                if (apoeType.isEmpty()) continue;
                                entity.setApoeType(apoeType);
                                entity.setAopePic1(apoePic1);
                            } catch (Exception e1) {
                                System.out.println(num + "数据不匹配，跳过生成报告");
                                continue;
                            }
                            //病及风险
                            if ("男".equals(sex)) {
                                setRiskAndLevel(p53Type, apoeType, entity);
                            } else {
                                setRiskAndLevelWomen(p53Type, apoeType, entity);
                            }
                            if (thesicks.endsWith("、")) {
                                thesicks = thesicks.substring(0, thesicks.length() - 1);
                            }
                            entity.setSicks(thesicks);
                            if (familySicks.endsWith("、")) {
                                familySicks = familySicks.substring(0, familySicks.length() - 1);
                            }
                            entity.setFamilySicks(familySicks);
                            //结果分析
                            String sicks = entity.getSicks();
                            if ("男".equals(sex)) {
                                StringBuffer sb = new StringBuffer();
                                if (entity.getSicks().contains("直肠炎") || entity.getSicks().contains("便秘") || entity.getSicks().contains("肠病")) {
                                    sb.append("随着人类对结直肠癌认识的深入和流行病学的发展, 发现结直肠癌是多因素作用、多基因参与、多阶段发展的疾病。流行病学研究表明，结直肠癌的发生是多因素参与、多步骤的复杂病理过程, 是生活饮食因素、宿主基因和环境因素等相互作用的结果。许多专家一致认为, 环境因素可能成为防治结直肠癌发生的重大科学问题。因此建议您摒弃不良生活饮食习惯，同时在饮食结构上多食用桔橘橙类水果、葱蒜类、白菜类食物，减少香蕉、芋薯类食物摄入。\n    ");
                                }
                                if (entity.getSicks().contains("胃病")) {
                                    sb.append("大量研究结果显示饮水来源、吸烟、经常吃腌制食品、有胃溃疡疾病史、近亲有胃癌疾病史及上消化道症状反复时间为胃癌发病的独立危险因素，提示暴露于以上环境因素、饮食习惯及遗传因素可能增加胃黏膜与致癌物质的接触机会，从而并削弱黏膜慢性炎症损伤修复的能力，继而导致慢性胃炎发展为胃癌。建议您去医疗机构检查，并积极治疗，同时在环境因素及饮食习惯上需多注意，杜绝不良因素诱导，健康生活！\n    ");
                                }
                                if (entity.getSicks().contains("肝硬化")
                                        || entity.getSicks().contains("肥胖")
                                        || entity.getSicks().contains("糖尿病")
                                        || entity.getSicks().contains("非酒精性脂肪肝")
                                        || entity.getSicks().contains("脂肪肝")
                                        || entity.getSicks().contains("肝病")) {
                                    sb.append("大量证据表明，肝癌的发生是一个多阶段、多因素共同累积的结果，其发生过程是由体外致癌因素联合自身免疫缺陷、基因水平等一系列因素导致的。肝癌病因复杂，包括不可控因素及可控制因素，不可控因素包括性别、年龄、血型、宿主遗传、基因类型及基因突变等。饮酒与吸烟是肝癌发生的危险因素之一，另外还有健康状况方面代谢综合征如肝硬化、肥胖、糖尿病、非酒精性脂肪肝以及社会心理及精神因素肝癌发生的危险因素之一。\n    ");
                                }
                                if (entity.getSicks().contains("肺炎") || entity.getSicks().contains("支气管炎")
                                        || entity.getSicks().contains("气管炎") || entity.getSicks().contains("肺结核")
                                        || entity.getSicks().contains("肺病")) {
                                    sb.append("研究证实，气管炎、肺结核等肺病是并发肺癌的主要原因，减少气管炎患者的人数将大大减少肺癌的发病率。气管感染的患者促使肺脏内的炎症因子的释放，浸润并作用于肺内的细胞，分泌IL-1、IL-6等细胞因子及肿瘤的坏死因子及其他相关因子。这些因子将作用于肺脏细胞，引起细胞的反应，最后导致肺癌发生。鉴于您积极治疗有相关疾病，同时一定不要抽烟，并远离二手烟接触，为您的健康保驾护航！\n    ");
                                }
                                if (entity.getSicks().contains("吸烟")) {
                                    sb.append("您有长时间吸烟史，香烟中的尼古丁、亚硝胺等物质可导致基因甲基化而诱导癌症的发生。主动吸烟和被动吸烟分别作为肺癌、肝癌患病的危险因素已经被世界范围内大量的流行病学调查所证实，且被动吸烟者宫颈黏液中烟草相关致癌物质含量高于主动吸烟者。建议您远离吸烟场所，减少接触癌症诱发的因素。\n    ");
                                }
                                if (entity.getSicks().contains("饮酒")) {
                                    sb.append("酒精被认为是可能诱发肝癌的危险因素。大量饮酒可促进肝癌发生。大数据分析结果表明，饮酒25 g·d-1及以上是肝癌的危险因素，并有随着每日饮酒量的增加，肝癌发病率增加的趋势。主要原因为：一方面，酒精可增加乳腺上皮细胞膜的通透性，通过乙醇诱导酶而增加致病因子的代谢，抑制DNA的损伤修复。您有饮酒习惯。建议您远离酒精，健康生活。\n    ");
                                }

                                sb.insert(0, "\n    ");
                                if (sb.toString().endsWith("\n    ")) {
                                    sb.delete(sb.length() - 5, sb.length());
                                }
                                entity.setResOnes(sb.toString());
                                StringBuffer sbtwo = new StringBuffer();
                                if (entity.getSicks().contains("高血压")) {
                                    sbtwo.append("研究表明高血压是诱发各种心脑血管疾病，尤其是脑卒中疾病的元凶。研究表明，血浆Hcy 水平在脑卒中患者中明显升高。根据中国心血管病人群监测( WHOMONICA 方案) 最新发表数据，我国脑卒中仍以每年8. 7%的速率增长，脑卒中发病率远远高于其他国家，居亚太地区第一名，较美国高出1 倍，男女性脑卒中发病率均高达27%，在脑卒中的多种可控因素中，高血压和HHcy 排在前两位，二者具有显著协同作用。研究表明，单独存在高血压或高同型半胱氨酸血症的患者，脑卒中死亡的风险分别为正常人群的3 倍和4 倍，当高血压同时合并Hcy 升高时，脑卒中发生风险增加明显。鉴于您血压偏高，建议您一定积极治疗，控制血压，预防并发症的发生。\n    ");
                                }
                                if (entity.getSicks().contains("冠心病") ||
                                        entity.getSicks().contains("脑供血不足") || entity.getSicks().contains("动脉硬化")
                                        || entity.getSicks().contains("心脏病")) {
                                    sbtwo.append("心脑血管疾病是心脏血管和脑血管疾病的统称，泛指由于高脂血症、血液黏稠、动脉粥样硬化、高血压等所导致的心脏、大脑及全身组织发生的缺血性或出血性疾病。为了降低心脑血管或相关疾病发生概率，建议您适当增加锻炼，健康饮食，舒心生活！\n    ");
                                }
                                if (entity.getSicks().contains("糖尿病")) {
                                    sbtwo.append("大量研究表明，糖尿病并发症有：糖尿病肾病、糖尿病眼病、糖尿病足病、糖尿病心血管病、糖尿病脑血管病等，鉴于您患有糖尿病，建议您密切关注以上疾病，同时积极治疗、控制血糖。\n    ");
                                }
                                sbtwo.insert(0, "\n    ");
                                if (sbtwo.toString().endsWith("\n    ")) {
                                    sbtwo.delete(sbtwo.length() - 5, sbtwo.length());
                                }
                                entity.setResTwos(sbtwo.toString());
                            } else {  //女性健康分析
                                StringBuffer sb1 = new StringBuffer();
                                if (sicks.contains("直肠炎") || sicks.contains("便秘") || sicks.contains("肠病")) {
                                    sb1.append("随着人类对结直肠癌认识的深入和流行病学的发展, 发现结直肠癌是多因素作用、多基因参与、多阶段发展的疾病。流行病学研究表明，结直肠癌的发生是多因素参与、多步骤的复杂病理过程, 是生活饮食因素、宿主基因和环境因素等相互作用的结果。许多专家一致认为, 环境因素可能成为防治结直肠癌发生的重大科学问题。因此建议您摒弃不良生活饮食习惯，同时在饮食结构上多食用桔橘橙类水果、葱蒜类、白菜类食物，减少香蕉、芋薯类食物摄入。\n    ");
                                }
                                if (sicks.contains("胃病")) {
                                    sb1.append("大量研究结果显示饮水来源、吸烟、经常吃腌制食品、有胃溃疡疾病史、近亲有胃癌疾病史及上消化道症状反复时间为胃癌发病的独立危险因素，提示暴露于以上环境因素、饮食习惯及遗传因素可能增加胃黏膜与致癌物质的接触机会，从而并削弱黏膜慢性炎症损伤修复的能力，继而导致慢性胃炎发展为胃癌。建议您去医疗机构检查，并积极治疗，同时在环境因素及饮食习惯上需多注意，杜绝不良因素诱导，健康生活！\n    ");
                                }
                                if (sicks.contains("肝硬化")
                                        || sicks.contains("肥胖")
                                        || sicks.contains("糖尿病")
                                        || sicks.contains("非酒精性脂肪肝")
                                        || sicks.contains("脂肪肝")
                                        || sicks.contains("肝病")) {
                                    sb1.append("大量证据表明，肝癌的发生是一个多阶段、多因素共同累积的结果，其发生过程是由体外致癌因素联合自身免疫缺陷、基因水平等一系列因素导致的。肝癌病因复杂，包括不可控因素及可控制因素，不可控因素包括性别、年龄、血型、宿主遗传、基因类型及基因突变等。饮酒与吸烟是肝癌发生的危险因素之一，另外还有健康状况方面代谢综合征如肝硬化、肥胖、糖尿病、非酒精性脂肪肝以及社会心理及精神因素肝癌发生的危险因素之一。\n    ");
                                }
                                if (sicks.contains("肺炎") || sicks.contains("支气管炎")
                                        || sicks.contains("气管炎") || sicks.contains("肺结核")
                                        || sicks.contains("肺病")) {
                                    sb1.append("研究证实，气管炎、肺结核等肺病是并发肺癌的主要原因，减少气管炎患者的人数将大大减少肺癌的发病率。气管感染的患者促使肺脏内的炎症因子的释放，浸润并作用于肺内的细胞，分泌IL-1、IL-6等细胞因子及肿瘤的坏死因子及其他相关因子。这些因子将作用于肺脏细胞，引起细胞的反应，最后导致肺癌发生。鉴于您积极治疗有相关疾病，同时一定不要抽烟，并远离二手烟接触，为您的健康保驾护航！\n    ");
                                }
                                if (sicks.contains("吸烟")) {
                                    sb1.append("您有长时间吸烟史，香烟中的尼古丁、亚硝胺等物质可导致基因甲基化而诱导癌症的发生。主动吸烟和被动吸烟分别作为肺癌、肝癌患病的危险因素已经被世界范围内大量的流行病学调查所证实，且被动吸烟者宫颈黏液中烟草相关致癌物质含量高于主动吸烟者。建议您远离吸烟场所，减少接触癌症诱发的因素。\n    ");
                                }
                                if (sicks.contains("饮酒")) {
                                    sb1.append("酒精被认为是可能诱发肝癌的危险因素。大量饮酒可促进肝癌发生。大数据分析结果表明，饮酒25 g·d-1及以上是肝癌的危险因素，并有随着每日饮酒量的增加，肝癌发病率增加的趋势。主要原因为：一方面，酒精可增加乳腺上皮细胞膜的通透性，通过乙醇诱导酶而增加致病因子的代谢，抑制DNA的损伤修复。您有饮酒习惯。建议您远离酒精，健康生活。\n    ");
                                }
                                sb1.insert(0, "\n    ");
                                if (sb1.toString().endsWith("\n    ")) {
                                    sb1.delete(sb1.length() - 5, sb1.length());
                                }
                                entity.setResOnes(sb1.toString());
                                StringBuffer sb2 = new StringBuffer();
                                if (sicks.contains("高血压")) {
                                    sb2.append("高血压是白内障的一个重要危险因素，长期罹患高血压，尤其是高收缩压状态能够促进白内障的发生和发展。体质指数（体质指数（BMI）=体重（kg）÷身高^2（m）。成人的BMI数值：过轻：低于18.5；正常：18.5-23.9；过重：24-27；肥胖：28-32；非常肥胖, 高于32）增高与白内障呈正相关，超重者患高血压发生白内障的危险性显著增高。因此应该倡导健康的生活方式，如参加更多的体力活动、合理膳食、减轻体质量、维持正常血压等，可有助于预防白内障的发生。鉴于您有高血压，建议您积极首先积极治疗高血压，同时合理膳食，保持正常体质指数，健康生活。\n    ");
                                }
                                if (sicks.contains("冠心病")
                                        || sicks.contains("脑供血不足")
                                        || sicks.contains("动脉硬化")
                                        || sicks.contains("心脏病")) {
                                    sb2.append("心脑血管疾病是心脏血管和脑血管疾病的统称，泛指由于高脂血症、血液黏稠、动脉粥样硬化、高血压等所导致的心脏、大脑及全身组织发生的缺血性或出血性疾病。为了降低心脑血管或相关疾病发生概率，建议您适当增加锻炼，健康饮食，舒心生活！\n    ");
                                }
                                if (sicks.contains("糖尿病")) {
                                    sb2.append("大量研究表明，糖尿病并发症有：糖尿病肾病、糖尿病眼病、糖尿病足病、糖尿病心血管病、糖尿病脑血管病等，鉴于您患有糖尿病，建议您密切关注以上疾病，同时积极治疗、控制血糖。\n      ");
                                }
                                sb2.insert(0, "\n    ");
                                if (sb2.toString().endsWith("\n    ")) {
                                    sb2.delete(sb2.length() - 5, sb2.length());
                                }
                                entity.setResTwos(sb2.toString());
                            }

                            if (sicks.contains("高血压") && !sicks.contains("糖尿病")) {
                                entity.setHelthManage("高血压健康管理");
                                entity.setHelthManageContent("g.docx");
                            } else if (sicks.contains("糖尿病") && !sicks.contains("高血压")) {
                                entity.setHelthManage("糖尿病健康管理");
                                entity.setHelthManageContent("t.docx");
                            } else if (sicks.contains("高血压") && sicks.contains("糖尿病")) {
                                entity.setHelthManage("健康管理");
                                entity.setHelthManageContent("gt.docx");
                            } else {
                                entity.setHelthManage("健康管理");
                                entity.setHelthManageContent("wu.docx");
                            }
                            reportList.add(entity);
                        }

                    }
                }
                for (ReportEntity entity : reportList) {
                    CreateWordReportImpl createWordReport = new CreateWordReportImpl();
                    createWordReport.createReport(entity);
                }
                JOptionPane.showMessageDialog(this, "成功生成报告", "标题", JOptionPane.WARNING_MESSAGE);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "生成报告错误", "标题", JOptionPane.WARNING_MESSAGE);
                e1.printStackTrace();
            }
        }
    }

    private void setRiskAndLevelWomen(String p53Type, String apoeType, ReportEntity entity) {
        setLungRiskAndLevelWomen(p53Type, apoeType, entity);
        setLiverRiskAndLevelWomen(p53Type, apoeType, entity);
        setColoRiskAndLevelWomen(p53Type, apoeType, entity);
        setOvarianRiskAndLevelWomen(p53Type, apoeType, entity);
        setEndomRiskAndLevelWomen(p53Type, apoeType, entity);

        setBreastRiskAndLevelWomen(p53Type, apoeType, entity);
        setFemoraRiskAndLevelWomen(p53Type, apoeType, entity);
        setLscheRiskAndLevelWomen(p53Type, apoeType, entity);
        setCataRiskAndLevelWomen(p53Type, apoeType, entity);
    }

    private void setCataRiskAndLevelWomen(String p53Type, String apoeType, ReportEntity entity) {
        String sicks = entity.getSicks() + entity.getFamilySicks();
        double sum = 0;
        if ("正常".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(36).get(4));
        } else if ("杂合突变".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(36).get(5));
        } else if ("纯合突变".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(36).get(6));
        }
        if (sicks.contains("吸烟、")) {
            sum += Double.parseDouble((String) config.get(36).get(7));
        }
        if (sicks.contains("饮酒、")) {
            sum += Double.parseDouble((String) config.get(36).get(8));
        }
        if (sicks.contains("脑供血不足、")) {
            sum += Double.parseDouble((String) config.get(36).get(9));
        }
        if (sicks.contains("白内障、")) {
            sum += Double.parseDouble((String) config.get(36).get(10));
        }
        if (sicks.contains("高血压、")) {
            sum += Double.parseDouble((String) config.get(36).get(11));
        }
        if (sicks.contains("糖尿病、")) {
            sum += Double.parseDouble((String) config.get(36).get(12));
        }
        if (sicks.contains("高血脂、")) {
            sum += Double.parseDouble((String) config.get(36).get(13));
        }
        if (sicks.contains("冠心病、")) {
            sum += Double.parseDouble((String) config.get(36).get(14));
        }
        if (sicks.contains("肥胖、")) {
            sum += Double.parseDouble((String) config.get(36).get(15));
        }
        if (sicks.contains("糖尿病家族病史、")) {
            sum += Double.parseDouble((String) config.get(36).get(16));
        }
        if (sicks.contains("便秘史、")) {
            sum += Double.parseDouble((String) config.get(36).get(17));
        }
        if (sicks.contains("血便史、")) {
            sum += Double.parseDouble((String) config.get(36).get(17));
        }
        if (sicks.contains("恶性肿瘤家族史、")) {
            sum += Double.parseDouble((String) config.get(36).get(17));
        }
        sum += getAgeRisk(entity, "白内障");
        String level = "";
        level = getRiskLevel(sum, level);
        DecimalFormat dFormat = new DecimalFormat("#.0000");
        entity.setCataRisk(dFormat.format(sum));
        entity.setCataLevel(level);

    }

    private void setLscheRiskAndLevelWomen(String p53Type, String apoeType, ReportEntity entity) {
        String sicks = entity.getSicks() + entity.getFamilySicks();
        double sum = 0;
        if ("正常".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(34).get(4));
        } else if ("杂合突变".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(34).get(5));
        } else if ("纯合突变".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(34).get(6));
        }
        if (sicks.contains("吸烟、")) {
            sum += Double.parseDouble((String) config.get(34).get(7));
        }
        if (sicks.contains("饮酒、")) {
            sum += Double.parseDouble((String) config.get(34).get(8));
        }
        if (sicks.contains("脑供血不足、")) {
            sum += Double.parseDouble((String) config.get(34).get(9));
        }
        if (sicks.contains("重体力活、")) {
            sum += Double.parseDouble((String) config.get(34).get(10));
        }
        if (sicks.contains("高血压、")) {
            sum += Double.parseDouble((String) config.get(34).get(11));
        }
        if (sicks.contains("糖尿病、")) {
            sum += Double.parseDouble((String) config.get(34).get(12));
        }
        if (sicks.contains("高血脂、")) {
            sum += Double.parseDouble((String) config.get(34).get(13));
        }
        if (sicks.contains("冠心病、")) {
            sum += Double.parseDouble((String) config.get(34).get(14));
        }
        if (sicks.contains("肥胖、")) {
            sum += Double.parseDouble((String) config.get(34).get(15));
        }
        if (sicks.contains("嗜盐、")) {
            sum += Double.parseDouble((String) config.get(34).get(16));
        }
        if (sicks.contains("高尿酸症、")) {
            sum += Double.parseDouble((String) config.get(34).get(17));
        }
        sum += getAgeRisk(entity, "缺血性脑卒中");
        String level = "";
        level = getRiskLevel(sum, level);
        DecimalFormat dFormat = new DecimalFormat("#.0000");
        entity.setLscheRisk(dFormat.format(sum));
        entity.setLscheLevel(level);
    }

    private void setFemoraRiskAndLevelWomen(String p53Type, String apoeType, ReportEntity entity) {
        String sicks = entity.getSicks() + entity.getFamilySicks();
        double sum = 0;
        if ("正常".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(32).get(4));
        } else if ("杂合突变".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(32).get(5));
        } else if ("纯合突变".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(32).get(6));
        }
        if (sicks.contains("吸烟、")) {
            sum += Double.parseDouble((String) config.get(32).get(7));
        }
        if (sicks.contains("饮酒、")) {
            sum += Double.parseDouble((String) config.get(32).get(8));
        }
        if (sicks.contains("风湿骨病、")) {
            sum += Double.parseDouble((String) config.get(32).get(9));
        }
        if (sicks.contains("股骨头坏死、")) {
            sum += Double.parseDouble((String) config.get(32).get(10));
        }
        if (sicks.contains("高血压、")) {
            sum += Double.parseDouble((String) config.get(32).get(11));
        }
        if (sicks.contains("糖尿病、")) {
            sum += Double.parseDouble((String) config.get(32).get(12));
        }

        sum += getAgeRisk(entity, "股骨头坏死");
        String level = "";
        level = getRiskLevel(sum, level);
        DecimalFormat dFormat = new DecimalFormat("#.0000");
        entity.setFemoralRisk(dFormat.format(sum));
        entity.setFemoralLevel(level);

    }

    private void setBreastRiskAndLevelWomen(String p53Type, String apoeType, ReportEntity entity) {
        String sicks = entity.getSicks() + entity.getFamilySicks();
        double sum = 0;
        if ("正常".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(30).get(4));
        } else if ("杂合突变".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(30).get(5));
        } else if ("纯合突变".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(30).get(6));
        }
        if (sicks.contains("吸烟、")) {
            sum += Double.parseDouble((String) config.get(30).get(7));
        }
        if (sicks.contains("饮酒、")) {
            sum += Double.parseDouble((String) config.get(30).get(8));
        }
        if (sicks.contains("恶性肿瘤家族史、")) {
            sum += Double.parseDouble((String) config.get(30).get(9));
        }
        if (sicks.contains("乳腺癌家族史、")) {
            sum += Double.parseDouble((String) config.get(30).get(10));
        }
        if (sicks.contains("乳腺癌、")) {
            sum += Double.parseDouble((String) config.get(30).get(11));
        }
        if (sicks.contains("乳腺增生、")) {
            sum += Double.parseDouble((String) config.get(30).get(12));
        }
        if (sicks.contains("饮茶、")) {
            sum += Double.parseDouble((String) config.get(30).get(13));
        }
        if (sicks.contains("喜红肉、")) {
            sum += Double.parseDouble((String) config.get(30).get(14));
        }
        if (sicks.contains("喜脂肪、")) {
            sum += Double.parseDouble((String) config.get(30).get(15));
        }
        sum += getAgeRisk(entity, "乳腺癌");
        String level = "";
        level = getRiskLevel(sum, level);
        DecimalFormat dFormat = new DecimalFormat("#.0000");
        entity.setBreastRisk(dFormat.format(sum));
        entity.setBreastLevel(level);
    }

    private void setEndomRiskAndLevelWomen(String p53Type, String apoeType, ReportEntity entity) {
        String sicks = entity.getSicks() + entity.getFamilySicks();
        double sum = 0;
        if ("正常".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(28).get(4));
        } else if ("杂合突变".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(28).get(5));
        } else if ("纯合突变".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(28).get(6));
        }
        if (sicks.contains("吸烟、")) {
            sum += Double.parseDouble((String) config.get(28).get(7));
        }
        if (sicks.contains("饮酒、")) {
            sum += Double.parseDouble((String) config.get(28).get(8));
        }
        if (sicks.contains("恶性肿瘤家族史、")) {
            sum += Double.parseDouble((String) config.get(6).get(9));
        }
        if (sicks.contains("子宫内膜癌家族史、")) {
            sum += Double.parseDouble((String) config.get(28).get(10));
        }
        if (sicks.contains("高血压、")) {
            sum += Double.parseDouble((String) config.get(28).get(11));
        }
        if (sicks.contains("糖尿病、")) {
            sum += Double.parseDouble((String) config.get(28).get(12));
        }
        if (sicks.contains("子宫内膜癌、")) {
            sum += Double.parseDouble((String) config.get(28).get(13));
        }
        if (sicks.contains("喜红肉、")) {
            sum += Double.parseDouble((String) config.get(28).get(14));
        }
        if (sicks.contains("喜脂肪、")) {
            sum += Double.parseDouble((String) config.get(28).get(15));
        }
        sum += getAgeRisk(entity, "子宫内膜癌");
        String level = "";
        level = getRiskLevel(sum, level);
        DecimalFormat dFormat = new DecimalFormat("#.0000");
        entity.setEndomRisk(dFormat.format(sum));
        entity.setEndomLevel(level);
    }

    private void setOvarianRiskAndLevelWomen(String p53Type, String apoeType, ReportEntity entity) {
        String sicks = entity.getSicks() + entity.getFamilySicks();
        double sum = 0;
        if ("正常".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(26).get(4));
        } else if ("杂合突变".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(26).get(5));
        } else if ("纯合突变".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(26).get(6));
        }
        if (sicks.contains("吸烟、")) {
            sum += Double.parseDouble((String) config.get(26).get(7));
        }
        if (sicks.contains("饮酒、")) {
            sum += Double.parseDouble((String) config.get(26).get(8));
        }
        if (sicks.contains("恶性肿瘤家族史、")) {
            sum += Double.parseDouble((String) config.get(26).get(9));
        }
        if (sicks.contains("卵巢癌家族史、")) {
            sum += Double.parseDouble((String) config.get(26).get(10));
        }
        if (sicks.contains("高血压、")) {
            sum += Double.parseDouble((String) config.get(26).get(11));
        }
        if (sicks.contains("糖尿病、")) {
            sum += Double.parseDouble((String) config.get(26).get(12));
        }
        if (sicks.contains("结直肠癌、")) {
            sum += Double.parseDouble((String) config.get(26).get(13));
        }
        if (sicks.contains("卵巢癌、")) {
            sum += Double.parseDouble((String) config.get(26).get(14));
        }
        if (sicks.contains("喜红肉、")) {
            sum += Double.parseDouble((String) config.get(26).get(15));
        }
        if (sicks.contains("喜脂肪、")) {
            sum += Double.parseDouble((String) config.get(26).get(16));
        }
        sum += getAgeRisk(entity, "卵巢癌");
        String level = "";
        level = getRiskLevel(sum, level);
        DecimalFormat dFormat = new DecimalFormat("#.0000");
        entity.setOvarianRisk(dFormat.format(sum));
        entity.setOvarianLevel(level);

    }

    private void setColoRiskAndLevelWomen(String p53Type, String apoeType, ReportEntity entity) {
        String sicks = entity.getSicks() + entity.getFamilySicks();
        double sum = 0;
        if ("正常".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(24).get(4));
        } else if ("杂合突变".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(24).get(5));
        } else if ("纯合突变".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(24).get(6));
        }
        if (sicks.contains("吸烟、")) {
            sum += Double.parseDouble((String) config.get(24).get(7));
        }
        if (sicks.contains("饮酒、")) {
            sum += Double.parseDouble((String) config.get(24).get(8));
        }
        if (sicks.contains("消化道溃疡、")) {
            sum += Double.parseDouble((String) config.get(24).get(9));
        }
        if (sicks.contains("直结肠炎、")) {
            sum += Double.parseDouble((String) config.get(24).get(10));
        }
        if (sicks.contains("高血压、")) {
            sum += Double.parseDouble((String) config.get(24).get(11));
        }
        if (sicks.contains("糖尿病、")) {
            sum += Double.parseDouble((String) config.get(24).get(12));
        }
        if (sicks.contains("结直肠癌、")) {
            sum += Double.parseDouble((String) config.get(24).get(13));
        }
        if (sicks.contains("肠息肉史、")) {
            sum += Double.parseDouble((String) config.get(24).get(14));
        }
        if (sicks.contains("喜腌制食物、")) {
            sum += Double.parseDouble((String) config.get(24).get(15));
        }
        if (sicks.contains("嗜盐、")) {
            sum += Double.parseDouble((String) config.get(24).get(16));
        }
        if (sicks.contains("便秘史、")) {
            sum += Double.parseDouble((String) config.get(24).get(17));
        }
        if (sicks.contains("血便史、")) {
            sum += Double.parseDouble((String) config.get(24).get(18));
        }
        if (sicks.contains("恶性肿瘤家族史、")) {
            sum += Double.parseDouble((String) config.get(24).get(19));
        }
        sum += getAgeRisk(entity, "结直肠癌");
        String level = "";
        level = getRiskLevel(sum, level);
        DecimalFormat dFormat = new DecimalFormat("#.0000");
        entity.setColoRisk(dFormat.format(sum));
        entity.setColoLevel(level);

    }

    private void setLiverRiskAndLevelWomen(String p53Type, String apoeType, ReportEntity entity) {
        String sicks = entity.getSicks() + entity.getFamilySicks();
        double sum = 0;
        if ("正常".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(22).get(4));
        } else if ("杂合突变".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(22).get(5));
        } else if ("纯合突变".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(22).get(6));
        }
        if (sicks.contains("吸烟、")) {
            sum += Double.parseDouble((String) config.get(22).get(7));
        }
        if (sicks.contains("饮酒、")) {
            sum += Double.parseDouble((String) config.get(22).get(8));
        }
        if (sicks.contains("喜鱼生类食物、")) {
            sum += Double.parseDouble((String) config.get(22).get(9));
        }
        if (sicks.contains("乙型肝炎病毒慢性感染、")) {
            sum += Double.parseDouble((String) config.get(22).get(10));
        }
        if (sicks.contains("高血压、")) {
            sum += Double.parseDouble((String) config.get(22).get(11));
        }
        if (sicks.contains("糖尿病、")) {
            sum += Double.parseDouble((String) config.get(22).get(12));
        }
        if (sicks.contains("肝癌、")) {
            sum += Double.parseDouble((String) config.get(22).get(13));
        }
        if (sicks.contains("肝癌家族史、")) {
            sum += Double.parseDouble((String) config.get(22).get(14));
        }
        if (sicks.contains("肝硬化、")) {
            sum += Double.parseDouble((String) config.get(22).get(15));
        }
        if (sicks.contains("脂肪肝、")) {
            sum += Double.parseDouble((String) config.get(22).get(16));
        }
        if (sicks.contains("肝炎、")) {
            sum += Double.parseDouble((String) config.get(22).get(17));
        }
        if (sicks.contains("喜腌制食物、")) {
            sum += Double.parseDouble((String) config.get(22).get(18));
        }
        if (sicks.contains("肝病、")) {
            sum += Double.parseDouble((String) config.get(22).get(19));
        }
        sum += getAgeRisk(entity, "肝癌");
        String level = "";
        level = getRiskLevel(sum, level);
        DecimalFormat dFormat = new DecimalFormat("#.0000");
        entity.setLiverRisk(dFormat.format(sum));
        entity.setLiverLevel(level);
    }

    private void setLungRiskAndLevelWomen(String p53Type, String apoeType, ReportEntity entity) {
        String sicks = entity.getSicks() + entity.getFamilySicks();
        double sum = 0;
        if ("正常".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(20).get(4));
        } else if ("杂合突变".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(20).get(5));
        } else if ("纯合突变".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(20).get(6));
        }
        if (sicks.contains("吸烟、")) {
            sum += Double.parseDouble((String) config.get(20).get(7));
        }
        if (sicks.contains("饮酒、")) {
            sum += Double.parseDouble((String) config.get(20).get(8));
        }
        if (sicks.contains("肺结核、")) {
            sum += Double.parseDouble((String) config.get(20).get(9));
        }
        if (sicks.contains("情绪不好、")) {
            sum += Double.parseDouble((String) config.get(20).get(10));
        }
        if (sicks.contains("高血压、")) {
            sum += Double.parseDouble((String) config.get(20).get(11));
        }
        if (sicks.contains("糖尿病、")) {
            sum += Double.parseDouble((String) config.get(20).get(12));
        }
        if (sicks.contains("肺癌、")) {
            sum += Double.parseDouble((String) config.get(20).get(13));
        }
        if (sicks.contains("肺癌家族史、")) {
            sum += Double.parseDouble((String) config.get(20).get(14));
        }
        if (sicks.contains("支气管炎、")) {
            sum += Double.parseDouble((String) config.get(20).get(15));
        }
        if (sicks.contains("肺炎、")) {
            sum += Double.parseDouble((String) config.get(20).get(16));
        }
        if (sicks.contains("肺病、")) {
            sum += Double.parseDouble((String) config.get(20).get(17));
        }

        sum += getAgeRisk(entity, "肺癌");
        String level = "";
        level = getRiskLevel(sum, level);
        DecimalFormat dFormat = new DecimalFormat("#.0000");
        entity.setLungRisk(dFormat.format(sum));
        entity.setLungLevel(level);
    }

    private void setRiskAndLevel(String p53Type, String apoeType, ReportEntity entity) {
        setLungRiskAndLevel(p53Type, apoeType, entity);
        setLiverRiskAndLevel(p53Type, apoeType, entity);
        setColoRiskAndLevel(p53Type, apoeType, entity);
        setProsRiskAndLevel(p53Type, apoeType, entity);
        setHyperRiskAndLevel(p53Type, apoeType, entity);

        setAlzhRiskAndLevel(p53Type, apoeType, entity);
        setLscheRiskAndLevel(p53Type, apoeType, entity);
        setCereRiskAndLevel(p53Type, apoeType, entity);
        setInfaRiskAndLevel(p53Type, apoeType, entity);
    }

    private void setInfaRiskAndLevel(String p53Type, String apoeType, ReportEntity entity) {
        String sicks = entity.getSicks() + entity.getFamilySicks();
        double sum = 0;
        if ("正常".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(18).get(4));
        } else if ("杂合突变".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(18).get(5));
        } else if ("纯合突变".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(18).get(6));
        }
        if (sicks.contains("吸烟、")) {
            sum += Double.parseDouble((String) config.get(18).get(7));
        }
        if (sicks.contains("饮酒、")) {
            sum += Double.parseDouble((String) config.get(18).get(8));
        }
        if (sicks.contains("脑梗、")) {
            sum += Double.parseDouble((String) config.get(18).get(9));
        }
        if (sicks.contains("心梗、")) {
            sum += Double.parseDouble((String) config.get(18).get(10));
        }
        if (sicks.contains("高血压、")) {
            sum += Double.parseDouble((String) config.get(18).get(11));
        }
        if (sicks.contains("糖尿病、")) {
            sum += Double.parseDouble((String) config.get(18).get(12));
        }
        if (sicks.contains("高血脂、")) {
            sum += Double.parseDouble((String) config.get(18).get(13));
        }
        if (sicks.contains("冠心病、")) {
            sum += Double.parseDouble((String) config.get(18).get(14));
        }
        if (sicks.contains("肥胖、")) {
            sum += Double.parseDouble((String) config.get(18).get(15));
        }
        if (sicks.contains("脑供血不足、")) {
            sum += Double.parseDouble((String) config.get(18).get(16));
        }
        if (sicks.contains("冠心病家族史、")) {
            sum += Double.parseDouble((String) config.get(18).get(17));
        }

        sum += getAgeRisk(entity, "心梗");
        String level = "";
        level = getRiskLevel(sum, level);
        DecimalFormat dFormat = new DecimalFormat("#.0000");
        entity.setInfaRisk(dFormat.format(sum));
        entity.setInfaLevel(level);
    }

    private void setCereRiskAndLevel(String p53Type, String apoeType, ReportEntity entity) {
        String sicks = entity.getSicks() + entity.getFamilySicks();
        double sum = 0;
        if ("正常".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(16).get(4));
        } else if ("杂合突变".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(16).get(5));
        } else if ("纯合突变".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(16).get(6));
        }
        if (sicks.contains("吸烟、")) {
            sum += Double.parseDouble((String) config.get(16).get(7));
        }
        if (sicks.contains("饮酒、")) {
            sum += Double.parseDouble((String) config.get(16).get(8));
        }
        if (sicks.contains("脑梗、")) {
            sum += Double.parseDouble((String) config.get(16).get(9));
        }
        if (sicks.contains("心梗、")) {
            sum += Double.parseDouble((String) config.get(16).get(10));
        }
        if (sicks.contains("高血压、")) {
            sum += Double.parseDouble((String) config.get(16).get(11));
        }
        if (sicks.contains("糖尿病、")) {
            sum += Double.parseDouble((String) config.get(16).get(12));
        }
        if (sicks.contains("高血脂、")) {
            sum += Double.parseDouble((String) config.get(16).get(13));
        }
        if (sicks.contains("冠心病、")) {
            sum += Double.parseDouble((String) config.get(16).get(14));
        }
        if (sicks.contains("肥胖、")) {
            sum += Double.parseDouble((String) config.get(16).get(15));
        }
        if (sicks.contains("脑供血不足、")) {
            sum += Double.parseDouble((String) config.get(16).get(16));
        }
        if (sicks.contains("嗜油、")) {
            sum += Double.parseDouble((String) config.get(16).get(17));
        }

        sum += getAgeRisk(entity, "脑梗");
        String level = "";
        level = getRiskLevel(sum, level);
        DecimalFormat dFormat = new DecimalFormat("#.0000");
        entity.setCereRisk(dFormat.format(sum));
        entity.setCereLevel(level);
    }

    private void setLscheRiskAndLevel(String p53Type, String apoeType, ReportEntity entity) {
        String sicks = entity.getSicks() + entity.getFamilySicks();
        double sum = 0;
        if ("正常".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(14).get(4));
        } else if ("杂合突变".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(14).get(5));
        } else if ("纯合突变".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(14).get(6));
        }
        if (sicks.contains("吸烟、")) {
            sum += Double.parseDouble((String) config.get(14).get(7));
        }
        if (sicks.contains("饮酒、")) {
            sum += Double.parseDouble((String) config.get(14).get(8));
        }
        if (sicks.contains("脑供血不足、")) {
            sum += Double.parseDouble((String) config.get(14).get(9));
        }
        if (sicks.contains("重体力活、")) {
            sum += Double.parseDouble((String) config.get(14).get(10));
        }
        if (sicks.contains("高血压、")) {
            sum += Double.parseDouble((String) config.get(14).get(11));
        }
        if (sicks.contains("糖尿病、")) {
            sum += Double.parseDouble((String) config.get(14).get(12));
        }
        if (sicks.contains("高血脂、")) {
            sum += Double.parseDouble((String) config.get(14).get(13));
        }
        if (sicks.contains("冠心病、")) {
            sum += Double.parseDouble((String) config.get(14).get(14));
        }
        if (sicks.contains("肥胖、")) {
            sum += Double.parseDouble((String) config.get(14).get(15));
        }
        if (sicks.contains("嗜盐、")) {
            sum += Double.parseDouble((String) config.get(14).get(16));
        }
        if (sicks.contains("高尿酸症、")) {
            sum += Double.parseDouble((String) config.get(14).get(17));
        }
        sum += getAgeRisk(entity, "缺血性脑卒中");
        String level = "";
        level = getRiskLevel(sum, level);
        DecimalFormat dFormat = new DecimalFormat("#.0000");
        entity.setLscheRisk(dFormat.format(sum));
        entity.setLscheLevel(level);
    }

    private void setAlzhRiskAndLevel(String p53Type, String apoeType, ReportEntity entity) {
        String sicks = entity.getSicks() + entity.getFamilySicks();
        double sum = 0;
        if ("正常".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(12).get(4));
        } else if ("杂合突变".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(12).get(5));
        } else if ("纯合突变".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(12).get(6));
        }
        if (sicks.contains("抑郁、")) {
            sum += Double.parseDouble((String) config.get(12).get(7));
        }
        if (sicks.contains("丧偶、")) {
            sum += Double.parseDouble((String) config.get(12).get(8));
        }
        if (sicks.contains("脑外伤、")) {
            sum += Double.parseDouble((String) config.get(12).get(9));
        }
        if (sicks.contains("认知功能障碍、")) {
            sum += Double.parseDouble((String) config.get(12).get(10));
        }
        if (sicks.contains("高血压、")) {
            sum += Double.parseDouble((String) config.get(12).get(11));
        }
        if (sicks.contains("糖尿病、")) {
            sum += Double.parseDouble((String) config.get(12).get(12));
        }
        if (sicks.contains("高血脂、")) {
            sum += Double.parseDouble((String) config.get(12).get(13));
        }
        if (sicks.contains("痴呆家族史、")) {
            sum += Double.parseDouble((String) config.get(12).get(14));
        }
        if (sicks.contains("嗜糖、")) {
            sum += Double.parseDouble((String) config.get(12).get(15));
        }
        if (sicks.contains("嗜油、")) {
            sum += Double.parseDouble((String) config.get(12).get(16));
        }

        sum += getAgeRisk(entity, "阿尔兹海默");
        String level = "";
        level = getRiskLevel(sum, level);
        DecimalFormat dFormat = new DecimalFormat("#.0000");
        entity.setAlzhRisk(dFormat.format(sum));
        entity.setAlzhLevel(level);
    }

    private void setHyperRiskAndLevel(String p53Type, String apoeType, ReportEntity entity) {
        String sicks = entity.getSicks() + entity.getFamilySicks();
        double sum = 0;
        if ("正常".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(10).get(4));
        } else if ("杂合突变".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(10).get(5));
        } else if ("纯合突变".equals(apoeType)) {
            sum = Double.parseDouble((String) config.get(10).get(6));
        }
        if (sicks.contains("吸烟、")) {
            sum += Double.parseDouble((String) config.get(10).get(7));
        }
        if (sicks.contains("饮酒、")) {
            sum += Double.parseDouble((String) config.get(10).get(8));
        }
        if (sicks.contains("肥胖、")) {
            sum += Double.parseDouble((String) config.get(10).get(9));
        }
        if (sicks.contains("荤食为主、")) {
            sum += Double.parseDouble((String) config.get(10).get(10));
        }
        if (sicks.contains("高血压、")) {
            sum += Double.parseDouble((String) config.get(10).get(11));
        }
        if (sicks.contains("糖尿病、")) {
            sum += Double.parseDouble((String) config.get(10).get(12));
        }
        if (sicks.contains("高血脂、")) {
            sum += Double.parseDouble((String) config.get(10).get(13));
        }
        if (sicks.contains("高血压家族史、")) {
            sum += Double.parseDouble((String) config.get(10).get(14));
        }
        if (sicks.contains("嗜糖、")) {
            sum += Double.parseDouble((String) config.get(10).get(15));
        }
        if (sicks.contains("嗜油、")) {
            sum += Double.parseDouble((String) config.get(10).get(16));
        }

        sum += getAgeRisk(entity, "高血压");
        String level = "";
        level = getRiskLevel(sum, level);
        DecimalFormat dFormat = new DecimalFormat("#.0000");
        entity.setHyperRisk(dFormat.format(sum));
        entity.setHypeLevel(level);
    }

    private void setProsRiskAndLevel(String p53Type, String apoeType, ReportEntity entity) {
        String sicks = entity.getSicks() + entity.getFamilySicks();
        double sum = 0;
        if ("正常".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(8).get(4));
        } else if ("杂合突变".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(8).get(5));
        } else if ("纯合突变".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(8).get(6));
        }
        if (sicks.contains("吸烟、")) {
            sum += Double.parseDouble((String) config.get(8).get(7));
        }
        if (sicks.contains("饮酒、")) {
            sum += Double.parseDouble((String) config.get(8).get(8));
        }
        if (sicks.contains("恶性肿瘤家族史、")) {
            sum += Double.parseDouble((String) config.get(8).get(9));
        }
        if (sicks.contains("前列腺炎、")) {
            sum += Double.parseDouble((String) config.get(8).get(10));
        }
        if (sicks.contains("高血压、")) {
            sum += Double.parseDouble((String) config.get(8).get(11));
        }
        if (sicks.contains("糖尿病、")) {
            sum += Double.parseDouble((String) config.get(8).get(12));
        }
        if (sicks.contains("前列腺癌、")) {
            sum += Double.parseDouble((String) config.get(8).get(13));
        }
        if (sicks.contains("前列腺癌家族史、")) {
            sum += Double.parseDouble((String) config.get(8).get(14));
        }
        sum += getAgeRisk(entity, "前列腺癌");
        String level = "";
        level = getRiskLevel(sum, level);
        DecimalFormat dFormat = new DecimalFormat("#.0000");
        entity.setProsRisk(dFormat.format(sum));
        entity.setProsLevel(level);
    }

    private void setColoRiskAndLevel(String p53Type, String apoeType, ReportEntity entity) {
        String sicks = entity.getSicks() + entity.getFamilySicks();
        double sum = 0;
        if ("正常".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(6).get(4));
        } else if ("杂合突变".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(6).get(5));
        } else if ("纯合突变".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(6).get(6));
        }
        if (sicks.contains("吸烟、")) {
            sum += Double.parseDouble((String) config.get(6).get(7));
        }
        if (sicks.contains("饮酒、")) {
            sum += Double.parseDouble((String) config.get(6).get(8));
        }
        if (sicks.contains("消化道溃疡、")) {
            sum += Double.parseDouble((String) config.get(6).get(9));
        }
        if (sicks.contains("直结肠炎、")) {
            sum += Double.parseDouble((String) config.get(6).get(10));
        }
        if (sicks.contains("高血压、")) {
            sum += Double.parseDouble((String) config.get(6).get(11));
        }
        if (sicks.contains("糖尿病、")) {
            sum += Double.parseDouble((String) config.get(6).get(12));
        }
        if (sicks.contains("结直肠癌、")) {
            sum += Double.parseDouble((String) config.get(6).get(13));
        }
        if (sicks.contains("肠息肉史、")) {
            sum += Double.parseDouble((String) config.get(6).get(14));
        }
        if (sicks.contains("喜腌制食物、")) {
            sum += Double.parseDouble((String) config.get(6).get(15));
        }
        if (sicks.contains("嗜盐、")) {
            sum += Double.parseDouble((String) config.get(6).get(16));
        }
        if (sicks.contains("便秘史、")) {
            sum += Double.parseDouble((String) config.get(6).get(17));
        }
        if (sicks.contains("血便史、")) {
            sum += Double.parseDouble((String) config.get(6).get(18));
        }
        if (sicks.contains("恶性肿瘤家族史、")) {
            sum += Double.parseDouble((String) config.get(6).get(19));
        }
        sum += getAgeRisk(entity, "结直肠癌");
        String level = "";
        level = getRiskLevel(sum, level);
        DecimalFormat dFormat = new DecimalFormat("#.0000");
        entity.setColoRisk(dFormat.format(sum));
        entity.setColoLevel(level);
    }

    private String getRiskLevel(double sum, String level) {
        if (sum == 1.0000) {
            level = "正常";
        } else if (sum > 1.0000 && sum <= 1.5000) {
            level = "一般";
        } else if (sum > 1.5000 && sum <= 2.5000) {
            level = "关注";
        } else if (sum > 2.5000 && sum <= 3.5000) {
            level = "特别关注";
        } else if (sum > 3.5000) {
            level = "重视";
        }
        return level;
    }

    private void setLiverRiskAndLevel(String p53Type, String apoeType, ReportEntity entity) {
        String sicks = entity.getSicks() + entity.getFamilySicks();
        double sum = 0;
        if ("正常".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(4).get(4));
        } else if ("杂合突变".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(4).get(5));
        } else if ("纯合突变".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(4).get(6));
        }
        if (sicks.contains("吸烟、")) {
            sum += Double.parseDouble((String) config.get(4).get(7));
        }
        if (sicks.contains("饮酒、")) {
            sum += Double.parseDouble((String) config.get(4).get(8));
        }
        if (sicks.contains("喜鱼生类食物、")) {
            sum += Double.parseDouble((String) config.get(4).get(9));
        }
        if (sicks.contains("乙型肝炎病毒慢性感染、")) {
            sum += Double.parseDouble((String) config.get(4).get(10));
        }
        if (sicks.contains("高血压、")) {
            sum += Double.parseDouble((String) config.get(4).get(11));
        }
        if (sicks.contains("糖尿病、")) {
            sum += Double.parseDouble((String) config.get(4).get(12));
        }
        if (sicks.contains("肺癌、")) {
            sum += Double.parseDouble((String) config.get(4).get(13));
        }
        if (sicks.contains("肺癌家族史、")) {
            sum += Double.parseDouble((String) config.get(4).get(14));
        }
        if (sicks.contains("肝硬化、")) {
            sum += Double.parseDouble((String) config.get(4).get(15));
        }
        if (sicks.contains("脂肪肝、")) {
            sum += Double.parseDouble((String) config.get(4).get(16));
        }
        if (sicks.contains("肝炎、")) {
            sum += Double.parseDouble((String) config.get(4).get(17));
        }
        if (sicks.contains("喜腌制食物、")) {
            sum += Double.parseDouble((String) config.get(4).get(18));
        }
        if (sicks.contains("肝病、")) {
            sum += Double.parseDouble((String) config.get(4).get(19));
        }
        sum += getAgeRisk(entity, "肝癌");
        String level = "";
        level = getRiskLevel(sum, level);
        DecimalFormat dFormat = new DecimalFormat("#.0000");
        entity.setLiverRisk(dFormat.format(sum));
        entity.setLiverLevel(level);
    }

    private void setLungRiskAndLevel(String p53Type, String apoeType, ReportEntity entity) {
        String sicks = entity.getSicks() + entity.getFamilySicks();
        double sum = 0;
        if ("正常".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(2).get(4));
        } else if ("杂合突变".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(2).get(5));
        } else if ("纯合突变".equals(p53Type)) {
            sum = Double.parseDouble((String) config.get(2).get(6));
        }
        if (sicks.contains("吸烟、")) {
            sum += Double.parseDouble((String) config.get(2).get(7));
        }
        if (sicks.contains("饮酒、")) {
            sum += Double.parseDouble((String) config.get(2).get(8));
        }
        if (sicks.contains("肺结核、")) {
            sum += Double.parseDouble((String) config.get(2).get(9));
        }
        if (sicks.contains("情绪不好、")) {
            sum += Double.parseDouble((String) config.get(2).get(10));
        }
        if (sicks.contains("高血压、")) {
            sum += Double.parseDouble((String) config.get(2).get(11));
        }
        if (sicks.contains("糖尿病、")) {
            sum += Double.parseDouble((String) config.get(2).get(12));
        }
        if (sicks.contains("肺癌、")) {
            sum += Double.parseDouble((String) config.get(2).get(13));
        }
        if (sicks.contains("肺癌家族史、")) {
            sum += Double.parseDouble((String) config.get(2).get(14));
        }
        if (sicks.contains("支气管炎、")) {
            sum += Double.parseDouble((String) config.get(2).get(15));
        }
        if (sicks.contains("肺炎、")) {
            sum += Double.parseDouble((String) config.get(2).get(16));
        }
        if (sicks.contains("肺病、")) {
            sum += Double.parseDouble((String) config.get(2).get(17));
        }
        sum += getAgeRisk(entity, "肺癌");
        String level = "";
        level = getRiskLevel(sum, level);
        DecimalFormat dFormat = new DecimalFormat("#.0000");
        entity.setLungRisk(dFormat.format(sum));
        entity.setLungLevel(level);
    }

    private double getAgeRisk(ReportEntity entity, String type) {
        String age = entity.getAge();
        if (StringUtils.isEmpty(age)||Integer.parseInt(age) < 20) {
            return 0;
        }
        if ("男".equals(entity.getSex())) {
            if ("肺癌".equals(type)) {
                return Double.parseDouble((String) config.get(2).get(Integer.parseInt(age)));
            } else if ("肝癌".equals(type)) {
                return Double.parseDouble((String) config.get(4).get(Integer.parseInt(age)));
            } else if ("结直肠癌".equals(type)) {
                return Double.parseDouble((String) config.get(6).get(Integer.parseInt(age)));
            } else if ("前列腺癌".equals(type)) {
                return Double.parseDouble((String) config.get(8).get(Integer.parseInt(age)));
            } else if ("高血压".equals(type)) {
                return Double.parseDouble((String) config.get(10).get(Integer.parseInt(age)));
            } else if ("阿尔兹海默".equals(type)) {
                return Double.parseDouble((String) config.get(12).get(Integer.parseInt(age)));
            } else if ("缺血性脑卒中".equals(type)) {
                return Double.parseDouble((String) config.get(14).get(Integer.parseInt(age)));
            } else if ("脑梗".equals(type)) {
                return Double.parseDouble((String) config.get(16).get(Integer.parseInt(age)));
            } else if ("心梗".equals(type)) {
                return Double.parseDouble((String) config.get(18).get(Integer.parseInt(age)));
            }
        } else if ("女".equals(entity.getSex())) {
            if ("肺癌".equals(type)) {
                return Double.parseDouble((String) config.get(20).get(Integer.parseInt(age)));
            } else if ("肝癌".equals(type)) {
                return Double.parseDouble((String) config.get(22).get(Integer.parseInt(age)));
            } else if ("结直肠癌".equals(type)) {
                return Double.parseDouble((String) config.get(24).get(Integer.parseInt(age)));
            } else if ("卵巢癌".equals(type)) {
                return Double.parseDouble((String) config.get(26).get(Integer.parseInt(age)));
            } else if ("子宫内膜癌".equals(type)) {
                return Double.parseDouble((String) config.get(28).get(Integer.parseInt(age)));
            } else if ("乳腺癌".equals(type)) {
                return Double.parseDouble((String) config.get(30).get(Integer.parseInt(age)));
            } else if ("股骨头坏死".equals(type)) {
                return Double.parseDouble((String) config.get(32).get(Integer.parseInt(age)));
            } else if ("缺血性脑卒中".equals(type)) {
                return Double.parseDouble((String) config.get(34).get(Integer.parseInt(age)));
            } else if ("白内障".equals(type)) {
                return Double.parseDouble((String) config.get(36).get(Integer.parseInt(age)));
            }
        }
        return 0;
    }

    private String getMTHFRType(String apoeRes) {
        if ("A".equals(apoeRes) || "A/A".equals(apoeRes)) {
            return "纯合突变";
        } else if ("A/T".equals(apoeRes) || "T/A".equals(apoeRes)) {
            return "纯合突变";
        } else if ("A/C".equals(apoeRes) || "C/A".equals(apoeRes)) {
            return "杂合突变";
        } else if ("A/G".equals(apoeRes) || "G/A".equals(apoeRes)) {
            return "纯合突变";
        } else if ("T".equals(apoeRes) || "T/T".equals(apoeRes)) {
            return "纯合突变";
        } else if ("T/C".equals(apoeRes) || "C/T".equals(apoeRes)) {
            return "杂合突变";
        } else if ("T/G".equals(apoeRes) || "G/T".equals(apoeRes)) {
            return "纯合突变";
        } else if ("C".equals(apoeRes) || "C/C".equals(apoeRes)) {
            return "正常";
        } else if ("C/G".equals(apoeRes) || "G/C".equals(apoeRes)) {
            return "杂合突变";
        } else if ("G".equals(apoeRes) || "G/G".equals(apoeRes)) {
            return "纯合突变";
        }
        return "";
    }

    private String getApoeType(String apoeRes) {
        if ("A".equals(apoeRes) || "A/A".equals(apoeRes)) {
            return "纯合突变";
        } else if ("A/T".equals(apoeRes) || "T/A".equals(apoeRes)) {
            return "纯合突变";
        } else if ("A/C".equals(apoeRes) || "C/A".equals(apoeRes)) {
            return "杂合突变";
        } else if ("A/G".equals(apoeRes) || "G/A".equals(apoeRes)) {
            return "纯合突变";
        } else if ("T".equals(apoeRes) || "T/T".equals(apoeRes)) {
            return "纯合突变";
        } else if ("T/C".equals(apoeRes) || "C/T".equals(apoeRes)) {
            return "杂合突变";
        } else if ("T/G".equals(apoeRes) || "G/T".equals(apoeRes)) {
            return "纯合突变";
        } else if ("C".equals(apoeRes) || "C/C".equals(apoeRes)) {
            return "正常";
        } else if ("C/G".equals(apoeRes) || "G/C".equals(apoeRes)) {
            return "杂合突变";
        } else if ("G/G".equals(apoeRes) || "G".equals(apoeRes)) {
            return "纯合突变";
        }
        return "";
    }

    private String getP53Type(String p53Res) {
        if ("A".equals(p53Res) || "A/A".equals(p53Res)) {
            return "正常";
        } else if ("A/T".equals(p53Res) || "T/A".equals(p53Res)) {
            return "杂合突变";
        } else if ("A/C".equals(p53Res) || "C/A".equals(p53Res)) {
            return "杂合突变";
        } else if ("A/G".equals(p53Res) || "G/A".equals(p53Res)) {
            return "杂合突变";
        } else if ("T".equals(p53Res) || "T/T".equals(p53Res)) {
            return "纯合突变";
        } else if ("T/C".equals(p53Res) || "C/T".equals(p53Res)) {
            return "纯合突变";
        } else if ("T/G".equals(p53Res) || "G/T".equals(p53Res)) {
            return "纯合突变";
        } else if ("C".equals(p53Res) || "C/C".equals(p53Res)) {
            return "纯合突变";
        } else if ("C/G".equals(p53Res) || "G/C".equals(p53Res)) {
            return "纯合突变";
        } else if ("G".equals(p53Res) || "G/G".equals(p53Res)) {
            return "纯合突变";
        }
        return "";
    }

    public static void main(String[] args) {
        // TODO 自动生成的方法存根
        new CreateReportAction();
    }
}  