package Launcher;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.File;

// Logger
import projectUtilities.LogUtility;
// Render
import projectUtilities.RenderUtility;
// Le classi Reasoner e ReasonerFactory per sfornare un nuovo reasoner per concetti ALC
import UrbanoOWLReasoner.Reasoner;
import UrbanoOWLReasoner.ReasonerFactory;

public class Test {

    /*
        ReasonerFactory è in questo caso una classe che implementa il design pattern Factory: permette così di
        sfornare diversi dipi di Reasoner che implementano diversi algoritmi di ottimizzazione.
     */
    private ReasonerFactory reasoner_factory;
    /*
        Dichiaro un reasoner generico, poi grazie alla factory ne istanzio uno che implementa un tipo
        particolare di algoritmo di ottimizzazione. In questo progetto è previsto l'unico algoritmo visto a corso,
        il Dependency Directed Backtracking, ma si lascia aperta la possibilità ai futuri sviluppi che volessero
        inserire nuovi algoritmi di ottimizzazione.
     */
    private Reasoner reasoner;

    
    private final org.semanticweb.HermiT.ReasonerFactory hermit_factory;
    private final OWLOntologyManager ontology_manager;

    /*
        Costruttore di classe che istanzia gli attributi per gestire i files da leggere,
        il OWLOntologyManager (ovvero il punto di accesso per usare le OWLAPI) e l'attributo
        factoryHermit per creare oggetti di tipo HermiTReasoner.
    */
    public Test(){
        
        // The central point of access is the OWLOntologyManager, which is used to load, create and access ontologies.
        ontology_manager = OWLManager.createOWLOntologyManager();
        
        // Istanziazione della Factory che sforna reasoners di tipo HermiT
        hermit_factory = new org.semanticweb.HermiT.ReasonerFactory();
    }

    /*
        Esegue la batteria di test per paragonare l'output del nostro reasoner con quello di HermiT sulle
        ontologie contenute nella cartella di default.
        Prende in input la stringa che indica quale algoritmo applicare sul Tableau (nel nostro caso è presente
        l'algoritmo Dependency Directed Backtracking visto a lezione) e un booleano che indica se abilitare o meno
        le funzioni di logging per generare una serie di file che contengono il log per ogni ontologia.
     */
    public String execute(String optAlgorithm, boolean log_flag, boolean solution_flag, boolean render_flag, File[] concept_files){

        String info = "";
        /*
            Istanzia un OWLRenderer per stampare l'ontologia in un formato human-readable.
            I formati supportati in questa implementazione sono:
             - Description Language
             - Manchester Syntax (https://www.w3.org/TR/owl2-manchester-syntax/)
         */
        
        // Abilita il logger solo se richiesto.
        if(log_flag)
            LogUtility.initLogger();
        else
            // Questo workaround permette di "uccidere" il singoletto se era stato già istanziato.
            LogUtility.turnOff();

        /*
            Se il numero di file è diverso da zero compie le operazioni di verifica, altrimenti restituisce
            un messaggio d'errore.
         */
        if(concept_files != null){
            // reasoner_factory serve per istanziare reasoner con diversi tipi di algoritmi di ottimizzazione.
            reasoner_factory = new ReasonerFactory();

            for (File concept_file : concept_files) {
                info = info.concat("____________________________________________" +
                                   "___________________________________________________________\n");

                // Elimina il concetto precedentemente caricato per caricare il successivo.
                ontology_manager.clearOntologies();
                
                OWLOntology alc_concept = null;
                try {
                    alc_concept = ontology_manager.loadOntologyFromOntologyDocument(concept_file);
                } catch (OWLOntologyCreationException oWLOntologyCreationException) {
                    /*
                        Se il file corrente non contiene un concetto OWL valido non mi fermo
                        e con lo statement "continue" vado alla prossima iterazione.
                        Garantiamo così ROBUSTEZZA al nostro reasoner.
                     */
                    info += "[ERRORE]: " + concept_file.getName() + "non è un concetto OWL valido! (.owl)";
                    continue;
                }
         
                /*
                    Dato un file che contiene un concetto ALC espresso tramite OWL2, lo estrae e lo
                    carica nelle strutture dati fornite da OWLAPI e conseguentemente restituisce owl_concept, che
                    rappresenta il concetto di cui bisogna dimostrare soddisfacibilità o insoddisfacibilità.
                    Tale operazione viene svolta per ogni file presente nella cartella di default contenente
                    i concetti ALC da verificare.
                */
                OWLClassExpression owl_concept = reasoner_factory.getExpression(alc_concept, concept_file.getName());

                if (owl_concept != null) {
                    // Mi faccio restituire un reasoner solo se owl_concept non è vuoto.
                    reasoner = (Reasoner) reasoner_factory.getReasoner(optAlgorithm);
                                            

                    // Stampa il concetto effettuando il rendering in DL e Manchester syntax
                    info = info.concat("Nome file:\n" + concept_file.getName() +"\n");
                    
                    if(render_flag){
                        info = info.concat("\nDL Syntax:\n" + RenderUtility.renderDL(owl_concept) +"\n");
                        info = info.concat("\nManchester Syntax:\n" + RenderUtility.renderMAN(owl_concept).replace("\n", "").replace("\r", "").replace("\t", "") +"\n");
                    }
                    

                    // Se il logger è abilitato sovrascrive/crea il file di log per il concetto corrente.
                    if(LogUtility.isEnabled()){
                        LogUtility.setFile(concept_file.getName().replace(".owl", "")
                                            + "_" + reasoner.getOptAlgorithm());
                        LogUtility.putInfo(concept_file.getName());
                    }


                    // Istanziazione dell'HermiT reasoner con conseguente verifica di soddisfacibilità del concetto.
                    OWLReasoner hermit_reasoner = hermit_factory.createReasoner(alc_concept);

                    long hermit_chrono_start = System.currentTimeMillis();
                    boolean hermit_result = hermit_reasoner.isSatisfiable(owl_concept);
                    long hermit_chrono_end = System.currentTimeMillis();


                    /*
                        Prendo i tempi di inizio e fine dell'esecuzione dell'algoritmo per confrontarli con quelli di
                        HermiT.
                     */
                    long reasoning_chrono_start = System.currentTimeMillis();
                    boolean reasoning_result = reasoner.isSatisfiable(owl_concept);
                    long reasoning_chrono_end = System.currentTimeMillis();                    

                    if(LogUtility.isEnabled()){                      
                        LogUtility.putInfo(reasoner.getOptAlgorithm() +": " + reasoning_result +
                                     " ("+(reasoning_chrono_end - reasoning_chrono_start) + " ms)");
                    }

                    info = info.concat("\n"+reasoner.getOptAlgorithm()+"\tHermiT\n"+
                                        "\t"+reasoning_result+"\t\t       "+hermit_result+"\n"
                                      + "\t"+(reasoning_chrono_end-reasoning_chrono_start)+" ms\t\t      "+(hermit_chrono_end - hermit_chrono_start)+" ms\n");
                    
                    if (reasoning_result && solution_flag){
                        info = info.concat("\nSoluzione trovata: \n" + reasoner.getSolution()+ "\n");
                    }
                }

            }

            return info;
        } else 
            return "Bisogna specificare almeno un file contenente un concetto ALC!";
    }
}