package com.jetbrains.MyWebApp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/myServlet")
public class MyServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.write("<html>");
        out.write("<head><title>Hola!</title></head>");
        out.write("<body>");

        String name = request.getParameter("name");
        String gitHub = request.getParameter("gitHub");
        String validationRegex = "^[a-zA-Z\\s]+";
        if(name.matches(validationRegex) && gitHub.matches(validationRegex)) {
            Person person = new Person();
            person.setName(name);
            person.setGitHub(gitHub);

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();

            out.write("<h1>Guardado!</h1>");

            out.write("<table border ='1'>");
            out.write("<tr>");
            out.write("<th>Name          </th>");
            out.write("<th>Usuario de GitHub</th>");
            out.write("</tr>");

            List<Person> people = em
                    .createQuery("Select p from Person p", Person.class)
                    .getResultList();
            for (Person p: people) {
                out.write("<tr>");
                out.write("<td>" + p.getName() +"</td>");
                out.write("<td>" + p.getGitHub() +"</td>");
                out.write("</tr>");
            }

            out.write("</table>");
        } else {
            out.write("<h1>Lo Siento! Sólo acepto letras!</h1>");
        }

        out.write("<br><a href=\"index.jsp\">Regresar al form</a>");
        out.write("</body></html>");
    }

    public void destroy() {
    }
}