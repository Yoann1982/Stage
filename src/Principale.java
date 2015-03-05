import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
//import org.semanticweb.skos.SKOSAnnotation;
//import org.semanticweb.skos.SKOSEntity;
//import org.semanticweb.skos.SKOSLiteral;
//import org.semanticweb.skos.SKOSUntypedLiteral;

import transco.ConceptSKOS;
import transco.SKOSReader;
import transco.WriteOntology;
import transco.OWLOntologyBuilder;


/**
 * @author yoann
 *
 */
public class Principale {

	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		// Version Linux
		//String nomFichier = "file:/home/yoann/BERGONIE/canals.skos";
		// Version Windows
		String nomFichier = "C:\\Users\\y.keravec\\Documents\\BERGONIE\\canals.skos";
		

		// Technique avec adaptation du code du convertisseur ancien
		
		// On lit le fichier qui alimente une structure de donnÃ©es interne
		SKOSReader reader = new SKOSReader();
		reader.loadFile(nomFichier);
		
		// On crée une instance de l'objet qui va permettre de créer une ontologie à partir
		// de la structure de données.
		
		OWLOntologyBuilder builder  = new OWLOntologyBuilder();
		
		// On parcours la structure de données pour alimenter l'ontologie créée.
		
		for (ConceptSKOS conceptCur : reader.getListConceptSKOS()) {
			builder.createClass(conceptCur);
		}
		
		// On exporte l'ontologie créée
		// Version Linux
		// String nomFichierSortie = "/home/yoann/BERGONIE/OUT/ontologie.owl";
		// Version Windows
		String nomFichierSortie = "C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\ontologie.owl";
		WriteOntology fileOnto = new WriteOntology();
		fileOnto.chargeOntology(builder.getOnto());
		//fileOnto.chargeOntology("file:/home/yoann/BERGONIE/pizza.owl");
		
		//fileOnto.parcoursWalker();
		fileOnto.writeFile(nomFichierSortie);
		
		// Lecture de l'ontologie
		
		/*
		
		OWLOntology myOnto = builder.getOnto();
		//OWLOntology myOnto = fileOnto.getOntologie();
		Set<OWLAnnotation> listAno = myOnto.getAnnotations();
		//Set<OWLAnnotation> listAno = fileOnto.getOntologie().getAnnotations();
		
		
		System.out.println("Debut de la lecture de vérification");
		
		for (OWLAnnotation curseur : listAno) {
			System.out.println("Propriété de l'annotation : " + curseur.getProperty());
		}
		
		Set<OWLClass> listeClasse = myOnto.getClassesInSignature();
		for (OWLClass curseurClasse : listeClasse) {
			System.out.println("Classe : " + curseurClasse.toString());
			
			Set<OWLAnnotation> listAnnotClass = curseurClasse.getAnnotations(myOnto);
			
			for (OWLAnnotation curseur : listAnnotClass) {
				System.out.println("Propriété de l'annotation de classe : " + curseur.getProperty());
				System.out.println("Propriété de l'annotation de classe : " + curseur.getValue());
			}
		}
		*/
		
	}

}
