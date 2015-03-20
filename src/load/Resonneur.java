package load;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator;
import org.semanticweb.HermiT.Reasoner;

import transco.WriteOntology;
import uk.ac.manchester.cs.owl.owlapi.OWLOntologyImpl;

public class Resonneur {

	private OWLOntology ontology;
	private OWLReasonerFactory reasonerFactory;
	private Reasoner hermit;

	public Resonneur(OWLOntology onto) {

		this.ontology = onto;
		hermit = new Reasoner(onto);
	}

	public void testUnsatisfiableClasses(OWLOntology onto) throws Exception {
		// a config object. Things like monitor, timeout, etc, go here
		OWLReasonerConfiguration config = new SimpleConfiguration(50000);
		// Create a reasoner that will reason over our ontology and its imports
		// closure. Pass in the configuration.
		OWLReasoner reasoner = this.reasonerFactory
				.createReasoner(onto, config);
		// Ask the reasoner to classify the ontology
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		// We can determine if the ontology is actually consistent (in this
		// case, it should be).
		// assertTrue(reasoner.isConsistent());
		// get a list of unsatisfiable classes
		Node<OWLClass> bottomNode = reasoner.getUnsatisfiableClasses();
		// leave owl:Nothing out
		Set<OWLClass> unsatisfiable = bottomNode.getEntitiesMinusBottom();
		if (!unsatisfiable.isEmpty()) {
			System.out.println("The following classes are unsatisfiable: ");
			for (OWLClass cls : unsatisfiable) {
				System.out.println(cls.getIRI().getFragment());
			}
		} else {
			System.out.println("There are no unsatisfiable classes");
		}
		// Look up and print all direct subclasses for all classes
		for (OWLClass c : this.ontology.getClassesInSignature()) {
			// the boolean argument specifies direct subclasses; false would
			// specify all subclasses
			// a NodeSet represents a set of Nodes.
			// a Node represents a set of equivalent classes
			NodeSet<OWLClass> subClasses = reasoner.getSubClasses(c, true);
			for (OWLClass subClass : subClasses.getFlattened()) {
				System.out.println(subClass.getIRI().getFragment()
						+ "\tsubclass of\t" + c.getIRI().getFragment());
			}
		}
		// for each class, look up the instances
		for (OWLClass c : this.ontology.getClassesInSignature()) {
			// the boolean argument specifies direct subclasses; false would
			// specify all subclasses
			// a NodeSet represents a set of Nodes.
			// a Node represents a set of equivalent classes/or sameAs
			// individuals
			NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c,
					true);
			for (OWLNamedIndividual i : instances.getFlattened()) {
				System.out.println(i.getIRI().toURI().getFragment()
						+ "\tinstance of\t" + c.getIRI().toURI().getFragment());
				// look up all property assertions
				for (OWLObjectProperty op : this.ontology
						.getObjectPropertiesInSignature()) {
					NodeSet<OWLNamedIndividual> petValuesNodeSet = reasoner
							.getObjectPropertyValues(i, op);
					for (OWLNamedIndividual value : petValuesNodeSet
							.getFlattened()) {
						System.out.println(i.getIRI().toURI().getFragment()
								+ "\t" + op.getIRI().toURI().getFragment()
								+ "\t" + value.getIRI().toURI().getFragment());
					}
				}
			}
		}
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
		WriteOntology writer = new WriteOntology(infOnt);
		writer.writeFile("C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\RESONNEUR.owl",new RDFXMLOntologyFormat());
		return infOnt;
	}

}
