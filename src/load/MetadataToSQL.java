package load;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Cette classe permet de générer un fichier SQL à partir d'une liste de Metadata
 * @author Yoann Keravec Date: 19/03/2015<br>
 *         Institut Bergonié<br>
 */

public class MetadataToSQL extends Exporter {
	private FileWriter fichier;

	
	public MetadataToSQL(String fichierSQL, List<Metadata> listeMetadata, String nomTable,
			String fichierFormat) {

		// Lecture du fichier de paramétrage qui contient le format de la table
		readFormatFile(fichierFormat);

		try {
			fichier = new FileWriter(fichierSQL);
			// Ecriture du fichier SQL
			
			// Ecriture des lignes
			for (Metadata meta : listeMetadata) {
				writeRecord(meta, nomTable);
			}
			System.out.println("Fichier SQL écrit : " + new File(fichierSQL).getCanonicalPath());
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
	 * Cette méthode permet d'écrire un enregistrement au sein du fichier SQL en
	 * sortie. Elle prend en entrée un enregistrement Metadata et le 
	 * nom de la table cible. Les enregistrements sont traités à partir de
	 * la liste de format de la table des métadatas. Le type et la taille de
	 * chaque enregistrement est testés. Un test de nullité est effectué afin de
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
		for (FormatTable format : listeFormat) {

			// On récupère la valeur correspondant à la colonne
			String colonne = format.getColumn();
			Object valeur = meta.get(colonne);
			
			if (cpt == 0) 
					listeColonnes += colonne;
			else 
					listeColonnes += "," + colonne;
			
			// On vérifie son format

			
			if (checkFormat(format, valeur))
				if (cpt == 0)
					listeValeurs += entoureGuillemet(valeur);
				else
					listeValeurs += "," + entoureGuillemet(valeur);
			else
				break; // On sort car on ne pourra pas écrire l'enregistement.
			cpt++;
		}
		
		listeColonnes += ") VALUES ";
		listeValeurs += ")"; 
		
		ligne += listeColonnes + listeValeurs + "\n";
		// On écrit dans le fichier si tous les enregistrements sont OK.
		try {
			fichier.write(ligne, 0, ligne.length());
		} catch (IOException e) {
			// TODO Auto-generated catch block e.printStackTrace();
		}
	}
	
}
