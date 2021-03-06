public class RaydenRuntime {
  ...
  public void loadRaydenFile(Reader reader) {
    loadFile(reader, definedKeywords);
  }
  public void loadLibraryFile(Reader reader) {
    loadFile(reader, definedImportedKeywords);
  }
  
  private void loadFile(Reader reader, 
    Map<String, KeywordDecl> keywordStore) throws ParseException {
    IParseResult result = parser.parse(reader);
    if (result.hasSyntaxErrors()) {
      for (INode error : result.getSyntaxErrors()) {
        reporter.error(error.getSyntaxErrorMessage().toString());
      }
      throw new ParseException("Provided input contains syntax errors");
    } //if

    if (result.getRootASTElement() instanceof Model) {
      reporter.log("Model loaded successfully.");
      Model model = (Model) result.getRootASTElement();

      EList<KeywordDecl> keywords = model.getKeywords();
      reporter.log("Loading " + keywords.size() + " keywords ...");
      for (KeywordDecl keyword : keywords) {
        keywordStore.put(RaydenModelUtils.normalizeKeyword(
          keyword.getName()), keyword);
      } //for
      
      EList<ImportDecl> imports = model.getImports();
      for (ImportDecl importDecl : imports) {
        try {
          loadLibraryFile(new FileReader(
            new File(workingFolder, importDecl.getImportLibrary())));
        } catch (Exception e) {
          reporter.error("Error during loading library '" + 
            importDecl.getImportLibrary() + "'");
        }
      } //for
    } //if
  } //loadFile
  ...
} //RaydenRuntime