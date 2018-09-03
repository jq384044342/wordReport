package www.qige.com.word;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

public class CreateReportAction extends JFrame implements ActionListener{
    JButton jbPerson = new JButton("导入个人信息");
    JButton jbResult = new JButton("导入检测结果");
    JButton jbReport = new JButton("生成报告");
    private Map<Integer, Map<Integer,Object>> personInfo;
    private Map<Integer, Map<Integer,Object>> resultInfo;

    public CreateReportAction(){
        jbPerson.setActionCommand("openPerson");
        jbResult.setActionCommand("openResult");
        jbReport.setActionCommand("createReport");
        jbPerson.setBackground(Color.CYAN);//设置按钮颜色
        jbResult.setBackground(Color.CYAN);//设置按钮颜色
        jbReport.setBackground(Color.CYAN);//设置按钮颜色
        this.getContentPane().add(jbPerson, BorderLayout.NORTH);//建立容器使用边界布局
        this.getContentPane().add(jbResult, BorderLayout.CENTER);//建立容器使用边界布局
        this.getContentPane().add(jbReport, BorderLayout.SOUTH);//建立容器使用边界布局
        //
        jbPerson.addActionListener(this);
        jbResult.addActionListener(this);
        jbReport.addActionListener(this);
        this.setTitle("检测报告生成工具");
        this.setSize(600, 400);
        this.setLocation(200,200);
        //显示窗口true  
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand().equals("openPerson")){
            JFileChooser jf = new JFileChooser();
            jf.showOpenDialog(this);//显示打开的文件对话框  
            File f =  jf.getSelectedFile();//使用文件类获取选择器选择的文件  
            String path = f.getAbsolutePath();//返回路径名
            ReadPersonExcelUtils readExcelUtils = new ReadPersonExcelUtils(path);
           try {
               personInfo = readExcelUtils.readExcelContent();
               JOptionPane.showMessageDialog(this, "成功导入个人信息", "标题",JOptionPane.WARNING_MESSAGE);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "导入个人信息错误", "标题",JOptionPane.WARNING_MESSAGE);
                e1.printStackTrace();
            }
        }else if (e.getActionCommand().equals("openResult")){
            JFileChooser jf = new JFileChooser();
            jf.showOpenDialog(this);//显示打开的文件对话框
            File f =  jf.getSelectedFile();//使用文件类获取选择器选择的文件
            String path = f.getAbsolutePath();//返回路径名
            ReadExcelUtils readExcelUtils = new ReadExcelUtils(path);
            try {
                resultInfo = readExcelUtils.readExcelContent();
                JOptionPane.showMessageDialog(this, "成功导入检测结果", "标题",JOptionPane.WARNING_MESSAGE);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "导入检测结果错误", "标题",JOptionPane.WARNING_MESSAGE);
                e1.printStackTrace();
            }
        }else if (e.getActionCommand().equals("createReport")){
            try {

                JOptionPane.showMessageDialog(this, "成功生成报告", "标题",JOptionPane.WARNING_MESSAGE);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "生成报告错误", "标题",JOptionPane.WARNING_MESSAGE);
                e1.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // TODO 自动生成的方法存根
        new CreateReportAction();
    }
}  