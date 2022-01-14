@class Child;
@interface Parent : NSObject {
    Child *child; // Instance variables are implicitly __strong
}
@end
@interface Child : NSObject {
    Parent *parent;
}
@end

// You can fix a retain cycle in ARC by using __weak variables or weak properties for your "back links", i.e. links to direct or indirect parents in an object hierarchy:

@class Child;
@interface Parent : NSObject {
    Child *child;
}
@end
@interface Child : NSObject {
    __weak Parent *parent;
}
@end