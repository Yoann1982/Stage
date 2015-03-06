import transco.ConceptSKOS;
import transco.Importer;
import transco.OWLReader;
import transco.SKOSBuilder;
import transco.SKOSReader;
import transco.WriteOntology;
import transco.OWLOntologyBuilder;


/**
 * @author Yoann Keravec
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
		
		// On lit le fichier qui alimente une structure de données interne
		SKOSReader reader = new SKOSReader();
		reader.loadFile(nomFichier);
		
		// On cr�e une instance de l'objet qui va permettre de cr�er une ontologie � partir
		// de la structure de donn�es.
		
		OWLOntologyBuilder builder  = new OWLOntologyBuilder();
		
		// On parcours la structure de donn�es pour alimenter l'ontologie cr��e.
		
		for (ConceptSKOS conceptCur : reader.getListConceptSKOS()) {
			builder.createClass(conceptCur);
		}
		
		// On exporte l'ontologie cr��e
		// Version Linux
		// String nomFichierSortie = "/home/yoann/BERGONIE/OUT/ontologie.owl";
		// Version Windows
		String nomFichierSortie = "C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\ontologie.owl";
		//OWLReader fileOnto = new OWLReader();
		//fileOnto.chargeOntology(nomFichierSortie);
		//fileOnto.chargeOntology(builder.getOnto());
		
		WriteOntology fileOntoWriter = new WriteOntology(builder.getOnto());
		//fileOnto.chargeOntology("file:/home/yoann/BERGONIE/pizza.owl");
		//fileOnto.parcoursWalker();
		fileOntoWriter.writeFile(nomFichierSortie);
		
		// Partie chargement ontologie, import & �criture
		
		String nomFichierOnto = "C:\\Users\\y.keravec\\Documents\\BERGONIE\\OWL\\bcbsarcoma_v3.owl";
		OWLReader fileOntoRead = new OWLReader();
		fileOntoRead.chargeOntology(nomFichierOnto);
		
		String ontoExterne = "http://www.w3.org/TR/skos-reference/skos-owl1-dl.rdf";
		Importer importer = new Importer(fileOntoRead.getOntology());
		importer.importOnto(ontoExterne);
		
		
		// On cr�e les objets SKOS
		SKOSBuilder skosBuilder = new SKOSBuilder(fileOntoRead.getOntology());
		skosBuilder.creeSKOSOntologie();
		// On importe l'ontologie SKOS dans l'ontologie cible
		Importer importerSKOS = new Importer(skosBuilder.getTargetOntology());
		importerSKOS.importOnto(ontoExterne);
		
		WriteOntology fileOntoWriterOnto = new WriteOntology(skosBuilder.getTargetOntology());
		String nomFichierSortieOnto = "C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\apresImport.owl";
		fileOntoWriterOnto.writeFile(nomFichierSortieOnto);
		
		
		// Lecture de l'ontologie
		
		/*
		
		OWLOntology myOnto = builder.getOnto();
		//OWLOntology myOnto = fileOnto.getOntologie();
		Set<OWLAnnotation> listAno = myOnto.getAnnotations();
		//Set<OWLAnnotation> listAno = fileOnto.getOntologie().getAnnotations();
		
		
		System.out.println("Debut de la lecture de v�rification");
		
		for (OWLAnnotation curseur : listAno) {
			System.out.println("Propri�t� de l'annotation : " + curseur.getProperty());
		}
		
		Set<OWLClass> listeClasse = myOnto.getClassesInSignature();
		for (OWLClass curseurClasse : listeClasse) {
			System.out.println("Classe : " + curseurClasse.toString());
			
			Set<OWLAnnotation> listAnnotClass = curseurClasse.getAnnotations(myOnto);
			
			for (OWLAnnotation curseur : listAnnotClass) {
				System.out.println("Propri�t� de l'annotation de classe : " + curseur.getProperty());
				System.out.println("Propri�t� de l'annotation de classe : " + curseur.getValue());
			}
		}
		*/
		
	}

}
