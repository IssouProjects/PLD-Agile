package org.apache.project.vue;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 */
public class FileOpener {

	/**
	 * @param stage
	 * @param fileDescription
	 * @param fileExtension
	 * @param userMsg
	 * @return
	 */
	public static File ouvrirFichier(Stage stage, String fileDescription, String fileExtension, String userMsg) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(userMsg);

		ExtensionFilter filter = new ExtensionFilter(fileDescription, fileExtension);

		fileChooser.getExtensionFilters().add(filter);

		File file = fileChooser.showOpenDialog(stage);

		System.out.println(file);

		return file;
	}
}
