package load;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import transco.Builder;

/**
 * Cette classe gère le chargement d'une hiérarchie SKOS au sein de la table
 * Metadata d'I2B2.
 * 
 * @author Yoann Keravec Date: 13/03/2015<br>
 *         Institut Bergonié<br>
 */
public class SKOSToI2B2Builder extends Builder {

	private List<Metadata> listeMetadata = new ArrayList<Metadata>();
	//private PrefixManager prefixOnto;
	//private String iriSKOS = "http://www.w3.org/2004/02/skos/core#";
	//private PrefixManager prefixSKOS = new DefaultPrefixManager(iriSKOS);
	private String inputFile;

	/**
	 * Constructeur de la classe SKOSToI2B2Builder.
	 * 
	 * @param ontologie
	 *            Ontologie d'origine (OWL) @see OWLOntology.
	 */
	public SKOSToI2B2Builder(OWLOntology ontologie) {
		this.originalOntology = ontologie;
	}

	/*
	 * Principe de la classe Chargement du fichier en entrée à l'aide de la
	 * classe OWLReader Recherche du ConceptScheme pour initialiser le niveau 0
	 * Recherche au sein de l'ontologie du concept "I2B2" Traitement récursif
	 * sur les fils pour créer les enregistrements => méthode dédiée
	 */

	/**
	 * Cette méthode permet de créer un enregistrement Metadata et de le stocker
	 * dans la liste d'enregistrement de la classe
	 */
	public Metadata createMetadataRecord(OWLIndividual individu, int niveau,
			String hierarchie, boolean isNoeud) {
		Metadata metadata = new Metadata();

		String prefLabel = getAnnotation(individu, prefixSKOS, "prefLabel");
		String basecode = getAnnotation(individu, prefixOnto, "C_BASECODE");

		// Retraitement de hierarchie:
		// Ajout des majuscules
		String hierarchieModif = WordUtils.capitalize(hierarchie);
		// Suppression des caractères spéciaux
		hierarchieModif = hierarchieModif.replaceAll("[^\\w\\\\]", "");
		String prefLabelModif = null;
		if (!prefLabel.equalsIgnoreCase("i2b2")) {
			// Pour i2b2 on ne met pas de majuscule
			// Même opération pour prefLabel
			prefLabelModif = WordUtils.capitalize(prefLabel);
			prefLabelModif = prefLabelModif.replaceAll("[^\\w\\\\]", "");
		} else prefLabelModif = prefLabel;

		String cPath = hierarchieModif + "\\";
		String cSymbol = prefLabelModif;
		String tooltip = cPath + cSymbol;
		String fullname = cPath + cSymbol + "\\";
		String nomFichier = "";
		nomFichier = new File(inputFile).getName();

		metadata.put("C_HLEVEL", niveau);
		metadata.put("C_FULLNAME", fullname);
		metadata.put("C_NAME", prefLabelModif);
		metadata.put("C_SYNONYM_CD", "N");
		if (isNoeud)
			// metadata.setcVisualAttributes("FA");
			metadata.put("C_VISUALATTRIBUTES", "FA");
		else
			// metadata.setcVisualAttributes("LA");
			metadata.put("C_VISUALATTRIBUTES", "LA");
		metadata.put("C_BASECODE", basecode);
		metadata.put("C_FACTTABLECOLUMN", "concept_cd");
		metadata.put("C_TABLENAME", "concept_dimension");
		metadata.put("C_COLUMNNAME", "concept_path");
		metadata.put("C_COLUMNDATATYPE", "T");
		metadata.put("C_OPERATOR", "LIKE");
		metadata.put("C_DIMCODE", fullname);
		metadata.put("C_TOOTIP", tooltip);
		metadata.put("M_APPLIED_PATH", "@");
		metadata.put("SOUCESYSTEM_CD", nomFichier);
		metadata.put("C_PATH", cPath);
		metadata.put("C_SYMBOL", cSymbol);

		return metadata;
	}

	/**
	 * @return the listeMetadata
	 */
	public List<Metadata> getListeMetadata() {
		return listeMetadata;
	}

	/**
	 * @param listeMetadata
	 *            the listeMetadata to set
	 */
	public void setListeMetadata(List<Metadata> listeMetadata) {
		this.listeMetadata = listeMetadata;
	}

	/**
	 * @return the inputFile
	 */
	public String getInputFile() {
		return inputFile;
	}

	/**
	 * @param inputFile
	 *            the inputFile to set
	 */
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	/**
	 * Cette méthode recherche les ConceptScheme. Pour chaque valeur retrouvé,
	 * un enregistrement de niveau 0 est créée.
	 */
	public void findAndCreateConceptScheme(int niveau) {

		// On recherche tous les ConceptScheme et pour chacun, on crée un
		// enregistrement
		// List<OWLNamedIndividual> scheme = fact.get

		// On récupère la liste d'individu de type ConceptScheme
		List<OWLIndividual> listeIndiv = findSchemeByObjectProperty(
				"ConceptScheme", prefixSKOS);

		// Pour chaque occurence on crée un record
		for (OWLIndividual curIndiv : listeIndiv) {
			createRecord(curIndiv, niveau, "\\"
					+ curIndiv.asOWLNamedIndividual().getIRI().toURI()
							.getFragment(), true);
		}

	}

	/**
	 * Cette méthode récursive permet de créer les enregistrements
	 * correspondants aux fils du concept en entrée
	 */
	public void creationEnregistrement(OWLNamedIndividual individu, int niveau) {

	}

	public void load(String conceptStart, int niveau) {
		// On recherche l'IRI de l'ontologie
		iriProject = foundIriProjectByIndividual();
		// On crée le préfixe associé
		if (iriProject != null) {
			prefixOnto = new DefaultPrefixManager(iriProject.toString() + "#");
			//int niveau = 0;

			// Recherche du Concept I2B2 et créations des enregistrements à
			// partir
			// de ce concept

			// On vérifie que le concept de départ fait bien partie de l'ontologie
			// Si ce n'est pas le cas, on alimente avec tout.
			OWLNamedIndividual startItem = null;
			if (conceptIsInOntology(conceptStart)) {
				startItem = rechercheIndividu(conceptStart,
						prefixOnto.getDefaultPrefix());
			} else {
				// on commence à i2b2
				startItem = rechercheIndividu("1",
						prefixOnto.getDefaultPrefix());
			}

			createRecord(startItem, niveau, "", true);

			String hierarchie = "\\"
					+ getAnnotation(startItem, prefixSKOS, "prefLabel");

			// On parcours les narrower de cet individu et on crée un
			// enregistrement
			// de niveau inférieur
			List<OWLIndividual> listeNarrowers = getNarrowers(startItem);

			// creation des enregistrement
			if (listeNarrowers != null && !listeNarrowers.isEmpty())
				createNarrowers(listeNarrowers, niveau, hierarchie);
		} else {
			System.err
					.println("L'IRI du projet n'a pu être trouvée. Arrêt des traitements.");
		}
	}

	public void createNarrowers(List<OWLIndividual> listeIndiv, int niveau,
			String hierarchie) {
		// OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		// OWLDataFactory fact = manager.getOWLDataFactory();

		for (OWLIndividual curseurIndiv : listeIndiv) {
			List<OWLIndividual> listeIndiv2 = getNarrowers(curseurIndiv);
			if (listeIndiv2 != null && !listeIndiv2.isEmpty()) {
				// Il s'agit d'un noeud
				createRecord(curseurIndiv, niveau + 1, hierarchie, true);
				String hierarchie2 = hierarchie + "\\"
						+ getAnnotation(curseurIndiv, prefixSKOS, "prefLabel");
				createNarrowers(listeIndiv2, niveau + 1, hierarchie2);
			} else {
				// Il s'agit d'une feuille
				createRecord(curseurIndiv, niveau + 1, hierarchie, false);
			}

		}
	}

	/**
	 * Cette methode crée un enregistrement.
	 * 
	 * @param niveau
	 */
	public void createRecord(OWLIndividual individu, int niveau,
			String hierarchie, boolean isNoeud) {

		// Création des metadata
		listeMetadata.add(createMetadataRecord(individu, niveau, hierarchie,
				isNoeud));

	}

	/**
	 * Cette méthode permet de liste tous les ObjectProperty de l'ontologie.
	 */
	public void listOP() {
		System.out.println("OP : "
				+ originalOntology.getObjectPropertiesInSignature());
	}

}
