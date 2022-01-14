
Pattern pattern1 = Pattern.compile("aa++bc");            // Compliant, for example it can match "aaaabc"
Pattern pattern2 = Pattern.compile("\\d*+(?<=[02468])"); // Compliant, for example it can match an even number like "1234"
