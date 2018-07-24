package peptide;
/*
*@Author:Yang Zhou
*@Description:  生成密码子表
*@Date:Create in 2018.07.2018/7/20 10:15
*@Others:
* */

import java.util.HashMap;
import java.util.Map;

public class CodonTable {
    final private static String[] codonTable = {
                                            "G GGU,GGC,GGA,GGG",
                                            "A GCU,GCC,GCA,GCG",
                                            "V GUU,GUC,GUA,GUG",
                                            "L CUU,CUC,CUA,CUG,UUA,UUG",
                                            "I AUU,AUC,AUA",
                                            "P CCU,CCA,CCG,CCC",
                                            "F UUU,UUC",
                                            "Y UAU,UAC",
                                            "W UGG",
                                            "S UCU,UCA,UCC,UCG,AGU,AGC",
                                            "T ACU,ACC,ACG,ACA",
                                            "C UGU,UGC",
                                            "M AUG ",
                                            "N AAU,AAC ",
                                            "Q CAA,CAG " ,
                                            "D GAU,GAC ",
                                            "E GAA,GAG ",
                                            "K AAA,AAG",
                                            "R CGU,CGC,CGG,CGA,AGA,AGG",
                                            "H CAU,CAC "};

    public Map<String,String> map;

    public CodonTable(){
        codonTable(codonTable);
    }

    public void codonTable(String[] table){

        map = new HashMap<>();
        for (int i = 0; i < table.length; i++) {
            String[] separate = table[i].split(" ");
            String[] codonArry = separate[1].split(",");
            for (int j = 0; j < codonArry.length; j++) {
                String codon = codonArry[j].replace('U','T').toLowerCase().trim();
                map.put(codon,separate[0].trim());
            }


        }


    }
    public static void main(String[] agrs){
        Map<String,String> map = new CodonTable().map;
    }
}

