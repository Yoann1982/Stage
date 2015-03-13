package load;

import transco.OWLReader;

/**
 * Cette classe gère le chargement d'une hiérarchie SKOS au sein de la table
 * Metadata d'I2B2.
 * 
 * @author Yoann Keravec Date: 13/03/2015<br>
 *         Institut Bergonié<br>
 */
public class SKOSToI2B2 {

	/*
	 * Principe de la classe
	 * Chargement du fichier en entrée à l'aide de la classe OWLReader
	 * Recherche du ConceptScheme pour initialiser le niveau 0
	 * Recherche au sein de l'ontologie du concept "I2B2"
	 * Traitement récursif sur les fils pour créer les enregistrements => méthode dédiée 
	 */
	
	public void load(String input) {
		
		// Chargement du fichier SKOS
		OWLReader reader = new OWLReader();
		reader.loadOntology(input);
		
		// Recherche
		
	}
	
	
}
