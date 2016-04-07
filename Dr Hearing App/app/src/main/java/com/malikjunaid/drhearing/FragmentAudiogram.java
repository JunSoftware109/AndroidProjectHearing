/*
 * {Simple class loads FragmentAudiogram.java}
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
 * A class used to create layout for audiogram fragment
 */
public class FragmentAudiogram extends Fragment { // extends Fragment class

	public FragmentAudiogram() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.audiogram_fragment, container, false);

	}

}