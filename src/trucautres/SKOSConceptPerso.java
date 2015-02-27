package trucautres;

import java.util.Vector;

/**
 * SKOSConcept is a representation of the Concept tag from the SKOS
 */
public class SKOSConceptPerso {
    String about;
    Vector prefLabels;
    Vector altLabels;
    Vector broader;
    Vector narrower;
    Vector related;
    Vector definition;
    Vector scopeNote;

    public SKOSConceptPerso(String about){
        if (about.indexOf("http://")!=-1 && about.indexOf("#")!=-1)
            this.about=about.substring(about.indexOf("#")+1,about.length());
        else if (about.indexOf("http://")!=-1 && about.indexOf("#")==-1)
            this.about=about.substring(about.lastIndexOf("/")+1,about.length());
        else 
            this.about=about.replaceFirst("#","");
        
        prefLabels=new Vector();
        altLabels=new Vector();
        broader=new Vector();
        narrower=new Vector();
        related=new Vector();
        definition=new Vector();
        scopeNote=new Vector();
    }
    public void setAbout(String about) {
        if (about.indexOf("http://")!=-1 && about.indexOf("#")!=-1)
            this.about=about.substring(about.indexOf("#")+1,about.length());
        else if (about.indexOf("http://")!=-1 && about.indexOf("#")==-1)
            this.about=about.substring(about.lastIndexOf("/")+1,about.length());
        else 
            this.about=about.replaceFirst("#","");
    }

    public String getAbout() {
        return about;
    }

    public void addPrefLabel(String prefLabel) {
        this.prefLabels.add(prefLabel) ;
    }

    public Vector getPrefLabels() {
        return prefLabels;
    }

    public void addAltLabel(String altLabel) {
        this.altLabels.add(altLabel) ;
    }

    public Vector getAltLabels() {
        return altLabels;
    }

    public void addBroader(String broader) {
        this.broader.add(broader) ;
    }

    public Vector getBroader() {
        return broader;
    }

    public void addNarrower(String narrower) {
        this.narrower.add(narrower);
    }

    public Vector getNarrower() {
        return narrower;
    }

    public void addRelated(String related) {
        this.related.add(related) ;
    }

    public Vector getRelated() {
        return related;
    }

    public void addDefinition(String definition) {
        this.definition.add(definition);
    }

    public Vector getDefinition() {
        return definition;
    }

    public void addScopeNote(String scopeNote) {
        this.scopeNote.add(scopeNote) ;
    }

    public Vector getScopeNote() {
        return scopeNote;
    }
}
