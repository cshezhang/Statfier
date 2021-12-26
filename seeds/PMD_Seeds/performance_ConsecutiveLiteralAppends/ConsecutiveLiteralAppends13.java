
public class Foo {
    public void bar(List l) {
        StringBuffer sb = new StringBuffer();
        sb.append("Hello cruel world");
        Iterator iter = l.iterator();
        while (iter.hasNext()) {
            List innerList = (List) iter.next();
            sb.append(",");
            for (Iterator ixtor = innerList.iterator(); ixtor.hasNext();) {
                Integer integer = (Integer) ixtor.next();
                sb.append("");
                if (ixtor.hasNext()) {
                    sb.append(",");
                }
            }
            sb.append("foo");
        }
    }

    public void bar2(List l) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello cruel world");
        Iterator iter = l.iterator();
        while (iter.hasNext()) {
            List innerList = (List) iter.next();
            sb.append(",");
            for (Iterator ixtor = innerList.iterator(); ixtor.hasNext();) {
                Integer integer = (Integer) ixtor.next();
                sb.append("");
                if (ixtor.hasNext()) {
                    sb.append(",");
                }
            }
            sb.append("foo");
        }
    }
}
        