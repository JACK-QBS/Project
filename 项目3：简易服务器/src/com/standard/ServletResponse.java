package com.standard;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public interface ServletResponse {
    OutputStream getOutputStream() throws  IOException;

    PrintWriter getWriter() throws IOException;

    void setContentType(String type);
}
