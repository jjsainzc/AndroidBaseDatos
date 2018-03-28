/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import aplicaciones.sainz.jorge.manejopersonas.conversores.ConversorDouble;
import aplicaciones.sainz.jorge.manejopersonas.conversores.ConversorFecha;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import dao.PersonaFacadeLocal;
import entidades.Persona;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author ALIENWARE
 */
@Path("personas_rs")
public class PersonasRS {

    @EJB
    PersonaFacadeLocal personaCtl;

    @Context
    private UriInfo context;

    List<Persona> personas;
    XStream xs;
    DateConverter dateConverter;
    final String[] PATRONES;

    /**
     * Creates a new instance of PersonasRS
     */
    public PersonasRS() {
        personas = new ArrayList();

        PATRONES = new String[]{"dd-MMM-yyyy",
            "dd-MMM-yy",
            "yyyy-MMM-dd",
            "yyyy-MM-dd",
            "yyyy-dd-MM",
            "yyyy/MM/dd",
            "yyyy.MM.dd",
            "MM-dd-yy",
            "dd-MM-yyyy"};

        xs = new XStream(new DomDriver());
        xs.alias("personas", List.class);
        xs.alias("personas", Vector.class);
        xs.alias("persona", Persona.class);

        dateConverter = new DateConverter("yyyy-MM-dd", PATRONES);

        xs.registerConverter(new ConversorDouble());
        xs.registerConverter(new ConversorFecha());
        xs.omitField(Persona.class, "personaId");
    }

    private final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();

    @GET
    @Produces(value = MediaType.APPLICATION_XML)
    public void getXml(@Suspended final AsyncResponse asyncResponse) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doGetXml());
            }
        });
    }

    private Response doGetXml() {
        String resultado = "";
        int codigo = 200;

        personas = personaCtl.findAll();

        if ((personas != null) && (personas.size() > 0)) {
            resultado = xs.toXML(personas);
        } else {
            resultado = "<resultado>"
                    + "<mensaje>No hay datos</mensaje>"
                    + "</resultado>";
            codigo = 202;
        }

        return Response
                .ok(resultado)
                .status(codigo)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .header("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia")
                .allow("OPTIONS")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML)
                .build();
    }

    @PUT
    @Consumes(value = MediaType.APPLICATION_XML)
    @Produces(value = MediaType.TEXT_PLAIN)
    public void putXml(@Suspended final AsyncResponse asyncResponse, final String content) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                doPutXml(content);
                asyncResponse.resume(javax.ws.rs.core.Response.ok().build());
            }
        });
    }

    private Response doPutXml(String content) {
        String resultado = "Ok";
        int codigo = 200;

        try {
            personas.clear();
            personas.addAll((List<? extends Persona>) xs.fromXML(content));
            for (Persona persona : personas) {
                personaCtl.create(persona);
            }
        } catch (NullPointerException e) {
            resultado = e.toString();
            codigo = 202;
        }

        return Response
                .ok(resultado)
                .status(codigo)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .header("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia")
                .allow("OPTIONS")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN)
                .build();
    }
}
