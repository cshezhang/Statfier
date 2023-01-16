package org.detector.thread;

import java.io.*;

public class StreamInfoThread extends Thread {

    private InputStream is;
    private String str;
    public StreamInfoThread(InputStream is, String str) {
        this.is = is;
        this.str = str;
    }
    public void run() {
        BufferedReader out = null;
        try {
            out = new BufferedReader(new InputStreamReader(is, "gbk"));
            String line;
            while ((line = out.readLine()) != null) {
                if (str.equals("error")) {
                    System.out.println("Error Output: " +line);
                } else {
                    System.out.println("Normal Output: " + line);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}