
//import etm.core.monitor.EtmPoint;

import java.net.URI;
import java.util.Iterator;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;

import trucautres.SKOSChunkParser;
import trucautres.SKOSParser;
import trucautres.Constants;

/**
 * Test the SKOS parsers. The jar dependencies are as follows:
 * 
 * - jsr173_1.0_api.jar - jsr173_1.0_ri.jar - stax-utils.jar - api.jar from the
 * OAEI libs (org.semanticweb.owl packages)
 * 
 */
public class UsageTest {
	public UsageTest() {
	}

	public static void main(String[] args) {
		UsageTest usageTest = new UsageTest();
		if (args.length == 1) {
			usageTest.parseSample(args[0]);
			usageTest.parseSampleInChunks(args[0]);
		} else {
			usageTest.printUsage();
		}

	}

	private void parseSample(String fileName) {

		OWLOntology o = null;
		SKOSParser skosParser = null;
		try {

			// library samples contain rdf:type tag which describes the entity
			if (fileName.indexOf("library") != -1)
				skosParser = new SKOSParser(Constants.nl);
			else
				skosParser = new SKOSParser();

			o = skosParser.parse(fileName);

			System.out.println("Number of classes converted from " + fileName
					+ " into OWLOntology:" + o.getClassesInSignature().size());

			System.out.println("Contenu de l'ontologie : " + o.toString());
			System.out.println("PLOP: " + o.getClassesInSignature().toString());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private void parseSampleInChunks(String fileName) {

		OWLOntology o = null;
		SKOSChunkParser skosChunkParser = null;
		int i = 1;
		try {

			if (fileName.indexOf("library") != -1)
				skosChunkParser = new SKOSChunkParser(fileName, Constants.nl);
			else
				skosChunkParser = new SKOSChunkParser(fileName);

			// System.out.println("Total number of concepts in SKOS file:"+skosChunkParser.getConceptNumber());
			skosChunkParser.setChunkSize(5);
			while (skosChunkParser.hasNextChunk()) {
				o = skosChunkParser.getNextChunk();
				// System.out.println("Iteration number:"+i+" Number of classes converter from "+fileName+" into OWLOntology chunk:"+o.getClasses().size());
				i++;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void printUsage() {
		System.out.println("Parameter should be an skos file");
	}

}
