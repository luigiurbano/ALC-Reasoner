package projectUtilities;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;




import java.nio.file.Files;


/*
   Classe utilizzata per ottenere un singolo oggetto che si occuperà di compiere le operazioni di logging.
   E' qui applicato il Design Pattern "Singleton": la ragione dell'applicazione è che durante tutta
   l'elaborazione necessiteremo di un unico oggetto di tipo LogUtility e che semmai si dovesse istanziare
   un nuovo oggetto di questa classe si andrà sempre a sostituire quello precedente.
*/
public class LogUtility {

    // Singleton.
    private static LogUtility singleton = null;

    // Attributo log della classe Logger di Log4J.
    private static Logger log;

    // Attributo statico che indica se il logger è abilitato o meno.
    private static boolean state;

    private LogUtility() {
        // File in cui verrà salvato l'intero log.
        File file = new File ("Log/Result.log");

        // Set dell'attributo logfile.name del logger.
        System.setProperty("logfile.name","Log/Result.log");

        // Istanza di Logger con input uguale al nome del file (solo il nome).
        log = Logger.getLogger("Result");

        // Abilita il logger.
        state = true;
    }

    /*
        Inizializza il logger (Singleton).
    */
    public static synchronized void initLogger(){
        if (singleton == null){
            singleton = new LogUtility();
        }
        state = true;
    }

    // Scrive log di informazioni generiche.
    public static void putInfo(String msg){
        // Importa proprietà del logger.
        InputStream properties = LogUtility.class.getClassLoader().getResourceAsStream("log4j.properties");
        PropertyConfigurator.configure(properties);
        
        log.info(msg + "\n"); // Log del messaggio di info.
    }

    // Scrive log di errore.
    public static void putError(String msg){
        // Importa proprietà del logger.
        InputStream properties = LogUtility.class.getClassLoader().getResourceAsStream("log4j.properties");
        PropertyConfigurator.configure(properties);
        
        log.error(msg + "\n"); // Log del messaggio di errore.
    }

    // Scrive log di debugging.
    public static void putDebug(String msg) {
        // Importa proprietà del logger.
        InputStream properties = LogUtility.class.getClassLoader().getResourceAsStream("log4j.properties");
        PropertyConfigurator.configure(properties);
        
        log.debug(msg + "\n");  // Log del messaggio di debug.
    }

    /*
        Metodo per settare il file in cui andranno scritti i log di ogni ontologia.
    
        Riceve in:
        - input il nome del file in cui dovrà essere scritto il log,
        così da poter suddividere i log di più ontologie in file differenti
        ed eseguire un logging più pulito.
        Il path del file ha una parte predefinita: solo il nome del file cambia
        ed è fornito in input.
    */
    public static void setFile(String name) {
        // Crea nuovo file.
        File file = new File ("Log/"+name+".log");

        try {
            // Elimina il file nel percorso selezionato.
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("[ERRORE] - Non è stato possibile eliminare il file "+file.getName());
        }


        System.setProperty("logfile.name","Log/"+name+".log");
    }

    // Verifica se il logger è attivo.
    public static boolean isEnabled(){
        return state;
    }

    // "Disattiva" il logger settando lo stato a false.
    public static void turnOff(){state = false;}
}