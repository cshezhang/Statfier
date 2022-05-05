package edu.polyu.analysis;

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.polyu.util.Util.random;

/**
 * @Description: This class is to save different algorithms.
 * @Author: Vanguard
 * @Date: 2022-03-16 21:34
 */
public class SelectionAlgorithm {

    public static List<ASTWrapper> Div_Selection(List<ASTWrapper> mutants) {
        List<ASTWrapper> res = new ArrayList<>();
        Map<String, Boolean> code2exist = new HashMap<>();
        for(ASTWrapper mutant : mutants) {
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

    public static List<ASTWrapper> Random_Selection(List<ASTWrapper> mutants) {
        List<ASTWrapper> res = new ArrayList<>();
        int cnt = 0;
//        int limit = Div_Selection(mutants).size();
        for(ASTWrapper mutant : mutants) {
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
