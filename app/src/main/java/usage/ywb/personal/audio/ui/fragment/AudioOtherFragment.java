package usage.ywb.personal.audio.ui.fragment;

import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import usage.ywb.personal.audio.R;


/**
 * @author yuwenbo
 * @version [ v1.0.0, 2016/6/24 ]
 */
public class AudioOtherFragment extends Fragment {


    private Equalizer mEqualizer;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null);

        return view;
    }



    public void initEqualizer(MediaPlayer mediaPlayer){
        this.releaseEqualizer();
        mEqualizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
        // 支持的的均衡器引擎数量
        short numberOfBands = mEqualizer.getNumberOfBands();
        // 均衡器频谱最低值
        short minLevelRange = mEqualizer.getBandLevelRange()[0];
        // 均衡器频谱最大值
        short maxLevelRange = mEqualizer.getBandLevelRange()[1];
        Log.i("AudioOtherFragment", "min: "+ maxLevelRange +" && min: " + minLevelRange);
        for(short i=0;i<numberOfBands;i++){
            setEqualizer();

        }

    }

    private void setEqualizer(){


    }



    private void releaseEqualizer(){
        if(mEqualizer != null){
            if(mEqualizer.getEnabled()){
                mEqualizer.setEnabled(false);
            }
            mEqualizer.release();
            mEqualizer = null;
        }
    }


    @Override
    public void onDestroy() {
        this.releaseEqualizer();
        super.onDestroy();
    }

}
