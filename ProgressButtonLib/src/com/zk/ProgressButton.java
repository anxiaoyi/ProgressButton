package com.zk;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zk.progressbutton.R;

public class ProgressButton extends View {
	
	private final int DEFAULT_TEXT_SIZE = 40;
	private final int DEFAULT_TEXT_COLOR = Color.WHITE;
	private final int DEFAULT_REACHED_AREA_COLOR = Color.rgb(24, 180, 237);
	private final int DEFAULT_UNREACHED_AREA_COLOR = Color.TRANSPARENT;
	private final int DEFAULT_CORNER_RADIUS = 5;

	private Paint mReachedAreaPaint;
	private Paint mUnReachedAreaPaint;
	private Paint mBorderPaint;
	private Paint mTextPaint;

	private int mReachedAreaColor;
	private int mUnReachedAreaColor;
	private int mBorderColor;
	private int mTextColor;
	private int mUnFinishedPressedColor;
	private int mFinishedPressedColor;

	private RectF mReachedAreaEndRect;
	private RectF mReachedAreaRect;
	private RectF mUnReachedAreaRect;
	private RectF mUnReachedAreaBeginRect;
	private RectF mBorderRect;

	private int cornerRadius;
	private boolean drawText;
	private String text;
	private float textSize;
	private float progressRatio;
	private boolean drawBorder;

	private int mSuggestedMinimumHeight;
	private int mSuggestedMinimumWidth;

	public ProgressButton(Context context) {
		this(context, null);
	}

	public ProgressButton(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.ProgressButtonStyle);
	}

	public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initResources(attrs, defStyleAttr);
	}

	private void initResources(AttributeSet attrs, int defStyleAttr) {
		if(isInEditMode()){
			return;
		}
		
		final TypedArray attributes = getContext().getTheme()
				.obtainStyledAttributes(attrs, R.styleable.ProgressButton,
						defStyleAttr, 0);

		textSize = attributes.getDimension(
				R.styleable.ProgressButton_text_size, DEFAULT_TEXT_SIZE);
		mReachedAreaColor = attributes.getColor(
				R.styleable.ProgressButton_progress_reached_color,
				DEFAULT_REACHED_AREA_COLOR);
		mUnReachedAreaColor = attributes.getColor(
				R.styleable.ProgressButton_progress_unreached_color,
				DEFAULT_UNREACHED_AREA_COLOR);
		mBorderColor = getContext().getResources().getColor(
				android.R.color.darker_gray);
		mTextColor = attributes.getColor(R.styleable.ProgressButton_text_color,
				DEFAULT_TEXT_COLOR);
		mUnFinishedPressedColor = attributes.getColor(
				R.styleable.ProgressButton_progress_unfinished_pressed_color,
				getContext().getResources().getColor(
						android.R.color.darker_gray));
		mFinishedPressedColor = attributes.getColor(
				R.styleable.ProgressButton_progress_finished_pressed_color,
				Color.RED);
		progressRatio = attributes.getFloat(
				R.styleable.ProgressButton_progress_ratio, .0F);
		cornerRadius = (int) attributes.getDimension(R.styleable.ProgressButton_corner_radius, DEFAULT_CORNER_RADIUS);
		text = attributes.getString(R.styleable.ProgressButton_text);
		drawBorder = attributes.getBoolean(R.styleable.ProgressButton_draw_border, true);

		mSuggestedMinimumHeight = 20;
		mSuggestedMinimumWidth = 100;

		mReachedAreaPaint = new Paint();
		mUnReachedAreaPaint = new Paint();
		mBorderPaint = new Paint();
		mTextPaint = new Paint();
		
		mTextPaint.setTextAlign(Align.CENTER);
		mReachedAreaPaint.setStyle(Style.FILL);
		mUnReachedAreaPaint.setStyle(Style.FILL);
		mBorderPaint.setStyle(Style.STROKE);
		mReachedAreaPaint.setAntiAlias(true);
		mUnReachedAreaPaint.setAntiAlias(true);
		mBorderPaint.setAntiAlias(true);

		mReachedAreaRect = new RectF();
		mUnReachedAreaRect = new RectF();
		mBorderRect = new RectF();
		mReachedAreaEndRect = new RectF();
		mUnReachedAreaBeginRect = new RectF();

		setText(text);
		setTextColor(mTextColor);
		setTextSize(textSize);
		setReachedAreaColor(mReachedAreaColor);
		setUnReachedAreaColor(mUnReachedAreaColor);
		setBorderColor(mBorderColor);
		setCornerRadius(cornerRadius);
		setDrawBorder(drawBorder);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		int width;
		int height;
		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else if (widthMode == MeasureSpec.AT_MOST) {
			width = Math.min(widthSize, getSuggestedMinimumWidth());
		} else {
			width = getSuggestedMinimumWidth();
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else if (heightMode == MeasureSpec.AT_MOST) {
			height = Math.min(heightSize, getSuggestedMinimumHeight());
		} else {
			height = getSuggestedMinimumHeight();
		}

		setMeasuredDimension(width, height);
	}

	@Override
	protected int getSuggestedMinimumHeight() {
		return mSuggestedMinimumHeight;
	}

	@Override
	protected int getSuggestedMinimumWidth() {
		return mSuggestedMinimumWidth;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		int width = getWidth();
		int height = getHeight();
		float middle = width * progressRatio;

		// draw border
		if(drawBorder){
			mBorderRect.set(0, 0, width, height);
			canvas.drawRoundRect(mBorderRect, cornerRadius, cornerRadius,
					mBorderPaint);
		}

		// draw reached rect
		mReachedAreaRect.set(0, 0, middle, height);
		if(middle + cornerRadius < width && middle - cornerRadius > cornerRadius / 2){
			mReachedAreaEndRect.set(middle - cornerRadius, 0, middle, height);
			canvas.drawRect(mReachedAreaEndRect, mReachedAreaPaint);
		}
		canvas.drawRoundRect(mReachedAreaRect, cornerRadius, cornerRadius,
				mReachedAreaPaint);

		// draw unreached rect
		mUnReachedAreaRect.set(middle, 0, width, height);
		if(middle + cornerRadius < width){
			mUnReachedAreaBeginRect.set(middle, 0, middle + cornerRadius, height);
			canvas.drawRect(mUnReachedAreaBeginRect, mUnReachedAreaPaint);
		}
		canvas.drawRoundRect(mUnReachedAreaRect, cornerRadius, cornerRadius,
				mUnReachedAreaPaint);

		// draw text
		if (drawText) {
			int xPos = canvas.getWidth() / 2;
			int yPos = (int) ((canvas.getHeight() / 2) - (mTextPaint.descent() + mTextPaint
					.ascent()) / 2);
			canvas.drawText(text, xPos, yPos, mTextPaint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isClickable()) {
			boolean finished = Math.abs(1.F - progressRatio) < 0.000001F;
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (finished) {
					mReachedAreaPaint.setColor(mFinishedPressedColor);
				} else {
					mUnReachedAreaPaint.setColor(mUnFinishedPressedColor);
				}
				invalidate();
				break;

			case MotionEvent.ACTION_UP:
				mReachedAreaPaint.setColor(mReachedAreaColor);
				mUnReachedAreaPaint.setColor(mUnReachedAreaColor);
				invalidate();
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * set progress current ratio
	 * 
	 * @param ratio
	 */
	public void setProgressRation(float ratio) {
		if(ratio < .0F){
			progressRatio = .0F;
		}else if(ratio > 1.0F){
			progressRatio = 1.0F;
		}else{
			this.progressRatio = ratio;
		}
		invalidate();
	}
	
	/**
	 * get progress ratio
	 * @return
	 */
	public float getProgressRation(){
		return this.progressRatio;
	}

	/**
	 * set text size
	 * 
	 * @param textSize
	 */
	public void setTextSize(float textSize) {
		this.textSize = textSize;
		mTextPaint.setTextSize(textSize);
		invalidate();
	}

	/**
	 * set text, use setText(null) to hide text
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.drawText = !TextUtils.isEmpty(text);
		this.text = text;
		invalidate();
	}

	/**
	 * set text color
	 * 
	 * @param color
	 */
	public void setTextColor(int color) {
		this.mTextColor = color;
		mTextPaint.setColor(color);
		invalidate();
	}

	/**
	 * set un finished pressed color
	 * 
	 * @param color
	 */
	public void setUnFinishedPressedColor(int color) {
		if(mUnFinishedPressedColor != color){
			this.mUnFinishedPressedColor = color;
			invalidate();
		}
	}

	/**
	 * set finished pressed color
	 * 
	 * @param color
	 */
	public void setFinishedPressedColor(int color) {
		if(mFinishedPressedColor != color){
			this.mFinishedPressedColor = color;
			invalidate();
		}
	}

	/**
	 * set corner radius, use setCornerRadius(0) to disable radius
	 * 
	 * @param cornerRadius
	 */
	public void setCornerRadius(int cornerRadius) {
		if(this.cornerRadius != cornerRadius){
			this.cornerRadius = (int) dp2px(cornerRadius);
		}
		invalidate();
	}
	
	/**
	 * set reached area color
	 * @param color
	 */
	public void setReachedAreaColor(int color){
		this.mReachedAreaColor = color;
		mReachedAreaPaint.setColor(color);
		invalidate();
	}

	/**
	 * set unreached area color
	 * @param color
	 */
	public void setUnReachedAreaColor(int color){
		this.mUnReachedAreaColor = color;
		mUnReachedAreaPaint.setColor(color);
		invalidate();
	}
	
	/**
	 * set border color
	 * @param color
	 */
	public void setBorderColor(int color){
		mBorderColor = color;
		mBorderPaint.setColor(color);
		invalidate();
	}
	
	/**
	 * draw border
	 * @param draw
	 */
	public void setDrawBorder(boolean draw){
		drawBorder = draw;
		invalidate();
	}
	
	private float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		
		ss.cornerRadius = cornerRadius;
		ss.reachedAreaColor = mReachedAreaColor;
		ss.unReachedAreaColor = mUnReachedAreaColor;
		ss.borderColor = mBorderColor;
		ss.textColor = mTextColor;
		ss.unFinishedPressedColor = mUnFinishedPressedColor;
		ss.finishedPressedColor = mFinishedPressedColor;
		ss.textSize = textSize;
		ss.progressRatio = progressRatio;
		ss.drawText = drawText;
		ss.text = text;
		ss.drawBorder = drawBorder;
		
		return ss;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        
        setCornerRadius(ss.cornerRadius);
        setReachedAreaColor(ss.reachedAreaColor);
        setUnReachedAreaColor(ss.unReachedAreaColor);
        setBorderColor(ss.borderColor);
        setTextColor(ss.textColor);
        setUnFinishedPressedColor(ss.unFinishedPressedColor);
        setFinishedPressedColor(ss.finishedPressedColor);
        setTextSize(ss.textSize);
        setProgressRation(ss.progressRatio);
        setText(ss.text);
	}

	static class SavedState extends BaseSavedState {

		int cornerRadius;
		int reachedAreaColor;
		int unReachedAreaColor;
		int borderColor;
		int textColor;
		int unFinishedPressedColor;
		int finishedPressedColor;
		float textSize;
		float progressRatio;
		boolean drawText;
		String text;
		boolean drawBorder;

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel source) {
			super(source);

			cornerRadius = source.readInt();
			reachedAreaColor = source.readInt();
			unReachedAreaColor = source.readInt();
			borderColor = source.readInt();
			textColor = source.readInt();
			unFinishedPressedColor = source.readInt();
			finishedPressedColor = source.readInt();
			textSize = source.readFloat();
			progressRatio = source.readFloat();
			drawText = (source.readInt() == 1);
			text = source.readString();
			drawBorder = (source.readInt() == 1);
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);

			dest.writeInt(cornerRadius);
			dest.writeInt(reachedAreaColor);
			dest.writeInt(unReachedAreaColor);
			dest.writeInt(borderColor);
			dest.writeInt(textColor);
			dest.writeInt(unFinishedPressedColor);
			dest.writeInt(finishedPressedColor);
			dest.writeFloat(textSize);
			dest.writeFloat(progressRatio);
			dest.writeInt(drawText ? 1 : 0);
			dest.writeString(text);
			dest.writeInt(drawBorder ? 1 : 0);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}
