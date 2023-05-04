package com.estar.holder;

import android.view.View;

import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;

public class LocalImaHolder extends Holder<Integer> {

    private ImageView imageView;

    public LocalImaHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        imageView = new ImageView(itemView.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public void updateUI(Integer data) {

    }
}
