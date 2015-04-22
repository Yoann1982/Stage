package load;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import param.Param;



/**
 * Cette classe contient les méthodes permettant de préparer les Metadata pour
 * leur exports vers une table, une fichier SQL, un fichier CSV, etc.
 * 
 * @author Yoann Keravec <br> Date: 19/03/2015<br>
 *         Institut Bergonié<br>
 */

public class Exporter {

	protected List<FormatTable> listeFormat = new ArrayList<FormatTable>();
	protected String retourChariot = Param.RETOUR_CHARIOT;
	
	protected String repertoireErreur = Param.REPERTOIRE_ERREUR;
	protected String repertoireParam =  Param.REPETOIRE_PARAM;
	
	public String entoureGuillemet(Object valeur) {

		String sortie = "";

		if (valeur != null) {
			String classe = valeur.getClass().toString();
			classe = classe.substring(classe.lastIndexOf(".") + 1);

			if (classe.equalsIgnoreCase("String")) {
				// On échappe le ' des chemins
				valeur = ((String) valeur).replace("'", "''");

				// On échappe le \ des chemins
				valeur = ((String) valeur).replace("\\", "\\\\");

				// On entoure de guillemet
				sortie = "'" + valeur.toString() + "'";
				return sortie;
			} else
				return valeur.toString();

		} else
			return null;
	}

	public String stringNull(Object valeur) {

		if (valeur == null)
			return "";
		else
			return valeur.toString();
	}

	public int checkFormat(FormatTable format, Object valeur) {

		// On parcours la liste de format pour retrouver les colonnes à traiter

		// String colonne = format.getColumn();
		String type = format.getType();
		int taille = format.getTaille();
		boolean isNullable = format.isNullable();

		int codeRetour = 0;
		if (valeur != null) {

			String classe = valeur.getClass().toString();
			classe = classe.substring(classe.lastIndexOf(".") + 1);
			if (!classe.equalsIgnoreCase(type)) {
				/*
				 * System.err .println("Erreur : Le format de la metadata " +
				 * colonne + " de valeur " + valeur +
				 * " ne correspond pas au format attendu :" + type + ".");
				 * System.err.println("Les Metadatas ne seront pas exportés.");
				 */
				codeRetour = 1;
			} else {
				// Format OK
				// Contrôle de la taille
				if (taille != -1) {
					// On vérifie la taille
					if (valeur.toString().length() > taille) {
						/*
						 * System.err.println("Erreur : La taille de la metadata "
						 * + colonne + " de valeur " + valeur +
						 * " ne correspond pas à la taille attendue :" + taille
						 * + "."); System.err
						 * .println("Les Metadatas ne seront pas exportés.");
						 */
						codeRetour = 2;
					}
				}
			}
		} else {
			if (!isNullable) {
				/*
				 * System.err.println("Erreur : La colonne " + colonne +
				 * " ne peut pas être nulle.");
				 * System.err.println("Les Metadatas ne seront pas exportés.");
				 */
				codeRetour = 3;
			}
		}
		return codeRetour;
	}

	public int getTaille(String type) {

		int taille = -1;
		if (type != null) {
			if (type.toLowerCase().contains("VARCHAR".toLowerCase())
					|| type.toLowerCase().contains("CHAR".toLowerCase())) {

				taille = Integer.parseInt(type.substring(type.indexOf("(") + 1,
						type.indexOf(")")));

				/*
				 * System.out.println("Taille : " + type.matches("(.*)"));
				 * System.out.println("REGEX : " + "\"([^\"]*)\"");
				 * System.out.println("REGEX2 : " + "[^\\(\\)]+");
				 * System.out.println("REGEX3 : " + "<.*>(.*)<.*>"); //
				 * "/(\\(.*\\))/g"
				 * 
				 * Matcher m = Pattern.compile(""\\((.*?)\\)""").matcher(type);
				 * while (m.find()) { System.out.println("Taille 2  : " +
				 * m.group(0)); }
				 */

			} else if (type.toLowerCase().contains("NUMBER".toLowerCase())
					|| type.toLowerCase().contains("NUMERIC".toLowerCase())) {
				taille = Integer.parseInt(type.substring(type.indexOf("(") + 1,
						type.indexOf(",")));
			}
		} else
			System.err.println("Erreur : le format de la colonne est inconnu.");
		return taille;
	}

	public String getType(String type) {

		String typeSortie = null;
		if (type != null) {
			if (type.toLowerCase().contains("VARCHAR".toLowerCase()))
				typeSortie = "String";
			else if (type.toLowerCase().contains("DATE".toLowerCase()))
				typeSortie = "Date";
			else if (type.toLowerCase().contains("NUMBER".toLowerCase()))
				typeSortie = "Integer";
			else if (type.toLowerCase().contains("NUMERIC".toLowerCase()))
				typeSortie = "Integer";
			else if (type.toLowerCase().contains("CHAR".toLowerCase()))
				typeSortie = "String";
			else if (type.toLowerCase().contains("CLOB".toLowerCase()))
				typeSortie = "String";
			else if (type.toLowerCase().contains("TEXT".toLowerCase()))
				typeSortie = "String";
		} else
			System.err.println("Erreur : le format de la colonne est inconnu.");
		return typeSortie;

	}

	/**
	 * Cette méthode permet de charger le contenu du fichier paramètre décrivant
	 * le format de la table Metadata
	 * 
	 * @param formatFile
	 *            Fichier contenant la description du format de la table
	 *            Metadata d'I2B2.
	 */
	public void readFormatFile(String formatFile) {

		try {
			// Création du flux bufférisé sur un FileReader, immédiatement suivi
			// par un
			// try/finally, ce qui permet de ne fermer le flux QUE s'il le
			// reader
			// est correctement instancié (évite les NullPointerException)

			BufferedReader buff = new BufferedReader(new FileReader(formatFile));
			try {
				String line;
				// Lecture du fichier ligne par ligne. Cette boucle se termine
				// quand la méthode retourne la valeur null.
				int numLigne = 1;
				while ((line = buff.readLine()) != null) {
					// on ignore l'entête
					if (numLigne != 1) {
						// On alimente un objet FormatTable
						String[] contenuLigne = line.split(";");
						if (contenuLigne.length == 3) {
							FormatTable formatTable = new FormatTable();
							formatTable.setColumn(contenuLigne[0]);
							formatTable.setType(getType(contenuLigne[1]));
							formatTable.setTaille(getTaille(contenuLigne[1]));
							if (contenuLigne[2].equalsIgnoreCase("yes"))
								formatTable.setNullable(true);
							else if (contenuLigne[2].equalsIgnoreCase("no"))
								formatTable.setNullable(false);
							else
								System.err
										.println("Valeur de Nullable incohérente. Valeur dans le fichier : "
												+ line);
							listeFormat.add(formatTable);
						} else {
							System.err
									.println("ERREUR : nombre de colonne lue incorrect. Ligne lue : "
											+ line);
						}

					}
					numLigne++;
				}
			} finally {
				// dans tous les cas, on ferme nos flux
				buff.close();
			}
		} catch (IOException ioe) {
			// erreur de fermeture des flux
			System.out.println("Erreur --" + ioe.toString());
		}
	}

	public String exportErreur(int codeErreur, String colonneErreur,
			Object valeurErreur, String ligne) {
		// Gestion de l'export d'erreur
		String ligneErreur = null;
		String messageErreur = null;
		if (codeErreur != 0) {
			switch (codeErreur) {
			case 1:
				messageErreur = "Le format de la metadata " + colonneErreur
						+ " de valeur " + valeurErreur.toString()
						+ " ne correspond pas au format attendu.";
				break;
			case 2:
				messageErreur = "La taille de la metadata " + colonneErreur
						+ " de valeur " + valeurErreur
						+ " ne correspond pas à la taille attendue.";
				break;
			case 3:
				messageErreur = "La colonne " + colonneErreur
						+ " ne peut pas être nulle.";
				break;
			default:
				break;
			}

			ligneErreur = ligne + ";" + codeErreur + ";"
					+ entoureGuillemet(messageErreur) + retourChariot;
		}
		return ligneErreur;
	}
}
