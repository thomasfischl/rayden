public class RaydenExpressionEvaluator {

  public RaydenExpressionEvaluator(RaydenScriptScope scope) {
    this.scope = scope;
  }

  public Object eval(Expr expression, String resultType) {
    ...
  }

  private Object eval(OrExpr expr) {
    ...
  }

  private Object eval(AndExpr expr) {
    ...
  }

  private Object eval(RelExpr expr) {
    ...
  }

  private Object eval(SimpleExpr expr) {
    ...
  }

  private Object eval(Term expr) {
    ...
  }

  private Object eval(NotFact expr) {
    ...
  }

  private Object eval(Fact expr) {
    if (expr.getBool() != null) {
      if ("true".equals(expr.getBool())) {
        return true;
      } else {
        return false;
      }
    } else if (expr.getString() != null) {
      return expr.getString();
    } else if (expr.getIdent() != null) {
      if (RESULT_TYPE_VARIABLE.equals(resultType)) {
        return expr.getIdent();
      }
      return scope.getVariable(expr.getIdent());
    } else if (expr.getExpr() != null) {
      return eval(expr.getExpr(), resultType);
    } else if (expr.getLocator() != null) {
      return evalLocator(expr.getLocator());
    } else {
      return expr.getNumber();
    }
  }