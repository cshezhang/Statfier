
import java.util.Arrays;

import edu.umd.cs.findbugs.annotations.Confidence;
import edu.umd.cs.findbugs.annotations.DesireWarning;
import edu.umd.cs.findbugs.annotations.ExpectWarning;
import edu.umd.cs.findbugs.annotations.NoWarning;

public class Ideas_2009_12_11 {

    int[][] data = new int[1][1];

    @Override
    @DesireWarning("EQ_CHECK_FOR_OPERAND_NOT_COMPATIBLE_WITH_THIS")
    public boolean equals(Object that) {
        int[][] thatData;
        if (that instanceof Ideas_2009_12_11)
            thatData = ((Ideas_2009_12_11) that).data;
        else if (that instanceof int[][])
            thatData = (int[][]) that;
        else
            return false;
        return Arrays.deepEquals(data, thatData);
    }

}
