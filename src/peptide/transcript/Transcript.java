package peptide.transcript;/*
*@Author:Yang Zhou
*@Description:
*@Date:Create in 2018.07.2018/7/19 19:10
*@Others:
* */

public class Transcript {
    private String Locus;
    private int mutationSite;
    private String subChar;

    public Transcript(String Locus,int mutationSite,String subChar){
        this.Locus = Locus;
        this.mutationSite = mutationSite;
        this.subChar = subChar;
    }
    public Transcript(){

    }

    public String getLocus() {
        return Locus;
    }

    public void setLocus(String locus) {
        Locus = locus;
    }

    public int getMutationSite() {
        return mutationSite;
    }

    public void setMutationSite(int mutationSite) {
        this.mutationSite = mutationSite;
    }

    public String getSubChar() {
        return subChar;
    }

    public void setSubChar(String subChar) {
        this.subChar = subChar;
    }
}
