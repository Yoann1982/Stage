package load;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Cette classe permet de générer un fichier CSV à partir d'une liste de
 * Metadata
 * 
 * @author Yoann Keravec Date: 19/03/2015<br>
 *         Institut Bergonié<br>
 */

public class MetadataToCSV extends Exporter {

	private FileWriter fichier;
	private FileWriter fichierKO;
	private String nomFichierKO = "C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\fichierKO.csv";

	/**
	 * Constructeur de la classe MetadataToCSV. Le constructeur prend en entrée
	 * le nom du fichier CSV à produire, une liste de Metadata et le nom du
	 * fichier décrivant le format de la table Metadata. La construction du
	 * fichier CSV s'effetue sur la base du contenu du fichier de format. Le
	 * caractère sépérateur par défaut est ";"
	 * 
	 * @param fichierCSV
	 * @param listeMetadata
	 * @param fichierFormat
	 */
	public MetadataToCSV(String fichierCSV, List<Metadata> listeMetadata,
			String fichierFormat) {

		try {
			fichier = new FileWriter(fichierCSV);
			new MetadataToCSV(fichier, listeMetadata, ";", fichierFormat);
		} catch (IOException e) {
			System.err.println("Erreur de la génération du FileWriter.");
			e.printStackTrace();
		}
		try {
			System.out.println(retourChariot + "Fichier CVS écrit : "
					+ new File(fichierCSV).getCanonicalPath());
		} catch (IOException e) {
			System.err.println("Erreur de l'affichage du nom du fichier généré.");
			e.printStackTrace();
		}
	}

	public MetadataToCSV(String fichierCSV, List<Metadata> listeMetadata,
			String separator, String fichierFormat) {

		try {
			fichier = new FileWriter(fichierCSV);
			new MetadataToCSV(fichier, listeMetadata, separator, fichierFormat);
		} catch (IOException e) {
			System.err.println("Erreur de la génération du FileWriter.");
			e.printStackTrace();
		}
		try {
			System.out.println(retourChariot + "Fichier CVS écrit : "
					+ new File(fichierCSV).getCanonicalPath());
		} catch (IOException e) {
			System.err.println("Erreur de l'affichage du nom du fichier généré.");
			e.printStackTrace();
		}
	}

	/**
	 * Constructeur de la classe MetadataToCSV. Le constructeur prend en entrée
	 * le nom du fichier CSV à produire, une liste de Metadata, le caractère
	 * séparateur à utiliser et le nom du fichier décrivant le format de la
	 * table Metadata. La construction du fichier CSV s'effetue sur la base du
	 * contenu du fichier de format.
	 * 
	 * @param fichierCSV
	 * @param listeMetadata
	 * @param separator
	 * @param fichierFormat
	 */
	public MetadataToCSV(FileWriter writer, List<Metadata> listeMetadata,
			String separator, String fichierFormat) {

		// Lecture du fichier de paramétrage qui contient le format de la table
		readFormatFile(fichierFormat);

		try {

			fichier = writer;
			try {
				fichierKO = new FileWriter(nomFichierKO);
			} catch (IOException e) {
				System.err.println("Erreur de la génération du FileWriter.");
				e.printStackTrace();
			}

			// Ecriture du fichier CSV
			// Ecriture de l'entête
			writeHeader(separator);
			// Ecriture des lignes
			for (Metadata meta : listeMetadata) {
				writeRecord(meta, separator);
			}
		} finally {
			if (fichier != null) {
				try {
					fichier.close();
				} catch (IOException e) {
					System.err.println("Erreur de la fermeture du fichier généré.");
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
					System.err.println("Erreur de la fermeture du fichier généré.");
					e.printStackTrace();
				}
			}
		}
		// System.out.println("Fichier CVS écrit : " + new
		// File(fichierCSV).getCanonicalPath());

	}

	/**
	 * Cette méthode permet d'écrire un enregistrement au sein du fichier CSV en
	 * sortie. Elle prend en entrée un enregistrement Metadata et le caractère
	 * séparateur du fichier CSV. Les enregistrements sont traités à partir de
	 * la liste de format de la table des métadatas. Le type et la taille de
	 * chaque enregistrement est testés. Un test de nullité est effectué afin de
	 * vérifier que les champs obligatoires sont bien renseignés.
	 * 
	 * @param meta
	 * @see Metadata
	 * @param separator
	 *            Caractère séparateur.
	 */
	public void writeRecord(Metadata meta, String separator) {
		// On récupère chaque champ
		// On le compare au format attendu
		// Si le format est incorrect, on écrit le champ en erreur dans un
		// fichier de sortie de log.

		String ligne = "";
		int cpt = 0;
		boolean formatOK = true;
		int codeErreur = 0;

		String sortieErreur = "";

		for (FormatTable format : listeFormat) {
			String colonne = format.getColumn();
			// On récupère la valeur correspondant à la colonne
			Object valeur = meta.get(colonne);
			// On vérifie son format
			codeErreur = checkFormat(format, valeur);
			if (codeErreur == 0)
				if (cpt == 0)
					ligne += stringNull(valeur);
				else
					ligne += separator + stringNull(valeur);
			else {
				if (colonne.equalsIgnoreCase("UPDATE_DATE")) {
					if (cpt == 0)
						ligne += "current_timestamp";
					else
						ligne += "," + "current_timestamp";
				} else {
					formatOK = false;
					// On enrichie la liste d'erreur qui sera exportée dans le
					// fichier des erreurs.
					sortieErreur = exportErreur(codeErreur, colonne, valeur,
							sortieErreur);
				}
			}
			cpt++;
		}
		// On écrit dans le fichier si tous les enregistrements sont OK.
		try {
			if (formatOK) {
				String ligneSortie = ligne + retourChariot;
				fichier.write(ligneSortie, 0, ligneSortie.length());
			} else {
				String ligneErreur = entoureGuillemet(ligne) + sortieErreur;
				fichierKO.write(ligneErreur, 0, ligneErreur.length());
			}
		} catch (IOException e) {
			System.err.println("Erreur de l'écriture du fichier des erreurs.");
			e.printStackTrace();
		}
	}

	/**
	 * Cette méthode permet d'écrire l'entête du fichier CSV. Il est constuit à
	 * partir de la liste de Format qui liste les colonnes de la table Metadata.
	 * 
	 * @param separator
	 *            Le caractère séparateur du fichier CSV.
	 */
	public void writeHeader(String separator) {

		String header = "";
		// On va parcourir la liste de format pour retrouver le nom des
		// colonnes.
		int cpt = 0;
		for (FormatTable formatLigne : listeFormat) {
			if (cpt == 0) {
				header += formatLigne.getColumn();
			} else {
				header += separator + formatLigne.getColumn();
			}
			cpt++;
		}
		header += retourChariot;
		try {
			fichier.write(header);
		} catch (IOException e) {
			System.err.println("Erreur de l'écriture de l'header du fichier généré.");
			e.printStackTrace();
		}
	}
}
