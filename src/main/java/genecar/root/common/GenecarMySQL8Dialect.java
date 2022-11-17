package genecar.root.common;

public class GenecarMySQL8Dialect extends org.hibernate.dialect.MySQL8Dialect {

  public GenecarMySQL8Dialect() {
    super();
    /* types for cast: */
    registerKeyword("signed");
    registerKeyword("SIGNED");
  }
}
