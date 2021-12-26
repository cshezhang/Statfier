
public class Useless {
    public void method() {
        boolean isSkipped = (pokeNick.equals(pokemon.getNickname())
            && renameResult.getNumber() == NicknamePokemonResponse.Result.UNSET_VALUE);

        // now the same in one line
        boolean isSkipped2 = (pokeNick.equals(pokemon.getNickname()) && renameResult.getNumber() == NicknamePokemonResponse.Result.UNSET_VALUE);

        // now without the outer parenthesis - no additional violation.
        boolean isSkipped3 = pokeNick.equals(pokemon.getNickname()) && renameResult.getNumber() == NicknamePokemonResponse.Result.UNSET_VALUE;
    }
}
        