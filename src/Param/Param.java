package Param;

public class Param {

	public static final String RETOUR_CHARIOT = System.lineSeparator();
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	
	public static final String  HOME = System.getProperty("user.home");
	public static final String  REPERTOIRE_OUTPUT = HOME
			+ FILE_SEPARATOR + "I2B2"
			+ FILE_SEPARATOR + "OUTPUT"
			+ FILE_SEPARATOR;

	public static final String REPERTOIRE_ERREUR = HOME
			+ FILE_SEPARATOR + "I2B2"
			+ FILE_SEPARATOR + "ERREUR"
			+ FILE_SEPARATOR;
	
	public static final String REPETOIRE_PARAM = HOME
			+ FILE_SEPARATOR + "I2B2"
			+ FILE_SEPARATOR + "PARAM"
			+ FILE_SEPARATOR;
	
	public static final String FICHIER_PARAM_ORACLE = REPETOIRE_PARAM + "formatOracle.csv";
	public static final String FICHIER_PARAM_MYSQL = REPETOIRE_PARAM + "formatMySQL.csv";
	public static final String FICHIER_PARAM_POSTGRESQSL = REPETOIRE_PARAM + "formatPostgreSQL.csv";
	
}
