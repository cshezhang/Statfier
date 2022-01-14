
Company bean = new Company();
HashMap map = new HashMap();
Enumeration names = request.getParameterNames();
while (names.hasMoreElements()) {
    String name = (String) names.nextElement();
    map.put(name, request.getParameterValues(name));
}
BeanUtils.populate(bean, map); // Sensitive: "map" is populated with data coming from user input, here "request.getParameterNames()"
