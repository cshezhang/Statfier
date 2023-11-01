import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

class SpelView implements View {
    private final String template;

    private final SpelExpressionParser parser = new SpelExpressionParser();

    private final StandardEvaluationContext context = new StandardEvaluationContext();

    private PropertyPlaceholderHelper.PlaceholderResolver resolver;

    public SpelView(String template) {
        this.template = template;
        this.context.addPropertyAccessor(new MapAccessor());
        this.resolver = name -> {

            try {
                Expression expression = parser.parseExpression(name); //BOOM!
                Object value = expression.getValue(context);
                return value == null ? null : value.toString();
            }
            catch (Exception e) {
                return null;
            }
        };
    }

    public String getContentType() {
        return "text/html";
    }

    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> map = new HashMap<>(model);
        String path = ServletUriComponentsBuilder.fromContextPath(request).build()
                .getPath();
        map.put("path", path==null ? "" : path);
        context.setRootObject(map);
        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}");
        String result = helper.replacePlaceholders(template, resolver);
        response.setContentType(getContentType());
        response.getWriter().append(result);
    }
}

/**
 * <p>
 * This custom error page is intended to facilitate the exploitation of
 * the injection.
 * Error based injection is then possible and no script should be needed to extract value.
 * </p>
 * <p>
 * (The default error page will be too generic otherwise.)
 * </p>
 */
@Controller
public class VerboseErrorController implements ErrorController {

    private static final String PATH = "/error";

    private static final String ERROR_TEMPLATE = "<html><body><h1>${error.error}</h1><h2>${error.exception}</h2><p>${error.message}</p></body></html>";

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(PATH)
    private ModelAndView error(HttpServletRequest request) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Map<String,Object> att = errorAttributes.getErrorAttributes(requestAttributes, true);

        //return new ModelAndView("error", att);
        ModelAndView model = new ModelAndView(new SpelView(ERROR_TEMPLATE));
        model.addObject("error", att);

        return model;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
