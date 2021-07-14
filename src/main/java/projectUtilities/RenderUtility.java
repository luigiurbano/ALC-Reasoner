package projectUtilities;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxOWLObjectRendererImpl;

/*
    Classe per gestire il rendering dei concetti in notazione DL e Manchester.
    E' stato implementato il design pattern "Singleton" poiché più
    classi possono necessitare dei render, che però restano invariati, quindi
    anziché avere più oggetti con la stessa funzione è stato scelto di rendere
    istanziabile un solo oggetto di tipo OWLRenderer, così da risparmiare memoria
    e rendere publici e statici i metodi per utilizzare i due render.
*/
public class RenderUtility {
    private static RenderUtility singleton; // Design Pattern Singleton (Inizializzo solo un OWLRenderer)
    private static OWLObjectRenderer dl_renderer;  // DL syntax render
    private static OWLObjectRenderer man_renderer; // Manchester syntax render

    private static void RenderUtility(){
        // Inizializzo il render in DL syntax.
        dl_renderer = new DLSyntaxObjectRenderer();
        dl_renderer.setShortFormProvider(new SimpleShortFormProvider());

        // Inizializzo il render in Manchester syntax.
        man_renderer = new ManchesterOWLSyntaxOWLObjectRendererImpl();
        man_renderer.setShortFormProvider(new SimpleShortFormProvider());
    }

    /*
        Inizializza il render (Singleton).
    */
    public static void initRenderer(){
        if(singleton == null)
            RenderUtility();
    }

    // Rendering in DL Syntax (utilizza dl_renderer).
    public static String renderDL(OWLObject object){
        if(singleton == null)
            RenderUtility();

        return dl_renderer.render(object);
    }

    // Rendering in MAN Syntax (utilizza man_renderer).
    public static String renderMAN(OWLObject object){
        if(singleton == null)
            RenderUtility();

        return man_renderer.render(object);
    }
}