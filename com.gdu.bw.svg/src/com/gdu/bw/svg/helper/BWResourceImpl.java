
package com.gdu.bw.svg.helper;

import java.util.ArrayList;

import com.gdu.bw.svg.model.BWNode;
import com.gdu.bw.svg.model.figure.SVGFigure;

/**
 * @author <a>Davy Du</a>
 * 
 * @since 1.0.0
 */
public class BWResourceImpl {

	public static BWResourceImpl INSTANCE = new BWResourceImpl();

	public BWNode getEObject(BWNode root, String uriFragment) {
		uriFragment = uriFragment.substring(1, uriFragment.length());
		int length = uriFragment.length();
		if (length > 0) {
			if (uriFragment.charAt(0) == '/') {
				ArrayList<String> uriFragmentPath = new ArrayList<String>(4);
				int start = 1;
				for (int i = 1; i < length; ++i) {
					if (uriFragment.charAt(i) == '/') {
						uriFragmentPath.add(start == i ? "" : uriFragment
								.substring(start, i));
						start = i + 1;
					}
				}
				uriFragmentPath.add(uriFragment.substring(start));
				return getEObject(root, uriFragmentPath);
			} else if (uriFragment.charAt(length - 1) == '?') {
				int index = uriFragment.lastIndexOf('?', length - 2);
				if (index > 0) {
					uriFragment = uriFragment.substring(0, index);
				}
			}
		}

		return getEObjectByID(uriFragment);
	}

	/**
	 * @param uriFragment
	 * @return
	 */
	private BWNode getEObjectByID(String uriFragment) {

		return null;
	}

	/**
	 * @param uriFragmentPath
	 * @return
	 */
	private BWNode getEObject(BWNode eObject, ArrayList<String> uriFragmentPath) {
		int size = uriFragmentPath.size();
		for (int i = 1; i < size; ++i) {
			if (eObject != null) {
				eObject = eObject.findBWNodeByPath(uriFragmentPath.get(i));
			} else {
				return null;
			}
		}
		return eObject;
	}

	public SVGFigure getSVGFigure(SVGFigure root, String uriFragment) {
		uriFragment = uriFragment.substring(1, uriFragment.length());
		int length = uriFragment.length();
		if (length > 0) {
			if (uriFragment.charAt(0) == '/') {
				ArrayList<String> uriFragmentPath = new ArrayList<String>(4);
				int start = 1;
				for (int i = 1; i < length; ++i) {
					if (uriFragment.charAt(i) == '/') {
						uriFragmentPath.add(start == i ? "" : uriFragment
								.substring(start, i));
						start = i + 1;
					}
				}
				uriFragmentPath.add(uriFragment.substring(start));
				return getSVGFigure(root, uriFragmentPath);
			} else if (uriFragment.charAt(length - 1) == '?') {
				int index = uriFragment.lastIndexOf('?', length - 2);
				if (index > 0) {
					uriFragment = uriFragment.substring(0, index);
				}
			}
		}

		return getSVGFigureByID(uriFragment);
	}

	/**
	 * @param uriFragment
	 * @return
	 */
	private SVGFigure getSVGFigureByID(String uriFragment) {
		// TODO Auto-generated method stub
		return null;
	}

	private SVGFigure getSVGFigure(SVGFigure eObject,
			ArrayList<String> uriFragmentPath) {
		int size = uriFragmentPath.size();
		for (int i = 1; i < size; ++i) {
			eObject = eObject.findSVGFigureByPath(uriFragmentPath.get(i));
		}
		return eObject;
	}
}
