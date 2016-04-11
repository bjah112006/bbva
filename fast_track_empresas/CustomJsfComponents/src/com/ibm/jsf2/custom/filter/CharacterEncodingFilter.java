package com.ibm.jsf2.custom.filter;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharacterEncodingFilter implements Filter {

	private static final String INIT_PARAM_ENCODING = "encoding";
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final String ERROR_ENCODING = "The 'encoding' init param must represent a valid charset. Encountered an invalid charset of '%s'.";

	private String encoding = DEFAULT_ENCODING;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding(encoding);
		response.setCharacterEncoding(encoding);

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String encoding = filterConfig.getInitParameter(INIT_PARAM_ENCODING);

		if (encoding != null) {
			try {
				Charset.forName(encoding);
			} catch (Exception e) {
				throw new IllegalArgumentException(String.format(
						ERROR_ENCODING, encoding), e);
			}

			this.encoding = encoding;
		}
	}

}
