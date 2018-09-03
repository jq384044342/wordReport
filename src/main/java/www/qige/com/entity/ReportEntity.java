package www.qige.com.entity;

import org.apache.poi.ss.usermodel.PictureData;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/27 0027.
 */
public class ReportEntity implements Serializable {
    private static final long serialVersionUID = -3286175499784576165L;
    private String name;
    private String sex;
    private String sicks;
    private String p53Res;
    private String p53Type;
    private String apoeRes;
    private String apoeType;
    private String lungRisk;
    private String lungLevel;
    private String liverRisk;
    private String liverLevel;
    private String coloRisk;
    private String coloLevel;
    private String prosRisk;
    private String prosLevel;
    private String hyperRisk;
    private String hypeLevel;
    private String alzhRisk;
    private String alzhLevel;
    private String lscheRisk;
    private String lscheLevel;
    private String cereRisk;
    private String cereLevel;
    private String infaRisk;
    private String infaLevel;
    private PictureData p53Pic1;
    private PictureData aopePic1;
    private String resOnes;
    private String helthManage;
    private String helthManageContent;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSicks() {
        return sicks;
    }

    public void setSicks(String sicks) {
        this.sicks = sicks;
    }

    public String getP53Res() {
        return p53Res;
    }

    public void setP53Res(String p53Res) {
        this.p53Res = p53Res;
    }

    public String getP53Type() {
        return p53Type;
    }

    public void setP53Type(String p53Type) {
        this.p53Type = p53Type;
    }

    public String getApoeRes() {
        return apoeRes;
    }

    public void setApoeRes(String apoeRes) {
        this.apoeRes = apoeRes;
    }

    public String getApoeType() {
        return apoeType;
    }

    public void setApoeType(String apoeType) {
        this.apoeType = apoeType;
    }

    public String getLungRisk() {
        return lungRisk;
    }

    public void setLungRisk(String lungRisk) {
        this.lungRisk = lungRisk;
    }

    public String getLungLevel() {
        return lungLevel;
    }

    public void setLungLevel(String lungLevel) {
        this.lungLevel = lungLevel;
    }

    public String getLiverRisk() {
        return liverRisk;
    }

    public void setLiverRisk(String liverRisk) {
        this.liverRisk = liverRisk;
    }

    public String getLiverLevel() {
        return liverLevel;
    }

    public void setLiverLevel(String liverLevel) {
        this.liverLevel = liverLevel;
    }

    public String getColoRisk() {
        return coloRisk;
    }

    public void setColoRisk(String coloRisk) {
        this.coloRisk = coloRisk;
    }

    public String getColoLevel() {
        return coloLevel;
    }

    public void setColoLevel(String coloLevel) {
        this.coloLevel = coloLevel;
    }

    public String getProsRisk() {
        return prosRisk;
    }

    public void setProsRisk(String prosRisk) {
        this.prosRisk = prosRisk;
    }

    public String getProsLevel() {
        return prosLevel;
    }

    public void setProsLevel(String prosLevel) {
        this.prosLevel = prosLevel;
    }

    public String getHyperRisk() {
        return hyperRisk;
    }

    public void setHyperRisk(String hyperRisk) {
        this.hyperRisk = hyperRisk;
    }

    public String getHypeLevel() {
        return hypeLevel;
    }

    public void setHypeLevel(String hypeLevel) {
        this.hypeLevel = hypeLevel;
    }

    public String getAlzhRisk() {
        return alzhRisk;
    }

    public void setAlzhRisk(String alzhRisk) {
        this.alzhRisk = alzhRisk;
    }

    public String getAlzhLevel() {
        return alzhLevel;
    }

    public void setAlzhLevel(String alzhLevel) {
        this.alzhLevel = alzhLevel;
    }

    public String getLscheRisk() {
        return lscheRisk;
    }

    public void setLscheRisk(String lscheRisk) {
        this.lscheRisk = lscheRisk;
    }

    public String getLscheLevel() {
        return lscheLevel;
    }

    public void setLscheLevel(String lscheLevel) {
        this.lscheLevel = lscheLevel;
    }

    public String getCereRisk() {
        return cereRisk;
    }

    public void setCereRisk(String cereRisk) {
        this.cereRisk = cereRisk;
    }

    public String getCereLevel() {
        return cereLevel;
    }

    public void setCereLevel(String cereLevel) {
        this.cereLevel = cereLevel;
    }

    public String getInfaRisk() {
        return infaRisk;
    }

    public void setInfaRisk(String infaRisk) {
        this.infaRisk = infaRisk;
    }

    public String getInfaLevel() {
        return infaLevel;
    }

    public void setInfaLevel(String infaLevel) {
        this.infaLevel = infaLevel;
    }

    public PictureData getP53Pic1() {
        return p53Pic1;
    }

    public void setP53Pic1(PictureData p53Pic1) {
        this.p53Pic1 = p53Pic1;
    }

    public PictureData getAopePic1() {
        return aopePic1;
    }

    public void setAopePic1(PictureData aopePic1) {
        this.aopePic1 = aopePic1;
    }

    public String getResOnes() {
        return resOnes;
    }

    public void setResOnes(String resOnes) {
        this.resOnes = resOnes;
    }

    public String getHelthManage() {
        return helthManage;
    }

    public void setHelthManage(String helthManage) {
        this.helthManage = helthManage;
    }

    public String getHelthManageContent() {
        return helthManageContent;
    }

    public void setHelthManageContent(String helthManageContent) {
        this.helthManageContent = helthManageContent;
    }
}
