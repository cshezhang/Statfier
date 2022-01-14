
interface KitchenTool { ... };
interface Plant {...}

public class Spatula implements KitchenTool { ... }
public class Tree implements Plant { ...}
//...

Spatula spatula = new Spatula();
KitchenTool tool = spatula;
KitchenTool [] tools = {tool};

Tree tree = new Tree();
Plant plant = tree;
Tree [] trees = {tree};


if (spatula.equals(tree)) { // Noncompliant; unrelated classes
  // ...
}
else if (spatula.equals(plant)) { // Noncompliant; unrelated class and interface
  // ...
}
else if (tool.equals(plant)) { // Noncompliant; unrelated interfaces
  // ...
}
else if (tool.equals(tools)) { // Noncompliant; array & non-array
  // ...
}
else if (trees.equals(tools)) { // Noncompliant; incompatible arrays
  // ...
}
else if (tree.equals(null)) { // Noncompliant
  // ...
}
