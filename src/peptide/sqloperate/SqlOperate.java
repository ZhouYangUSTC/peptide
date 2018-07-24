package peptide.sqloperate;/*
*@Author:Yang Zhou
*@Description: 数据库相关操作
*@Date:Create in 2018.07.2018/7/20 18:59
*@Others:
* */

import peptide.PeptideGroup;
import peptide.genebank.BankModel;
import peptide.transcript.Transcript;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static peptide.CreatePeptideLibrary.alteredPeptideGroup;

public class SqlOperate {
    String URL="jdbc:mysql://localhost:3306/peptidetest";
    String NAME="root";
    String PASSWORD="";
    public  Connection connection = null; // 数据库连接
    public  Statement statement = null; // 待查询语句描述对象
    public  ResultSet rs = null;


    public void connet() throws ClassNotFoundException,SQLException {
        //1.加载驱动程序
        Class.forName("com.mysql.jdbc.Driver");
        //2.获得数据库的连接
        connection = DriverManager.getConnection(URL, NAME, PASSWORD);



    }

    public  void conClose ()throws SQLException {

            this.connection.close();
            this.statement.close();

    }

    /**
     *
     * @方法名称: query ；
     * @方法描述: 查询操作Transcript ；
     * @参数 ：@param sql：查询操作语句 ；
     * @返回类型: list of transcript
     * @创建人：
     * @创建时间：；
     * @throws
     */
    public  List<Transcript> queryTranscript(String sql) throws SQLException, ClassNotFoundException {
        connet();

        statement = connection.createStatement(); // 准备执行语句
        rs  = statement.executeQuery(sql);
        List<Transcript> list = new ArrayList<>();
        Transcript trans = null;
        while (rs.next()){
            trans = new Transcript();
            trans.setLocus(rs.getString(2));
            trans.setMutationSite(rs.getInt(3));
            trans.setSubChar(rs.getString(4));
            list.add(trans);

        }
        conClose();
        rs.close();

        return list;

    }
    /**:
     * @description:从数据库中获取bankmodel
     * @param:
     * @return:
     * @auther: Yang Zhou
     * @date: 2018/7/20 20:27
     */
    public BankModel queryBankModel(String sql) throws SQLException, ClassNotFoundException {


        statement = connection.createStatement(); // 准备执行语句
        rs  = statement.executeQuery(sql);
        BankModel   bankModel = null;
        if(rs.next()){
            bankModel = new BankModel(rs.getString(2),rs.getString(5),rs.getString(6),rs
            .getInt(3),rs.getInt(4));

        }

        return bankModel;
    }
    /*public static void main(String[] agrs){
        SqlOperate sqlOperate = new SqlOperate();
        try{

        List<Transcript> trsList = sqlOperate.queryTranscript("select * from transcript");

        BankModel bank = null;
        PeptideGroup group = null;
        List<PeptideGroup> list = new ArrayList<>();
        for (int i = 0; i < trsList.size(); i++) {
            bank = sqlOperate.queryBankModel("select * from processeddata where locus='"+trsList.get(i).getLocus()+"'");
            group = alteredPeptideGroup(bank,trsList.get(i));
            if(group !=null){
                list.add(group);
            }

        }

        }catch (Exception ex){
            ex.getStackTrace();
            System.out.println(ex.getMessage());
        }
    }*/
}
