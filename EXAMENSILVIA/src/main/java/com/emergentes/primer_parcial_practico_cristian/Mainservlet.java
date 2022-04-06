
package com.emergentes.primer_parcial_practico_cristian;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "Mainservlet", urlPatterns = {"/Mainservlet"})
public class Mainservlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession ses = request.getSession();
        if(ses.getAttribute("lista")==null){
            ArrayList<producto> listaux= new ArrayList<producto>();
            ses.setAttribute("lista", listaux);
            
        }
        ArrayList<producto> lista = (ArrayList<producto>)ses.getAttribute("lista");
        String op = request.getParameter("op");
        String opcion = (op != null) ? request.getParameter("op") : "view";
        producto prod = new producto();
        int id, pos;
        switch(opcion){
            case "nuevo":
                request.setAttribute("produc", prod);
                request.getRequestDispatcher("producto.jsp").forward(request, response);
                break;
            
            case "editar":
                id=Integer.parseInt(request.getParameter("id"));
                pos = buscarIndice(request,id);
                prod=lista.get(pos);
                request.setAttribute("produc", prod);
                request.getRequestDispatcher("producto.jsp").forward(request, response);
                break;
            case "eliminar":
                pos = buscarIndice(request,Integer.parseInt(request.getParameter("id")));
                lista.remove(pos);
                ses.setAttribute("lista", lista);
                response.sendRedirect("index.jsp");
                break;
            case "view":
                response.sendRedirect("index.jsp");
        }
      
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession ses = request.getSession();
        ArrayList<producto> lista = (ArrayList<producto>)ses.getAttribute("lista");
        producto prod = new producto();
        int id = Integer.parseInt(request.getParameter("id"));
        String descripcion = request.getParameter("descripcion");
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));
        double precio = Double.parseDouble(request.getParameter("precio"));
        String categoria = request.getParameter("categoria");
        prod.setId(id);
        prod.setDescripcion(descripcion);
        prod.setCantidad(cantidad);
        prod.setPrecio(precio);
        prod.setCategoria(categoria);
        int idt = prod.getId();
        if(idt==0){
            int ultID;
            ultID = ultimoId(request);
            prod.setId(ultID);
            lista.add(prod);
        }else{
            lista.set(buscarIndice(request,idt),prod);
        }
        ses.setAttribute("lista", lista);
        response.sendRedirect("index.jsp");
    }
    
    private int ultimoId(HttpServletRequest request){
        HttpSession ses = request.getSession();
        ArrayList<producto> lista = (ArrayList<producto>)ses.getAttribute("lista");
        int idaux=0;
        for (producto item : lista){
            idaux=item.getId();
        }
        return idaux+1;
    }
    
    private int buscarIndice(HttpServletRequest request, int id) {
        HttpSession ses = request.getSession();
        ArrayList<producto> lista = (ArrayList<producto>)ses.getAttribute("lista");
        int i=0;
        if(lista.size()>0){
            while (i<lista.size()){
                if (lista.get(i).getId()==id){
                    break;
                }
                else{
                    i++;
                }
            }
        }
        return i;
    }


}
