
class Foo {
	private static Class<? extends Annotation> ejbRefClass;

	private static Class<? extends Annotation> webServiceRefClass;

	static {
		try {
			Class<? extends Annotation> clazz = (Class<? extends Annotation>)					Class.forName("javax.xml.ws.WebServiceRef");
			webServiceRefClass = clazz;
		} catch (ClassNotFoundException ex) {
			webServiceRefClass = null;
		}

		try {
			Class<? extends Annotation> clazz = Class.forName("javax.ejb.EJB");
			ejbRefClass = clazz;
		} catch (ClassNotFoundException ex) {
			ejbRefClass = null;
		}
	}


	private static Class<? extends Annotation> other = webServiceRefClass;

}
        