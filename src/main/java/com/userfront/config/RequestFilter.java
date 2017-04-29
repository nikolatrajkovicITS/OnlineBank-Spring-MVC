package com.userfront.config;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component                            // To registered this bean as component
@Order(Ordered.HIGHEST_PRECEDENCE)    // This say: HIGHEST_PRECEDENCE - This filter is ispred ostalih svih filtera registoravanih u spring kontenjeru| ako nestavi ovu anotaciju ovaj filter moze da bude iza Spring Securty filtera sto znaci da bi svaka ocija bila odbijena po defaultu
public class RequestFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        HttpServletResponse response = (HttpServletResponse) res;          // We need to Cast requests and response in corrensponding HTTP req and res
        HttpServletRequest request = (HttpServletRequest) req;

        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");  // Ovo nam treba kada Angular 2 pokusava da posalje neke Credencijale backned ce da odobri to,

        if (!(request.getMethod().equalsIgnoreCase("OPTIONS"))) {        // ako nije Options moze biti get, post...
            try {
                chain.doFilter(req, res);
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Pre-flight");
            response.setHeader("Access-Control-Allow-Methods", "POST,GET,DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "authorization, content-type," +
                    "access-control-request-headers,access-control-request-method,accept,origin,authorization,x-requested-with");
            response.setStatus(HttpServletResponse.SC_OK);
        }

    }

    public void init(FilterConfig filterConfig) {}

    public void destroy() {}

}
