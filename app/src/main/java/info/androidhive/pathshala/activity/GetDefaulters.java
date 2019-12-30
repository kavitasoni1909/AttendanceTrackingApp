package info.androidhive.pathshala.activity;

/**
 * Created by lenovo on 3/24/16.
 */
public class GetDefaulters {
    public String Roll;
    public int Id;
    public String Name;
    public String Percentage;
    public GetDefaulters(int id,String s_roll, String s_name, String per){

        Id = id;
        Roll=s_roll;
        Name=s_name;
        Percentage=per;

    }
}
