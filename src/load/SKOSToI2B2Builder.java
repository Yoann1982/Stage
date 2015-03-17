package load;

import java.util.ArrayList;
import java.util.List;

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
	private PrefixManager prefixOnto;
	private String iriSKOS = "http://www.w3.org/2004/02/skos/core#";
	private PrefixManager prefixSKOS = new DefaultPrefixManager(iriSKOS);
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
		String cPath = hierarchie;
		String cSymbol = prefLabel;
		String tooltip = cPath + "\\" + cSymbol;
		String fullname = cPath + "\\" + cSymbol + "\\";

		metadata.setcHLevel(niveau);
		metadata.setcFullName(fullname);
		metadata.setcName(prefLabel);
		metadata.setcSynonym('N');
		if (isNoeud)
			metadata.setcVisualAttributes("FA");
		else
			metadata.setcVisualAttributes("LA");
		metadata.setcBaseCode(basecode);
		metadata.setSourceSystem(inputFile);
		metadata.setcPath(cPath);
		metadata.setcSymbol(cSymbol);
		metadata.setcFactTableColumn("concept_cd");
		metadata.setcTableName("concept_dimension");
		metadata.setcColumnName("concept_path");
		metadata.setcColumnDataType("T");
		metadata.setcOperator("LIKE");
		metadata.setcDimCode(fullname);
		metadata.setcTooltip(tooltip);
		metadata.setmAppliedPath("@");
		System.out.print("HLEVEL : " + niveau + "\t");
		System.out.print("FullName : " + fullname + "\t");
		System.out.print("Name : " + prefLabel + "\t");
		System.out.print("Basecode : " + basecode);
		System.out.print("SourceSystem : " + inputFile + "\t");
		System.out.print("C_PATH : " + cPath + "\t");
		System.out.print("C_SYMBOL : " + cSymbol + "\t");
		System.out.print("\n");
		return metadata;
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

	public void load() {

		// On recherche l'IRI de l'ontologie
		iriProject = foundIriProjectByIndividual();
		// On crée le préfixe associé
		if (iriProject != null) {
			prefixOnto = new DefaultPrefixManager(iriProject.toString() + "#");

			System.out.println("Création des niveaux 0.");
			// Chargement d'un résoneur pour pouvoir trouver les broader mais
			// également les narrower

			// Recherche des ConceptScheme et création au niveau 0
			int niveau = 0;
			findAndCreateConceptScheme(niveau);

			System.out.println("Création des niveaux 1 et supérieur.");

			// Recherche du Concept I2B2 et créations des enregistrements à
			// partir
			// de ce concept
			// Set<OWLNamedIndividual> listeIndiv = ontology
			// .getIndividualsInSignature();

			// On recherche l'élément Thing et on parcours ses fils

			// On cherche l'individu correspondant à #2 (SarcomaBcb)

			OWLNamedIndividual i2b2 = rechercheIndividu("1",
					prefixOnto.getDefaultPrefix());

			String hierarchie = "\\"
					+ getAnnotation(i2b2, prefixSKOS, "prefLabel");

			// OWLNamedIndividual indivI2B2 =
			// fact.getOWLNamedIndividual("1",prefixOnto);

			// On parcours les narrower de cet individu et on crée un
			// enregistrement
			// de niveau inférieur
			List<OWLIndividual> listeNarrowers = getNarrowers(i2b2);

			// creation des enregistrement
			if (listeNarrowers != null && !listeNarrowers.isEmpty())
				createNarrowers(listeNarrowers, niveau, hierarchie);
		} else {
			System.out
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

		// System.out.println("Création de l'enregistrement : " + individu
		// + " au niveau " + niveau + ".");
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
