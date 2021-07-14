package UrbanoOWLReasoner;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import uk.ac.manchester.cs.owl.owlapi.OWLEquivalentClassesAxiomImpl;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import projectUtilities.LogUtility;
/*
    ALCReasonerFactory è la classe che rappresenta il design pattern Factory per inizializzare
    nuovi ALCReasoner. Oltre a sfornare nuovi reasoner si occupa anche delle operazioni preliminari
    sull'ontologia fornita in input per ottenere l'espressione che descrive il
    concetto ALC (alc_concept) che sarà fornito al reasoner al momento dell'invocazione del metodo is.Satisfiable.

 */
public class ReasonerFactory implements OWLReasonerFactory {
    /*
       L'ontology manager che sarà utilizzato per processare l'ontologia al fine di estrarne gli assiomi.
    */
    private final OWLOntologyManager ontology_manager;


    public ReasonerFactory() {
        ontology_manager = OWLManager.createOWLOntologyManager();
    }

    /*
        E' il Factory Method che restituisce un nuovo Reasoner fornendogli la stringa che indica il tipo
        di algoritmo che dovra essere applicato sul tableau al momento della verifica di soddisfacibilità.
     */
    public OWLReasoner getReasoner(String opt_algorithm) {
        return new Reasoner(opt_algorithm);
    }

    /*
        Metodo factory da implementare affinche questa classe sia sottoclasse di OWLReasoner.
        Non sarà mai richiamato dato che le funzioni che sarebbero dovute essere espletate da questo metodo
        sono state, per scelta implementativa, demandate ai metodi getExpression() (il quale compie tutte le operazioni
        necessarie per ottenere l'espressione da elaborare) e al metodo soprastante per quanto riguarda la creazione
        vera e propria di un nuovo reasoner.
     */
    @Override
    public OWLReasoner createReasoner(OWLOntology ontology) {
        throw new UnsupportedOperationException("Method createReasoner(OWLOntology ontology) of class ALCReasonerFactory" +
                                   " has been replaced by getReasoner(String optAlgorithm)");
    }

    /*  Dato il concetto ALC (ancora di tipo OWLOntology) vengono effettuate le operazioni per restituire
        questo concetto sottoforma di OWLExpression, la quale sarà poi utilizzata per la verifica di
        soddisfacibilità.
    * */
    public OWLClassExpression getExpression(OWLOntology alc_concept, String concept_filename){

        // Azzera le ontologie, in realtà concetti ALC, precedentemente caricate per far spazio alla nuova.
        ontology_manager.clearOntologies();
        
        // Istanzia la data_factory per istanziare assiomi, entità e concetti.
        OWLDataFactory data_factory = ontology_manager.getOWLDataFactory();
        
        // Ottiene l'identificativo dell'ontologia appena caricata dal file.
        Optional<IRI> optional_iri = alc_concept.getOntologyID().getOntologyIRI();
        if(!optional_iri.isPresent())
            return null;
        
        IRI iri = optional_iri.get();

        /*
            Carica tutto il ramo Class identificato con l'IRI specificato dalla 
            stringa presa in input. In particolare andiamo a caricare l'assioma
            definito con la sintassi
                                <owl:Class rdf:about="IRI#alc_concept">
         */
        OWLClass owl_class_id = data_factory.getOWLClass(iri + "#alc_concept");
        
        // Costruisce il set di assiomi della ABOX iniziale
        Set<OWLAxiom> axioms_set = alc_concept.axioms(owl_class_id).collect(Collectors.toSet());

        if (axioms_set.size() > 1 || axioms_set.size() < 0){
            if(LogUtility.isEnabled()){
                LogUtility.setFile(concept_filename.replace(".owl",""));
                LogUtility.putError("Concetto in input non valido! Vi deve essere un unico assioma!");
            }

            return null;
        }

        // Prendo l'assioma dal set di quelli contenuti nella ABOX del concetto ALC iniziale
        OWLEquivalentClassesAxiomImpl axiom = (OWLEquivalentClassesAxiomImpl) axioms_set.iterator().next();
        // Inserisco tutti gli assiomi trovati in una unica espressione da restituire al chiamante.
        Set<OWLClassExpression> expression_set = axiom.classExpressions().collect(Collectors.toSet());
        
        for (OWLClassExpression exp : expression_set) {
            if (!exp.isOWLClass()) {
                return exp;
            }
        }

        // Restituisco l'espressione di cui si deve verificare la soffisfacibilità.
        return null;
    }

    @Override
    public OWLReasoner createNonBufferingReasoner(OWLOntology owlOntology, OWLReasonerConfiguration owlReasonerConfiguration) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public OWLReasoner createReasoner(OWLOntology owlOntology, OWLReasonerConfiguration owlReasonerConfiguration) {
        throw new UnsupportedOperationException("Operation not supported!");
    }

    @Override
    public String getReasonerName() {
        return "Reasoner";
    }

    @Override
    public OWLReasoner createNonBufferingReasoner(OWLOntology owlOntology) {
        throw new UnsupportedOperationException("Operation not supported!");
    }
}