package transco;

import java.io.File;
import java.net.URI;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;

/**
 * Cette méthode permet le chargement d'un fichier OWL en mémoire
 * 
 * @author Yoann Keravec<br>
 *         Date: 09/03/2015<br>
 *         Institut Bergonié<br>
 */

public class OWLReader {

	private OWLOntology ontology;
	private OWLOntologyFormat format;

	/**
	 * Getter de l'attribut format.
	 * 
	 * @return @see OWLOntologyFormat
	 */
	public OWLOntologyFormat getFormat() {
		return format;
	}

	/**
	 * Setter de l'attribut format.
	 * 
	 * @param @see OWLOntologyFormat format
	 */
	public void setFormat(OWLOntologyFormat format) {
		this.format = format;
	}

	/**
	 * Cette méthode permet de récupérer l'ontologie chargée.
	 * 
	 * @return Une ontologie.
	 */
	public OWLOntology getOntology() {
		return ontology;
	}

	/**
	 * Cette méthode permet de charger une ontologie en mémoire.
	 * 
	 * @param ontologie
	 */
	public void setOntologie(OWLOntology ontologie) {
		this.ontology = ontologie;
	}

	/**
	 * Cette méthode vérifie s'il y a des prefix "vide" dans l'attribut format
	 * et les supprime de celui-ci.
	 */
	public void cleanFormat() {

		if (format != null) {

			PrefixOWLOntologyFormat formatCopy = new PrefixOWLOntologyFormat();
			formatCopy.copyPrefixesFrom(format.asPrefixOWLOntologyFormat());

			format.asPrefixOWLOntologyFormat().clearPrefixes();

			if (formatCopy.isPrefixOWLOntologyFormat()) {
				Set<String> listePrefix = formatCopy
						.asPrefixOWLOntologyFormat().getPrefixNames();
				for (String prefix : listePrefix) {
					if (!prefix.equals(":")) {
						// On le remet dans le format
						format.asPrefixOWLOntologyFormat().setPrefix(
								prefix,
								formatCopy.asPrefixOWLOntologyFormat()
										.getIRI(prefix).toString());
					}
				}
			}
		}
	}

	/**
	 * Cette méthode permet de charger en mémoire une ontologie à partir d'un
	 * fichier OWL. Elle vérifie si l'IRI en entrée est listée dans les prefix
	 * de l'ontologie. Si ce n'est pas le cas, elle crée un nouveau prefix
	 * correspondant.
	 * 
	 * @param nomFichier
	 *            Fichier OWL.
	 * @param iriOnto
	 *            Chaîne de caractèrs correspondant à l'IRI de l'ontologie.
	 * @param prefixEntree
	 *            Chaînes de caractères correspondant au préfixe devant être
	 *            ajouté si l'IRI n'est pas listée dans les préfixes du fichier
	 *            en entrée.
	 */
	public void loadOntology(String nomFichier, String iriOnto,
			String prefixEntree) {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		URI uriFichier = new File(nomFichier).toURI();
		IRI ontologyIRIOWL = IRI.create(uriFichier);
		try {
			this.ontology = manager.loadOntology(ontologyIRIOWL);
			// On récupère le formalisme de l'ontologie traitée
			format = manager.getOntologyFormat(ontology);

			// on supprime les prefix vide
			cleanFormat();

			// On vérifie s'il s'agit d'un format qui accepte les prefixes.
			// Dans ce cas, on va récupérer la liste de préfix et vérifier
			// S'il y en a un qui correspond à l'IRI indiquée en paramètre.
			// Si il n'y a pas de prefix associé, on l'ajoute

			if (format.isPrefixOWLOntologyFormat()) {
				Set<String> listePrefix = format.asPrefixOWLOntologyFormat()
						.getPrefixNames();
				boolean prefixPresent = false;
				for (String prefix : listePrefix) {
					if (IRI.create(iriOnto).equals(
							format.asPrefixOWLOntologyFormat().getIRI(prefix))) {
						prefixPresent = true;
						break;
					}
				}
				// Si on n'a pas retrouvé de prefix associé, on l'ajoute
				if (!prefixPresent) {
					if (iriOnto.contains("#")) {
						format.asPrefixOWLOntologyFormat().setPrefix(
								prefixEntree, iriOnto);
					} else {
						format.asPrefixOWLOntologyFormat().setPrefix(
								prefixEntree, iriOnto + "#");
					}
				}
			}
		} catch (OWLOntologyCreationException e) {
			System.err
					.println("Erreur lors du chargement de l'ontologie (appel via Manager).");
			e.printStackTrace();
		}
	}

	/**
	 * Cette méthode permet de charger en mémoire une ontologie à partir d'un
	 * fichier OWL Elle vérifie si l'IRI en entrée est listée dans les prefix de
	 * l'ontologie Si ce n'est pas le cas, elle crée un nouveau prefix
	 * correspondant (avec la valeur par défaut "bcbs").
	 * 
	 * @param nomFichier
	 *            Fichier OWL.
	 * @param iriOnto
	 *            Chaînes de caractères correspondant à l'IRI de l'ontologie.
	 */
	public void loadOntology(String nomFichier, String iriOnto) {
		loadOntology(nomFichier, iriOnto, "bcbs");
	}

	/**
	 * Cette méthode permet de charger en mémoire une ontologie à partir d'un
	 * fichier OWL. L'IRI n'est pas connu et sera récupéré par parcours du contenu du fichier.
	 * 
	 * @param nomFichier
	 *            Fichier OWL.
	 */
	public void loadOntology(String nomFichier) {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		URI uriFichier = new File(nomFichier).toURI();
		IRI ontologyIRIOWL = IRI.create(uriFichier);
		try {
			this.ontology = manager.loadOntology(ontologyIRIOWL);
			// On récupère le formalisme de l'ontologie traitée
			format = manager.getOntologyFormat(ontology);
		} catch (OWLOntologyCreationException e) {
			System.err
					.println("Erreur lors du chargement de l'ontologie (appel via Manager).");
			e.printStackTrace();
		}
	}
}
