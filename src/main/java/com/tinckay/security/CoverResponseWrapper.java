package com.tinckay.security;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

/**
 * Created by root on 1/20/17.
 */
public class CoverResponseWrapper extends HttpServletResponseWrapper {
    ByteArrayOutputStream output;
    FilterServletOutputStream filterOutput;
    //HttpResponseStatus status = HttpResponseStatus.OK;

    public CoverResponseWrapper(HttpServletResponse response) {
        super(response);
        output = new ByteArrayOutputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (filterOutput == null) {
            filterOutput = new FilterServletOutputStream(output);
        }
        return filterOutput;
    }

    public byte[] getDataStream() {
        return output.toByteArray();
    }
}
