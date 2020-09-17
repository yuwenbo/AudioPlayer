package usage.ywb.personal.audio.view;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import usage.ywb.personal.audio.R;

/**
 * @author frank.yu 2015.06.02
 */
public class LyricView extends View {


    private Paint paint;
    private Paint paintPlaying;
    private int currTime;

    public LyricView(Context context) {
        this(context, null);
    }

    public LyricView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.white));
        paint.setStrokeWidth(2);
        paint.setTextSize(45);
        paint.setTextAlign(Paint.Align.CENTER);
        paintPlaying = new Paint();
        paintPlaying.setColor(getResources().getColor(R.color.blue));
        paintPlaying.setStrokeWidth(3);
        paintPlaying.setTextSize(55);
        paintPlaying.setTextAlign(Paint.Align.CENTER);
    }

    private List<String> texts;
    private List<Integer> times;

    /**
     * @param texts
     *            the texts to set
     */
    public void setTexts(final List<String> texts) {
        this.texts = texts;
    }

    /**
     * @param times
     *            the times to set
     */
    public void setTimes(final List<Integer> times) {
        this.times = times;
    }

    private int lineIndex = 0;

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (texts == null || times == null) {
            return;
        } else if (texts.size() == 0) {
            canvas.drawText("没有找到歌词文件", getWidth() / 2, getHeight() / 2, paint);
        } else if (texts.size() == 1) {
            canvas.drawText("歌词文件解析出错", getWidth() / 2, getHeight() / 2, paint);
        } else {
            for (int i = 0; i < texts.size(); i++) {
                if (i < times.size() - 1) {
                    if (times.get(i) <= currTime && times.get(i + 1) > currTime) {
                        lineIndex = i;
                        canvas.drawText(texts.get(i), getWidth() / 2, getHeight() / 2 + (i - lineIndex) * 90,
                                paintPlaying);
                    } else {
                        canvas.drawText(texts.get(i), getWidth() / 2, getHeight() / 2 + (i - lineIndex) * 90, paint);
                    }
                } else {
                    if (times.get(i) <= currTime) {
                        lineIndex = i;
                        canvas.drawText(texts.get(i), getWidth() / 2, getHeight() / 2 + (i - lineIndex) * 90,
                                paintPlaying);
                    } else {
                        canvas.drawText(texts.get(i), getWidth() / 2, getHeight() / 2 + (i - lineIndex) * 90, paint);
                    }
                }
            }
        }

    }

    public void syncLyric(final int currTime) {
        this.currTime = currTime;
        postInvalidate();
    }

}
