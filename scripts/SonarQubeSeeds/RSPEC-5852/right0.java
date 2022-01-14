
java.util.regex.Pattern.compile("(a+)++").matcher(
"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+
"aaaaaaaaaaaaaaa!").matches(); // Compliant

java.util.regex.Pattern.compile("(h|h|ih(((i|a|c|c|a|i|i|j|b|a|i|b|a|a|j))+h)ahbfhba|c|i)*+").matcher(
"hchcchicihcchciiicichhcichcihcchiihichiciiiihhcchi"+
"cchhcihchcihiihciichhccciccichcichiihcchcihhicchcciicchcccihiiihhihihihi"+
"chicihhcciccchihhhcchichchciihiicihciihcccciciccicciiiiiiiiicihhhiiiihchccch"+
"chhhhiiihchihcccchhhiiiiiiiicicichicihcciciihichhhhchihciiihhiccccccciciihh"+
"ichiccchhicchicihihccichicciihcichccihhiciccccccccichhhhihihhcchchihih"+
"iihhihihihicichihiiiihhhhihhhchhichiicihhiiiiihchccccchichci").matches(); // Compliant
