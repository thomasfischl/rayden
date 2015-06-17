public class RaydenRuntime {

  private final Stack<RaydenScriptScope> scopeStack = new Stack<>();

  ...
  
  private void executeKeyword(KeywordCall keywordCall) {
    scopeStack.clear();

    try {
      reporter.reportTestCaseStart(keywordCall.getName());
      scopeStack.push(new RaydenScriptScope(null, 
          Lists.newArrayList(keywordCall)));

      Object currKeyword = null;
      RaydenScriptScope currScope = null;
      while (!scopeStack.isEmpty()) {

        currScope = scopeStack.peek();
        currKeyword = currScope.getNextKeyword();
        if (currKeyword == null) {
          scopeStack.pop();
          continue;
        }

        if (currKeyword instanceof KeywordCall) {
          KeywordCall keyword = (KeywordCall) currKeyword;
          executeKeywordCall(keyword, currScope);
        } //if

        if (currKeyword instanceof KeywordDecl) {
          KeywordDecl keyword = (KeywordDecl) currKeyword;
          if (currScope.getKeywordCall().getKeywordList() != null 
            && currScope.getKeywordCall().getParameters() != null) {
            executeScriptedCompoundKeywordDecl(keyword, currScope);
          } else {
            executeKeywordDecl(keyword, currScope);
          }
        } //if
        
      } //while
    } finally {
      reporter.reportTestCaseEnd(keywordCall.getName());
    }
  } //executeKeyword
  
  ... 
}