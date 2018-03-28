/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import aplicaciones.sainz.jorge.manejopersonas.conversores.ConversorDouble;
import aplicaciones.sainz.jorge.manejopersonas.conversores.ConversorFecha;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import dao.PersonaFacadeLocal;
import entidades.Persona;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ALIENWARE
 */
public class PersonaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    PersonaFacadeLocal personaCtl;

    List<Persona> personas;
    XStream xs;
    DateConverter dateConverter;
    final String[] PATRONES;

    public PersonaServlet() {
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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String resultado;
        int codigo = HttpServletResponse.SC_BAD_REQUEST;

        response.setContentType("application/xml;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        PrintWriter out = response.getWriter();

        personas = personaCtl.findAll();

        if ((personas != null) && (personas.size() > 0)) {
            resultado = xs.toXML(personas);
            codigo = HttpServletResponse.SC_OK;
        } else {
            resultado = "<resultado>"
                    + "<mensaje>No hay datos</mensaje>"
                    + "</resultado>";
            codigo = HttpServletResponse.SC_ACCEPTED;
        }
        response.setStatus(codigo);

        out.println(resultado);
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
