package com.temasysreact;

import android.view.ViewGroup;
import android.view.View;
import android.view.View.MeasureSpec;
import android.util.Log;
import android.content.Context;

import com.facebook.react.uimanager.*;

import com.temasysreact.sdk.TemasysClient;



public class TemasysRendererView extends ViewGroup
{
    private static final String TAG = "TemasysRendererView";

    private Boolean preview;
    private String userID;



    public TemasysRendererView(Context context)
    {
        super(context);

        Log.d(TAG, "constructor");


        this.preview = true;
        this.userID = new String();
    }



    @Override
    protected void onDetachedFromWindow()
    {
        if (this.preview)
        {
            TemasysClient.instance().setLocalPreview(this.userID, null);
        }
        else
        {
            TemasysClient.instance().setRemoteView(this.userID, null);
        }
    }


   @Override
   protected void onLayout(boolean changed, int l, int t, int r, int b) {
       final int count = getChildCount();
       int curWidth, curHeight, curLeft, curTop, maxHeight;

       //get the available size of child view
       final int childLeft = this.getPaddingLeft();
       final int childTop = this.getPaddingTop();
       final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
       final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
       final int childWidth = childRight - childLeft;
       final int childHeight = childBottom - childTop;

       maxHeight = 0;
       curLeft = childLeft;
       curTop = childTop;

       for (int i = 0; i < count; i++) {
           View child = getChildAt(i);

           if (child.getVisibility() == GONE)
               return;

           //Get the maximum size of the child
           child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
           curWidth = child.getMeasuredWidth();
           curHeight = child.getMeasuredHeight();
           //wrap is reach to the end
           if (curLeft + curWidth >= childRight) {
               curLeft = childLeft;
               curTop += maxHeight;
               maxHeight = 0;
           }
           //do the layout
           child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);
           //store the max height
           if (maxHeight < curHeight)
               maxHeight = curHeight;
           curLeft += curWidth;
       }
   }



    public void setPreviewStatus(Boolean preview)
    {
        this.preview = preview;
        applyProperties();
    }



    public void setCallId(String userID)
    {
        this.userID = userID;
        applyProperties();
    }



    private void applyProperties()
    {
        Log.d(TAG, "applyProperties");

        if (this.preview)
        {
            TemasysClient.instance().setLocalPreview(this.userID, this);
        }
        else
        {
            TemasysClient.instance().setRemoteView(this.userID, this);
        }
    }
}
