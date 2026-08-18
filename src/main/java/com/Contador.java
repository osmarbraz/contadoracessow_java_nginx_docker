package com;

import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Contador", urlPatterns = {"/servlet/Contador"})
public class Contador extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try ( PrintWriter out = response.getWriter()) {    
            // Conecta ao Redis
            Jedis redis = new Jedis("redis", 6379);

            // Obtém a operação solicitada
            String acao = request.getParameter("acao");

            // Executa a operação
            if ("incrementar".equals(acao)) {
                redis.incr("contador");
            } else {
                if ("zerar".equals(acao)) {
                    redis.del("contador");
                }
            }

            // Obtém o valor atual
            String valor = redis.get("contador");
            if (valor == null) {
                valor = "0";
            }

            // Fecha a conexão com o Redis
            redis.close();

            // Recupera o nome do servidor
            String servidor = System.getenv("HOSTNAME");
            
            // Página HTML
            out.println("<html><head><title>Contador</title></head><body>");
            out.println("<h1>Contador</h1>");       
            out.println("<p>Servidor: " + servidor + "</p>");
            out.println("<p>Valor: " + valor + "</p><p>");
            out.println("<a href='/servlet/Contador?acao=incrementar'>Incrementar</a>");
            out.println("</p>");
            out.println("<p>");
            out.println("<a href='/servlet/Contador?acao=zerar'>Zerar</a>");
            out.println("</p>");
            out.print("</body></html>");
        }
    }
}
