package info.androidhive.pathshala.activity;

/**
 * Created by Bhavik Chandora on 02-05-2016.
 */
public class Info {
    public String coursename;
    public String name;
    public int Roll;
    public int Id;



    public Info(int id,int roll,String crsname, String stdntname){
        coursename=crsname;
        name = stdntname;
        Id=id;
        Roll=roll;


    }
}
