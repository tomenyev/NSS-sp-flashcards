package cz.cvut.ear.flashcards.interceptor;

import cz.cvut.ear.flashcards.security.jwt.JwtUtils;
import cz.cvut.ear.flashcards.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Interceptor
 * @author Roman Filatov
 * @author Yevheniy Tomenchuk
*/
@Component
public class Interceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    /** Pre Handler
     * @param request
     * @param response
     * @param handler
     * @return boolean Drop connection or not
    */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean res = this.tokenCheck(request);
        if(request.getRequestURI().contains("/api/auth")) {
            Constants.authLogger.writeToLog(
                    "Auth request!\n"+
                            "\trequest URI: " + request.getRequestURI() + "\n" +
                            "\tmethod: " + request.getMethod() + "\n" +
                            "\tauth type: " + request.getAuthType() + "\n" +
                            "\ttoken: " + request.getHeader("authorization") + "\n"
            );
        }
        if(!res) {
            Constants.badAccessLogger.writeToLog(
                    "Bad access error!\n"+
                            "\trequest URI: " + request.getRequestURI() + "\n" +
                            "\tmethod: " + request.getMethod() + "\n" +
                            "\tauth type: " + request.getAuthType() + "\n" +
                            "\ttoken: " + request.getHeader("authorization") + "\n"
            );
            response.sendError(407, "Authorize first to use application!");
        }
        return res;
    }

    /** Post Handler
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @return boolean Drop connection or not
    */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(request.getRequestURI().contains("/api/auth")) {
            Constants.authLogger.writeToLog(
                    "Auth response!\n"+
                            "\trequest URI: " + request.getRequestURI() + "\n" +
                            "\tmethod: " + request.getMethod() + "\n" +
                            "\tresponse status: " + response.getStatus() + "\n" +
                            "\tauth type: " + request.getAuthType() + "\n" +
                            "\ttoken: " + request.getHeader("authorization") + "\n"
            );
        }
    }

    /** Check token
     * @param request
    */
    private boolean tokenCheck(HttpServletRequest request) {
        String jwt = jwtUtils.parseJwt(request);
        return request.getRequestURI().contains("/api/auth") || (jwt != null && jwtUtils.validateJwtToken(jwt));
    }
}
