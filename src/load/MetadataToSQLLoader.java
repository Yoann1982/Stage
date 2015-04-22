package load;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Cette classe permet de générer un fichier SQLLoader à partir d'une liste de
 * Metadata
 * 
 * @author Yoann Keravec <br> Date: 19/03/2015<br>
 *         Institut Bergonié<br>
 */
public class MetadataToSQLLoader extends Exporter {
	private FileWriter fichier;

	public MetadataToSQLLoader(File fichierCTL, File fichierLoader,
			List<Metadata> listeMetadata, String nomTable, String fichierFormat) {
		FileWriter fichierWriterCTL = null;
		FileWriter fichierWriterCSV = null;
		try {
			fichierWriterCTL = new FileWriter(fichierCTL);
			fichierWriterCSV = new FileWriter(fichierLoader);
			new MetadataToSQLLoader(fichierWriterCTL, fichierWriterCSV, ";",
					listeMetadata, nomTable, fichierFormat);
		} catch (IOException e1) {
			System.err.println("Erreur de la génération des FileWriter.");
			e1.printStackTrace();
		}
		try {
			System.out.println("Fichier CTL écrit : "
					+ fichierCTL.getCanonicalPath());
			System.out.println("Fichier CSV écrit : "
					+ fichierLoader.getCanonicalPath());
			
		} catch (IOException e) {
			System.err.println("Erreur de l'affichage du nom des fichiers générés.");
			e.printStackTrace();
		}

	}

	public MetadataToSQLLoader(String fichierCTL, String fichierLoader,
			List<Metadata> listeMetadata, String nomTable, String fichierFormat) {
		FileWriter fichierWriterCTL = null;
		FileWriter fichierWriterCSV = null;
		try {
			fichierWriterCTL = new FileWriter(fichierCTL);
			fichierWriterCSV = new FileWriter(fichierLoader);
			new MetadataToSQLLoader(fichierWriterCTL, fichierWriterCSV, ";",
					listeMetadata, nomTable, fichierFormat);
		} catch (IOException e) {
			System.err.println("Erreur de la génération des FileWriter.");
			e.printStackTrace();
		}
		try {
			System.out.println("Fichier CTL écrit : "
					+ new File(fichierCTL).getCanonicalPath());
			System.out.println("Fichier CSV écrit : "
					+ new File(fichierLoader).getCanonicalPath());
		} catch (IOException e) {
			System.err.println("Erreur de l'affichage du nom des fichiers générés.");
			e.printStackTrace();
		}
	}

	public MetadataToSQLLoader(String fichierCTL, String fichierLoader,
			String separator, List<Metadata> listeMetadata, String nomTable,
			String fichierFormat) {
		FileWriter fichierWriterCTL = null;
		FileWriter fichierWriterCSV = null;
		try {
			fichierWriterCTL = new FileWriter(fichierCTL);
			fichierWriterCSV = new FileWriter(fichierLoader);
			new MetadataToSQLLoader(fichierWriterCTL, fichierWriterCSV,
					separator, listeMetadata, nomTable, fichierFormat);
		} catch (IOException e) {
			System.err.println("Erreur de la génération des FileWriter.");
			e.printStackTrace();
		}
		try {
			System.out.println("Fichier CTL écrit : "
					+ new File(fichierCTL).getCanonicalPath());
			System.out.println("Fichier CSV écrit : "
					+ new File(fichierLoader).getCanonicalPath());
		} catch (IOException e) {
			System.err.println("Erreur de l'affichage des noms des fichiers en sortie.");
			e.printStackTrace();
		}
	}

	public MetadataToSQLLoader(FileWriter fichierCTL, FileWriter fichierLoader,
			String separator, List<Metadata> listeMetadata, String nomTable,
			String fichierFormat) {

		fichier = fichierCTL;

		// Lecture du fichier de paramétrage qui contient le format de la table
		readFormatFile(fichierFormat);

		// Ecriture du fichier SQL

		String ligne = "LOAD DATA INFILE '"
				+ fichierLoader
				+ "'" + retourChariot + "INTO TABLE "
				+ nomTable
				+ " FIELDS TERMINATED BY '"
				+ separator
				+ "' OPTIONALY ENCLOSED BY '\"' LINES TERMINATED BY '\\r\\n' \n"
				+ "IGNORE 1 LINES" + retourChariot +"(";
		String listeColonnes = "";
		int cpt = 0;
		for (FormatTable format : listeFormat) {
			if (cpt == 0)
				listeColonnes += format.getColumn();
			else
				listeColonnes += "," + format.getColumn();
			cpt++;
		}
		listeColonnes += ")";

		// Ligne complète
		ligne += listeColonnes + retourChariot;

		new MetadataToCSV(fichierLoader, listeMetadata, separator,
				fichierFormat);

		// Ecriture des lignes
		try {
			fichier.write(ligne, 0, ligne.length());
		} catch (IOException e) {
			System.err.println("Erreur de l'écriture de la ligne dans le fichir de sortie.");
		} finally {
			if (fichier != null) {
				try {
					fichier.close();
				} catch (IOException e) {
					System.err.println("Erreur de la fermeture du fichier de sortie.");
					e.printStackTrace();
				}
			}
		}
	}
}
