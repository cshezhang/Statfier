package org.rainyday.analysis;

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.rainyday.util.Utility.random;

/**
 * @Description: This class is to save different algorithms.
 * @Author: Vanguard
 * @Date: 2022-03-16 21:34
 */
public class SelectionAlgorithm {

    public static List<TypeWrapper> Div_Selection(List<TypeWrapper> mutants) {
        List<TypeWrapper> res = new ArrayList<>();
        Map<String, Boolean> code2exist = new HashMap<>();
        for(TypeWrapper mutant : mutants) {
            String transType = mutant.getTransSeq().toString();
            ASTNode node = mutant.getTransNodes().get(mutant.getTransNodes().size() - 1);
//            int nodeType = node.getNodeType();
            int nodeType = 0;
            if(!code2exist.containsKey(transType + nodeType)) {
                code2exist.put(transType + nodeType, true);
                res.add(mutant);
            }
        }
        return res;
    }

    public static List<TypeWrapper> Random_Selection(List<TypeWrapper> mutants) {
        List<TypeWrapper> res = new ArrayList<>();
        int cnt = 0;
//        int limit = Div_Selection(mutants).size();
        for(TypeWrapper mutant : mutants) {
            if(random.nextDouble() > 0.5) {
                res.add(mutant);
                cnt++;
//                if(cnt >= limit) {
//                    break;
//                }
            }
        }
        return res;
    }

}
