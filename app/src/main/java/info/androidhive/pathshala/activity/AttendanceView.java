package info.androidhive.pathshala.activity;

/**
 * Created by lenovo on 3/24/16.
 */
public class AttendanceView {
    String StudentRoll;
    String StudentName;
    int TCount;
    int PCount;
    int ACount;
    public AttendanceView (String s_roll,String s_name, int tcount,int pcount,int acount)
    {
        StudentRoll=s_roll;
        StudentName=s_name;
        TCount=tcount;
        PCount=pcount;
        ACount=acount;
    }
}
