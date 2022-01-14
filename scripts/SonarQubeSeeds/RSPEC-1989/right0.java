
public void doGet(HttpServletRequest request, HttpServletResponse response)
  throws IOException, ServletException {
  try {
    String ip = request.getRemoteAddr();
    InetAddress addr = InetAddress.getByName(ip);
    //...
  }
  catch (UnknownHostException uhex) {
    //...
  }
}
