package src;

public class Token {
  Type type;
  String value;

  enum Type {
    CLEAR,
    INCREASE,
    DECREASE,
    WHILE,
    NOT,
    DO,
    VARIABLE,
    NUMBER,
    NULL;
  }
   public Token(String word) {
    this.type = determineType(word);
   }

   public Type determineType(String word) {
    Type type = Type.NULL;
    switch (word) {
      case "clear":
        type = Type.CLEAR;
        break;
      case "incr":
        type = Type.INCREASE;
        break;
      case "decr":
        type = Type.DECREASE;
        break;
      case "while":
        type = Type.WHILE;
        break;
      case "not":
        type = Type.NOT;
        break;
      case "do":
        type = Type.DO;
        break;
      case "[0-9]+":
        type = Type.NUMBER;
        this.value = word;
        break;
      case "[a-zA-z]+":
        type = Type.VARIABLE;
        this.value = word;
        break;
    }
     return type;
   }
}
