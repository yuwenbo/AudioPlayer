package usage.ywb.personal.audio.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import usage.ywb.personal.audio.R;
import usage.ywb.personal.audio.entity.AudioEntity;
import usage.ywb.personal.audio.dao.LyricProvider;
import usage.ywb.personal.audio.ui.adapter.LyricAdapter;
import usage.ywb.personal.audio.view.VisualizerView;

import java.util.List;

/**
 * @author yuwenbo
 * @version [ v1.0.0, 2016/6/24 ]
 */
public class AudioLyricFragment extends Fragment {

    private LyricProvider lyric;
    private Visualizer visualizer;
    private VisualizerView visualizerView;

    private ListView lyricListView;
    private LyricAdapter adapter;

    private List<String> texts;
    private List<Integer> times;
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lyric = new LyricProvider();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_lyric, null);
        visualizerView = (VisualizerView) view.findViewById(R.id.vv_lyric_visualizer);
        lyricListView = (ListView) view.findViewById(R.id.lv_lyric);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            Log.i("MainActivity","申请录音权限-------Fragment");
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                _initVisualizer();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("AudioLyricFragment", "onResume");
        setVisualizerEnable(true);
    }

    public void syncLyric(int currTime){
        int lineIndex = 0;
        if(times == null || texts == null){
            return;
        }
        for (int i = 0; i < texts.size(); i++) {
            if (i < times.size() - 1) {
                if (times.get(i) <= currTime && times.get(i + 1) > currTime) {
                    lineIndex = i;
                }
            } else {
                if (times.get(i) <= currTime) {
                    lineIndex = i;
                }
            }
        }
        adapter.setPosition(lineIndex);
        lyricListView.smoothScrollToPositionFromTop(lineIndex, lyricListView.getHeight()/2);
    }

    public void setVisualizerEnable(boolean enable){
        if(visualizer != null){
            visualizer.setEnabled(enable);
        }
    }

    /**
     * 初始化歌词文件
     * @param entity
     */
    public void initLyric(AudioEntity entity) {
        final String name = entity.getName().substring(0, entity.getName().lastIndexOf(".")) + ".lrc";
        Log.i("ywb", "NAME: " + name);
        lyric.initLyricFile(name);
        texts = lyric.getTexts();
        times = lyric.getTimes();
        if(adapter == null){
            adapter = new LyricAdapter(getActivity(), texts);
            lyricListView.setAdapter(adapter);
        }else{
            adapter.reset(texts);
        }
    }


    private void _initVisualizer(){
        this.releaseVisualizer();
        visualizer = new Visualizer(mediaPlayer.getAudioSessionId());
        // 设置每次捕获频谱的大小，音乐在播放中的时候采集的数据的大小或者说是采集的精度吧，而且getCaptureSizeRange()所返回的数组里面就两个值
        // 数组[0]是最小值（128），数组[1]是最大值（1024）。
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[0]);
        // 设置一个监听器来监听不断而来的所采集的数据。一共有4个参数，第一个是监听者，第二个单位是毫赫兹，表示的是采集的频率，第三个是是否采集波形，第四个是是否采集频率
        visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            // 回调采集的是波形数据
            @Override
            public void onWaveFormDataCapture(final Visualizer visualizer, final byte[] waveform, final int samplingRate) {
                // visualizerView 是一个自定义的view用来按照波形来画图
                Log.i("", "----------------onWaveFormDataCapture-----");
                visualizerView.updateVisualizer(waveform);
            }
            // 回调采集的是快速傅里叶变换有关的数据
            @Override
            public void onFftDataCapture(final Visualizer visualizer, final byte[] fft, final int samplingRate) {
                Log.i("", "------onFftDataCapture--------------------");
            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false);
    }

    public void initVisualizer( MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 2);
            }
        }else{
            _initVisualizer();
        }
    }

    /**
     * 释放频谱资源
     */
    private void releaseVisualizer(){
        if(visualizer != null){
            if(visualizer.getEnabled()){
                visualizer.setEnabled(false);
            }
            visualizer.release();
            visualizer = null;
        }
    }

    @Override
    public void onPause() {
        setVisualizerEnable(false);
        Log.i("AudioLyricFragment", "onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        this.releaseVisualizer();
        super.onDestroy();
    }


}
