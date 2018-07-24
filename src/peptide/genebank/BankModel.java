package peptide.genebank;
/*
*@Author:Yang Zhou
*@Description:The class is the model of peptide
*
*@Date:Create in 2018.07.18.14:22
*@Others:
* */

public class BankModel {
    private String locus;//基因座，基因编号
    private String CDS;//编码区
    private String origin;//原始DNA序列
    private int start;//CDS起始编译的位置
    private int end;   //CDS结束编译的位置
    public BankModel(){}

    public BankModel(String locus,  String cds,String origin,int start,int end){
        this.locus = locus;
        this.CDS = cds;
        this.origin = origin;
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getLocus() {
        return locus;
    }

    public void setLocus(String locus) {
        this.locus = locus;
    }

    public String getCDS() {
        return CDS;
    }

    public void setCDS(String CDS) {
        this.CDS = CDS;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }


}
