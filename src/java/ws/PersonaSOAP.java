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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author ALIENWARE
 */
@WebService(serviceName = "PersonaSOAP")
public class PersonaSOAP {
    @EJB
    PersonaFacadeLocal personaCtl;

    List<Persona> personas;
    XStream xs;
    DateConverter dateConverter;
    final String[] PATRONES;

    public PersonaSOAP() {
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
    
    @WebMethod(operationName = "getPersonasXML")
    public String getPersonasXML() {
        String resultado = "";

        personas = personaCtl.findAll();

        if ((personas != null) && (personas.size() > 0)) {
            resultado = xs.toXML(personas);
        } else {
            resultado = "<resultado>"
                    + "<mensaje>No hay datos</mensaje>"
                    + "</resultado>";
        }
        return resultado;
    }
}
