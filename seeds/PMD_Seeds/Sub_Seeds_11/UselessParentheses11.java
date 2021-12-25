
public class Test {
    public void testMethod() {
        if((lookahead.type == Keyword.STRING || lookahead.type == Keyword.NUMBER) && lookahead.type != baseType)
            throw new IncompatibleAttributeTypeException(attribute);
        System.out.println( "number " + ( 1 + 2 ) );

        if(lookahead.type != baseType && (lookahead.type == Keyword.STRING || lookahead.type == Keyword.NUMBER))
            throw new IncompatibleAttributeTypeException(attribute);
    }
}
        