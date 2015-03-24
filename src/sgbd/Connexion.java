package sgbd;

import java.sql.*;
import java.util.Vector;

/**
 * 
 * Cette Classe effectue la connexion SQL et le requêtage des tables
 * 
 * @author Yoann Keravec
 * 
 * 
 * 
 */
public class Connexion {
	

	/**
	 * Objet Connection permettant la connexion avec la base MySQl.
	 * 
	 */
	private Connection connexion = null;

	/**
	 * Objet Statement permettant le requêtage des tables.
	 * 
	 * @see Connexion#creationStatement()
	 */
	private Statement statement = null;

	/**
	 * URL de connexion à la base de données.
	 */
	//private String url = "jdbc:mysql://localhost:3306/PROJETRI";
	//private String url = "jdbc:postgresql://localhost:5432/i2b2";
	private String url = "jdbc:postgresql://localhost/i2b2";
	/**
	 * Login de connexion à la base.
	 */
	private String utilisateur = "postgres";

	/**
	 * mot de passe de connexion à la base.
	 */
	private String motDePasse = "";

	/**
	 * Methode de chargement du driver MySQL.
	 * 
	 */
	
	public Connexion() {
	}
	
	public Connexion(String utilisateur, String mdp) {
		this.utilisateur = utilisateur;
		this.motDePasse = mdp;
	}
	
	public int chargementDriver() {

		/* Chargement du driver JDBC pour MySQL */
		try {
			System.out.println("[Technique] Chargement du driver...");
			Class.forName("org.postgresql.Driver");
			System.out.println("[Technique] Driver charge.");
			return 0;
		} catch (ClassNotFoundException e) {
			System.err
					.println("[Technique] Erreur lors du chargement : le driver n'a pas ete trouve dans le classpath ! <br/>"
							+ e.getMessage());
			e.printStackTrace();
			return 1;
		}

	}

	/**
	 * Cette méthode crée la connexion à la base MySQL.
	 * 
	 * @return code retour de bonne fin. 0 : OK , 1 : erreur
	 * @see Connexion#connexion
	 */
	public int initConnexion() {
		/* Connexion à la base de données */

		try {
			System.out.println("[Technique] Connexion a la base de donnees...");
			this.connexion = DriverManager.getConnection(url, utilisateur,
					motDePasse);
			System.out.println("[Technique] Connexion reussie !");
			return 0;
		} catch (SQLException e) {
			System.err
					.println("[Technique] Erreur lors de la connexion : <br/>"
							+ e.getMessage());
			return 1;
		}
	}

	/**
	 * Cette méthode effectue la fermeture de la connexion avec la base MySQL.
	 * 
	 * @return code retour de bonne fin. 0 : OK , 1 : erreur
	 * @see Connexion#statement
	 */
	public int fermetureConnexion() {
		if (connexion != null) {
			try {
				connexion.close();
				System.out
						.println("[Technique] Fermeture de l'objet Connection.");
				return 0;
			} catch (SQLException e) {
				System.err
						.println("[Technique] Erreur lors de la fermeture de l'objet Connection. "
								+ e.getMessage());
				e.printStackTrace();
				return 1;
			}
		} else
			return 0;
	}

	/**
	 * Cette méthode crée le statement.
	 * 
	 * @return code retour de bonne fin. 0 : OK , 1 : erreur
	 * 
	 */
	public int creationStatement() {
		/* Création de l'objet gérant les requêtes */
		try {

			this.statement = connexion.createStatement();
			System.out.println("[Technique] Objet requete cree !");
			return 0;
		} catch (SQLException erreur) {
			System.err.println("[Technique] Erreur à la creation du statement"
					+ erreur.getMessage());
			erreur.printStackTrace();
			return 1;
		}

	}

	/**
	 * Cette méthode effectue la fermeture de l'objet statement.
	 * 
	 * @return code retour de bonne fin. 0 : OK , 1 : erreur
	 * @see Connexion#statement
	 */
	public int fermetureStatement() {
		if (this.statement != null) {
			try {
				statement.close();
				System.out
						.println("[Technique] Fermeture de l'objet Statement.");
				return 0;
			} catch (SQLException erreur) {
				System.err
						.println("[Technique] Erreur lors de la fermeture de l'objet Statement "
								+ erreur.getMessage());
				erreur.printStackTrace();

				return 1;
			}
		} else
			return 0;
	}

	/**
	 * Cette méthode génère un ResultSet à partir d'un objet Requete contenant
	 * du code SQL.
	 * 
	 * @param r
	 *            : l'objet Requete
	 * @return un ResultSet
	 * @see Requete
	 */
	public ResultSet executerRequete(Requete r) {

		ResultSet resultat = null;
		/* Exécution d'une requête de lecture */
		//System.out.println("La requete lancée est " + r.getRequeteALancer());

		try {
			resultat = this.statement.executeQuery(r.getRequeteALancer());

		} catch (SQLException e) {
			System.err.println("[Technique] Erreur a l'execution de la requete"
					+ e.getMessage());
			e.printStackTrace();
		}
		return resultat;
	}

	/**
	 * Cette méthode génère un ResultSet à partir d'un objet Requete contenant
	 * du code SQL.
	 * 
	 * @param r
	 *            : l'objet Requete
	 * @return un ResultSet
	 * @see Requete
	 */
	public ResultSet executerRequete(String r) {

		ResultSet resultat = null;
		/* Exécution d'une requête de lecture */
		//System.out.println("La requete lancée est " + r.getRequeteALancer());

		try {
			resultat = this.statement.executeQuery(r);

		} catch (SQLException e) {
			System.err.println("[Technique] Erreur a l'execution de la requete"
					+ e.getMessage());
			e.printStackTrace();
		}
		return resultat;
	}
	
	/**
	 * Cette méthode génère un ResultSet à partir d'un objet Requete contenant
	 * du code SQL.
	 * 
	 * @param r
	 *            : l'objet Requete
	 * @return un ResultSet
	 * @see Requete
	 */
	public int executerInsert(String r) {

		int resultat = 0;
		/* Exécution d'une requête de lecture */
		//System.out.println("La requete lancée est " + r.getRequeteALancer());

		try {
			System.out.println("Requête : " + r);
			resultat = this.statement.executeUpdate(r);
			//resultat = this.statement.executeUpdate("INSERT INTO Metadata (C_HLEVEL,C_FULLNAME,C_NAME,C_SYNONYM_CD,C_VISUALATTRIBUTES,C_TOTALNUM,C_BASECODE,C_METADATAXML,C_FACTTABLECOLUMN,C_TABLENAME,C_COLUMNNAME,C_COLUMNDATATYPE,C_OPERATOR,C_DIMCODE,C_COMMENT,C_TOOTIP,M_APPLIED_PATH,UPDATE_DATE,DOWNLOAD_DATE,IMPORT_DATE,SOUCESYSTEM_CD,VALUETYPE_CD,M_EXCLUSION_CD,C_PATH,C_SYMBOL) VALUES (0,'\\i2b2\\','i2b2','N','FA',null,'BCBS:1',null,'concept_cd','concept_dimension','concept_path','T','LIKE','\\i2b2\\',null,'\\i2b2','@',current_timestamp,null,null,'skosSortiePrefix.owl',null,null,'','i2b2');");

		} catch (SQLException e) {
			System.err.println("[Technique] Erreur a l'execution de la requete"
					+ e.getMessage());
			e.printStackTrace();
		}
		return resultat;
	}
	
	
	/**
	 * Cette méthode permet de connaître le nombre de lignes présentes dans le ResultSet en entrée.
	 * @param resultat : L'objet ResultSet dont on veut connaître le nombre de lignes.
	 * @return le nombre de lignes présentes dans le ResultSet.
	 */
	public int nbLignesResultat(ResultSet resultat) {

		int nombreLignes;

		try {

			// on place le curseur sur le dernier tuple
			resultat.last();
			// on r�cup�re le num�ro de la ligne
			nombreLignes = resultat.getRow();

			// System.out.println("Nombre de lignes " + nombreLignes);

			// on replace le curseur avant la premi�re ligne
			resultat.beforeFirst();

		} catch (SQLException erreur) {
			System.err
					.println("nbLignesResultat : Erreur a l'exploitation des resultats "
							+ erreur.getMessage());
			erreur.printStackTrace();
			return 1;
		}
		return nombreLignes;

	}

	/**
	 * Cette méthode permet de connaître le nombre de colonnes d'un ResultSet.
	 * @param resultat : le ResultSet dont on veut connaître le nombre de colonnes.
	 * @return : le nombre de colonnes.
	 */
	public int nbColonnesResultat(ResultSet resultat) {
		int nombreColonnes;

		try {

			ResultSetMetaData resultatStructure = resultat.getMetaData();
			nombreColonnes = resultatStructure.getColumnCount();

		} catch (SQLException erreur) {
			System.err
					.println("Nb Colonnes : Erreur a l'exploitation des resultats"
							+ erreur.getMessage());
			erreur.printStackTrace();
			return 1;
		}
		return nombreColonnes;

	}

	/**
	 * Cette méthode crée une liste (vecteur) des noms de colonnes présentes 
	 * dans le ResultSet en entrée.
	 * @param resultat : 
	 * @return : Un vecteur contenant les noms de colonnes.
	 */
	public Vector<String> listeColonnesResultat(ResultSet resultat) {

		try {

			ResultSetMetaData resultatStructure = resultat.getMetaData();
			int nbColonnes = resultatStructure.getColumnCount();
			Vector<String> listeColonnes = new Vector<String>();
			;

			String nomColonne;
			for (int i = 1; i <= nbColonnes; i++) {

				nomColonne = resultatStructure.getColumnName(i);
				System.out.println("Nom de la colonne " + nomColonne);
				listeColonnes.add(nomColonne);

			}
			return listeColonnes;

		} catch (SQLException erreur) {
			System.err
					.println("Liste Colonnes : Erreur a l'exploitation des resultats "
							+ erreur.getMessage());
			erreur.printStackTrace();
			return null;

		}
	}

}