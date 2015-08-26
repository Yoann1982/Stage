package load;

import java.util.ArrayList;
import java.util.List;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator;
import org.semanticweb.HermiT.Reasoner;

import uk.ac.manchester.cs.owl.owlapi.OWLOntologyImpl;

/**
 * Cette classe contient les méthodes permettant d'effectuer du résonnement sur
 * une ontologie.
 * 
 * @author Yoann Keravec <br>
 *         Date: 19/03/2015<br>
 *         Institut Bergonié<br>
 */

public class Raisonneur {

	private OWLOntology ontology;
	private Reasoner hermit;

	public Raisonneur(OWLOntology onto) {

		this.ontology = onto;
		hermit = new Reasoner(onto);
	}

	/**
	 * Cette méthode permet de restituer une ontologie correspondante à celle
	 * chargée complétée de toute les inférences sur les propriétés. Le
	 * résonneur utilisé est HermiT.
	 * 
	 * @return @see OWLOntology L'ontologie complétée.
	 */
	public OWLOntology findPropertyAssertion() {

		List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
		new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
		gens.add(new InferredPropertyAssertionGenerator());
		OWLOntologyManager m = ontology.getOWLOntologyManager();

		OWLOntology infOnt = new OWLOntologyImpl(m, new OWLOntologyID());
		infOnt = ontology;

		InferredOntologyGenerator iog = new InferredOntologyGenerator(hermit,
				gens);
		iog.fillOntology(m, infOnt);
		return infOnt;
	}

}
