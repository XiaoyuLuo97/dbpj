import it.polimi.db2.entities.MarketingQuestion;
import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.Questionnaire;
import it.polimi.db2.services.ProductService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/UserHome")
public class GoToUserHomePage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;

    @EJB(name = "it.polimi.db2.services/ProductService")
    private ProductService productService;


    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect to the login page if session is new.
        String path = "/WEB-INF/UserHome.html";
        ServletContext servletContext = getServletContext();
        String loginpath = getServletContext().getContextPath() + "/index.html";
        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute("user") == null) {
            response.sendRedirect(loginpath);
            return;
        }

        //get the today's product, and add it into webContext.
        Product product=productService.getTodayProduct();

        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        if(product==null){
            ctx.setVariable("errorMsg", "No product of today");
        }else{
            System.out.println("Product Of the day: "+product.getName()+", the number of marketing questions: "+product.getMarketingQuestionsList().size());
            ctx.setVariable("productOfDay", product);
        }

        templateEngine.process(path, ctx, response.getWriter());

        request.getSession().removeAttribute("errorMsgHome");
        request.getSession().removeAttribute("product");

    }


    public void destroy(){

    }
}
