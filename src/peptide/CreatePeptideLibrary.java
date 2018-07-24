package peptide;/*
*@Author:Yang Zhou
*@Description: 生成预测肽库
*@Date:Create in 2018.07.2018/7/20 14:43
*@Others:
* */


import peptide.genebank.BankModel;
import peptide.genebank.GeneBank;
import peptide.sqloperate.SqlOperate;
import peptide.transcript.Transcript;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;




/**:
 * @description: 获取到生成所有的测试肽
 * @param:
 * @return:
 * @auther: Yang Zhou
 * @date: 2018/7/22 14:55
 */

public class CreatePeptideLibrary {


    public static void main(String[] sgrs){
       try
       {
           peptideLibary();
       }catch(Exception ex){
           System.out.println(ex.getMessage());
       }
    }

    /**:
     * @description:获取到生成所有的测试肽的集合
     * @param:
     * @return:
     * @auther: Yang Zhou
     * @date: 2018/7/22 14:55
     */
    public static List<PeptideGroup> peptideLibary() throws SQLException,ClassNotFoundException{
        SqlOperate sqlOperate = new SqlOperate();
        List<Transcript> trsList = sqlOperate.queryTranscript("select * from transcript");

        BankModel bank = null;
        PeptideGroup group = null;
        List<PeptideGroup> list = new ArrayList<>();
        sqlOperate.connet();
        for (int i = 0; i < trsList.size(); i++) {


            System.out.println(i);//测试

            bank = sqlOperate.queryBankModel("select * from processeddata where locus='"+trsList.get(i).getLocus()+"'");
            if(bank != null){
                group = alteredPeptideGroup(bank,trsList.get(i));
            }

            if(group !=null){
                list.add(group);
            }

        }
        sqlOperate.conClose();//为何拿到循环外关闭？
        sqlOperate.rs.close();
       return list;
    }



/**:
 * @description:获取转录本的突变信息，将基因bank中的相应位置替换掉，生成待测试的肽库
 * @param: genebank，transcript
 * @return: peptidegroup
 * @auther: Yang Zhou
 * @date: 2018/7/22 10:34
 */
    public static PeptideGroup alteredPeptideGroup(BankModel bank, Transcript trs){
        int location = trs.getMutationSite();
        PeptideGroup group = null;
        if(!trs.getLocus().trim().equals(trs.getLocus().trim()))
        {
            return null;
        }

        if(location<bank.getStart()||location>bank.getEnd()-3)//减去3，是因为突变发生在末尾，及终止子缺失。这里不予考虑
        {
            return null;
        }
        int cdsLocation = getAlterAcidSite(bank.getStart(),trs.getMutationSite());
        String alterDNA = alteredDNA(bank,trs);
        char acid = getalterAcid(alterDNA);
        if(acid != ' '){
            String alteredCDS = replacesChar(bank.getCDS(),acid,cdsLocation);
            List<String> peptideList = getpeptideGroup(alteredCDS,cdsLocation);
            if(peptideList != null){
                group = new PeptideGroup(trs.getLocus(),peptideList);
            }

        }
        return group;




    }
    /**:
     * @description:获取突变后的DNA
     * @param:
     * @return:
     * @auther: Yang Zhou
     * @date: 2018/7/22 14:43
     */
    public static String alteredDNA(BankModel bank, Transcript trs){
        int location = trs.getMutationSite();
        int k = getAlterDNAsite(bank.getStart(),trs.getMutationSite());
        char[] dnaChar = new char[3];
        switch (k){
            case 0:
                    dnaChar[0] = trs.getSubChar().trim().charAt(0);
                    dnaChar[1] = bank.getOrigin().trim().charAt(location); //注意：location记录位置是从1开始，而origin字符串编码从0开始，截取时要注意相对位置的变化。
                    dnaChar[2] = bank.getOrigin().trim().charAt(location+1);

                break;
            case 1:
                    dnaChar[0] = bank.getOrigin().trim().charAt(location-2);
                    dnaChar[1] = trs.getSubChar().trim().charAt(0); //注意：location记录位置是从1开始，而origin字符串编码从0开始，截取时要注意相对位置的变化。
                    dnaChar[2] = bank.getOrigin().trim().charAt(location);

                break;
            case 2:
                    dnaChar[0] = bank.getOrigin().trim().charAt(location-3);//-2-1
                    dnaChar[1] = bank.getOrigin().trim().charAt(location-2); //注意：location记录位置是从1开始，而origin字符串编码从0开始，截取时要注意相对位置的变化。
                    dnaChar[2] = trs.getSubChar().trim().charAt(0);

                break;
        }
        return new String(dnaChar);
    }
    /**:
     * @description: 获取突变DNA的位置
     * @param:
     * @return:
     * @auther: Yang Zhou
     * @date: 2018/7/22 14:44
     */

    public static int getAlterDNAsite(int startSite,int location){
        return ((location-startSite)%3);
    }
    /**:
     * @description:获取突变的氨基酸位置
     * @param:
     * @return:
     * @auther: Yang Zhou
     * @date: 2018/7/22 14:45
     */
    public static int getAlterAcidSite(int startSite,int location){
        return ((location-startSite)/3);
    }
/**:
 * @description:将突变的DNA转换为相应的氨基酸
 * @param:
 * @return:
 * @auther: Yang Zhou
 * @date: 2018/7/22 14:45
 */
    public static char getalterAcid(String dna){
        CodonTable codonTable = new CodonTable();
        if(codonTable.map.containsKey(dna)){
            char acid = codonTable.map.get(dna).trim().charAt(0);
            return acid;

        }
        return ' ';
    }

    /**:
     * @description:替换掉相应位置的字符
     * @param:
     * @return:
     * @auther: Yang Zhou
     * @date: 2018/7/22 12:23
     */
    public static String replacesChar(String str,char c,int position) throws StringIndexOutOfBoundsException{
        StringBuilder sb = new StringBuilder(str);
        if (str != null) {
            sb.setCharAt(position, c);
        }
        return sb.toString();
    }
    /**:
     * @description: 根据替换掉的cds生成9个位置上的peptide
     * @param:
     * @return:
     * @auther: Yang Zhou
     * @date: 2018/7/22 13:08
     */
    public static List<String> getpeptideGroup(String alteredCDS,int location){
        List<String> list = new ArrayList<>();
        int strLenth = alteredCDS.length();
        if(location<0||location>=strLenth||strLenth<9){
            return null;
        }
        else if(location-8<=0 && location+8>=strLenth){
            for (int i = 0; i < strLenth-8; i++) {
                String str = alteredCDS.substring(i,i+9);
                list.add(str);
            }
        }
        else if(location-8<=0 && location+8<strLenth){
            for (int i = 0; i <= location; i++) {
                String str = alteredCDS.substring(i,i+9);
                list.add(str);
            }
        }
        else if(location-8>=0 && location+8>strLenth){
            for (int i = location; i < strLenth; i++) {
                String str = alteredCDS.substring(i-8,i+1);
                list.add(str);
            }
        }
        else if(location-8>=0&&location+8<strLenth){
            for (int i = location; i < location+9; i++) {
                String str = alteredCDS.substring(i-8,i+1);
                list.add(str);
            }
        }
        else if(location == strLenth-8){
            return null;
        }
        return list;
    }

}
