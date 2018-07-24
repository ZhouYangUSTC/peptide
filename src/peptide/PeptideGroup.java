package peptide;/*
*@Author:Yang Zhou
*@Description: 最后生成的九个长度多肽
*@Date:Create in 2018.07.2018/7/20 21:01
*@Others:
* */

import java.util.List;

public class PeptideGroup {
    private String locus;
    private List<String> peptideList;

    public PeptideGroup(){}

    public PeptideGroup(String locus,List<String> list){
        this.locus = locus;
        this.peptideList = list;
    }

    public String getLocus() {
        return locus;
    }

    public void setLocus(String locus) {
        this.locus = locus;
    }

    public List<String> getPeptideList() {
        return peptideList;
    }

    public void setPeptideList(List<String> peptideList) {
        this.peptideList = peptideList;
    }
}
