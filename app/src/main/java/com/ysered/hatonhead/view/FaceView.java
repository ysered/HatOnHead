package com.ysered.hatonhead.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;
import com.ysered.hatonhead.R;

/**
 * View which displays a bitmap containing a face along with overlay graphics that identify the
 * locations of detected facial landmarks.
 */
public class FaceView extends View {

    private Bitmap hatBitmap;
    private Bitmap bitmap;
    private SparseArray<Face> faces;
    private boolean isShowAnnotations = false;

    public FaceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * Sets the bitmap background and the associated face detections.
     */
    public void setContent(Bitmap bitmap, SparseArray<Face> faces, boolean isShowAnnotations) {
        this.bitmap = bitmap;
        this.faces = faces;
        this.isShowAnnotations = isShowAnnotations;
        invalidate();
    }

    /**
     * Draws the bitmap background and the associated face landmarks.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((bitmap != null) && (faces != null)) {
            double scale = drawBitmap(canvas);
            if (isShowAnnotations) {
                drawFaceAnnotations(canvas, scale);
            }
        }
    }

    private void init(AttributeSet attrs) {
        final TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.FaceView);
        final int hatImageRes = array.getResourceId(R.styleable.FaceView_hat_image_resource, R.drawable.hat);
        hatBitmap = BitmapFactory.decodeResource(getContext().getResources(), hatImageRes);
        array.recycle();
    }

    /**
     * Draws the bitmap background, scaled to the device size.  Returns the scale for future use in
     * positioning the facial landmark graphics.
     */
    private double drawBitmap(Canvas canvas) {
        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = bitmap.getWidth();
        double imageHeight = bitmap.getHeight();
        double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);

        Rect destBounds = new Rect(0, 0, (int) (imageWidth * scale), (int) (imageHeight * scale));
        canvas.drawBitmap(bitmap, null, destBounds, null);
        return scale;
    }

    /**
     * Draws a small circle for each detected landmark, centered at the detected landmark position.
     * <p>
     * <p>
     * Note that eye landmarks are defined to be the midpoint between the detected eye corner
     * positions, which tends to place the eye landmarks at the lower eyelid rather than at the
     * pupil position.
     */
    private void drawFaceAnnotations(Canvas canvas, double scale) {
        final Paint circlePaint = new Paint();
        circlePaint.setColor(Color.GREEN);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(5);

        final Paint rectPaint = new Paint();
        rectPaint.setColor(Color.RED);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeCap(Paint.Cap.ROUND);
        rectPaint.setStrokeWidth(5);

        // assume that only one face on the photo
        if (faces.size() > 0) {
            final Face face = faces.valueAt(0);
            final int faceX1 = (int) (face.getPosition().x * scale);
            final int faceY1 = (int) (face.getPosition().y * scale);
            final int faceX2 = (int) (faceX1 + face.getWidth());
            final int faceY2 = (int) (faceY1 + face.getHeight());

            for (Landmark landmark : face.getLandmarks()) {
                final int cx = (int) (landmark.getPosition().x * scale);
                final int cy = (int) (landmark.getPosition().y * scale);
                canvas.drawCircle(cx, cy, 10, circlePaint);
                canvas.drawRect(faceX1, faceY1, faceX2, faceY2, rectPaint);

                final Bitmap scaledHat = createScaledBitmapByWidth(hatBitmap, face.getWidth());
                if (landmark.getType() == Landmark.RIGHT_EYE) {
                    final int hatY = (int) ((landmark.getPosition().y - scaledHat.getHeight()) * scale);
                    drawBitmap(canvas, scaledHat, faceX1, hatY, (int) face.getEulerZ());
                }
            }
        }
    }

    private Bitmap createScaledBitmapByWidth(Bitmap bitmap, float desiredWidth) {
        final float scale = desiredWidth / bitmap.getWidth();
        final int adjustedHeight = Math.round(bitmap.getHeight() * scale);
        return Bitmap.createScaledBitmap(bitmap, (int) desiredWidth, adjustedHeight, true);
    }

    private void drawBitmap(Canvas canvas, Bitmap bitmap, int x, int y, int angle) {

        final Matrix hatMatrix = new Matrix();
        hatMatrix.postRotate(-angle);
        hatMatrix.postTranslate(x, y);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawBitmap(bitmap, hatMatrix, paint);
    }
}