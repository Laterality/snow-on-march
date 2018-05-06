package latera.kr.snowonmarch.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.icu.util.Measure;
import android.util.AttributeSet;
import android.view.View;

import latera.kr.snowonmarch.R;

public class CircleButtonView extends View {

	private int mColor;

	private Paint mPaint;
	private RectF mArea;

	public CircleButtonView(Context context, AttributeSet attrs) {
		super(context);
		mArea = null;
		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.CircleButtonView,
				0, 0
		);

		try {
			mColor = a.getColor(R.styleable.CircleButtonView_color, 0);
		}
		finally {
			a.recycle();
		}

		init();
	}

	private void init() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(mColor);
	}

	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);

		int radius = Math.min(c.getWidth(), c.getHeight()) / 2;
		if (mArea == null) {
			mArea = new RectF(c.getWidth() / 2 - radius, c.getHeight() / 2 - radius,
					c.getWidth() / 2 + radius, c.getHeight() / 2 + radius);
		}

		c.drawOval(mArea, mPaint);
	}

	public void setColor(int color) {
		this.mColor = color;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(mColor);
	}

}
