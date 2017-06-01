package github.com.stonevoicer;

/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2017/5/31
 * Version  1.0
 * Description:
 */

public class VoicerUtil {


    public static final int MODE_NORMAL=0;
    public static final int MODE_LUOLI=1;
    public static final int MODE_DASHU=2;
    public static final int MODE_JINGSONG=3;
    public static final int MODE_GAOGUAI=4;
    public static final int MODE_KONGLING=5;
    /**
     *
     * @param path
     * @param type
     */
    public native static void fix(String path,int type);
    static{
        System.loadLibrary("fmod");
        System.loadLibrary("fmodL");
        System.loadLibrary("stonevoicer");
    }
}
