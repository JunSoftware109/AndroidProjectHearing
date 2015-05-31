package com.example.testtonegenv10;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentFrequency extends Fragment {

	public FragmentFrequency() {
	} // empty constructor

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ---Inflate the layout for this fragment---

		return inflater.inflate(R.layout.frequency_fragment, container, false);
	}
}
