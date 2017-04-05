package wtf.java9.inference;

import javax.swing.*;

/**
 * There seem to be problems with casting a type that is parameterized with a wildcard to another
 * type.
 */
public class CastWildcardParam {

	/**
	 * {@link SpinnerNumberModel#getMaximum()} returns a {@link Comparable Comparable<T>} that can
	 * not be cast to the type that was set earlier.
	 */
	public void spinnerModel(SpinnerNumberModel spinnerModel) {
		// this could have happened somewhere else:
		// spinnerModel.setMinimum(0);
		// spinnerModel.setMaximum(360);
		// spinnerModel.setValue(45);

		int newValue = (int) spinnerModel.getValue() + 1;
		newValue = Math.min(newValue, (int) spinnerModel.getMaximum()); // fail
		newValue = Math.max(newValue, (int) (Object) spinnerModel.getMinimum()); // pass
		spinnerModel.setValue(newValue);
	}

}
