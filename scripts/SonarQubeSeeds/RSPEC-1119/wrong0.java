
int matrix[][] = {
  {1, 2, 3},
  {4, 5, 6},
  {7, 8, 9}
};

outer: for (int row = 0; row < matrix.length; row++) {   // Non-Compliant
  for (int col = 0; col < matrix[row].length; col++) {
    if (col == row) {
      continue outer;
    }
    System.out.println(matrix[row][col]);                // Prints the elements under the diagonal, i.e. 4, 7 and 8
  }
}
