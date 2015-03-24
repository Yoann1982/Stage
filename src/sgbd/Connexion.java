package sgbd;

import java.sql.*;

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
	 *            : requête SQL.
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
	 * Cette méthode effectue un ordre d'update ou d'insert et renvoie un code d'erreur. 0 si ok.
	 * 
	 * @param r
	 *            : la requête à executer.
	 */
	public int executerInsert(String r) {

		int resultat = 0;
		/* Exécution d'une requête de lecture */
		//System.out.println("La requete lancée est " + r.getRequeteALancer());

		try {
			resultat = this.statement.executeUpdate(r);

		} catch (SQLException e) {
			System.err.println("[Technique] Erreur a l'execution de la requete"
					+ e.getMessage());
			e.printStackTrace();
		}
		return resultat;
	}
}