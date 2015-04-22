package load;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Cette classe permet de générer un fichier SQL à partir d'une liste de
 * Metadata
 * 
 * @author Yoann Keravec <br> Date: 19/03/2015<br>
 *         Institut Bergonié<br>
 */

public class MetadataToSQL extends Exporter {
	private FileWriter fichier;
	private FileWriter fichierKO;
	//private String nomFichierKO = "C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\fichierKO.sql";
	
	private String nomFichierKO = repertoireErreur + "fichierKO.sql";

	public MetadataToSQL(File fichierSQL, List<Metadata> listeMetadata,
			String nomTable, String fichierFormat) {
		try {
			fichier = new FileWriter(fichierSQL);
			new MetadataToSQL(fichier, listeMetadata, nomTable, fichierFormat);

		} catch (IOException e) {
			System.err.println("Erreur lors de la génération du FileWriter.");
			e.printStackTrace();
		}
		try {
			System.out.println(retourChariot + "Fichier SQL écrit : "
					+ fichierSQL.getCanonicalPath());
		} catch (IOException e) {
			System.err.println("Erreur lors de l'affichage du nom du fichier SQL.");
			e.printStackTrace();
		}
	}

	public MetadataToSQL(String fichierSQL, List<Metadata> listeMetadata,
			String nomTable, String fichierFormat) {
		try {
			fichier = new FileWriter(fichierSQL);
			new MetadataToSQL(fichier, listeMetadata, nomTable, fichierFormat);

		} catch (IOException e) {
			System.err.println("Erreur lors de la génération du FileWriter.");
			e.printStackTrace();
		}
		try {
			System.out.println(retourChariot + "Fichier SQL écrit : "
					+ new File(fichierSQL).getCanonicalPath());
		} catch (IOException e) {
			System.err.println("Erreur lors de l'affichage du fichier SQL.");
			e.printStackTrace();
		}
	}

	public MetadataToSQL(FileWriter writer, List<Metadata> listeMetadata,
			String nomTable, String fichierFormat) {

		// Lecture du fichier de paramétrage qui contient le format de la table
		readFormatFile(fichierFormat);
		try {

			fichier = writer;
			try {
				// Préparation fichier d'erreur
				fichierKO = new FileWriter(nomFichierKO);
			} catch (IOException e) {
				System.err.println("Erreur lors de la génération du FileWriter du fichier d'erreurs.");
				e.printStackTrace();
			}
			// Ecriture des lignes
			for (Metadata meta : listeMetadata) {
				writeRecord(meta, nomTable);
			}
		} finally {
			if (fichier != null) {
				try {
					fichier.close();
				} catch (IOException e) {
					System.err.println("Erreur lors de la fermeture du FileWriter.");
					e.printStackTrace();
				}
			}
			if (fichierKO != null) {
				try {
					System.out
							.println(retourChariot + "Erreurs rencontrées lors de la génération du fichier. Consultez le fichier des erreurs.");
					System.out.println("Fichier SQL des erreur : "
							+ new File(nomFichierKO).getCanonicalPath());
					fichierKO.close();
				} catch (IOException e) {
					System.err.println("Erreur lors de la fermeture du fichier d'erreurs.");
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Cette méthode permet d'écrire un enregistrement au sein du fichier SQL en
	 * sortie. Elle prend en entrée un enregistrement Metadata et le nom de la
	 * table cible. Les enregistrements sont traités à partir de la liste de
	 * format de la table des métadatas. Le type et la taille de chaque
	 * enregistrement est testés. Un test de nullité est effectué afin de
	 * vérifier que les champs obligatoires sont bien renseignés.
	 * 
	 * @param meta
	 * @see Metadata
	 * @param nomTable
	 *            Nom de la table cible
	 */
	public void writeRecord(Metadata meta, String nomTable) {

		String ligne = "INSERT INTO " + nomTable + " (";
		String listeColonnes = "";
		String listeValeurs = "(";

		int cpt = 0;
		boolean formatOK = true;
		int codeErreur = 0;

		String sortieErreur = "";

		for (FormatTable format : listeFormat) {

			// On récupère la valeur correspondant à la colonne
			String colonne = format.getColumn();
			Object valeur = meta.get(colonne);

			if (cpt == 0)
				listeColonnes += colonne;
			else
				listeColonnes += "," + colonne;

			// On vérifie son format
			codeErreur = checkFormat(format, valeur);
			if (codeErreur == 0) {
				if (cpt == 0)
					listeValeurs += entoureGuillemet(valeur);
				else
					listeValeurs += "," + entoureGuillemet(valeur);
			} else {
				// Si c'est la colonne UPDATE_DATE, on met sysdate
				if (colonne.equalsIgnoreCase("UPDATE_DATE")) {
					if (cpt == 0)
						listeValeurs += "current_timestamp";
					else
						listeValeurs += "," + "current_timestamp";
				} else {
					formatOK = false;
					// On enrichie la liste d'erreurs qui sera exportée dans le
					// fichier des erreurs.
					sortieErreur = exportErreur(codeErreur, colonne, valeur,
							sortieErreur);
				}
			}
			cpt++;
		}

		listeColonnes += ") VALUES ";
		listeValeurs += ")";

		ligne += listeColonnes + listeValeurs;

		// On écrit dans le fichier si tous les enregistrements sont OK.
		try {
			if (formatOK) {
				String ligneSortie = ligne + ";" + retourChariot;
				fichier.write(ligneSortie, 0, ligneSortie.length());
			} else {
				String ligneErreur = entoureGuillemet(ligne) + sortieErreur;
				fichierKO.write(ligneErreur, 0, ligneErreur.length());
			}
		} catch (IOException e) {
			System.err.println("Erreur lors de l'écriture dans le fichier d'erreurs.");
			e.printStackTrace();
		}
	}

}
