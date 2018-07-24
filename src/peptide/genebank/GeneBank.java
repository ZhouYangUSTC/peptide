package peptide.genebank;/*
*@Author:Yang Zhou
*@Description:get the main information of the bank
*@Date:Create in 2018.07.18.14:53
*@Others:
* */

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneBank {
    /**:
     * @description:读取文件，并转化为mode类
     * @param: 文件路径
     * @return: model列表
     * @auther: Yang Zhou
     * @date: 2018/7/18 15:04
     */

    public static List<BankModel> readfile(String fileName) throws IOException{
        FileInputStream file = new FileInputStream(fileName);
        BufferedReader in = new BufferedReader(new InputStreamReader(file, "UTF-8"));
        List<BankModel> bankModelList = new ArrayList<>();
        BankModel bankModel;
        String line = in.readLine();
        int linenum = 1;
        while(line !=null){

            if(line.contains("VERSION")&&!hasLowerCase(line))
            {
                bankModel = new BankModel();
                String[] arrLocus = line.split(" ");
                bankModel.setLocus(arrLocus[5]);
                while((line = in.readLine())!=null)
                {


                    if(line.contains("CDS ") && !hasLowerCase(line) && hasDigit(line)) {

                        String location = filter(line);
                                //line.replaceAll("[A-Z\\s]+","");
                        String [] str = location.replaceAll("[A-Z\\s]+","").split("\\.");
                        bankModel.setStart(Integer.valueOf(str[0]));
                        bankModel.setEnd(Integer.valueOf(str[2]));
                    }

                    if(line.contains("/translation")) {
                        StringBuffer CDSbuff = new StringBuffer();
                        CDSbuff.append(line.replaceAll(" ",""));
                        while ((line = in.readLine()) != null) {
                            if (hasDigit(line)||hasLowerCase(line))
                                break;

                            CDSbuff.append(line.replaceAll(" ",""));
                            linenum++;
                            System.out.println(linenum);
                        }



                        bankModel.setCDS(CDSbuff.toString().substring(14,CDSbuff.length()-1));
                    }

                    if(line !=null && line.contains("ORIGIN")){

                        StringBuffer ORbuff = new StringBuffer();

                        while((line = in.readLine()) != null)
                        {
                            if(line.contains("//"))
                                break;
                            ORbuff.append(line.replaceAll(" ",""));
                            linenum++;
                            System.out.println(linenum);
                        }

                        bankModel.setOrigin(ORbuff.toString().replaceAll("\\d+",""));
                        break;
                    }
                    linenum++;
                    System.out.println(linenum);
                }
                if(bankModel.getCDS()!= null)
                {
                    bankModelList.add(bankModel);
                }


            }
            linenum++;
            System.out.println(linenum);
            line = in.readLine();

        }
        in.close();
    return bankModelList;
    }
/**:
 * @description:获取所有文件的文件名
 * @param: 文件路径
 * @return: 文件路径列表
 * @auther: Yang Zhou
 * @date: 2018/7/18 20:45
 */
    public static List<String> getFiles(String filePath) {

        List<String> fileName = new ArrayList<>();
        File file = new File(filePath);
        File[] fs = file.listFiles();
        for (File f : fs) {
            if (!f.isDirectory() && f.getName().contains(".gbff"))
                fileName.add(f.getAbsolutePath());

        }
        return fileName;
    }
    /**:
     * @description:将处理过的GeneBank信息写入文件
     * @param: ModelList,FileNameList
     * @return: null
     * @auther: Yang Zhou
     * @date: 2018/7/18 21:00
     */
    public static void WriteToFile(String filePath) throws IOException{


        List<String> fileList = getFiles(filePath);
        File file = new File("E:\\ProcessedData\\processeddata1.txt");
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fWriter = new FileWriter(file,true);
        BufferedWriter out = new BufferedWriter(fWriter);



        for (int i = 0; i < fileList.size(); i++) {




            List<BankModel> bankModels = readfile(fileList.get(i));
            for (BankModel bankModel : bankModels) {
                    out.write(bankModel.getLocus()+"\t"+ bankModel.getStart()+"\t"+ bankModel.getEnd()+"\t"+ bankModel.getCDS()+"\t"+ bankModel.getOrigin());
                    out.write("\n");
                }


            }
        out.flush();
        out.close();
    }
    /**:
     * @description:判断是否包含数字
     * @param: string
     * @return: boolean
     * @auther: Yang Zhou
     * @date: 2018/7/19 11:13
     */
    public static boolean hasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }

    /**:
     * @description: 判断是否含小写字母
     * @param: String
     * @return: 含有：true，不含：false
     * @auther: Yang Zhou
     * @date: 2018/7/19 12:34
     */
    public static boolean hasLowerCase(String str){
        boolean flag = false;
        Pattern p = Pattern.compile(".*[a-z]+.*");
        Matcher m = p.matcher(str);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }

/**:
 * @description:去掉特殊字符
 * @param: String
 * @return: 去掉后的字符串
 * @auther: Yang Zhou
 * @date: 2018/7/19 12:50
 */
    public static String filter(String str) {
        String regEx = "[`~!@#$%^&*()\\-+={}':;,\\[\\]<>/?￥%（）_+|【】‘；：”“’。，、？\\s]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
    public static void main(String[] agrs) {
        String fileName = "E:\\抗原预测\\表位预测\\RNATEST\\";
        //System.out.print(filter(fileName));

        try{
            WriteToFile(fileName);
        }catch (IOException ex){
            ex.getStackTrace();
        }


    }

}
