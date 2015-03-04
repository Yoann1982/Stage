package trucautres;

//import java.net.URI;

//import java.util.HashMap;
import java.util.Iterator;
//import java.util.Map;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
//import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
//import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
//import org.semanticweb.owlapi.model.change.AddAnnotationInstance;
//import org.semanticweb.owlapi.model.change.AddEntity;
//import org.semanticweb.owlapi.model.change.ChangeVisitor;
//import org.semanticweb.owlapi.model.OWLOntologyChangeVisitor;
//import org.semanticweb.owlapi.model.change.OntologyChange;
//import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.apibinding.OWLManager;


/**
 * OWLOntologyBuilder is used to create and maintain the ontology for the
 * alignment
 */
public class OWLOntologyBuilder {
	private OWLOntology onto = null;
	private OWLDataFactory fact = null;
	// private OWLOntologyChangeVisitor visitor=null;
	private OWLOntologyManager manager;

	private String uriStr = null;

	public OWLOntologyBuilder(String uriStr) {

		try {
			this.uriStr = uriStr;
			// Map parameters = new HashMap();
			this.manager = OWLManager.createOWLOntologyManager();
			fact = this.manager.getOWLDataFactory();

			// URI uri = new URI(uriStr);
			onto = this.manager.createOntology();
			// onto= conn.createOntology( uri, uri );

			// visitor = conn.getChangeVisitor( onto );
			// OWLClass owlClass = fact.getOWLClass( new URI(uriStr+"#Thing") );
			// OWLClass owlClass = fact.getOWLClass( IRI.create(uriStr+"#Thing")
			// );
			// AddAxiom modif = new AddAxiom(onto,owlClass)

			// OWLOntologyChange oc = new AddEntity( onto,owlClass, null );
			// oc.accept( visitor );

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Creates an OWL class from the SKOS concept The following mappings have
	 * been assumed skos:Concept -> owl:Class skos:prefLabel -> rdfs:label
	 * skos:altLabel -> rdfs:label skos:broader -> rdfs:subClassOf skos:narrower
	 * -> No need to handle in the class since it will be an another class like
	 * owl:Class with rdfs:subClassOf this class skos:definition -> rdfs:comment
	 * skos:scopeNote -> rdfs:comment
	 * 
	 * @param skosConcept
	 */
	public void createClass(SKOSConceptPerso skosConceptPerso) {
		OWLClass owlClass = null;
		OWLAnnotationProperty prop = null;
		// AddAnnotationInstance aai=null;
		String str = null;
		try {
			if (skosConceptPerso.getAbout().indexOf(getUriStr()) != -1)
				owlClass = fact.getOWLClass(IRI.create(skosConceptPerso
						.getAbout()));
			else
				owlClass = fact.getOWLClass(IRI.create(uriStr + "#"
						+ skosConceptPerso.getAbout()));

			for (@SuppressWarnings("rawtypes")
			Iterator iter = skosConceptPerso.getPrefLabels().iterator(); iter
					.hasNext();) {
				str = (String) iter.next();
				// prop = fact.getOWLAnnotationProperty(
				// IRI.create("http://www.w3.org/2000/01/rdf-schema#label") );

				OWLAnnotation commentAnno = fact.getOWLAnnotation(
						fact.getRDFSComment(), fact.getOWLLiteral(str));

				OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(IRI
						.create("http://www.w3.org/2000/01/rdf-schema#label"),
						commentAnno);
				this.manager.applyChange(new AddAxiom(onto, ax));

				// aai=new AddAnnotationInstance( onto,owlClass, prop, str, null
				// );
				// aai.accept( visitor );
			}
			for (@SuppressWarnings("rawtypes")
			Iterator iter = skosConceptPerso.getAltLabels().iterator(); iter
					.hasNext();) {
				str = (String) iter.next();

				OWLAnnotation commentAnno = fact.getOWLAnnotation(
						fact.getRDFSComment(), fact.getOWLLiteral(str));

				OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(IRI
						.create("http://www.w3.org/2000/01/rdf-schema#label"),
						commentAnno);
				this.manager.applyChange(new AddAxiom(onto, ax));

				//prop = fact.getOWLAnnotationProperty(IRI.create("http://www.w3.org/2000/01/rdf-schema#label"));
				//aai = new AddAnnotationInstance(onto, owlClass, prop, str, null);
				//aai.accept(visitor);
			}
			for (@SuppressWarnings("rawtypes")
			Iterator iter = skosConceptPerso.getBroader().iterator(); iter
					.hasNext();) {
				str = (String) iter.next();
				
				OWLAnnotation commentAnno = fact.getOWLAnnotation(
						fact.getRDFSComment(), fact.getOWLLiteral(str));

				OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(IRI
						.create("http://www.w3.org/2000/01/rdf-schema#label"),
						commentAnno);
				this.manager.applyChange(new AddAxiom(onto, ax));
				
				/*prop = fact
						.getOWLAnnotationProperty(IRI
								.create("http://www.w3.org/2000/01/rdf-schema#subClassOf"));
				aai = new AddAnnotationInstance(onto, owlClass, prop, str, null);
				aai.accept(visitor);*/
			}
			for (@SuppressWarnings("rawtypes")
			Iterator iter = skosConceptPerso.getDefinition().iterator(); iter
					.hasNext();) {
				str = (String) iter.next();
				OWLAnnotation commentAnno = fact.getOWLAnnotation(
						fact.getRDFSComment(), fact.getOWLLiteral(str));

				OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(IRI
						.create("http://www.w3.org/2000/01/rdf-schema#label"),
						commentAnno);
				this.manager.applyChange(new AddAxiom(onto, ax));
				/*prop = fact
						.getOWLAnnotationProperty(IRI
								.create("http://www.w3.org/2000/01/rdf-schema#comment"));
				aai = new AddAnnotationInstance(onto, owlClass, prop, str, null);
				aai.accept(visitor);*/
			}
			for (@SuppressWarnings("rawtypes")
			Iterator iter = skosConceptPerso.getScopeNote().iterator(); iter
					.hasNext();) {
				str = (String) iter.next();
				OWLAnnotation commentAnno = fact.getOWLAnnotation(
						fact.getRDFSComment(), fact.getOWLLiteral(str));

				OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(IRI
						.create("http://www.w3.org/2000/01/rdf-schema#label"),
						commentAnno);
				this.manager.applyChange(new AddAxiom(onto, ax));
				/*prop = fact
						.getOWLAnnotationProperty(IRI
								.create("http://www.w3.org/2000/01/rdf-schema#comment"));
				aai = new AddAnnotationInstance(onto, owlClass, prop, str, null);
				aai.accept(visitor);*/
			}
			//OntologyChange oc = new AddEntity(onto, owlClass, null);
			//oc.accept(visitor);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Get a particular class from the Ontology
	 * 
	 * @param className
	 * @return OWLClass
	 */
	public OWLClass getClass(String className) {
		OWLClass ret = null;
		try {
			//URI uri = new URI(uriStr + "#" + className);
			onto.getClass();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;

	}

	public OWLOntology getOntology() {
		return onto;
	}

	public String getUriStr() {
		return uriStr;
	}
}
