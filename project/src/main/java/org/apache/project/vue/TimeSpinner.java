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

public class TimeSpinner extends Spinner<LocalTime> {
	
	enum EditMode{
		HOURS,
		MINUTES,
		SECONDS
	}
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final ObjectProperty<EditMode> mode = new SimpleObjectProperty<>(EditMode.MINUTES);
    
    private SpinnerValueFactory<LocalTime> factory;
    
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
            	switch(mode.get()) {
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
            	case SECONDS:
            		setValue(getValue().plusSeconds(steps));
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
				if(!(Boolean)newValue) {
					factory.setValue(stringToTime(getEditor().getText()));
				}
				
			}
        	
        });
        
        this.getEditor().textProperty().addListener(new ChangeListener<Object>(){

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {	            
				if (!getEditor().getText().matches("[0-9]{0,2}:[0-9]{0,2}:[0-9]{0,2}")) {
		            updateText(); // we reset the previous text
				}
			}
        	
        });
        
        this.getEditor().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ENTER)
					factory.setValue(stringToTime(getEditor().getText()));
			}
        });

        this.getEditor().addEventHandler(InputEvent.ANY, new EventHandler<InputEvent>() {
            
			@Override
			public void handle(InputEvent event) {
				
				int caretPos = getEditor().getCaretPosition();
				int hrIndex = getEditor().getText().indexOf(':');
	            int minIndex = getEditor().getText().indexOf(':', hrIndex + 1);
				
	            if (caretPos <= hrIndex) {
	                mode.set( EditMode.HOURS );
	            } else if (caretPos <= minIndex) {
	                mode.set( EditMode.MINUTES );
	            } else {
	                mode.set( EditMode.SECONDS );
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
    
    private void updateText() {
    	getEditor().setText(this.getValue().format(formatter));
    }
    
    private void selectText() {
    	int hrIndex = getEditor().getText().indexOf(':');
        int minIndex = getEditor().getText().indexOf(':', hrIndex + 1);
        
        getEditor().deselect();
        
		switch(mode.getValue()) {
		case HOURS:
			getEditor().selectRange(0, hrIndex);
    		break;
    	case MINUTES:
    		getEditor().selectRange(hrIndex+1, minIndex);
    		break;
    	case SECONDS:
    		getEditor().selectRange(minIndex+1, getEditor().getText().length());
    		break;
		}
    }
    
    private LocalTime stringToTime(String string) {
    	String[] tokens = string.split(":");
        int hours = secureParseInt(tokens[0]);
        int minutes = secureParseInt(tokens[1]);
        int seconds = secureParseInt(tokens[2]);
        int totalSeconds = (hours * 60 + minutes) * 60 + seconds;
        return LocalTime.of((totalSeconds / 3600) % 24, (totalSeconds / 60) % 60, seconds % 60);
    }
    
    private int secureParseInt(String string) {
        if (string != null && string.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(string);
    }

    public TimeSpinner() {
        this(LocalTime.now());
    }
}
