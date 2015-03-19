package load;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Cette classe permet de générer un fichier SQLLoader à partir d'une liste de
 * Metadata
 * 
 * @author Yoann Keravec Date: 19/03/2015<br>
 *         Institut Bergonié<br>
 */
public class MetadataToSQLLoader extends Exporter {
	private FileWriter fichier;

	public MetadataToSQLLoader(String fichierSQL, String fichierLoader, String separator, List<Metadata> listeMetadata,
			String nomTable, String fichierFormat) {

		// Lecture du fichier de paramétrage qui contient le format de la table
		readFormatFile(fichierFormat);

		try {
			fichier = new FileWriter(fichierSQL);
			// Ecriture du fichier SQL

			String ligne = "LOAD DATA INFILE '"
					+ fichierLoader
					+ "'\nINTO TABLE "
					+ nomTable
					+ " FIELDS TERMINATED BY '"
					+ separator
					+ "' OPTIONALY ENCLOSED BY '\"' LINES TERMINATED BY '\\r\\n' \n"
					+ "IGNORE 1 LINES\n(";
			String listeColonnes = "";
			int cpt = 0;
			for (FormatTable format : listeFormat) {
				if (cpt == 0) listeColonnes += format.getColumn();
				else listeColonnes += "," + format.getColumn();
				cpt++;
			}
			listeColonnes += ")";
			
			// Ligne complète
			ligne += listeColonnes + "\n";
			
			// Génération du fichier CSV associé
			MetadataToCSV csv = new MetadataToCSV(fichierLoader, listeMetadata, separator, fichierFormat);

			// Ecriture des lignes
			try {
				fichier.write(ligne, 0, ligne.length());
			} catch (IOException e) {
				// TODO Auto-generated catch block e.printStackTrace();
			}
			System.out.println("Fichier SQL écrit : "
					+ new File(fichierSQL).getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fichier != null) {
				try {
					fichier.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
