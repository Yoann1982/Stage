package trucautres;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.semanticweb.owlapi.model.OWLOntology;

import trucautres.SKOSParserException;
import trucautres.OWLOntologyBuilder;
import trucautres.Constants;

/**
 * SKOSParser is a basic parser for the SKOS. It parses the whole SKOS into one OWLOntology
 */
public class SKOSParser {

    private String xmlNs=null;
    private String rdfNs=null;
    private String ontoURI=null;
    protected String language=null;
    
    /**
     * Constructor of the parser
     */
    public SKOSParser(){
        this.xmlNs=Constants.xmlNs;
        this.rdfNs=Constants.rdfNs; 
        language=Constants.en;
    }
    
    /**
     * Constructor of the parser
     * @param language xml:lang tags that will be loaded into the ontology
     */
    public SKOSParser(String language){
        this.xmlNs=Constants.xmlNs;
        this.rdfNs=Constants.rdfNs; 
        this.language=language;
    }
    
    /**
     * Parses the SKOS file and creates an OWLOntology
     * @param fileName
     * @return OWLOntology
     */
        public OWLOntology parse(String fileName) throws SKOSParserException{
        int event;
        OWLOntologyBuilder ontologyBuilder=null;
        String ontoURI;
        SKOSConceptPerso skosConcept=null;
        try  {
            ontoURI=getBaseURI(fileName);
            ontologyBuilder=new OWLOntologyBuilder(ontoURI);
            
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader reader = inputFactory.createXMLStreamReader(new java.io.FileInputStream(fileName));
            
            while(reader.hasNext()) {
              event=reader.next();
              switch (event){
                  case XMLStreamConstants.END_DOCUMENT:reader.close();break;
                  case XMLStreamConstants.START_ELEMENT:
                    if(reader.getLocalName().equals("Concept")){
                         ontologyBuilder.createClass(getConcept(reader)); 
                    }else if(reader.getLocalName().equals("Description")){
                        if (language.equals(Constants.nl ))
                            skosConcept=getDutchRDFDescription(reader);
                        else
                            skosConcept=getRDFDescription(reader);
                        //skosConcept is null if the description is about a conceptScheme    
                        if (skosConcept!=null)
                             ontologyBuilder.createClass(skosConcept); 
                    }
                  
                    break;
                  
              }
                
            }
            
        } catch (Exception ex)  {
            throw new SKOSParserException("Error parsing file:"+fileName,ex);
        }
        return ontologyBuilder.getOntology();
    }
    
    /**
     * Get the base URI from the SKOS. e.g. http://agclass.nal.usda.gov/nalt/2007.xml
     * GEMET http://www.eionet.europa.eu/gemet/concept/8
     * GTT http://stitch.cs.vu.nl/gtt#gtt_145844706
     * @param fileName SKOS filename
     * @return String
     */
    protected String getBaseURI(String fileName) throws SKOSParserException{
        int event;
        boolean hasFoundOntoURI=false;
        String ret=null;
        int count=0;
        try  {
            
            
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader reader = inputFactory.createXMLStreamReader(new java.io.FileInputStream(fileName));
            
            while(reader.hasNext() && count<2) {
              event=reader.next();
              switch (event){
                  case XMLStreamConstants.START_ELEMENT:
                    if(reader.getLocalName().equals("RDF")){
                        ret=reader.getAttributeValue(xmlNs,"base");
                        if (ret !=null){
                            hasFoundOntoURI=true;
                            count=2;
                        }
                    } else if(reader.getLocalName().equals("Concept") && !hasFoundOntoURI){
                        ret=reader.getAttributeValue(rdfNs,"about");
                        ret=ret.substring(0,ret.indexOf("#"));
                        if (ret.length()!=0){
                            count++;
                            hasFoundOntoURI=true;
                        }
                        
                    } else if(reader.getLocalName().equals("Description") && !hasFoundOntoURI){
                        ret=reader.getAttributeValue(rdfNs,"about");
                        if (ret.indexOf("#")!=-1)
                           ret=ret.substring(0,ret.indexOf("#"));
                        else
                            ret=ret.substring(0,ret.lastIndexOf("/") );
                        count=2;
                    }
           
                    break;
                  
              }
                
            }
            reader.close();
            
        } catch (Exception ex)  {
           throw new SKOSParserException("Error getting URI for file:"+fileName,ex);
           
        }
        return ret;        
    }
    
    /**
     * Get the whole Concept tag from the SKOS file
     * @param reader XMLStreamReader
     * @return SKOSConcept
     */
    protected SKOSConceptPerso getConcept(XMLStreamReader reader) throws SKOSParserException {

        int event;
        SKOSConceptPerso concept=null;
        boolean process=true;
        try  {
            if (reader.getAttributeCount()>0){
                concept=new SKOSConceptPerso(reader.getAttributeValue(rdfNs,"about"));
                while(process){
                    event=reader.next();
                    switch(event){
                        case XMLStreamConstants.START_ELEMENT: if(reader.getLocalName().equals("prefLabel") && reader.getAttributeCount()>0 && reader.getAttributeValue(xmlNs,"lang" ).equals(language) ){
                                                                    reader.next();
                                                                    concept.addPrefLabel(reader.getText());
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("altLabel") && reader.getAttributeCount()>0 && reader.getAttributeValue(xmlNs,"lang" ).equals(language) ){
                                                                    reader.next();
                                                                    concept.addAltLabel(reader.getText());
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("broader") && reader.getAttributeCount()>0 && reader.getAttributeValue(rdfNs,"resource" )!=null ){

                                                                    concept.addBroader(reader.getAttributeValue(rdfNs,"resource" ));
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("narrower") && reader.getAttributeCount()>0 && reader.getAttributeValue(rdfNs,"resource" )!=null ){

                                                                    concept.addNarrower(reader.getAttributeValue(rdfNs,"resource" ));
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("related") && reader.getAttributeCount()>0 && reader.getAttributeValue(rdfNs,"resource" )!=null ){

                                                                    concept.addRelated(reader.getAttributeValue(rdfNs,"resource" ));
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("definition") && reader.getAttributeCount()>0 && reader.getAttributeValue(rdfNs,"resource" )!=null ){

                                                                    concept.addDefinition(reader.getAttributeValue(rdfNs,"resource" ));
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("definition") && reader.getAttributeCount()>0 && reader.getAttributeValue(xmlNs,"lang" ).equals(language) ){
                                                                    reader.next();
                                                                    concept.addDefinition(reader.getText());
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("scopeNote") && reader.getAttributeCount()>0 && reader.getAttributeValue(xmlNs,"lang" ).equals(language) ){
                                                                    reader.next();
                                                                    concept.addScopeNote(reader.getText());
                                                                    continue;
                                                                }
                        case XMLStreamConstants.END_ELEMENT:if(reader.getLocalName().equals("Concept")){
                                                            process=false;
                                                            break;
                                                            }

                    }
                }

            }

        } catch (Exception ex)  {
            throw new SKOSParserException("Error getting concept from SKOS:",ex);
        }

        return concept;
    }

    /**
     * Get the whole Description tag from the RDF file. The labels for the library skos are in Dutch.
     * @param reader XMLStreamReader
     * @return SKOSConcept or noll it it is not a concept tag.
     * @throws SKOSParserException
     */
    protected SKOSConceptPerso getDutchRDFDescription(XMLStreamReader reader) throws SKOSParserException {

        int event;
        SKOSConceptPerso concept=null;
        boolean process=true;
        boolean isConcept=true;
        String skosConcept=Constants.conceptURI;
        try  {
            if (reader.getAttributeCount()>0){
                concept=new SKOSConceptPerso(reader.getAttributeValue(rdfNs,"about"));
                while(process){
                    event=reader.next();
                    switch(event){
                        case XMLStreamConstants.START_ELEMENT: if(reader.getLocalName().equals("type") && reader.getAttributeCount()>0 && !reader.getAttributeValue(rdfNs,"resource" ).equals(skosConcept) ){
                                                                    isConcept=false;
                                                                    continue;
                                                                }
                                                                else if(reader.getLocalName().equals("prefLabel") && reader.getAttributeCount()>0 && reader.getAttributeValue(xmlNs,"lang" ).equals(language) ){
                                                                    reader.next();
                                                                    concept.addPrefLabel(reader.getText());
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("altLabel") && reader.getAttributeCount()>0 && reader.getAttributeValue(xmlNs,"lang" ).equals(language) ){
                                                                    reader.next();
                                                                    concept.addAltLabel(reader.getText());
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("broader") && reader.getAttributeCount()>0 && reader.getAttributeValue(rdfNs,"resource" )!=null ){

                                                                    concept.addBroader(reader.getAttributeValue(rdfNs,"resource" ));
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("narrower") && reader.getAttributeCount()>0 && reader.getAttributeValue(rdfNs,"resource" )!=null ){

                                                                    concept.addNarrower(reader.getAttributeValue(rdfNs,"resource" ));
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("related") && reader.getAttributeCount()>0 && reader.getAttributeValue(rdfNs,"resource" )!=null ){

                                                                    concept.addRelated(reader.getAttributeValue(rdfNs,"resource" ));
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("definition") && reader.getAttributeCount()>0 && reader.getAttributeValue(rdfNs,"resource" )!=null ){

                                                                    concept.addDefinition(reader.getAttributeValue(rdfNs,"resource" ));
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("definition") && reader.getAttributeCount()>0 && reader.getAttributeValue(xmlNs,"lang" ).equals(language) ){
                                                                    reader.next();
                                                                    concept.addDefinition(reader.getText());
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("scopeNote") && reader.getAttributeCount()>0 && reader.getAttributeValue(xmlNs,"lang" ).equals(language) ){
                                                                    reader.next();
                                                                    concept.addScopeNote(reader.getText());
                                                                    continue;
                                                                }
                        case XMLStreamConstants.END_ELEMENT:if(reader.getLocalName().equals("Description")){
                                                            process=false;
                                                            break;
                                                            }

                    }
                }

            }

        } catch (Exception ex)  {
            throw new SKOSParserException("Error getting concept from rdf:Description:",ex);
        }
        if (!isConcept)
            concept=null;
            
        return concept;
    }

    /**
     * Get the whole Description tag from the RDF file. The labels for the environment skos are dependent on the language version of the file.
     * @param reader XMLStreamReader
     * @return SKOSConcept
     * @throws SKOSParserException
     */
    protected SKOSConceptPerso getRDFDescription(XMLStreamReader reader) throws SKOSParserException {

        int event;
        SKOSConceptPerso concept=null;
        boolean process=true;

        try  {
            if (reader.getAttributeCount()>0){
                concept=new SKOSConceptPerso(reader.getAttributeValue(rdfNs,"about"));
                while(process){
                    event=reader.next();
                    switch(event){
                        case XMLStreamConstants.START_ELEMENT:  if(reader.getLocalName().equals("prefLabel") ){
                                                                    reader.next();
                                                                    concept.addPrefLabel(reader.getText());
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("altLabel")  ){
                                                                    reader.next();
                                                                    concept.addAltLabel(reader.getText());
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("broader") && reader.getAttributeCount()>0 && reader.getAttributeValue(rdfNs,"resource" )!=null ){

                                                                    concept.addBroader(reader.getAttributeValue(rdfNs,"resource" ));
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("narrower") && reader.getAttributeCount()>0 && reader.getAttributeValue(rdfNs,"resource" )!=null ){

                                                                    concept.addNarrower(reader.getAttributeValue(rdfNs,"resource" ));
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("related") && reader.getAttributeCount()>0 && reader.getAttributeValue(rdfNs,"resource" )!=null ){

                                                                    concept.addRelated(reader.getAttributeValue(rdfNs,"resource" ));
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("definition") && reader.getAttributeCount()>0 && reader.getAttributeValue(rdfNs,"resource" )!=null ){

                                                                    concept.addDefinition(reader.getAttributeValue(rdfNs,"resource" ));
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("definition")  ){
                                                                    reader.next();
                                                                    concept.addDefinition(reader.getText());
                                                                    continue;
                                                                }else if(reader.getLocalName().equals("scopeNote")  ){
                                                                    reader.next();
                                                                    concept.addScopeNote(reader.getText());
                                                                    continue;
                                                                }
                        case XMLStreamConstants.END_ELEMENT:if(reader.getLocalName().equals("Description")){
                                                            process=false;
                                                            break;
                                                            }

                    }
                }

            }

        } catch (Exception ex)  {
            throw new SKOSParserException("Error getting concept from rdf:Description:",ex);
        }
            
        return concept;
    }


}

