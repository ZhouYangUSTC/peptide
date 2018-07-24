package peptide.transcript;
/*
*@Author:Yang Zhou
*@Description:
*@Date:Create in 2018.07.2018/7/19 18:01
*@Others:
* */

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MutationInformation {
    /**:
     * @description:获取Transcrip列表集合
     * @param: filepath
     * @return: List
     * @auther: Yang Zhou
     * @date: 2018/7/19 19:23
     */
    public static List<Transcript> readTranscriptFromFile(String fileName) throws IOException{
        FileInputStream file = new FileInputStream(fileName);
        BufferedReader in = new BufferedReader(new InputStreamReader(file, "UTF-8"));
        List<Transcript> scriptList= new ArrayList<>();
        ;
        String line = in.readLine();
        while((line=in.readLine()) != null){
            String[] devide = line.split(",");
            if((!devide[3].equals("") || devide[3] !=null) && devide[3].contains(":")) {
                Transcript transcript = separator(devide[3].trim());
                if(transcript!=null)
                    scriptList.add(transcript);
            }

        }

        return scriptList;
    }
/**:
 * @description:将转录信息分割成基因座、突变位点、突变字符，并返回一个Transcript对象
 * @param: String 转录信息
 * @return: transcript对象
 * @auther: Yang Zhou
 * @date: 2018/7/19 19:40
 */
    public static Transcript separator(String tranString){
        if(tranString.equals("")|| tranString == null)
            return null;
        String[] str = tranString.split("\\:|\\/");
        String locus = str[0];
        int mutationSite = getMutationSite(str[str.length-1]);
        int location = str[str.length-1].length()-1;
        String subChar = String.valueOf(str[str.length-1].charAt(location)).toLowerCase(); //获取突变字符，在最后一位
        return new Transcript(locus,mutationSite,subChar);
    }

/**:
 * @description: 获取突变位点，由于string中含有“+”、“-”号，分开判断
 * @param:
 * @return:
 * @auther: Yang Zhou
 * @date: 2018/7/19 20:46
 */
    public static int getMutationSite(String string){


        if(string.contains("+")){
            String[] strArry = string.split("\\+");
            return (extractDigit(strArry[0])+extractDigit(strArry[1]));
         }
         else if (string.contains("-")){
            String[] strArry = string.split("\\-");
            return (extractDigit(strArry[0])+extractDigit(strArry[1]));
        }

        return extractDigit(string);

    }

/**:
 * @description: 获取字符串中的数字
 * @param:
 * @return:
 * @auther: Yang Zhou
 * @date: 2018/7/19 20:07
 */
    public static int extractDigit(String string){
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(string);
        if(m.find()){
            return Integer.valueOf(m.group(0));
        }
        return 0;
    }

/**:
 * @description: 写入文件
 * @param: filepath
 * @return:
 * @auther: Yang Zhou
 * @date: 2018/7/19 22:05
 */
public static void writeToFile(String fileName) throws IOException{
    FileWriter fWriter = new FileWriter("E:\\ProcessedData\\Transcript.txt");
    BufferedWriter out = new BufferedWriter(fWriter);



    List<Transcript> trans= readTranscriptFromFile(fileName);
    for (Transcript tran:trans) {
        out.write(tran.getLocus()+"\t"+tran.getMutationSite()+"\t"+tran.getSubChar());
        out.write("\n");
    }

    out.flush();
    out.close();
}
/**: 
 * @description:获取转换过的transcript
 * @param: 转换过的文件路径
 * @return: 
 * @auther: Yang Zhou
 * @date: 2018/7/20 14:39
 */
    public static List<Transcript> readFromFile(String fileName) throws IOException{
        FileInputStream file = new FileInputStream(fileName);
        BufferedReader in = new BufferedReader(new InputStreamReader(file,"UTF-8"));
        List<Transcript> list = new ArrayList<>();
        Transcript transcript = null;
        String line = in.readLine();
        while(line !=null || !line.equals("")){
            String[] str = line.split("\t");
            transcript = new Transcript(str[0],Integer.valueOf(str[1]),str[2].trim());
            list.add(transcript);
            line = in.readLine();
        }
        return list;
        
    }

    public static void main(String[] agrs){
        String fileName = "E:\\抗原预测\\表位预测\\RNATEST\\HNFFCDNAA-VS-A375DNAA.snv.annot.csv";
        String filePath = "E:\\ProcessedData\\Transcript.txt";
        try{
            writeToFile(fileName);
            //readFromFile(filePath);
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }

    }
}
