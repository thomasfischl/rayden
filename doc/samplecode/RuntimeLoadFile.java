public class RaydenRuntime {
  ...
  public void loadRaydenFile(Reader reader) {
    loadFile(reader, definedKeywords);
  }
  public void loadLibraryFile(Reader reader) {
    loadFile(reader, definedImportedKeywords);
  }
  
  private void loadFile(Reader reader, Map<String, KeywordDecl> keywordStore) {
    IParseResult result = parser.parse(reader);
    if (result.hasSyntaxErrors()) {
      for (INode error : result.getSyntaxErrors()) {
        reporter.error(error.getSyntaxErrorMessage().toString());
      }
      throw new ParseException("Provided input contains syntax error.");
    }

    if (result.getRootASTElement() instanceof Model) {
      reporter.log("Successful loaded model.");
      Model model = (Model) result.getRootASTElement();

      EList<KeywordDecl> keywords = model.getKeywords();
      reporter.log("Loading " + keywords.size() + " keywords ...");
      for (KeywordDecl keyword : keywords) {
        keywordStore.put(RaydenModelUtils.normalizeKeyword(keyword.getName()), keyword);
      }
      
      EList<ImportDecl> imports = model.getImports();
      for (ImportDecl importDecl : imports) {
        try {
          loadLibraryFile(new FileReader(new File(workingFolder, importDecl.getImportLibrary())));
        } catch (Exception e) {
          reporter.error("Error during loading library '" + importDecl.getImportLibrary() + "'");
        }
      }
    }
  }
  ...
}