package trucautres;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.semanticweb.owlapi.model.OWLOntology;

import trucautres.SKOSParserException;
import trucautres.OWLOntologyBuilder;
import trucautres.Constants;

/**
 * SKOSChunkParser parses the SKOS file in chunks. It allows the similarity algorithm to iterate through
 * the SKOS file without building a large ontology in the memory.
 */
public class SKOSChunkParser extends SKOSParser {
    private XMLInputFactory inputFactory;
    private OWLOntologyBuilder ontologyBuilder;
    private String skosFileName;
    private int conceptNumber;
    private int currentPosition;
    private int chunkSize;
    private String ontoURI;
    
    /**
     * Constructor of the parser
     * @param fileName SKOS file name
     * @throws SKOSParserException
     */
    public SKOSChunkParser(String fileName) throws SKOSParserException {
        super();
        try{
            this.init(fileName);
        }catch(Exception ex){
            throw new SKOSParserException("Error creating SKOSChunkParser for file:"+getSkosFileName(),ex);
        }  
    }
    
    /**
     * Constructor of the parser
     * @param fileName SKOS file name
     * @param language xml:lang tags that will be loaded into the ontology
     * @throws SKOSParserException
     */
    public SKOSChunkParser(String fileName,String language) throws SKOSParserException {
        super(language);
        try{
            this.init(fileName);
            
        }catch(Exception ex){
            throw new SKOSParserException("Error creating SKOSChunkParser for file:"+getSkosFileName(),ex);
        }  
    }
    

    private void init(String fileName) throws SKOSParserException{
        try{
            inputFactory=XMLInputFactory.newInstance();
            skosFileName=fileName;
            conceptNumber=parseFile();
            setOntoURI(getBaseURI(fileName));
            setCurrentPosition(0);
            setChunkSize(50);
        }catch(Exception ex){
            throw new SKOSParserException("Error initialising SKOSChunkParser for file:"+getSkosFileName(),ex);
        }        
    }
    
    /**
     * Determine if there are more chunks that needs to be loaded from the SKOS file
     * @return true if there are more chunks, false otherwise
     */
    public boolean hasNextChunk(){
        boolean ret=false;
        if (getCurrentPosition()<getConceptNumber())
           ret=true;       
        return ret;
    }
    
    /**
     * Get a chunk from the SKOS file and return the OWLOntology representation.
     * @return OWLOntology
     */
    public OWLOntology getNextChunk() throws SKOSParserException{
        OWLOntology ret=null;
        SKOSConceptPerso skosConcept=null;
        int event;
        int startPosition;
        int endPosition;
        int conceptCount;//counts the concepts position in the source file
        boolean conceptFound;
        
        try  {
            
            XMLStreamReader reader = inputFactory.createXMLStreamReader(new java.io.FileInputStream(getSkosFileName()));
            ontologyBuilder=new OWLOntologyBuilder(getOntoURI());
            startPosition=getCurrentPosition();
            endPosition=startPosition+getChunkSize();
            conceptCount=0;
            while(reader.hasNext() && ( getCurrentPosition()<endPosition ) ) {
              event=reader.next();
              switch (event){
                  case XMLStreamConstants.START_ELEMENT:
                    if(reader.getLocalName().equals("Concept")){
                        if (conceptCount>=startPosition && conceptCount<=endPosition){
                            ontologyBuilder.createClass(getConcept(reader));
                            increaseCurrentPosition();
                        }
                        conceptCount++;
                    }else if(reader.getLocalName().equals("Description")){
                        conceptFound=false;
                        if(language.equals(Constants.nl)){
                            skosConcept=this.getDutchRDFDescription(reader);
                            if (skosConcept!=null){
                                conceptFound=true; 
                                if (conceptCount>=startPosition && conceptCount<=endPosition){
                                    ontologyBuilder.createClass(skosConcept);
                                    increaseCurrentPosition(); 
                                }

                            }
                                
                        }else {
                            conceptFound=true;
                            if (conceptCount>=startPosition && conceptCount<=endPosition){
                                skosConcept=this.getRDFDescription(reader);
                                ontologyBuilder.createClass(skosConcept);
                                increaseCurrentPosition();
                            }
                        }

                        if (conceptFound)
                            conceptCount++;
                    }        
                    break; 
              }
                
            } 
            
            reader.close();
            ret=ontologyBuilder.getOntology();
        } catch (Exception ex)  {
            throw new SKOSParserException("Error parsing file:"+getSkosFileName(),ex);
        } 
        return ret;
        
     
      
      
    }
    
    /**
     * Set how many concepts should be added into the OWLOntology in one iteration.
     * @param chunkSize
     */
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    private int getChunkSize() {
        return chunkSize;
    }
    
    /**
     * Parse the SKOS file and determine the number of Concepts in it. 
     * @return number of Concepts
     */
    private int parseFile() throws SKOSParserException{
        int event;
        int conceptNumber=0;
        SKOSConceptPerso skosConcept=null;
        try  {
            
            XMLStreamReader reader = inputFactory.createXMLStreamReader(new java.io.FileInputStream(getSkosFileName()));
            
            while(reader.hasNext()) {
              event=reader.next();
              switch (event){
                  case XMLStreamConstants.END_DOCUMENT:reader.close();break;
                  case XMLStreamConstants.START_ELEMENT:
                    if(reader.getLocalName().equals("Concept")){
                        conceptNumber++;   
                    }else if(reader.getLocalName().equals("Description")){
                        if(language.equals(Constants.nl)){
                            skosConcept=this.getDutchRDFDescription(reader);
                            if (skosConcept!=null)
                                conceptNumber++;
                        }else {
                            conceptNumber++;
                        }
                    }
                        
                    break;
                  
              }
                
            } 
            reader.close();
        } catch (Exception ex)  {
            throw new SKOSParserException("Error parsing file:"+getSkosFileName(),ex);
        } 
        return conceptNumber;
        
    }
    


    private String getSkosFileName() {
        return skosFileName;
    }


    private void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    private int getCurrentPosition() {
        return currentPosition;
    }


    private void setOntoURI(String ontoURI) {
        this.ontoURI = ontoURI;
    }

    private String getOntoURI() {
        return ontoURI;
    }
    
    private void increaseCurrentPosition(){
        this.currentPosition++;
    }

    public int getConceptNumber() {
        return conceptNumber;
    }
}
