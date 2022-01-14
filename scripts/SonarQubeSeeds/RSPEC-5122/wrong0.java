
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setHeader("Content-Type", "text/plain; charset=utf-8");
    resp.setHeader("Access-Control-Allow-Origin", "*"); // Sensitive
    resp.setHeader("Access-Control-Allow-Credentials", "true");
    resp.setHeader("Access-Control-Allow-Methods", "GET");
    resp.getWriter().write("response");
}
