package org.apache.project.vue;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Widget permettant de sélectionner un horaire
 */
public class TimeSpinner extends Spinner<LocalTime> {

	enum EditMode {
		HOURS, MINUTES
	}

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH'h'mm");

	private final ObjectProperty<EditMode> mode = new SimpleObjectProperty<>(EditMode.MINUTES);

	private SpinnerValueFactory<LocalTime> factory;

	/**
	 * Permet de créer un sélecteur d'horaire, initialisé à l'horaire donné en
	 * paramètre
	 * 
	 * @param time
	 */
	public TimeSpinner(final LocalTime time) {
		setEditable(true);

		factory = new SpinnerValueFactory<LocalTime>() {

			{
				setValue(time);
			}

			@Override
			public void decrement(int steps) {
				increment(-steps);
			}

			@Override
			public void increment(int steps) {
				switch (mode.get()) {
				case HOURS:
					setValue(getValue().plusHours(steps));
					updateText();
					selectText();
					break;
				case MINUTES:
					setValue(getValue().plusMinutes(steps));
					updateText();
					selectText();
					break;
				}
			}

		};

		this.setValueFactory(factory);

		updateText();

		this.getEditor().focusedProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				if (!(Boolean) newValue) {
					factory.setValue(stringToTime(getEditor().getText()));
				}

			}

		});

		this.getEditor().textProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				if (!getEditor().getText().matches("[0-9]{0,2}h[0-9]{0,2}")) {
					updateText(); // we reset the previous text
				}
			}

		});

		this.getEditor().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					factory.setValue(stringToTime(getEditor().getText()));
					updateText();
				}
			}
		});

		this.getEditor().addEventHandler(InputEvent.ANY, new EventHandler<InputEvent>() {

			@Override
			public void handle(InputEvent event) {

				int caretPos = getEditor().getCaretPosition();
				int hrIndex = getEditor().getText().indexOf('h');

				if (caretPos <= hrIndex) {
					mode.set(EditMode.HOURS);
				} else {
					mode.set(EditMode.MINUTES);
				}
			}
		});

		mode.addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				selectText();
			}

		});
	}

	/**
	 * 
	 */
	private void updateText() {
		getEditor().setText(this.getValue().format(formatter));
	}

	/**
	 * 
	 */
	private void selectText() {
		int hrIndex = getEditor().getText().indexOf('h');
		// int minIndex = getEditor().getText().indexOf(':', hrIndex + 1);

		getEditor().deselect();

		switch (mode.getValue()) {
		case HOURS:
			getEditor().selectRange(0, hrIndex);
			break;
		case MINUTES:
			getEditor().selectRange(hrIndex + 1, getEditor().getLength());
			break;
		}
	}

	/**
	 * @param string
	 * @return
	 */
	private LocalTime stringToTime(String string) {
		String[] tokens = string.split("h");
		int hours = secureParseInt(tokens, 0);
		int minutes = secureParseInt(tokens, 1);
		int totalSeconds = (hours * 60 + minutes) * 60;
		return LocalTime.of((totalSeconds / 3600) % 24, (totalSeconds / 60) % 60);
	}

	/**
	 * @param string
	 * @param index
	 * @return
	 */
	private int secureParseInt(String[] string, int index) {
		if (string.length <= index) {
			return 0;
		}
		if (string[index] != null && string[index].isEmpty()) {
			return 0;
		}
		return Integer.parseInt(string[index]);
	}

	/**
	 * 
	 */
	public TimeSpinner() {
		this(LocalTime.now());
	}
}
