package UrbanoOWLReasoner;

import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.FreshEntityPolicy;
import org.semanticweb.owlapi.reasoner.IndividualNodeSetPolicy;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import org.semanticweb.owlapi.util.Version;



/*
    E' la classe che definisce un Reasoner di tipo ALC.
    Fa uso delle OWLAPI per implementare i metodi atti a verificare la 
    soddisfacibilità di un'ontologia.
*/
public class Reasoner implements OWLReasoner {

    /*
        Tableau sarà istanziato come una delle sottoclassi che implementano la 
        classe astratta Tableau. Tale classe indica solo gli attributi ed i 
        metodi necessari a definire un generico tableau, senza entrare nel 
        merito di QUALE algoritmo di OTTIMIZZAZIONE deve essere utilizzato per
        la risoluzione dello stesso.
    */
    private Tableau tableau = null;
    private String opt_algorithm;

    // Faccio l'override vuoto, non mi serve crearlo senza alcun parametro.
    public Reasoner(){
    }

    /*
        Setta il tipo di algoritmo impiegato per risolvere il Tableau come 
        stringa. Ciò è necessario in più fasi dell'esecuzione (e.g. quando 
        andiamo a scrivere i file di log).
    */
    public Reasoner(String opt_algo){
        opt_algorithm = opt_algo;
    }
    
    /*
        Istanzia il Tableau del Reasoner corrente on-the-fly.
        Questa scelta deriva dal fatto che il tableau, per essere istanziato, 
        necessita dell'insieme di clausole di cui si deve verificare la 
        soddisfacibilità e del tipo di algoritmo da utilizzare nel processo
        di reasoning.
    */
    private void initTableau(OWLClassExpression to_satisfy){
        switch(opt_algorithm){
            case "Dependency-Directed Backtracking":
                opt_algorithm = "Dependency-Directed Backtracking";
                tableau = new DDBTableau(to_satisfy.getNNF(), -1);
                break;
            default:
                throw new UnsupportedOperationException("The optimization algorithm " +
                                                        "specified it's not implemented yet!");
        }
        
    }

    // Restituisce la stringa che rappresenta il modello trovato dal Tableau.
    public String getSolution(){
        return tableau.getSolution();
    }

    // Restituisce che tipo di algoritmo di ottimizzazione impiega il Tableau.
    public String getOptAlgorithm(){
        return opt_algorithm;
    }

    @Override
    public boolean isSatisfiable(OWLClassExpression to_satisfy) {
        initTableau(to_satisfy);
        return tableau.SAT();
    }
    
    /*
        Funzioni da implementare di OWLReasoner.
    */
    @Override
    public Node<OWLClass> getUnsatisfiableClasses() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public boolean isEntailed(OWLAxiom owlAxiom) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public boolean isEntailed(Set<? extends OWLAxiom> set) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public boolean isEntailmentCheckingSupported(AxiomType<?> axiomType) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public Node<OWLClass> getTopClassNode() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public Node<OWLClass> getBottomClassNode() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public NodeSet<OWLClass> getSubClasses(OWLClassExpression owlClassExpression, boolean b) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public NodeSet<OWLClass> getSuperClasses(OWLClassExpression owlClassExpression, boolean b) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public Node<OWLClass> getEquivalentClasses(OWLClassExpression owlClassExpression) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public NodeSet<OWLClass> getDisjointClasses(OWLClassExpression owlClassExpression) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public Node<OWLObjectPropertyExpression> getTopObjectPropertyNode() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public Node<OWLObjectPropertyExpression> getBottomObjectPropertyNode() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public NodeSet<OWLObjectPropertyExpression> getSubObjectProperties(OWLObjectPropertyExpression owlObjectPropertyExpression, boolean b) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public NodeSet<OWLObjectPropertyExpression> getSuperObjectProperties(OWLObjectPropertyExpression owlObjectPropertyExpression, boolean b) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public Node<OWLObjectPropertyExpression> getEquivalentObjectProperties(OWLObjectPropertyExpression owlObjectPropertyExpression) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public NodeSet<OWLObjectPropertyExpression> getDisjointObjectProperties(OWLObjectPropertyExpression owlObjectPropertyExpression) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public Node<OWLObjectPropertyExpression> getInverseObjectProperties(OWLObjectPropertyExpression owlObjectPropertyExpression) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public NodeSet<OWLClass> getObjectPropertyDomains(OWLObjectPropertyExpression owlObjectPropertyExpression, boolean b) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public NodeSet<OWLClass> getObjectPropertyRanges(OWLObjectPropertyExpression owlObjectPropertyExpression, boolean b) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public Node<OWLDataProperty> getTopDataPropertyNode() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public Node<OWLDataProperty> getBottomDataPropertyNode() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public NodeSet<OWLDataProperty> getSubDataProperties(OWLDataProperty owlDataProperty, boolean b) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public NodeSet<OWLDataProperty> getSuperDataProperties(OWLDataProperty owlDataProperty, boolean b) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public Node<OWLDataProperty> getEquivalentDataProperties(OWLDataProperty owlDataProperty) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public NodeSet<OWLDataProperty> getDisjointDataProperties(OWLDataPropertyExpression owlDataPropertyExpression) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public NodeSet<OWLClass> getDataPropertyDomains(OWLDataProperty owlDataProperty, boolean b) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public NodeSet<OWLClass> getTypes(OWLNamedIndividual owlNamedIndividual, boolean b) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public NodeSet<OWLNamedIndividual> getInstances(OWLClassExpression owlClassExpression, boolean b) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public NodeSet<OWLNamedIndividual> getObjectPropertyValues(OWLNamedIndividual owlNamedIndividual, OWLObjectPropertyExpression owlObjectPropertyExpression) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public Set<OWLLiteral> getDataPropertyValues(OWLNamedIndividual owlNamedIndividual, OWLDataProperty owlDataProperty) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public Node<OWLNamedIndividual> getSameIndividuals(OWLNamedIndividual owlNamedIndividual) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public NodeSet<OWLNamedIndividual> getDifferentIndividuals(OWLNamedIndividual owlNamedIndividual) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public long getTimeOut() {
        return 0;
    }

    @Override
    public FreshEntityPolicy getFreshEntityPolicy() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public IndividualNodeSetPolicy getIndividualNodeSetPolicy() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public void dispose() {

    }

    @Override
    public String getReasonerName() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public Version getReasonerVersion() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public BufferingMode getBufferingMode() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public List<OWLOntologyChange> getPendingChanges() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public Set<OWLAxiom> getPendingAxiomAdditions() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public Set<OWLAxiom> getPendingAxiomRemovals() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public OWLOntology getRootOntology() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public void interrupt() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public void precomputeInferences(InferenceType... inferenceTypes) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public boolean isPrecomputed(InferenceType inferenceType) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public Set<InferenceType> getPrecomputableInferenceTypes() {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public boolean isConsistent() {
        throw new UnsupportedOperationException("Operation not supported!");
    }
}