package com.enci.merchant.config

import org.springframework.stereotype.Component

import javax.servlet.*
import javax.servlet.http.HttpServletResponse
import java.io.IOException

@Component
class CorsFilter : Filter {
    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {
        // TODO Auto-generated method stub
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest, res: ServletResponse,
                          chain: FilterChain) {
        val response = res as HttpServletResponse
        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Methods",
                "POST, GET, OPTIONS, DELETE")
        response.setHeader("Access-Control-Allow-Headers",
                "Content-Type, x-requested-with, X-Custom-Header, Authorization")
        chain.doFilter(req, res)
    }

    override fun destroy() {
        // TODO Auto-generated method stub
    }
}
