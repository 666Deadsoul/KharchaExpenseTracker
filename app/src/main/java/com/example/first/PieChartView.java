package com.example.first;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import java.util.List;
public class PieChartView extends View {
    private Paint paint = new Paint();
    private List<Float> data; // Total expenses per category

    // Define an array of lighter colors
    private int[] colors = {
            Color.parseColor("#B81B10"),  // Light Red
            Color.parseColor("#146818"),  // Light Green
            Color.parseColor("#263797"),  // Light Blue
            Color.parseColor("#E4CF15"),  // Light Yellow
            Color.parseColor("#009688")   // Light Cyan
    };

    public PieChartView(Context context, List<Float> data) {
        super(context);
        this.data = data;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float total = 0;
        for (Float value : data) total += value;

        float startAngle = 0;

        // Use a larger RectF to draw the pie chart
        int padding = 40; // Padding for better fit
        RectF pieBounds = new RectF(padding, padding, getWidth() - padding, getHeight() - padding);

        for (int i = 0; i < data.size(); i++) {
            float sweepAngle = (data.get(i) / total) * 360;
            paint.setColor(colors[i % colors.length]); // Use the lighter colors
            canvas.drawArc(pieBounds, startAngle, sweepAngle, true, paint);

            // Calculate and draw percentage in the center of the segment
            float percentage = (data.get(i) / total) * 100;
            String percentageText = String.format("%.1f%%", percentage);

            // Calculate the position for the text
            float textX = (float) (getWidth() / 2 + (getWidth() / 4) * Math.cos(Math.toRadians(startAngle + sweepAngle / 2)));
            float textY = (float) (getHeight() / 2 + (getHeight() / 4) * Math.sin(Math.toRadians(startAngle + sweepAngle / 2)));

            // Set text properties
            paint.setColor(Color.WHITE);
            paint.setTextSize(30);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(percentageText, textX, textY, paint);

            startAngle += sweepAngle;
        }
    }
}
