/*
 * {Simple class loads FragmentFrequency.java}
 *
 * @version Build (6 June 2015)
 * @author Junaid Malik
 */
package com.malikjunaid.drhearing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A class used to create layout for frequency fragment
 */
public class FragmentFrequency extends Fragment {

	public FragmentFrequency() {
	} // default constructor

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ---Inflate the layout for this fragment---
		return inflater.inflate(R.layout.frequency_fragment, container, false);
	}
}
