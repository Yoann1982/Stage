package load;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import sgbd.Connexion;

/**
 * Cette classe permet le chargement en base de données des metadatas.
 * 
 * @author y.keravec
 * Date: 19/03/2015<br>
 *         Institut Bergonié<br>
 */

public class MetadataToDatabase extends Exporter {

	private String utilisateur = "postgres";
	private String mdp = "";

	// Méthode par fichier d'insert
	public MetadataToDatabase(File fichierSQL) {

		// 1 - Récupération des informations de connexion à la base de données
		// (cryptées)

		// demandeUserMdp();

		// Connexion maConnexion = new Connexion(utilisateur,mdp);
		Connexion maConnexion = new Connexion();

		// Chargement du driver MySQL
		maConnexion.chargementDriver();

		// Création de la connexion avec la base
		if (maConnexion.initConnexion() == 1) {
			System.out.println("La base de donnée n'est pas accessible.");
		}

		// Création du Statement
		maConnexion.creationStatement();

		// 3 - Chargement des données dans la base de données
		// Récupération des données à inserer
		String insert = null;
		try {
			// Création du flux bufférisé sur un FileReader, immédiatement suivi
			// par un
			// try/finally, ce qui permet de ne fermer le flux QUE s'il le
			// reader
			// est correctement instancié (évite les NullPointerException)
			/*BufferedReader buff = new BufferedReader(new FileReader(fichierSQL));
			try {
				String line;
				// Lecture du fichier ligne par ligne. Cette boucle se termine
				// quand la méthode retourne la valeur null.
				while ((line = buff.readLine()) != null) {
					// Alimentation de la base
					System.out.println("LINE : " + line);
					maConnexion.executerInsert(line);
				}
			} finally {
				// dans tous les cas, on ferme nos flux
				buff.close();
			}*/
			insert = loadFileString(fichierSQL);
		} catch (IOException e) {
			System.err.println("Erreur lors de la lecture du fichier temporaire.");
			e.printStackTrace();
		}
		// System.out.println(" DEBUG requête :" + insert);
		if (maConnexion.executerInsert(insert) == 1) 
			System.out.println("Insertion au sein de la table d'I2B2.");
		else 
			System.err.println("Erreur à l'insertion des données dans la table d'I2B2.");

		// 4 - Déconnexion

		// Fermeure de l'objet Statement
		maConnexion.fermetureStatement();
		// Fermeture de la connexion à la base
		maConnexion.fermetureConnexion();

	}

	public void demandeUserMdp() {
		System.out.println("Compte utilisateur de la base : ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			utilisateur = br.readLine();
			System.out.println("Mot de passe : ");

			mdp = br.readLine();

		} catch (IOException e1) {
			System.err.println("Erreur de la lecture des données saisies.");
			e1.printStackTrace();
		}
	}

	public static String loadFileString(File file) throws IOException {
		Reader reader = new InputStreamReader(new FileInputStream(file),
				"ascii");
		try {
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[512];
			int nbRead = reader.read(buffer);
			while (nbRead > 0) {
				builder.append(buffer, 0, nbRead);
				nbRead = reader.read(buffer);
			}
			return builder.toString();
		} finally {
			reader.close();
		}
	}

	// Méthode par fichier SQL Loader
	public MetadataToDatabase(String fichierCtl) {
		try {

			//demandeUserMdp();

			String sqlldrCmd = "sqlldr " + utilisateur + "/" + mdp
					+ ", control=" + fichierCtl;

			System.out.println("SQLLDR Started ....... ");
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(sqlldrCmd);
			proc.waitFor();
			System.out.println("Code sortie : " + proc.exitValue());
			System.out.println("SQLLDR Ended ........  ");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
