

for(int i=0; i<10; i++) {
    for(int j=0; j<i; j++) {
        for(int k=0; k<j; k++) {
            for(int l=0; l<k; l++) { // violation, max allowed nested loop number is 2
            }
        }
    }
}

for(int i=0; i<10; i++) {
    for(int j=0; j<i; j++) {
        for(int k=0; k<j; k++) { // ok
        }
    }
}
        