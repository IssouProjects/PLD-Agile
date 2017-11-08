package org.apache.project.vue;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * Classe permettant à l'utilisateur de choisir des fichiers
 */
public class FileOpener {

	/**
	 * Affiche une fenêtre de sélection de fichier sur le disque
	 * 
	 * @param stage
	 *            La fenêtre qui appelle cette fenêtre
	 * @param fileDescription
	 *            la description du fichier requis
	 * @param fileExtension
	 *            l'extension souhaitée pour le fichier (exemple: (*.xml))
	 * @param userMsg
	 *            le titre de la fenêtre
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
