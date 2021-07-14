package UrbanoOWLReasoner;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;

import uk.ac.manchester.cs.owl.owlapi.OWLObjectIntersectionOfImpl;

import projectUtilities.LogUtility;
import projectUtilities.RenderUtility;

/*
    La classe DDBTableau rappresenta un Tableau semantico che applica 
    l'ottimizzazione denominata Depencency Directed Backjumping, per evitare di
    visitare nodi che se visitati dopo un clash porterebbero comunque ad 
    ulteriori clash con conseguente spreco di risorse.
*/
public class DDBTableau extends Tableau{
    
    // Indice del concetto attualmente esaminato.
    private int current_concept = 0;
    
    /*
        Indica per ogni concetto l'insieme di branch da cui esso dipende.
        Necessario per effettuare il backjumping.
    */
    private final List<List<Integer>> dependencies;

    // Lista contenente i punti candidati su cui poter effettuare il backjump in caso di clash.
    private List<Integer> jump_candidates;
    
    /*
        existential_quantifiers è una mappa avente come chiave una espressione, la quale indica
        una relazione R, e come valore una lista di interi, i quali rappresentano l'indice del concetto
        in concepts che è stato espanso con tale ruolo R.
        Questo attributo è necessario per l'espansione delle regole ∀R.C e ∃R.C al fine
        di verificare se introdurre un nuovo individuo oppure no.
    */
    private final Map<OWLObjectPropertyExpression, List<Integer>> existential_quantifiers;

    /*
        existential_quantifiers è una mappa avente come chiave una espressione, la quale indica
        una relazione R, e come valore una lista di interi, i quali rappresentano l'indice del concetto
        in concepts che è stato espanso con tale ruolo R.
        Questo attributo è necessario per l'espansione delle regole ∀R.C e ∃R.C al fine
        di verificare se introdurre un nuovo individuo oppure no.
    */
    private final Map<OWLObjectPropertyExpression, List<Integer>> universal_quantifiers;

    /*
       exp_comparator è un Comparator che si occupa di confrontare due espressioni
       e decidere quale è in relazione d'ordine >.
       L'utilità di questo attributo sta nel fatto che quando andremo a costruire
       l'insieme dei concetti coinvolti in una espressione permetteremo di
       avere un ordine di valutazione di ogni concetto.
    */
    private Comparator<OWLClassExpression> exp_comparator;


    /*
       Costruttore di DDBTableau.
       Riceve in input un concetto di tipo OWLClassExpression ed un intero
       il quale corrisponde all'etichetta del concetto (current_concept)
    */
    public DDBTableau(OWLClassExpression concept, int parent) {
        /*
            Verifica se state è stata settata a true.
            In caso positivo, inizializza il Logger ed inizia a scrivere.
        */
        if(LogUtility.isEnabled())
            LogUtility.putDebug("SAT: " + parent);
        
         // Inizializza lista di concetti ed inserisce il concetto ALC fornito in input.
        concepts = new ArrayList<>();
        concepts.add(0, concept);
        
        /*
            La il concetto zero indica l'intero assioma di cui andremo a verificare la soddisfacibilità.
            Pertanto tale concetto non dipenderà da nessun'altro concetto, motivo per il quale
            la lista di dipendenze di tale concetto conterrà solo il valore -1 per indicare
            che si tratta del concetto zero.
        */
        dependencies = new ArrayList<>();
        dependencies.add(0,Collections.singletonList(-1));
        
        // Inizializza le mappe existential_quantifiers ed universal_quantifiers.
        universal_quantifiers = new HashMap<>();
        existential_quantifiers = new HashMap<>();

        /*
            Comparator di espressioni utile per scegliere quale regola applicare
            durante il processo di espansione.
            L'ordine di valutazione è il seguente:

            "∧ < ∨ < ∃ < ∀ < ¬ < Atom"
        */
        exp_comparator = (Comparator<OWLClassExpression>) new Comparator<OWLClassExpression>(){
            @Override
            public int compare(OWLClassExpression exp1, OWLClassExpression exp2){
                ClassExpressionType exp1_type = exp1.getClassExpressionType();
                ClassExpressionType exp2_type = exp2.getClassExpressionType();

                // Controlla se le due espressioni appartengono alla stessa classe.
                if(exp1_type.equals(exp2_type))
                    return 0;

                if(exp1_type.equals(ClassExpressionType.OBJECT_INTERSECTION_OF))
                    return -1;

                if(exp1_type.equals(ClassExpressionType.OBJECT_UNION_OF))
                    if(exp2_type.equals(ClassExpressionType.OBJECT_INTERSECTION_OF))
                        return 1;
                    else
                        return -1;

                if(exp1_type.equals(ClassExpressionType.OBJECT_SOME_VALUES_FROM))
                    if(exp2_type.equals(ClassExpressionType.OBJECT_UNION_OF) ||
                       exp2_type.equals(ClassExpressionType.OBJECT_INTERSECTION_OF))
                        return 1;
                    else
                        return -1;

                if(exp1_type.equals(ClassExpressionType.OBJECT_ALL_VALUES_FROM))
                    if(exp2_type.equals(ClassExpressionType.OBJECT_UNION_OF) ||
                       exp2_type.equals(ClassExpressionType.OBJECT_INTERSECTION_OF) ||
                       exp2_type.equals(ClassExpressionType.OBJECT_SOME_VALUES_FROM))
                        return 1;
                    else
                        return -1;

                if(exp1_type.equals(ClassExpressionType.OWL_CLASS) ||
                   exp1_type.equals(ClassExpressionType.OBJECT_COMPLEMENT_OF))
                    return 1;

                return -1;
            }
        };
    }
    
    @Override
    public boolean SAT() {
        /* 
           Se non sono stati esaminati tutti i concetti e dunque applicate
           tutte le regole d'espansione, va ad espandere la regola del
           concetto corrente.
        */
        if(current_concept <= concepts.size() - 1) {
        
            // Verifico che tipo di operatore è presente nel concetto in esame.
            OWLClassExpression curr_concept = concepts.get(current_concept);
            
            // Estraggo il tipo di regola dell'espressione corrente.
            ClassExpressionType exp_type = curr_concept.getClassExpressionType();
            
            /*
               Risolvo esaustivamente prima Intersezioni, poi Unioni.
               Infine quantificazioni esistenziali e universali.
             */
            if(exp_type.equals(ClassExpressionType.OBJECT_INTERSECTION_OF))
                return solveIntersectionRule((OWLObjectIntersectionOf) curr_concept);
            
            if(exp_type.equals(ClassExpressionType.OBJECT_UNION_OF))
                return solveUnionRule((OWLObjectUnionOf) curr_concept);
            
            if(exp_type.equals(ClassExpressionType.OBJECT_SOME_VALUES_FROM))
                return solveSomeRule((OWLObjectSomeValuesFrom) curr_concept);
            
            if(exp_type.equals(ClassExpressionType.OBJECT_ALL_VALUES_FROM))
                return solveAllRule((OWLObjectAllValuesFrom) curr_concept);
            
            /*
                Se è una classe o un complemento allora sono su una foglia, cioè
                il concetto è un atomo e non è possibile più applicare regole
                di espansione. L'algoritmo, quindi, verifica se vi è stato un 
                clash: in caso positivo ritorna false, altrimenti incrementa
                l'indice di regola e richiama SAT.
            */
            if(exp_type.equals(ClassExpressionType.OWL_CLASS) ||
               exp_type.equals(ClassExpressionType.OBJECT_COMPLEMENT_OF)){
                
                // Controlla se si è verificato un clash. 
                if(Clash())
                    return false;

                // Verifico la soddisfacibilità del prossimo concetto contenuto in concepts.
                current_concept++;
                return SAT();
            }
        }
        
        // Se arriva qui, allora tutti i concetti di un ramo sono soddisfacibili,
        // poiché non si è mai verificato un clash.
        return true;
    }
    
    /* 
        Effettua l'espansione della regola di INTERSEZIONE (AND - ∧ - ∩).
        Condizione: C1 ∩ C2(x) ∈ A e {C1(x), C2(X)} ⊄ A
        Azione: A ← A ∪ {C1(x), C2(x)}
    */ 
    private boolean solveIntersectionRule(OWLObjectIntersectionOf intersection_rule){
        // Effettua il logging, se abilitato.
        if(LogUtility.isEnabled())
            LogUtility.putDebug("CONCETTO N: " + current_concept + " (INTERSEZIONE): "
                                      + RenderUtility.renderMAN(intersection_rule).replace("\n", "").replace("\r", "").replace("\t", ""));

        /*
            Prendo gli operandi, ovvero i concetti (che siano essi atomici 
            o composti) sono coinvolti nell'intersezione e li pongo nella lista
            denominata "involved_concepts".
        */
        List<OWLClassExpression> involved_concepts =
                                    intersection_rule.operands().sorted(exp_comparator).collect(Collectors.toList());

        /*
            Verifico per ogni concetto (o anche detto operando)
            coinvolto nell'intersezione se è già presente nell'insieme di concetti
            che andranno a costituire il modello dell'assioma.
        */
        int added = 0;
        for(OWLClassExpression concept: involved_concepts) {
            /*
               Dato che il modello è un insieme, se un concetto è già
               presente non verrà reinserito.
            */
            if(!concepts.contains(concept)){
                concepts.add(concepts.size(), concept);
                added++;
            }
        }
        
        /*
            Per ogni nuovo concetto aggiunti alla lista dei concetti viene aggiornata
            la lista delle dipendenze che sono date dall'insieme di dipendenze 
            appartenenti al branch corrente: il branch corrente è identificato 
            dall'intero 'current_concept' e con dependencies.get(current_concept)
            andiamo a passare l'insieme di dipendenze di questo branch al metodo
            updateDependencies.
        */
        if(added != 0)
            updateDependencies(dependencies.get(current_concept), concepts.size() - added, concepts.size());
        
        current_concept++; // Incrementa indice di regola.
        return SAT();   // Chiamata ricorsiva sulla regola successiva.
    }

    /*
        Effettua l'espansione della regola di unione (OR - ∨ - ∪).
        Condizione: C1 ∪ C2(x) ∈ A e {C1(x), C2(X)} ⊄ A
        Azione: Duplica l'Abox. A' ← A ∪ {C1(x)}, A'' ← A ∪ {C2(x)}
    */
    private boolean solveUnionRule(OWLObjectUnionOf union_rule){
        // Effettua il logging, se abilitato.
        if(LogUtility.isEnabled())
            LogUtility.putDebug("CONCETTO N: "+ current_concept + " (UNIONE): " + RenderUtility.renderMAN(union_rule).replace("\n", "").replace("\r", "").replace("\t", ""));

        
        // Insieme dei concetti coinvolti nell'unione. (come in solveIntersectionRule)
        List<OWLClassExpression> involved_concepts = union_rule.operands().collect(Collectors.toList());

        // Ordina i concetti secondo la relazione d'ordine definita in exp_comparator.
        involved_concepts.sort(exp_comparator);
        
        /*
           Inserisco l'intero corrispondente al concetto in analisi all'interno di curr_concept_dependencies, il quale
           contiene le dipendenze registrate a partire dall'espansione della regola corrente.
           ('current_concept').
        */
        ArrayList<Integer> curr_concept_dependencies = new ArrayList<>();
        curr_concept_dependencies.add(current_concept);
        
        /*
           Memorizzo preventivamente la lista di concetti e dipendenze al fine
           di poter ripristinare lo stato in caso di backjumping su questo nodo.
        */
        ArrayList<OWLClassExpression> old_concepts = new ArrayList<>(concepts);
        ArrayList<List<Integer>> old_dependencies = new ArrayList<>(dependencies);
        
        // Salva l'intero che indica il concetto corrente nell'insieme di regole valutate fino ad ora.
        int old_concept = current_concept;
         
        // Indica un concetto, di quelli trovati come operando dell'operazione di Unione.
        OWLClassExpression concept;

        /*
          Itero su tutti i concetti contenuti nell'UNIONE e verifico dato il concetto corrente:
          - CASO 1: se il concetto corrente D è già contenuto all'interno di concepts allora
                    vuol dire che tale concetto è già stato incontrato lungo il ramo che
                    sta venendo analizzato dal Tableau. E' dunque già stata verificata
                    la soddisfacibilità e possiamo richiamare ricorsivamente SAT()
                    per espandere la regola del prossimo concetto.

          - CASO 2: se il concetto non è ancora contenuto alla lista dei concetti
                    incontrati lungo il branch del Tableau si deve applicare la regola
                    e costruire una nuova ABOX formata dai concetti contenuti nel branch
                    corrente e il concetto attuale.

              - SOTTOCASO 2.1: dopo aver creato la ABOX vado a verificare la soddisfacibilità
                               e se L(x) con il nuovo concetto D1 provoca un clash provo a
                               costruire una nuova ABOX con un altro concetto D2 contenuto
                               nell'unione.
              - SOTTOCASO 2.2: dopo aver creato la ABOX vado a verificare la soddisfacibilità
                               e non trovo clash. Dato che si tratta di una disgiunzione
                               posso richiamarmi ricorsivamente sul prossimo concetto
                               per applicare la regola di espansione.
        */
        for (int i = 0; i < involved_concepts.size(); i++) {
            
            // Estrae l'i-esimo disgiunto.
            concept = involved_concepts.get(i);
            
            /*
                Inserisce il concetto appena estratto all'interno dell'insieme  
                dei concetti valutati fino ad ora da Tableau solo se non è già 
                presente.
            */
            if(!concepts.contains(concept)) {
                if(LogUtility.isEnabled())
                    LogUtility.putDebug("SCELGO " + RenderUtility.renderMAN(concept).replace("\n", "").replace("\r", "").replace("\t", "")
                                        + " (NON ANCORA INTRODOTTO)");
                /*
                    Aggiunge il concetto all'interno di "concepts", ovvero 
                    l'insieme di concetti finora valutati, e di seguito va a 
                    registrare le dipendenze di tale concetto prelevandole dai 
                    branch che sono stati attraversati (per tenere traccia del 
                    branch si conserva 'curr_rul_dependencies').
                */
                concepts.add(concepts.size(), concept);
                updateDependencies(curr_concept_dependencies, concepts.size() - 1, concepts.size());
                
                /*
                    Verifico se c'è clash dopo aver aggiunto alla ABOX corrente
                    il concetto "concept" (come previsto dalla regola di espansione).
                */
                if(Clash()){
                    
                    /* 
                        Se c'è clash si procede a disfare le operazioni che hanno
                        portato alla costruzione della ABOX CORRENTE per 
                        effettuare la costruzione della ABOX con il prossimo 
                        disgiunto.
                    */
                    concepts.remove(concepts.size() - 1);
                    dependencies.remove(dependencies.size() - 1);
                } else {
                    /*
                        Se non avviene un clash all'interno della ABOX corrente
                        procedo ad esaminare il concetto seguente incrementando di
                        1 il contatore current_concept e richiamandomi
                        ricorsivamente. La chiamata ricorsiva parte solamente 
                        se ho finito di esaminare tutti i concetti coinvolti 
                        nell'unione (-1 poichè i concetti partono da zero).
                    */
                    current_concept++;
                    
                    // Caso in cui tutti i disgiunti sono stati esaminati.
                    if (i == involved_concepts.size() - 1) return SAT();

                    // Caso in cui non ancora tutti i disgiunti sono stati esaminati
                    if(SAT()) return true;
                    /*
                        Caso in cui:
                        - SAT() è risultato falso
                        - concept non è presente come etichetta nelle quali si è
                          verificato un clash

                        Allora è proprio concept a causare l'insoddisfacibilità e
                        non posso effettuare il backjumping previsto dal 
                        Dipendency Directed Backtracking a nessun altro branch.
                    */
                    else if(!jump_candidates.contains(old_concept)){
                        return false;
                    }

                    /*
                        Caso in cui concept è presente nell'insieme delle etichette
                        che causano il clash e dunque è possibile saltare proprio
                        a concept per effettuare il trashing e percorrere un altro
                        ramo.
                    
                        Ripristino il concetto corrente.
                    */
                    current_concept = old_concept;
                    
                    
                    /*
                        Per ripristinare completamente il Tableau prima del 
                        backjump provvedo a ripristinare anche la lista di 
                        concetti e dipendenze.
                    */
                    concepts.removeAll(Collections.unmodifiableList(concepts));
                    concepts.addAll(old_concepts);
                    
                    dependencies.removeAll(Collections.unmodifiableList(dependencies));
                    dependencies.addAll(old_dependencies);

                    /*
                        Ripristino le strutture atte a contenere i quantificatori
                        allo stato in cui 'current_concept' = concept.
                    */
                    restoreRelation(existential_quantifiers);
                    restoreRelation(universal_quantifiers);
                    
                    if(LogUtility.isEnabled())
                        LogUtility.putDebug("EFFETTUO IL BACKJUMP ALLA REGOLA: " + old_concept);
                }
                
                /*
                    Aggiornamento delle dipendenze per il prossimo disgiunto.
                */
                for(Integer c: jump_candidates) {
                    if(!curr_concept_dependencies.contains(c))
                        curr_concept_dependencies.add(c);
                }
                
                /*
                   Effettuo l'ordinamento per estrarre il punto migliore in caso
                   di backjump conseguente ad un clash.
                */
                Collections.sort(curr_concept_dependencies);
            } else {
                if(LogUtility.isEnabled())
                    LogUtility.putDebug("SCELGO " + RenderUtility.renderMAN(concept).replace("\n", "").replace("\r", "").replace("\t", "") + " CHE ERA GIA' STATO INTRODOTTO");

                    /*
                        Dato che la ABOX corrente non contiene clash poichè il concetto esaminato era
                        già stato incontrato sul ramo corrente del Tableau devo semplicemente effettuare
                        una chiamata ricorsiva sul prossimo concetto e se il ramo viene chiuso con un
                        true posso restituire true, altrimenti andrò a costruire l'ABOX seguente
                        che include il prossimo disgiunto.
                    */
                    current_concept++;
                    if(SAT()) 
                        return true;
            }
        }

        /*
            Dato che tutti i concetti di questa regola di espansione sono insoddisfacibili vado a
            rimuovere tale branch dai possibili punti di jump in caso di backjump.
        */
        jump_candidates.remove(Integer.valueOf(old_concept));
        return false;
    }

    /* 
        Effettua l'espansione della regola esistenziale (some - ∃).
        Condizione: ∃R.C(x) ∈ A e ∄z t.c. {C(z), R(x,z)} ⊄ A
        Azione: A ← A ∪ {C(z), R(x, z)}, con z individuo che non occorre in A.
    */
    private boolean solveSomeRule(OWLObjectSomeValuesFrom some_rule){
        // Effettua il logging, se abilitato.
        if(LogUtility.isEnabled())
            LogUtility.putDebug("CONCETTO N: " + current_concept +
                                     " (QUANTIFICATORE ESISTENZIALE): " + RenderUtility.renderMAN(some_rule).replace("\n", "").replace("\r", "").replace("\t", ""));

        // Ottengo il tipo di relazione R presente all'interno del quantificatore esistenziale.
        OWLObjectPropertyExpression relation = some_rule.getProperty();
        
        /*
            Ottengo l'espressione che indica a quale insieme di individui deve 
            appartenere d' che sarà in relazione R con d, dato che per 
            definizione R(d, d').
        */
        OWLClassExpression filler = some_rule.getFiller();
        
        // Inizializzo l'insieme di branch su cui è poter effettuare backjump in caso di clash.
        jump_candidates = new ArrayList<>();
        
        // Lista che contiene gli individui, se esistono, che sono nella relazione "relation".
        List<Integer> existential_related = new ArrayList<>();

        /*  
            CASO 1: Esistono già degli individui che sono in questa relazione.

            Verifico se esistono concetti (o individui, regole) che godono di 
            tale relazione: ovvero, se esiste una relazione come quella corrente
            all'interno della Map existentialQuantifiers.
        */
        if(existential_quantifiers.get(relation) !=  null){
            
            // Se esiste allora aggiungo tutti gli individui a existential_related.
            existential_related.addAll(existential_quantifiers.get(relation));
            
            /*
                Verifico se la regola esistenziale ad ora analizzata è già 
                presente all'interno dei concetti soddisfacibili che andranno a 
                costituire il modello finale (e.g se sto analizzando ∃R.A 
                verifico se all'interno dei concetti trovati nella mappa
                existentialQuantifiers per R esiste A, in tal caso vuol dire che
                esiste un individuo che espande questa regola e posso procedere
                ad esaminare le regole seguenti del Tableau).
            */
            for (Integer related_index : existential_related) {
                /*
                    Casting del concetto identificato dall'intero 'related_index'
                    ad ObjectSomeValuesFrom per estrarre il filler e confrontarlo 
                    con quello presente all'interno della relazione originale. 
                    Se i due filler equivalgono vuol dire che la regola è già 
                    presente all'interno delle regole soddisfacibili e dunque 
                    si può proseguire ad esaminare la prossima regola.
                */
                if(filler.equals(((OWLObjectSomeValuesFrom) concepts.get(related_index)).getFiller())){
                    if(LogUtility.isEnabled())
                        LogUtility.putDebug("CONCETTO DEL QUANTIFICATORE ESISTENZIALE GIA' INTRODOTTO");

                    current_concept++; // Passo al prossimo concetto prima di invocare SAT.
                    return SAT();  // Richiamo SAT sul prossimo concetto.
                }
            }
        }
        /*
            CASO 2: Non esiste alcun individuo nella relazione "relation" che compare
                    nella definizione della regola ∃, oppure anche se vi sono individui
                    in questa relazione non c'è alcun individuo che contiene il concetto (filler)
                    nella sua lista di concetti finora soddisfacibili.
                    In questo caso si istanzia un nuovo individuo che rispetti entrambe le condizioni,
                    salvando la relazione. Prima di fare ciò però verifico se esiste già una
                    relazione "relation" nei quantificatori universali già espansi e verifico se ci sono
                    clash con il filler corrente.
         */
        if(universal_quantifiers.get(relation) != null){
            ArrayList<OWLClassExpression> involved_concepts = new ArrayList<>();
            /*
                Recupera tutti i concetti (o anche detti filler, ovvero dopo il punto in  ∃R.filler)
                che sono filler nei quantificatori universali con la STESSA RELAZIONE.
                Questa operazione serve poichè i quantificatori universale e esistenziale
                che hanno la stessa relazione potrebbero avere filler che causano clash
                (e.g.  ∃R.A e ∀R.¬A)
            */
            for(Integer rel_index: universal_quantifiers.get(relation)) {
                involved_concepts.add(((OWLObjectAllValuesFrom)concepts.get(rel_index)).getFiller());
                for(Integer dep_index: dependencies.get(rel_index)){
                    /*  FONDAMENTALE.
                        Quando introduco un nuovo individuo y vado ad inserire l'insieme delle dipendenze
                        degli individui introdotti dai quantificatori universali con la stessa relazione R.
                        Faccio ciò poichè tali dipendenze possono essere dei punti candidati al backjumping.
                     */
                    if(!jump_candidates.contains(dep_index))
                        jump_candidates.add(dep_index);
                }
            }
            
            /*
                Aggiunge ai concetti trovati come filler di tutti i 
                quantificatori universali che hanno la stessa relazione R anche 
                il concetto del quantificatore esistenziale corrente.
            */
            involved_concepts.add(filler);
            
            // Effettuo l'ordinamento dei concetti finora trovati in ∀ e ∃ con egual R.
            involved_concepts.sort(exp_comparator);
            /*
                Crea una espressione che sia l'intersezione di tutti i concetti
                che compaiono come filler nei quantificatori universali ed 
                esistenziali per verificare se vi è clash o se è possibile 
                aggiungere un nuovo individuo senza che si verifichi un clash.
             */
            filler = new OWLObjectIntersectionOfImpl(involved_concepts);
        }
        
        /*
            E' un tableau ausiliario utilizzato per verificare:
            - Caso 1 (non esiste ancora alcun individuo che è nella relazione R ne
              con l'operatore universale che esistenziale): va a verificare solo
              la soddisfacibilità del filler originale.
            - Caso 2 (non esiste ancora alcun individuo che è nella relazione R
              con l'operatore esistenziale R ma vi sono alcuni individui che
              sono nella relazione R con l'operatore universale): va a verificare
              l'intersezione dei filler di tutti i concetti dell'universale e del filler
              della regola esistenziale corrente.
        */
        Tableau filler_tableau = new DDBTableau(filler, current_concept);
        
        if(filler_tableau.SAT()){
            if(LogUtility.isEnabled())
                LogUtility.putDebug("QUANTIFICATORE ESISTENZIALE "+ current_concept +" NON CAUSA CLASH");

            /*
                Dato che il tableau che prende in input l'intersezione dei filler
                e non si verificano clash vado ad inserire all'interno della lista
                degli individui che appartengono alla relazione R quello appena
                introdotto (ovvero quello identificato dalla regola corrente).
             */
            existential_related.add(existential_related.size(), current_concept);
            
            // Aggiorno la lista degli individui che sono nella relazione "relation"
            existential_quantifiers.put(relation, existential_related);
            
            // Chiamata a SAT() sul prossimo concetto della lista "concepts".
            current_concept++;
            return SAT();
        }
        else
            {
                // Altrimenti si è verificato un clash e si restituisce falso aggiornando la lista delle dipendenze.
                if(LogUtility.isEnabled())
                    LogUtility.putDebug("QUANTIFICATORE ESISTENZIALE "+ current_concept +" CAUSA CLASH");

                /*
                    Prendo gli indici che indicano le dipendenze della regola
                    corrente e li aggiungo alla lista di quelli che hanno causato
                    un clash.
                */
                for(Integer dep_index: dependencies.get(current_concept)) {
                    if(!jump_candidates.contains(dep_index))
                        jump_candidates.add(dep_index);
                }

                /*
                    Infine riordino la lista per permettere all'algoritmo
                    di backjumping di ritornare all'antenato più lontano da cui
                    dipende il clash.
                */
                Collections.sort(jump_candidates);

                // Se arrivo qui, il concetto è insoddisfacibile.
                return false;
            }
    }

    /* 
        Effettua l'espansione della regola esistenziale (some - ∃).
        Condizione: {∀R.C(x), R(x,y)} ⊆ A per quale individuo y e C(y)∉ A
        Azione: A ← A ∪ {C(y)}.
    */
    private boolean solveAllRule(OWLObjectAllValuesFrom all_rule){
        // Stampo i log.
        if(LogUtility.isEnabled())
            LogUtility.putDebug("CONCETTO N: " + current_concept + " (QUANTIFICATORE UNIVERSALE): "
                                     + RenderUtility.renderMAN(all_rule).replace("\n", "").replace("\r", "").replace("\t", ""));
        
        // Ottengo il tipo di relazione R presente all'interno del quantificatore universale.
        OWLObjectPropertyExpression relation = all_rule.getProperty();
        
        /*
            Ottengo l'espressione che indica a quale insieme di individui deve 
            appartenere d' che sarà in relazione R con d, dato che per 
            definizione R(d, d').
        */
        OWLClassExpression filler = all_rule.getFiller();
      
        /*
            Se non è stata effettuata l'espansione di alcun concetto con quantificatore
            esistenziale che contiene la relazione "relation" procedo
            semplicemente ad aggiungere la relazione all'interno di universal_quantifiers
            dato che non vi è il bisogno di verificare se la regola esistenziale
            ed universale hanno un conflitto.
        */
        if (existential_quantifiers.get(relation) == null){
            if(LogUtility.isEnabled())
                LogUtility.putDebug("NON VI SONO QUANTIFICATORI ESISTENZIALI CON LA REGOLA CORRENTE");

        } else {
            /*
                Altrimenti, se è stata trovata una regola di quantificazione esistenziale
                avente la stessa relazione "relation" vado a verificare immediatamente
                che non vi siano clashes tra i fillers. (alla stessa maniera
                in cui è fatto nel metodo solveSomeRule)
             */

            // Inizializzo l'insieme di branch su cui è poter effettuare backjump in caso di clash.
            jump_candidates = new ArrayList<>();

            /*
                Raccolgo tutti i filler (o concetti) delle regole di quantificazione universale e
                esistenziale.
            */
            ArrayList<OWLClassExpression> universal_fillers_container = new ArrayList<>();
            universal_fillers_container.add(filler);

            /*
                Verifico se esistono individui (o concetti) che godono di
                tale relazione: ovvero, se esiste una relazione come quella corrente
                all'interno della Map universal_quantifiers.
            */
            if(universal_quantifiers.get(relation) != null){
                /*
                    Recupera tutti i concetti (o anche detti filler, ovvero
                    dopo il punto in  ∀R.filler) che sono filler nei
                    quantificatori universali con la STESSA RELAZIONE.
                    Questa operazione serve poichè i quantificatori universali
                    che hanno la stessa relazione potrebbero avere filler che
                    causano clash (e.g.  ∀R.A e ∀R.¬A)
                */
                for(Integer rel_index: universal_quantifiers.get(relation)){

                    // Aggiungo i filler della quantificatore universale in posizione "rel_index".
                    universal_fillers_container.add(((OWLObjectAllValuesFrom) concepts.get(rel_index))
                                                                                                .getFiller());

                    for(Integer dep_index: dependencies.get(rel_index)){
                        /*  FONDAMENTALE.
                            Quando introduco un nuovo individuo y vado ad inserire l'insieme delle
                            dipendenze degli individui introdotti dai quantificatori universali con la
                            stessa relazione R. Faccio ciò poichè tali dipendenze possono essere
                            dei punti candidati al backjump.
                        */
                        if(!jump_candidates.contains(dep_index))
                            jump_candidates.add(dep_index);
                    }
                }
                // Effettuo l'ordinamento dei concetti finora trovati in ∀ con egual R.
                universal_fillers_container.sort(exp_comparator);
            }

            // Lista che contiene gli individui che sono nella relazione "relation"
            // di quantificatori esistenziali.
            ArrayList<Integer> existential_related = new ArrayList<>(existential_quantifiers.get(relation));

            for(Integer related_index : existential_related){
                 /*
                    Casting del concetto identificato dall'intero 'related_index'
                    ad ObjectSomeValuesFrom per estrarre il filler e confrontarlo
                    con quello presente all'interno della relazione originale.
                    Se i due filler non equivalgono vuol dire che la regola
                    non è presente all'interno delle regole soddisfacibili e dunque
                    bisogna verificarne la soddisfacibilità.
                */
                if(!filler.equals(((OWLObjectSomeValuesFrom) concepts.get(related_index)).getFiller())){

                    ArrayList<OWLClassExpression> involved_concepts = new ArrayList<>();

                    // Inserisco i concetti che sono coinvolti fino ad ora nella relazione R
                    involved_concepts.add(((OWLObjectSomeValuesFrom) concepts.get(related_index)).getFiller());

                    // Inserisco tutti i filler universali già spacchettati.
                    involved_concepts.addAll(universal_fillers_container);
                    involved_concepts.sort(exp_comparator);

                    // Creo intersezione dei concetti in involved_concepts.
                    OWLObjectIntersectionOf concept = new OWLObjectIntersectionOfImpl(involved_concepts);

                    Tableau filler_tableau = new DDBTableau(concept, current_concept);

                    // Caso in cui il concetto è insoddisfacibile.
                    if (!filler_tableau.SAT()) {
                        if(LogUtility.isEnabled())
                            LogUtility.putDebug("QUANTIFICAZIONE UNIVERSALE INSODDISFACIBILE");

                        /*
                            Prendo gli indici che indicano le dipendenze della
                            regola corrente e li aggiungo alla lista di quelli
                            che hanno causato il clash.
                        */
                        for(Integer dep_index: dependencies.get(current_concept)){
                            if(!jump_candidates.contains(dep_index))
                                jump_candidates.add(dep_index);
                        }

                        /*
                            Prendo gli indici che indicano le dipendenze della
                            regola esistenziale in cui la relazione è vincolata
                            e li aggiungo alla lista di quelli che hanno causato
                            il clash.
                        */
                        for(Integer dep_index: dependencies.get(related_index)){
                            if(!jump_candidates.contains(dep_index))
                                jump_candidates.add(dep_index);
                        }

                        /*
                            Infine riordino la lista per permettere
                            all'algoritmo di backjumping di ritornare
                            all'antenato più lontano da cui dipende il clash.
                        */
                        Collections.sort(jump_candidates);
                        return false;
                    }

                    if(LogUtility.isEnabled())
                        LogUtility.putDebug("QUANTIFICAZIONE UNIVERSALE "+ current_concept
                                                +" SODDISFACIBILE");
                }
            }
        }
        
        /*
            Controllo se in universalQuantifiers in corrispondenza della key
            'relation' è già presente una lista di dipendenze: in caso negativo
            utilizzo la funzione '.put' per inizializzare le dipendenze con
            'currentRule', altrimenti se già vi sono dipendenze accodo 'currentRule'.
        */
        if(universal_quantifiers.get(relation) == null){
            universal_quantifiers.put(relation, Collections.singletonList(current_concept));
        } else {
            universal_quantifiers.get(relation).addAll(new ArrayList<>(current_concept));
        }

        // Richiamo SAT sul prossimo concetto.

        current_concept++;
        return SAT();
    }
    
    /*
        Dato che dependencies è una lista di liste poichè per ogni concetto, 
        rappresentato dall'indice della prima lista, sono presenti un insieme di
        dipendenze, rappresentati dall'indice della seconda lista, questa 
        funzione scorre la lista di concetti (la prima) e per ogni concetto 
        inizializza una lista contenente le dipendenze di quel concetto.
    */
    private void updateDependencies(List<Integer> concept, int from_position, int to_position){
        for(int i = from_position; i < to_position; i++)
            dependencies.add(i, new ArrayList<>(concept));
    }
    
    /* Restituisce vero se si è verificato un clash sull'insieme dei concetti attualmente contenuti
       all'interno della lista concepts, falso altrimenti.
    */
    private boolean Clash() {
        // Inizializzo la lista di punti candidati al jump.
        jump_candidates = new ArrayList<>();
        
        // Itera su tutti i concetti.
        for (int curr = 0; curr < concepts.size(); curr++) {
            
            // Seleziona il concetto corrente.
            OWLClassExpression curr_concept = concepts.get(curr);

            // Se il concetto corrente è BOTTOM possiamo già fermarci.
            if(curr_concept.isOWLNothing())
                return true;

            /*
                Se non ho trovato nessun bottom fisso il concetto corrente e lo 
                vado a paragonare con tutti i concetti contenuti nel tableau 
                fino alla chiusura del ramo. La verifica consiste nel 
                confrontare il concetto corrente, curr_concept, con uno dei 
                concetti successivi, next_concept, per osservare se 
                curr_concept == not(next_concept): in tal caso siamo in presenza
                di un clash.
            */
            for (int next = curr+1; next < concepts.size(); next++) {

                OWLClassExpression next_concept = concepts.get(next);

                /*
                    Se sono in presenza di un clash vado ad aggiungere tutti gli
                    id dei branch da cui dipendono i concetti che hanno causato 
                    un clash. L'ultima istruzione di questo blocco effettua 
                    l'ordinamento proprio per permettere all'algoritmo di 
                    ritornare al branch più vecchio da cui dipende il clash: ciò
                    ci permette di effettuare l'operazione di TRASHING in
                    maniera corretta. Se non venisse effettuato l'ordinamento si
                    rischierebbe di effettuare un backjumping ad un branch che 
                    potrebbe portare ad un nuovo clash dipendente ancora una 
                    volta dai concetti che hanno causato questo clash.
                */
                if (curr_concept.equals(next_concept.getComplementNNF())){
                    
                    // Scrittura dei log.
                    if(LogUtility.isEnabled())
                        LogUtility.putDebug("CLASH!! "+ RenderUtility.renderMAN(curr_concept).replace("\n", "").replace("\r", "").replace("\t", "") +
                                                 " AND " + RenderUtility.renderMAN(next_concept).replace("\n", "").replace("\r", "").replace("\t", ""));
                    
                    // Aggiungo tutte le dipendenze di curr_concept in jump_candidates.
                    jump_candidates.addAll(dependencies.get(curr));
                    
                    /*
                        Itera su tutte le dipendenze del concetto paragonato a quello attuale per
                        aggiungere anche queste, se non sono già presenti, all'interno della lista
                        dei punti candidati dove effettuare il salto per l'algoritmo di backjumping.
                    */
                    for (Integer d: dependencies.get(next)) {
                        // Trattandosi di un insieme non vado ad inserire lo 
                        // stesso branch più di una volta.
                        if(!jump_candidates.contains(d))
                            jump_candidates.add(d);
                    }
                    
                    // Ordina in maniera crescente i jump_candidates.
                    Collections.sort(jump_candidates);
                    
                    // Essendosi verificato il clash, restituisco TRUE.
                    return true;
                }
            }
        }
        
        // Se non si verificato il clash potrò ritornare FALSE.
        return false;
    }
    
    @Override
    // Restituisce una configurazione che soddisfa il concetto ALC in input.
    // Non si tratta di un modello vero e proprio (con gli individui e le relazioni)
    // dato che non era richiesto dalla traccia. Abbiamo voluto però tenere traccia
    // di tutti i concetti che messi in congiunzione soddisfacessero il Tableau.
    public String getSolution(){
        String solution = "";
      
        // Itera sui concetti selezionati da SAT e seleziona solo gli atomi, 
        // quindi esclude dalla soluzione tutti i concetti composti 
        // (intersezioni o unioni).
         for(int i=0; i < concepts.size(); i++) {
                OWLClassExpression exp = concepts.get(i);
                ClassExpressionType typeExp = exp.getClassExpressionType();
                if(typeExp.equals(ClassExpressionType.OWL_CLASS) ||
                   typeExp.equals(ClassExpressionType.OBJECT_COMPLEMENT_OF) ||
                   typeExp.equals(ClassExpressionType.OBJECT_SOME_VALUES_FROM) ||
                   typeExp.equals(ClassExpressionType.OBJECT_ALL_VALUES_FROM)){
                    // Aggiungi espressione alla soluzione.
                    solution += RenderUtility.renderDL(exp).replace("\n", "").replace("\r", "").replace("\t", "");
                    if(i < concepts.size()-1)
                        solution += " & ";
                }
        }

        return solution;
    }

    // Serve a ripristinare le mappe delle Relazioni introdotte da quantificazioni universali ed esistenziali
    // nel caso in cui ci fosse un backjumping.
    private void restoreRelation(Map<OWLObjectPropertyExpression, List<Integer>> relation){
        Set<OWLObjectPropertyExpression> relationList = relation.keySet();
        
        for(OWLObjectPropertyExpression exp : relationList) {
            ArrayList<Integer> temporary_relation = new ArrayList<>(relation.remove(exp));
            /*
                Itero su temporary_relation per andare ad eliminare dalla struttura che
                rappresenta un insieme di concetti introdotti da una data relazione R
                Per effettuare questo ripristino vado semplicemente ad eliminare
                tutti gli interi maggiori di current_concept (ovvero che
                non sarebbero stati inseriti nella lista se non avessimo analizzato
                la regola corrente)
            */
            for (int i = temporary_relation.size()-1; i >= 0; i--) {
                if(temporary_relation.get(i) > current_concept)
                    temporary_relation.remove(i);
            }
            
            /*
                exp: rappresenta la relazione in analisi.
                temporary_relation: è la lista di interi ripristinata.
            */
            if (!temporary_relation.isEmpty()) {
                relation.put(exp, temporary_relation);
            }
        }
    }
}


