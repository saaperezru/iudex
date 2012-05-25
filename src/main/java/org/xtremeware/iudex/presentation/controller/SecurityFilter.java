package org.xtremeware.iudex.presentation.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.xtremeware.iudex.helper.Role;

/**
 *
 * @author healarconr
 */
@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"}, initParams = {
    @WebInitParam(name = "permissions", value =
    "/META-INF/permissions.properties"),
    @WebInitParam(name = "denyByDefault", value = "true"),
    @WebInitParam(name = "anybody", value = "ALL")})
// TODO: Use the standard logger in this filter
public class SecurityFilter implements Filter {

    private FilterConfig filterConfig = null;
    private Map<String, String[]> permissions = null;
    private boolean denyByDefault = true;
    private String anybody = null;

    private boolean checkAuthorization(ServletRequest request,
            ServletResponse response)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestUri = httpServletRequest.getRequestURI();
        String contextPath = httpServletRequest.getContextPath();
        String path;
        User user = (User) httpServletRequest.getSession().
                getAttribute("user");

        String roleName;
        if (user != null && user.isLoggedIn()) {
            roleName = user.getRole().toString();
        } else {
            roleName = "";
        }

        if (!contextPath.equals("")) {
            path = requestUri.substring(contextPath.length());
        } else {
            path = requestUri;
        }

        Set<String> keySet = permissions.keySet();
        boolean allowedAccess = false;

        checkCycle:
        for (String key : keySet) {
            if (path.matches(key)) {
                String[] allowedRoles = permissions.get(key);
                for (String allowedRole : allowedRoles) {
                    if (allowedRole.equals(roleName) || allowedRole.equals(
                            anybody)) {
                        allowedAccess = true;
                        break checkCycle;
                    }
                }
            }
        }

        if (!allowedAccess && denyByDefault) {
            ((HttpServletResponse) response).sendError(
                    HttpURLConnection.HTTP_UNAUTHORIZED);
            return false;
        } else {
            return true;
        }
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        if (checkAuthorization(request, response)) {
            chain.doFilter(request, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Init method for this filter
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        denyByDefault = Boolean.parseBoolean(filterConfig.getInitParameter(
                "denyByDefault"));
        anybody = filterConfig.getInitParameter("anybody");
        try {
            Properties properties = new Properties();
            permissions = new HashMap<String, String[]>();

            properties.load(getClass().getResourceAsStream(filterConfig.
                    getInitParameter("permissions")));
            Enumeration enumeration = properties.propertyNames();
            String key;

            while (enumeration.hasMoreElements()) {
                key = (String) enumeration.nextElement();
                permissions.put(key, properties.getProperty(key).replace(" ", "").
                        split(","));
            }
        } catch (IOException ex) {
            // PENDING! Log this exception
        }
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("SecurityFilter()");
        }
        StringBuilder sb = new StringBuilder("SecurityFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }
}
