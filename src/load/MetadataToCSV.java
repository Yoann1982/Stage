package load;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cette classe permet de générer un fichier CSV à partir d'une liste de
 * Metadata
 * 
 * @author y.keravec
 *
 */
public class MetadataToCSV extends Exporter {

	private FileWriter fichier;

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
	public MetadataToCSV(String fichierCSV, List<Metadata> listeMetadata,
			String separator, String fichierFormat) {

		// Lecture du fichier de paramétrage qui contient le format de la table
		readFormatFile(fichierFormat);

		try {
			fichier = new FileWriter(fichierCSV);
			// Ecriture du fichier CSV
			// Ecriture de l'entête
			writeHeader(separator);
			// Ecriture des lignes
			for (Metadata meta : listeMetadata) {
				writeRecord(meta, separator);
			}
			System.out.println("Fichier CVS écrit : " + fichierCSV);
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
		for (FormatTable format : listeFormat) {

			// On récupère la valeur correspondant à la colonne
			Object valeur = meta.get(format.getColumn());
			// On vérifie son format

			if (checkFormat(format, valeur))
				if (cpt == 0)
					ligne += stringNull(valeur);
				else
					ligne += separator + stringNull(valeur);
			else
				break; // On sort car on ne pourra pas écrire l'enregistement.
			cpt++;
		}
		ligne += "\n";
		// On écrit dans le fichier si tous les enregistrements sont OK.
		try {
			fichier.write(ligne, 0, ligne.length());
		} catch (IOException e) {
			// TODO Auto-generated catch block e.printStackTrace();
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
		header += "\n";
		try {
			fichier.write(header);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
