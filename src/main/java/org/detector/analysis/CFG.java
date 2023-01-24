/*
 * @Description: 
 * @Author: RainyD4y
 * @Date: 2021-10-14 09:25:07
 */
package org.detector.analysis;

import java.util.ArrayList;

/**
 * Description:
 * Author: RainyD4y
 * Date: 2021/10/11 1:32 PM
 */
public class CFG {

    ArrayList<BasicBlock> blocks;
    BasicBlock entry;

    public CFG() {
        this.blocks = new ArrayList<>();
    }

}
