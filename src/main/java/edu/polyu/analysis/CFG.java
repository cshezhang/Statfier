/*
 * @Description: 
 * @Author: Vanguard
 * @Date: 2021-10-14 09:25:07
 */
package edu.polyu.analysis;

import java.util.ArrayList;

/**
 * Description:
 * Author: Vanguard
 * Date: 2021/10/11 1:32 下午
 */
public class CFG {

    ArrayList<BasicBlock> blocks;
    BasicBlock entry;

    public CFG() {
        this.blocks = new ArrayList<>();
    }

}
