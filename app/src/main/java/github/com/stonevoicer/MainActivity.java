package github.com.stonevoicer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.fmod.FMOD;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        FMOD.init(this);
    }
    private void  requestPermission(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);
        }
    }

    public void mFix(View v) {
        //音频路径可以去获取麦克风的数据，我这里写死了
        String path = "file:///android_asset/xiaoxin.wav";
        switch (v.getId()) {
            case R.id.btn_normal:
                VoicerUtil.fix(path, VoicerUtil.MODE_NORMAL);
                break;
            case R.id.btn_luoli:
                VoicerUtil.fix(path, VoicerUtil.MODE_LUOLI);
                break;
            case R.id.btn_dashu:
                VoicerUtil.fix(path, VoicerUtil.MODE_DASHU);
                break;
            case R.id.btn_jingsong:
                VoicerUtil.fix(path, VoicerUtil.MODE_JINGSONG);
                break;
            case R.id.btn_gaoguai:
                VoicerUtil.fix(path, VoicerUtil.MODE_GAOGUAI);
                break;
            case R.id.btn_kongling:
                VoicerUtil.fix(path, VoicerUtil.MODE_KONGLING);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FMOD.close();
    }
}
