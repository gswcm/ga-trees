package net.gswcm.tog.Common;


import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import net.gswcm.tog.R;

import java.util.ArrayList;

public class TreeImageFragment extends Fragment {


	private ArrayList<Drawable> listOfDrawables;
	private ImageSwitcher imageSwitcher;
	private int imageIndex;

	public TreeImageFragment() {
		super();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_image, container, false);
		imageSwitcher = (ImageSwitcher)view.findViewById(R.id.treeImageSwitcher);
		imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
			@Override
			public View makeView() {
				ImageView imageView = new ImageView(getActivity());
				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				ImageSwitcher.LayoutParams lp = new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
				imageView.setLayoutParams(lp);
				return imageView;
			}
		});
		Animation in = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
		Animation out = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
		imageSwitcher.setInAnimation(in);
		imageSwitcher.setOutAnimation(out);
		imageSwitcher.setImageDrawable(listOfDrawables.get(imageIndex));
		imageSwitcher.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				imageIndex = (imageIndex + 1) % listOfDrawables.size();
				imageSwitcher.setImageDrawable(listOfDrawables.get(imageIndex));
			}
		});
		/*
		imageSwitcher.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				imageSwitcher.setImageDrawable(listOfDrawables.get(imageIndex));
				imageIndex = (imageIndex + 1) % listOfDrawables.size();
				Log.d("----> ", String.format("X: %3.1f, Y: %3.1f", motionEvent.getX(), motionEvent.getY()));
				return true;
			}
		});
		*/
		getActivity().getActionBar().hide();
		return view;
	}
	@Override
	public void onStart() {
		super.onStart();
		Toast.makeText(getActivity(), "Hit Back to return. Tap on image to cycle", Toast.LENGTH_SHORT).show();
	}


	public static TreeImageFragment getInstance(int indexOfClickedView, ArrayList<Drawable> listOfDrawables) {
		TreeImageFragment f = new TreeImageFragment();
		f.listOfDrawables = listOfDrawables;
		f.imageIndex = indexOfClickedView;
		return f;
	}
}
