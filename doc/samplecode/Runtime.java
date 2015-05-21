public class RaydenRuntime {

  private final Stack<RaydenScriptScope> stack = new Stack<>();

  ...
  
  private void executeKeyword(KeywordCall keywordCall) {
    stack.clear();

    try {
      reporter.reportTestCaseStart(keywordCall.getName());
      stack.push(new RaydenScriptScope(null, Lists.newArrayList(keywordCall)));

      Object currKeyword = null;
      RaydenScriptScope currScope = null;
      while (!stack.isEmpty()) {

        currScope = stack.peek();
        currKeyword = currScope.getNextOpt();
        if (currKeyword == null) {
          stack.pop();
          continue;
        }

        if (currKeyword instanceof KeywordCall) {
          KeywordCall keyword = (KeywordCall) currKeyword;
          executeKeywordCall(keyword, currScope);
        }

        if (currKeyword instanceof KeywordDecl) {
          KeywordDecl keyword = (KeywordDecl) currKeyword;

          if (currScope.getKeywordCall().getKeywordList() != null 
            && currScope.getKeywordCall().getParameters() != null) {
            executeScriptedCompoundKeywordDecl(keyword, currScope);
          } else {
            executeKeywordDecl(currScope, keyword);
          }
        }
      }
    } finally {
      reporter.reportTestCaseEnd(keywordCall.getName());
    }
  }
  
  ... 
}