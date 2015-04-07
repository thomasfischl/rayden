package com.github.thomasfischl.rayden.runtime;

import com.github.thomasfischl.rayden.raydenDSL.AndExpr;
import com.github.thomasfischl.rayden.raydenDSL.Expr;
import com.github.thomasfischl.rayden.raydenDSL.Fact;
import com.github.thomasfischl.rayden.raydenDSL.NotFact;
import com.github.thomasfischl.rayden.raydenDSL.OrExpr;
import com.github.thomasfischl.rayden.raydenDSL.RelExpr;
import com.github.thomasfischl.rayden.raydenDSL.SimpleExpr;
import com.github.thomasfischl.rayden.raydenDSL.Term;

public class RaydenExpressionEvaluator {

  private final RaydenScriptScope scope;

  public RaydenExpressionEvaluator(RaydenScriptScope scope) {
    this.scope = scope;
  }

  //
  // Expr: expr=OrExpr ;
  //
  public Object eval(Expr expression) {
    return eval(expression.getExpr());
  }

  //
  // OrExpr: exprs+=AndExpr ( opt+='||' exprs+=AndExpr)* ;
  //
  private Object eval(OrExpr expr) {
    if (expr.getExpr().size() == 1) {
      return eval(expr.getExpr().get(0));
    }

    boolean result = false;
    for (AndExpr e : expr.getExpr()) {
      Object res = eval(e);
      if (res instanceof Boolean) {
        result = result || (Boolean) res;
      } else {
        throw new RaydenScriptException("Expression error: Part expression result is not a boolean. Type: '" + res.getClass() + "'");
      }

      if (result) {
        return true;
      }
    }
    return false;
  }

  //
  // AndExpr: expr+=RelExpr ( opt+='&&' expr+=RelExpr)* ;
  //
  private Object eval(AndExpr expr) {
    if (expr.getExpr().size() == 1) {
      return eval(expr.getExpr().get(0));
    }

    boolean result = true;
    for (RelExpr e : expr.getExpr()) {
      Object res = eval(e);
      if (res instanceof Boolean) {
        result = result && (Boolean) res;
      } else {
        throw new RaydenScriptException("Expression error: Part expression result is not a boolean. Type: '" + res.getClass() + "'");
      }

      if (!result) {
        return false;
      }
    }
    return true;
  }

  //
  // RelExpr: expr+=SimpleExpr ( ( opt+='==' | opt+='!=' | opt+='<' | opt+='<=' | opt+='>' | opt+='>=' ) expr+=SimpleExpr )? ;
  //
  private Object eval(RelExpr expr) {
    Object res = eval(expr.getExpr().get(0));

    if (expr.getExpr().size() == 2) {
      Object res2 = eval(expr.getExpr().get(1));
      String opt = expr.getOpt();

      if ("==".equals(opt)) {
        if (res instanceof Double && res2 instanceof Double) {
          res = ((Double) res).equals(res2);
        } else if (res instanceof String && res2 instanceof String) {
          res = ((String) res).equals(res2);
        } else if (res instanceof Boolean && res2 instanceof Boolean) {
          res = ((Boolean) res).equals(res2);
        } else {
          throw new RaydenScriptException("Expression error: RelExpr error");
        }
      } else if ("!=".equals(opt)) {
        if (res instanceof Double && res2 instanceof Double) {
          res = !((Double) res).equals(res2);
        } else if (res instanceof String && res2 instanceof String) {
          res = !((String) res).equals(res2);
        } else if (res instanceof Boolean && res2 instanceof Boolean) {
          res = !((Boolean) res).equals(res2);
        } else {
          throw new RaydenScriptException("Expression error: RelExpr error");
        }
      } else if ("<".equals(opt)) {
        if (res instanceof Double && res2 instanceof Double) {
          res = (Double) res < (Double) res2;
        } else {
          throw new RaydenScriptException("Expression error: RelExpr error");
        }
      } else if ("<=".equals(opt)) {
        if (res instanceof Double && res2 instanceof Double) {
          res = (Double) res <= (Double) res2;
        } else {
          throw new RaydenScriptException("Expression error: RelExpr error");
        }
      } else if (">".equals(opt)) {
        if (res instanceof Double && res2 instanceof Double) {
          res = (Double) res > (Double) res2;
        } else {
          throw new RaydenScriptException("Expression error: RelExpr error");
        }
      } else if (">=".equals(opt)) {
        if (res instanceof Double && res2 instanceof Double) {
          res = (Double) res >= (Double) res2;
        } else {
          throw new RaydenScriptException("Expression error: RelExpr error");
        }
      } else {
        throw new RaydenScriptException("Expression error: Invalid operator '" + opt + "'.");
      }
    }

    return res;
  }

  //
  // SimpleExpr: preopt=('+' | '-')? expr+=Term ( opt+=('+'|'-') expr+=Term )* ;
  //
  private Object eval(SimpleExpr expr) {
    Object res = eval(expr.getExpr().get(0));

    for (int i = 1; i < expr.getExpr().size(); i++) {
      Object res2 = eval(expr.getExpr().get(i));
      String opt = expr.getOpt().get(i - 1);

      if ("+".equals(opt)) {
        if (res instanceof Double && res2 instanceof Double) {
          res = (Double) res + (Double) res2;
        } else if (res instanceof Double && res2 instanceof String) {
          res = "" + res + res2;
        } else if (res instanceof Boolean && res2 instanceof String) {
          res = "" + res + res2;
        } else if (res instanceof String && res2 instanceof String) {
          res = "" + res + res2;
        } else if (res instanceof String && res2 instanceof Double) {
          res = "" + res + res2;
        } else if (res instanceof String && res2 instanceof Boolean) {
          res = "" + res + res2;
        } else {
          throw new RaydenScriptException("Expression error: SimpleExpr error");
        }
      } else if ("-".equals(opt)) {
        if (res instanceof Double && res2 instanceof Double) {
          res = (Double) res - (Double) res2;
        } else {
          throw new RaydenScriptException("Expression error: SimpleExpr error");
        }
      } else {
        throw new RaydenScriptException("Expression error: Invalid operator '" + opt + "'.");
      }
    }

    return res;
  }

  //
  // Term: expr+=NotFact (( opt+='*' | opt+='/' | opt+='%' ) expr+=NotFact)* ;
  //
  private Object eval(Term expr) {
    Object res = eval(expr.getExpr().get(0));

    for (int i = 1; i < expr.getExpr().size(); i++) {
      Object res2 = eval(expr.getExpr().get(i));
      String opt = expr.getOpt().get(i - 1);

      if ("*".equals(opt)) {
        if (res instanceof Double && res2 instanceof Double) {
          res = (Double) res * (Double) res2;
        } else {
          throw new RaydenScriptException("Expression error: Term error");
        }
      } else if ("/".equals(opt)) {
        if (res instanceof Double && res2 instanceof Double) {
          res = (Double) res / (Double) res2;
        } else {
          throw new RaydenScriptException("Expression error: Term error");
        }
      } else if ("%".equals(opt)) {
        if (res instanceof Double && res2 instanceof Double) {
          res = (Double) res % (Double) res2;
        } else {
          throw new RaydenScriptException("Expression error: Term error");
        }
      } else {
        throw new RaydenScriptException("Expression error: Invalid operator '" + opt + "'.");
      }
    }

    return res;
  }

  //
  // NotFact: (opt='!')? expr=Fact ;
  //
  private Object eval(NotFact expr) {
    if (expr.getOpt() != null) {
      Object res = eval(expr.getExpr());
      if (res instanceof Boolean) {
        return !(Boolean) res;
      } else {
        throw new RaydenScriptException("Expression error: NotFact result is no boolean");
      }
    } else {
      return eval(expr.getExpr());
    }
  }

  //
  // Fact: bool='false' | bool='true' | number=NUMBER | string=STRING | ident =IDEXT | '(' expr=Expr ')' ;
  //
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
      return scope.getVariable(expr.getIdent());
    } else if (expr.getExpr() != null) {
      return eval(expr.getExpr());
    } else if (expr.getLocator() != null) {
      return new RaydenExpressionLocator(expr.getLocator());
    } else if (expr.getSymbol() != null) {
      return new RaydenExpressionSymbol(expr.getSymbol());
    } else {
      return expr.getNumber();
    }
  }
}
