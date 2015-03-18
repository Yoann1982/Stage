package load;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe permet de générer un fichier CSV à partir d'une liste de
 * Metadata
 * 
 * @author y.keravec
 *
 */
public class MetadataToCSV {

	private FileWriter fichier;
	private List<FormatTable> listeFormat = new ArrayList<FormatTable>();

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

	public void writeRecord(Metadata meta, String separator) {
		// On récupère chaque champ
		// On le compare au format attendu
		// Si le format est incorrect, on écrit le champ en erreur dans un
		// fichier de sortie de log.

		String cHlevel = stringNull(meta.getcHLevel());

		
		
		String cFullname = stringNull(meta.getcFullName());
		String cName = stringNull(meta.getcName());
		String synonym = stringNull(meta.getcSynonym());
		String visualAttributes = stringNull(meta.getcVisualAttributes());
		String totalNum = stringNull(meta.getcTotalNum());
		String basecode = stringNull(meta.getcBaseCode());
		String metadataXML = stringNull(meta.getcMetaDataXML());
		String cFacttablecolumn = stringNull(meta.getcFactTableColumn());
		String cTableName = stringNull(meta.getcTableName());
		String cColumnName = stringNull(meta.getcColumnName());
		String cColumnDataType = stringNull(meta.getcColumnDataType());
		String cOperator = stringNull(meta.getcOperator());
		String cDimcode = stringNull(meta.getcDimCode());
		String cComment = stringNull(meta.getcComment());
		String cTooltip = stringNull(meta.getcTooltip());
		String mAppliedPath = stringNull(meta.getmAppliedPath());
		String updateDate = stringNull(meta.getUpdateDate());
		String downloadDate = stringNull(meta.getDownloadDate());
		String importDate = stringNull(meta.getImportDate());
		String sourceSystem = stringNull(meta.getSourceSystem());
		String valueType = stringNull(meta.getValueType());
		String mExclusion = stringNull(meta.getmExclusion());
		String cPath = stringNull(meta.getcPath());
		String cSymbol = stringNull(meta.getcSymbol());
		String ligne = cHlevel + separator + cFullname + separator + cName
				+ separator + synonym + separator + visualAttributes
				+ separator + totalNum + separator + basecode + separator
				+ metadataXML + separator + cFacttablecolumn + separator
				+ cTableName + separator + cColumnName + separator
				+ cColumnDataType + separator + cOperator + separator
				+ cDimcode + separator + cComment + separator + cTooltip
				+ separator + mAppliedPath + separator + updateDate + separator
				+ downloadDate + separator + importDate + separator
				+ sourceSystem + separator + valueType + separator + mExclusion
				+ separator + cPath + separator + cSymbol + "\n";
		try {
			fichier.write(ligne, 0, ligne.length());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String stringNull(String input) {

		if (input == null)
			return "";
		else
			return input;
	}

	public String stringNull(Date input) {

		if (input == null)
			return "";
		else
			return input.toString();
	}

	public String stringNull(Integer input) {

		if (input == null)
			return "";
		else
			return input.toString();
	}
	
	public void writeHeader(String separator) {

		String header = "";
		// On va parcourir la liste de format pour retrouver le nom des
		// colonnes.
		int cpt = 0;
		System.out.print("HEADER");
		for (FormatTable formatLigne : listeFormat) {
			if (cpt == 0) {
				header += formatLigne.getColumn();
				System.out.print(formatLigne.getColumn());
			} else {
				header += separator + formatLigne.getColumn();
				System.out.print(" " + formatLigne.getColumn());
			}
			cpt++;
		}
		header += "\n";
		System.out.print("\n");
		try {
			fichier.write(header);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
							formatTable.setType(contenuLigne[1]);
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

}
