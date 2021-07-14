package UrbanoOWLReasoner;

import java.util.List;

import org.semanticweb.owlapi.model.OWLClassExpression;


/*
   Classe astratta che rappresenta la struttura di base del Tableau, ovvero
   l'oggetto che conterrà l'espressione e su cui il reasoner applicherà
   l'algoritmo di ragionamento.
*/
public abstract class Tableau {

    /* 
       Lista di concetti: all'inizio vi è solo il "concetto zero", ovvero proprio il concetto ALC
       di cui si deve verificare ancora la soddisfacibilità. Al termine dell'esecuzione vi saranno
       tutti i concetti che sono stati espansi.
    */
    protected List<OWLClassExpression> concepts;


    /*
        Attiva il processo di ragionamento e ne restituisce il risultato
       (true = soddisfacibile, false = insoddisfacibile).
    */
    abstract public boolean SAT();

    /*
        Restituisce una configurazione che soddisfi il concetto (soluzione)
        NOTA: Il metodo getSolution() non verifica se il concetto è soddisfacibile.
              Per un corretto utilizzo bisogna verificare la soddisfacibilità
              del concetto richiamando il metodo SAT().
    */
    abstract protected String getSolution();
}